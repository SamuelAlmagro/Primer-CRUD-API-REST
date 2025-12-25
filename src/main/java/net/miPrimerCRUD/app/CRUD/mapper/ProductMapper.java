package net.miPrimerCRUD.app.CRUD.mapper;

import net.miPrimerCRUD.app.CRUD.DTO.ProductDTO;
import net.miPrimerCRUD.app.CRUD.entities.Product;

public class ProductMapper {

    public static ProductDTO toDTO(Product product) {
        if (product == null) return null;
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getUser() != null ? product.getUser().getId() : null,
                product.getUser() != null ? product.getUser().getName() : null
        );
    }

    public static Product toEntity(ProductDTO dto) {
        if (dto == null) return null;
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        return product;
    }
}
