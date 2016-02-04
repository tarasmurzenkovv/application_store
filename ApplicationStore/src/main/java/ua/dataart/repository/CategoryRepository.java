package ua.dataart.repository;

import ua.dataart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select category from Category category where category.categoryName=:name")
    Category getCategoryByName(@Param("name")String categoryName);
}
