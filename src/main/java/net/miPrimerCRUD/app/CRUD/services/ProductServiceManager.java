package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceManager implements ProductService{
    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> findAll() {
        return (List<Product>) this.repository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
    }

    @Override
    public Product save(Product product) {
        return this.repository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product prod = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
        prod.setName(product.getName());
        prod.setPrice(product.getPrice());
        return this.repository.save(prod);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
        this.repository.deleteById(id);
    }

}
