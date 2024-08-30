package specification;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.model.ProductSearch;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> containsName(String name){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%"+name+"%");
    }
    public static Specification<Product> containsCategory(String categoryName){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("category").get("name"), "%"+categoryName+"%");
    }
    public static Specification<Product> hasPriceBetween(double low, double high){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), low, high);
    }
    public static Specification<Product> filter(ProductSearch productSearch){
        Specification<Product> specification = Specification.where(null);
        if(productSearch.getProductName() != null && !productSearch.getProductName().isBlank()){
            specification = specification.and(containsName(productSearch.getProductName()));
        }
        if((productSearch.getCategoryName() != null && !productSearch.getCategoryName().isBlank())){
            specification = specification.and(containsCategory(productSearch.getCategoryName()));
        }
        if(productSearch.getMinimumPrice() > -1 && productSearch.getMaximumPrice() > 0){
            specification = specification.and(hasPriceBetween(productSearch.getMinimumPrice(), productSearch.getMaximumPrice()));
        }
        return specification;
    }
}
