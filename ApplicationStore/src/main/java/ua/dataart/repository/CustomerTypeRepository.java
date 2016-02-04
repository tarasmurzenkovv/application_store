package ua.dataart.repository;

import ua.dataart.model.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CustomerTypeRepository extends JpaRepository<CustomerType, Long> {
}
