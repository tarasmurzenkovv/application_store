package ua.dataart.repository;

import ua.dataart.model.Application;
import ua.dataart.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("select application from Application application where application.applicationName=:name")
    Application getApplicationByName(@Param("name") String name);

    @Query("select application from Application application order by application.applicationDownloads desc, application.dateOfUpload desc")
    List<Application> getTheMostRatedApplications(Pageable pageable);

    @Query("select application from Application application where application.category =:category")
    List<Application> getApplicationsPerCategory(@Param("category") Category category);

    @Query("select case when count(application) > 0 then true else false end from Application application where application.applicationName=:name")
    Boolean checkIfApplicationNameExists(@Param("name") String name);
}
