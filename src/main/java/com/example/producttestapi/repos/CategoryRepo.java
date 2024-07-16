package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Integer> {
    @Query(value ="SELECT * FROM category WHERE parent_category_id is null" , nativeQuery = true)
    List<Category> findParentCategory();
    @Query(value = "SELECT * FROM category WHERE parent_category_id =:id",nativeQuery = true)
    List<Category> findSubCategoriesOfParentCategory(int id);
}
