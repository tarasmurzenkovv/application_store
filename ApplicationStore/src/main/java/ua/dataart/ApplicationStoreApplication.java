package ua.dataart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class ApplicationStoreApplication extends SpringBootServletInitializer{

    private static Class<ApplicationStoreApplication> applicationStoreApplicationClass = ApplicationStoreApplication.class;

    public static void main(String[] args) {
        SpringApplication.run(applicationStoreApplicationClass , args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(applicationStoreApplicationClass);
    }
}
