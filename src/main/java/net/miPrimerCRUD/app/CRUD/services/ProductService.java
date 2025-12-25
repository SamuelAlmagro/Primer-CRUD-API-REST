package net.miPrimerCRUD.app.CRUD.services;

import net.miPrimerCRUD.app.CRUD.entities.Product;
import java.util.List;


public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    Product update(Long id,Product product);
    void deleteById(Long id);
}
