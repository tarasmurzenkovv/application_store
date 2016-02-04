package ua.dataart.repository;


import ua.dataart.model.Application;
import ua.dataart.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select customer from Customer customer where customer.login =:login")
    Customer findCustomerByLogin(@Param("login") String customerLogin);

    @Query("select application from Application application where application.customer=:customer")
    List<Application> findCustomerApplications(@Param("customer") Customer customer);

    @Query("select case when count(customer) > 0 then true else false end from Customer customer where customer.email=:email")
    Boolean checkIfEmailAddressExists(@Param("email") String customerEmail);

    @Query("select case when count(customer) > 0 then true else false end from Customer customer where customer.login=:login")
    Boolean checkIfUserLoginExists(@Param("login") String login);

}
