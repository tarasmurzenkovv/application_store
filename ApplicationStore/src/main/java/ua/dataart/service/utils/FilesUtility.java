package ua.dataart.service.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.dataart.controllers.AuthenticatedCustomer;
import ua.dataart.controllers.application.ApplicationRegistrationException;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FilesUtility {
    private final static Logger logger = Logger.getLogger(FilesUtility.class);
    @Value("${root_path}")
    private String rootPath;
    @Value("${path_to_512}")
    private String pathToDefaultHighQualityPicture;
    @Value("${path_to_128}")
    private String pathToDefaultLowQualityPicture;
    @Value("${zipped_apps_folder_name}")
    private String zippedApplicationsFolderName;
    @Value("${extracted_apps_folder_name}")
    private String extractedApplicationFolderName;
    @Autowired
    private AuthenticatedCustomer authenticatedCustomer;

    public FilesUtility() {}

    public Map<String, String> processArchive(MultipartFile file){
        Map<String, String> qualityVsPathToPictures;
        Map<String, String> assembledApplicationMetaData = new HashMap<>();
        String userName = authenticatedCustomer.getAuthenticatedCustomer().getLogin();
        String fileName = file.getOriginalFilename();
        String applicationsPath = buildPathToFolder(rootPath, extractedApplicationFolderName);
        String pathToUserFolder = buildPathToFolder(applicationsPath, userName);
        String pathToUploadedFile = uploadedFile(file, pathToUserFolder, fileName);
        if(StringUtils.isEmpty(pathToUploadedFile)){
            throw new ApplicationRegistrationException("You have already uploaded such archive");
        }
        String pathToExtractedFolder = unzip(pathToUploadedFile, pathToUserFolder, fileName);
        Boolean isValid =  containsTxtFile(pathToExtractedFolder);
        if(isValid){
            Map<String, String> metaData = getApplicationMetaData(getFileWithDescription(pathToExtractedFolder));
            if(isValidMetaData(metaData)){
                qualityVsPathToPictures = qualityVsPathToPicture(pathToExtractedFolder);
                assembledApplicationMetaData.put("name", metaData.get("name"));
                assembledApplicationMetaData.put("package", metaData.get("package"));
                assembledApplicationMetaData.put("high", qualityVsPathToPictures.get("high"));
                assembledApplicationMetaData.put("low", qualityVsPathToPictures.get("low"));
                assembledApplicationMetaData.put("path_to_app_archive",getApplicationArchive(pathToUploadedFile));
                assembledApplicationMetaData.put("path_to_app_folder",pathToExtractedFolder);
            }else{
                deleteFile(pathToUploadedFile);
                deleteFolder(pathToExtractedFolder);
                throw  new ApplicationRegistrationException("Invalid structure of meta data: inside a text file must be present 'name' and 'package'. ");
            }
        }else{
            deleteFile(pathToExtractedFolder);
            deleteFile(pathToUploadedFile);
            throw  new ApplicationRegistrationException("Invalid structure of the application archive: a text file with the application description must be present.");
        }

        return assembledApplicationMetaData;
    }

    private String getApplicationArchive(String pathToApplicationArchive){
        File applicationFile = new File(pathToApplicationArchive);
        if(!applicationFile.isFile()){
            throw new ApplicationRegistrationException("Cannot locate archive with the application");
        }
        return  applicationFile.getPath();
    }

    private String buildPathToFolder(String root, String node){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(root).append(File.separator).append(node);
        return stringBuilder.toString();
    }

    private boolean isValidMetaData(Map<String, String> extractedMetaData){
        boolean containsName = extractedMetaData.containsKey("name");
        boolean containsDescription = extractedMetaData.containsKey("package");
        return containsDescription&&containsName;
    }

    private Map<String, String> getApplicationMetaData(String pathToTxtFile){
        Map<String, String> applicationMetaData = new HashMap<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(pathToTxtFile));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] parts = sCurrentLine.split(":");
                applicationMetaData.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            logger.error("Exception occurred while reading txt file", e);
            throw new ApplicationRegistrationException("Unable to read txt application file");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                logger.error("Unable to close txt file input stream", e);
            }
        }
        return applicationMetaData;
    }

    private  Map<String, String> mapLogicalNamesToPhysicalLocations(String pathToFolder, Map<String, String> description){
        Map<String, String> pathToLocationPictureName = new HashMap<>();

        String lowQualityPictureName = description.get("picture_128");
        String pathToLowQualityPictureName = buildPathToFolder(pathToFolder, lowQualityPictureName);
        if(new File(pathToLowQualityPictureName).isFile()){
            pathToLocationPictureName.put("low",pathToLowQualityPictureName);
        }else{
            pathToLocationPictureName.put("low", pathToDefaultLowQualityPicture);
        }

        String highQualityPictureName = description.get("picture_512");
        String pathToHighQualityPictureName = buildPathToFolder(pathToFolder, highQualityPictureName);
        if(new File(pathToHighQualityPictureName).isFile()){
            pathToLocationPictureName.put("high",pathToHighQualityPictureName);
        }else{
            pathToLocationPictureName.put("high", pathToDefaultHighQualityPicture);
        }
        return pathToLocationPictureName;
    }

    private  Map<String, String> qualityVsPathToPicture(String pathToFolder){
        Map<String, String> pictureDescriptionsFromFileWithDescription;

        String pathToFileWithTheDescription = getFileWithDescription(pathToFolder);
        pictureDescriptionsFromFileWithDescription = getApplicationMetaData(pathToFileWithTheDescription);

        return mapLogicalNamesToPhysicalLocations(pathToFolder, pictureDescriptionsFromFileWithDescription);
    }

    private String getFileWithDescription(String pathToFolder){
        File folder = new File(pathToFolder);
        String pathToFileWithDescription = null;
        File[] files = folder.listFiles();
        if(files == null){
            throw new ApplicationRegistrationException("Files are missing from an application folder");
        }
        for(File file:files){
            String pathToFile = file.getAbsolutePath();
            String ext = FilenameUtils.getExtension(pathToFile);
            if(ext.equals("txt")){
                pathToFileWithDescription = pathToFile;
            }
        }
        return pathToFileWithDescription;
    }

    private  Boolean containsTxtFile(String pathToFolder){
        Map<String, String> extensionVsFiles = new HashMap<>();

        File folder = new File(pathToFolder);
        File[] files = folder.listFiles();
        if(files == null){
            return false;
        }
        for(File file:files){
            String pathToFile = file.getAbsolutePath();
            String ext = FilenameUtils.getExtension(pathToFile);
            if(extensionVsFiles.containsKey("txt")){
                return false;
            }
            extensionVsFiles.put(ext, pathToFile);
        }

        return true;
    }

    private  String unzip(String source, String destination, String fileName){
        String fileNameWithoutExt = FilenameUtils.removeExtension(fileName);
        String pathToExtractedFolder = buildPathToFolder(destination, fileNameWithoutExt);
        try {

            ZipFile zipFile = new ZipFile(source);
            File applicationFolder = new File(buildPathToFolder(destination,fileNameWithoutExt));
            if(applicationFolder.exists()){
                throw new ApplicationRegistrationException("You have already uploaded such archive. ");
            }
            List fileHeaderList = zipFile.getFileHeaders();
            for (Object aFileHeaderList : fileHeaderList) {
                FileHeader fileHeader = (FileHeader) aFileHeaderList;
                if (fileHeader.isDirectory()) {
                    deleteFile(source);
                    throw new ApplicationRegistrationException("Application zip archive cannot contain any directories. ");
                }
            }
            zipFile.extractAll(applicationFolder.getPath());
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return pathToExtractedFolder;
    }

    private  String uploadedFile(MultipartFile file, String pathToFolder, String fileName) {
        String pathToExtractedArchive = null;
        BufferedOutputStream stream = null;
        try {
            byte[] bytes = file.getBytes();
            File dir = new File(pathToFolder);
            if(!dir.exists()){
                if(!dir.mkdirs()){
                    throw new RuntimeException("Cannot create directories");
                }
            }
            File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
            stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(bytes);
            logger.info("Server File Location=" + serverFile.getAbsolutePath());
            pathToExtractedArchive = serverFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Failed to upload file. ", e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                logger.error("Unable to close input stream.", e);
            }
            return pathToExtractedArchive;
        }
    }

    private void deleteFile(String pathToFile){
        try {
            FileUtils.forceDelete(new File(pathToFile));
        } catch (IOException e) {
            logger.error("Unable to delete application archive.", e);
        }
    }
    private void deleteFolder(String pathToFolder){
        try {
            FileUtils.deleteDirectory(new File(pathToFolder));
        } catch (IOException e) {
            logger.error("Error occurred while deleting files.", e);
        }
    }
}
