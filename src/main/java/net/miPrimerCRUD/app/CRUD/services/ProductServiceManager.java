package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.repositories.ProductRepository;
import net.miPrimerCRUD.app.CRUD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceManager implements ProductService{

    @Autowired
    private ProductRepository repository;

    @Autowired
    private UserRepository userRepository;

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
        if (product.getUser() != null && product.getUser().getId() != null) {
            User user = userRepository.findById(product.getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + product.getUser().getId() + " no encontrado"));
            product.setUser(user);
        }
        return this.repository.save(product);
    }

    @Override
    public Product update(Long id, Product product) {
        Product prod = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));

        prod.setName(product.getName());
        prod.setPrice(product.getPrice());

        // Actualizar el usuario si viene en el request
        if (product.getUser() != null && product.getUser().getId() != null) {
            User user = userRepository.findById(product.getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + product.getUser().getId() + " no encontrado"));
            prod.setUser(user);
        }

        return this.repository.save(prod);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));
        this.repository.deleteById(id);
    }
}