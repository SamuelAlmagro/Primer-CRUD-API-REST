package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.repositories.ProductRepository;
import net.miPrimerCRUD.app.CRUD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    // Nuevo método: listar solo los productos del usuario actual
    public List<Product> findMyProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario actual no encontrado"));

        // Filtrar productos que pertenecen al usuario actual
        return ((List<Product>) repository.findAll())
                .stream()
                .filter(product -> product.getUser() != null &&
                        product.getUser().getId().equals(currentUser.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        Product product = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));

        validateOwnership(product);
        return product;
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

        validateOwnership(prod);

        prod.setName(product.getName());
        prod.setPrice(product.getPrice());

        if (product.getUser() != null && product.getUser().getId() != null) {
            User user = userRepository.findById(product.getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + product.getUser().getId() + " no encontrado"));
            prod.setUser(user);
        }

        return this.repository.save(prod);
    }

    @Override
    public void deleteById(Long id) {
        Product product = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));

        validateOwnership(product);

        this.repository.deleteById(id);
    }

    private void validateOwnership(Product product) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario actual no encontrado"));

        if (currentUser.getRole().equals("ADMIN")) {
            return;
        }

        if (product.getUser() == null || !product.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para acceder a este producto");
        }
    }
}