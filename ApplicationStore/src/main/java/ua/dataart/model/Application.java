package ua.dataart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "application", uniqueConstraints = {@UniqueConstraint(columnNames = {"application_name"})})
public class Application {
    @Id
    @GeneratedValue
    @Column(name = "application_id")
    @JsonIgnore
    private Integer id;

    @NotEmpty(message = "Enter application name.")
    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @NotEmpty(message = "Enter application description.")
    @Column(name = "application_description", nullable = false)
    private String applicationDescription;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", nullable = false)
    private Date dateOfUpload;

    @Column(name = "application_downloads")
    private Integer applicationDownloads;

    @NotEmpty(message = "Application path cannot be empty.")
    @Column(name = "path_to_file", nullable = false)
    private String pathToApplicationFile;

    @Column(name="path_to_small_image")
    private String pathToSmallImage;

    @Column(name="path_to_large_image")
    private String pathToLargeImage;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "category"))
    private Category category;

    @JsonIgnore
    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    public Application() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getApplicationDownloads() {
        return applicationDownloads;
    }

    public void setApplicationDownloads(Integer applicationDownloads) {
        this.applicationDownloads = applicationDownloads;
    }

    public String getPathToSmallImage() {
        return pathToSmallImage;
    }

    public void setPathToSmallImage(String pathToSmallImage) {
        this.pathToSmallImage = pathToSmallImage;
    }

    public String getPathToLargeImage() {
        return pathToLargeImage;
    }

    public void setPathToLargeImage(String pathToLargeImage) {
        this.pathToLargeImage = pathToLargeImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public String getPathToApplicationFile() {
        return pathToApplicationFile;
    }

    public void setPathToApplicationFile(String pathToApplicationFile) {
        this.pathToApplicationFile = pathToApplicationFile;
    }

    public Date getDateOfUpload() {
        return dateOfUpload;
    }

    public void setDateOfUpload(Date dateOfUpload) {
        this.dateOfUpload = dateOfUpload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Application that = (Application) o;

        return applicationName.equals(that.applicationName);

    }

    @Override
    public int hashCode() {
        return applicationName.hashCode();
    }

    @Override
    public String toString() {
        return "Application{" +
                "applicationName='" + applicationName + '\'' +
                ", applicationDescription='" + applicationDescription + '\'' +
                '}';
    }
}
