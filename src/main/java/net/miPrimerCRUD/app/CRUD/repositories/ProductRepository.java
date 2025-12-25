package net.miPrimerCRUD.app.CRUD.repositories;

import net.miPrimerCRUD.app.CRUD.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
