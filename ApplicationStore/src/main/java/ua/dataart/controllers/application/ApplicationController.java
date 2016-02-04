package ua.dataart.controllers.application;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.dataart.model.Application;
import ua.dataart.service.ApplicationService;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class ApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private ApplicationService applicationService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/upload_application", method = RequestMethod.POST)
    public void handleFileUpload(@RequestParam(value = "applicationName") String applicationName,
                                 @RequestParam(value = "applicationDescription") String applicationDescription,
                                 @RequestParam(value = "applicationCategory") String applicationCategoryName,
                                 @RequestParam(value = "file") MultipartFile file) {
        applicationService.submitANewApplication(applicationName, applicationDescription, applicationCategoryName, file);
        logger.debug("Uploaded a new application");
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/download_app", method = RequestMethod.POST)
    public void downloadApplication(@RequestParam("name") String applicationName,
                                    HttpServletResponse response) throws IOException {

        logger.debug("Getting an existing application.");
        response.setContentType("application/zip");
        Application application = applicationService.getApplication(applicationName);
        String pathToApplication = application.getPathToApplicationFile();
        File foundApplication = new File(pathToApplication);
        InputStream inputStream = new FileInputStream(foundApplication);
        applicationService.incrementAndSaveNumberOfDownloads(application);
        IOUtils.copy(inputStream, response.getOutputStream());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/apps_per_category", params = {"name"}, method = RequestMethod.GET)
    public List<Application> getApplicationsPerCategory(@RequestParam(value = "name") String categoryName) {
        logger.debug("Retrieving applications per category");
        return applicationService.getApplicationsPerCategory(categoryName);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/popular_apps", method = RequestMethod.GET)
    public List<Application> getTheMostPopularApplications() {
        logger.debug("Retrieving the most popular applications");
        return applicationService.getTheMostPopularApplications();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/images/{image_type}/{app_name}")
    public byte[] getSmallImage(@PathVariable(value = "image_type") String imageType,
                                @PathVariable(value = "app_name") String applicationName) {
        try {
            logger.debug("Retrieving application image. Application name: " + applicationName);
            return applicationService.getApplicationImage(applicationName, imageType);
        } catch (IOException e) {
            logger.error("Exception occurred while retrieving an application image. Application name: " + applicationName, e);
            throw new ApplicationRegistrationException("Unable to get application image.");
        }
    }
}
