package ua.dataart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.dataart.configuration.security.UserDetailsServiceImplementation;
import ua.dataart.controllers.customer.CredentialsAreInDataBaseException;
import ua.dataart.model.Application;
import ua.dataart.model.Customer;
import ua.dataart.model.CustomerType;
import ua.dataart.repository.CustomerRepository;
import ua.dataart.repository.CustomerTypeRepository;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    @Autowired
    private CustomerTypeRepository customerTypeRepository;

    public Customer getCustomerByLogin(String customerLogin) {
        return customerRepository.findCustomerByLogin(customerLogin);
    }

    public List<Application> getUploadedApplicationsForCustomer(Customer customer){
        return customerRepository.findCustomerApplications(customer);
    }

    public List<CustomerType> getAllCustomerTypes(){
        return customerTypeRepository.findAll();
    }

    public Boolean checkIfEmailExists(Customer customer){
        return customerRepository.checkIfEmailAddressExists(customer.getEmail());
    }

    public Boolean checkIfLoginExists(Customer customer ){
        return customerRepository.checkIfUserLoginExists(customer.getLogin());
    }

    @Transactional
    public void registerANewCustomer(Customer customer){
        if(checkIfLoginExists(customer)){
            throw new CredentialsAreInDataBaseException("Such login in data base. Choose another one.");
        }
        if(checkIfEmailExists(customer)){
            throw new CredentialsAreInDataBaseException("Such email in data base. Choose another one");
        }

        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        customerRepository.saveAndFlush(customer);
        UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(customer.getLogin());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
