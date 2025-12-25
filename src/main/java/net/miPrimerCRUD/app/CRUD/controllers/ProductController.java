package net.miPrimerCRUD.app.CRUD.controllers;

import jakarta.validation.Valid;
import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.services.ProductServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST que expone los endpoints HTTP para realizar operaciones CRUD sobre productos.
 *
 * Todos los endpoints están bajo la ruta base: /api/products
 *
 * Ejemplos de URLs:
 * - GET    /api/products          → listar todos
 * - POST   /api/products          → crear nuevo
 * - GET    /api/products/{id}     → buscar por ID
 * - PUT    /api/products/{id}     → actualizar
 * - DELETE /api/products/{id}     → eliminar
 */

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductServiceManager serviceManager;

    @GetMapping()
    @Transactional(readOnly = true)
    public List<Product> findAllProducts(){
        return this.serviceManager.findAll();
    }

    @PostMapping()
    @Transactional
    public Product save(@Valid @RequestBody Product product) {
        return this.serviceManager.save(product);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public Product getByIdProduct(@PathVariable Long id){
        return this.serviceManager.findById(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Product product){
        Product updated = this.serviceManager.update(id, product);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        this.serviceManager.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
