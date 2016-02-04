package ua.dataart.controllers;

import ua.dataart.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ua.dataart.service.CustomerService;

@Component
public class AuthenticatedCustomer {

    @Autowired
    private CustomerService customerRepository;

    public Customer getAuthenticatedCustomer(){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return customerRepository.getCustomerByLogin(login);
    }
}
