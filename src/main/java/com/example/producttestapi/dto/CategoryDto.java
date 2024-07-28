package com.example.producttestapi.dto;

import com.example.producttestapi.entities.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class CategoryDto {
    private String name;
    private List<CategoryDto> subCategories = new ArrayList<>();

}
