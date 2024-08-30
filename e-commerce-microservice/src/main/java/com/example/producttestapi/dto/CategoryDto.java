package com.example.producttestapi.dto;

import com.example.producttestapi.entities.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class CategoryDto {
    @NotEmpty(message = "Category name is required")
    @Length(min = 2, max = 20, message = "Category name can't be less than 2 characters and more than 20 characters")
    private String name;
    private List<CategoryDto> subCategories = new ArrayList<>();
    private String parentCategoryName;

}
