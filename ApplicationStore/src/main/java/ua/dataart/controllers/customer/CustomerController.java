package ua.dataart.controllers.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ua.dataart.controllers.AuthenticatedCustomer;
import ua.dataart.model.Application;
import ua.dataart.model.Customer;
import ua.dataart.model.CustomerType;
import ua.dataart.service.CustomerService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    // store cookie for a one day
    @Value("${cookie_max_age_seconds}")
    private int cookieMaxAge;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticatedCustomer authenticatedCustomer;

    private void saveCookie(String cookieName, String customerLogin, String customerType, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, customerLogin);
        cookie.setMaxAge(cookieMaxAge);
        cookie.setValue(customerType);
        response.addCookie(cookie);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/ajax_login", method = RequestMethod.POST)
    public void processUserLogin(HttpServletResponse response) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerService.getCustomerByLogin(login);
        logger.debug("Success with customer login. Customer " + customer.toString());
        saveCookie("ApplicationStore", customer.getLogin(), customer.getCustomerType().getCustomerTypeName(), response);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void registerANewCustomer(@Valid @RequestBody Customer customer) {
        logger.debug("Registering a new customer: " + customer.toString());
        customerService.registerANewCustomer(customer);
    }

    @RequestMapping(value = "/customer_types", method = RequestMethod.GET)
    public List<CustomerType> getCustomerTypes() {
        logger.debug("Getting customer types");
        List<CustomerType> customerTypes = customerService.getAllCustomerTypes();
        return customerTypes;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/customer_applications", method = RequestMethod.GET)
    public List<Application> getCustomerApplications() {
        logger.debug("Getting customer applications");
        return customerService.getUploadedApplicationsForCustomer(authenticatedCustomer.getAuthenticatedCustomer());
    }
}
