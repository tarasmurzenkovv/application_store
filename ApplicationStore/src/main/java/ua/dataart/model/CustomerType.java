package ua.dataart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="customer_type")
public class CustomerType {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "customer_type", nullable = false, unique = true)
    private String customerTypeName;

    @JsonIgnore
    @OneToMany(mappedBy = "customerType")
    private Set<Customer> customers;

    public CustomerType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomerType that = (CustomerType) o;

        return customerTypeName.equals(that.customerTypeName);

    }

    @Override
    public int hashCode() {
        return customerTypeName.hashCode();
    }

    @Override
    public String toString() {
        return "CustomerType{" +
                "customerTypeName='" + customerTypeName + '\'' +
                '}';
    }
}
