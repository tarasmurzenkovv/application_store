package ua.dataart.model;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message = "Enter your first name.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Enter your last name.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "Enter your date of birth.")
    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @NotBlank(message = "Enter your password. ")
    @Size(min = 5, message = "Password length must be more than 5 characters.")
    @Column(name = "user_password", nullable = false)
    private String password;

    @Email
    @NotBlank(message = "Enter your email.")
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_login", nullable = false, unique = true)
    @NotBlank(message = "Enter your login.")
    private String login;

    @JoinColumn(name = "customer_type", nullable = false)
    @ManyToOne
    @NotNull(message = "Select your group.")
    private CustomerType customerType;

    @OneToMany(mappedBy = "customer")
    private Set<Application> userApplications;

    public Customer() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerType getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = (StringUtils.isEmpty(customerType.getCustomerTypeName()) ? null : customerType);
    }

    public Set<Application> getUserApplications() {
        return userApplications;
    }

    public void setUserApplications(Set<Application> userApplications) {
        this.userApplications = userApplications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return email.equals(customer.email) && login.equals(customer.login);

    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + login.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", customerType=" + customerType +
                '}';
    }
}
