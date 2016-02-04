package ua.dataart.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ua.dataart.controllers.AuthenticatedCustomer;
import ua.dataart.controllers.application.ApplicationRegistrationException;
import ua.dataart.model.Application;
import ua.dataart.model.Category;
import ua.dataart.model.Customer;
import ua.dataart.repository.ApplicationRepository;
import ua.dataart.repository.CategoryRepository;
import ua.dataart.service.utils.FilesUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ApplicationService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    @Autowired
    private ApplicationRepository applicationRepository;
    @Value("${top_number}")
    private Integer numberOfTopApplicationsToShow;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FilesUtility filesUtility;
    @Autowired
    private AuthenticatedCustomer authenticatedCustomer;

    public Application getApplication(String applicationName){
        return applicationRepository.getApplicationByName(applicationName);
    }

    public Integer incrementAndSaveNumberOfDownloads(Application application){
        Integer numberOfDownloads = application.getApplicationDownloads();
        numberOfDownloads++;
        application.setApplicationDownloads(numberOfDownloads);
        applicationRepository.saveAndFlush(application);
        return numberOfDownloads;
    }

    public List<Application> getTheMostPopularApplications(){
        Pageable topNumber = new PageRequest(0, numberOfTopApplicationsToShow);
        return applicationRepository.getTheMostRatedApplications(topNumber);
    }

    public List<Application> getApplicationsPerCategory(String categoryName){
        Category category = categoryRepository.getCategoryByName(categoryName);
        return applicationRepository.getApplicationsPerCategory(category);
    }

    public Boolean checkIfApplicationNameExists(String applicationName){
        return applicationRepository.checkIfApplicationNameExists(applicationName);
    }

    @Transactional
    public void submitANewApplication(String applicationName,
                                             String applicationDescription,
                                             String applicationCategoryName,
                                             MultipartFile file){
        if (StringUtils.isEmpty(applicationName)) {
            logger.debug("Application name is empty. ");
            throw new ApplicationRegistrationException("Provide an application name");
        }

        if (StringUtils.isEmpty(applicationDescription)) {
            logger.debug("Application description is empty. ");
            throw new ApplicationRegistrationException("Provide an application description");
        }

        if (StringUtils.isEmpty(applicationCategoryName)) {
            logger.debug("Application category is empty. ");
            throw new ApplicationRegistrationException("Select an application category");
        }
        if (checkIfApplicationNameExists(applicationName)) {
            logger.debug("Application name already exists. ");
            throw new ApplicationRegistrationException("Such name exists. Choose another application name. ");
        }

        Map<String, String> extractedFromUploadedArchiveApplicationMetaData = filesUtility.processArchive(file);
        Application application = new Application();
        Customer customer = authenticatedCustomer.getAuthenticatedCustomer();
        Category category = categoryService.getCategoryByName(applicationCategoryName);
        application.setApplicationName(applicationName);
        application.setApplicationDescription(applicationDescription);
        application.setApplicationDownloads(0);
        application.setDateOfUpload(new Date());
        application.setPathToApplicationFile(extractedFromUploadedArchiveApplicationMetaData.get("path_to_app_archive"));
        application.setPathToLargeImage(extractedFromUploadedArchiveApplicationMetaData.get("high"));
        application.setPathToSmallImage(extractedFromUploadedArchiveApplicationMetaData.get("low"));
        application.setCategory(category);
        application.setCustomer(customer);
        applicationRepository.save(application);
    }

    public byte[] getApplicationImage(String applicationName, String imageType) throws IOException {
        Application application = getApplication(applicationName);
        String pathToImageToDisplay = application.getPathToSmallImage();

        if (StringUtils.equals("large", imageType)) {
            pathToImageToDisplay = application.getPathToLargeImage();
        }
        File picture = new File(pathToImageToDisplay);
        if (!picture.exists()) {
            logger.error("Unable to locate picture file for application: " + applicationName);
        }
        return Files.readAllBytes(picture.toPath());
    }
}
