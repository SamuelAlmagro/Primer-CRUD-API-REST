package net.miPrimerCRUD.app.CRUD.controllers;

import jakarta.validation.Valid;
import net.miPrimerCRUD.app.CRUD.DTO.ProductDTO;
import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.mapper.ProductMapper;
import net.miPrimerCRUD.app.CRUD.services.ProductServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductServiceManager serviceManager;

    @GetMapping()
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllProducts() {
        return this.serviceManager.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping()
    @Transactional
    public ProductDTO save(@Valid @RequestBody Product product) {
        Product savedProduct = this.serviceManager.save(product);
        return ProductMapper.toDTO(savedProduct);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ProductDTO getByIdProduct(@PathVariable Long id) {
        Product product = this.serviceManager.findById(id);
        return ProductMapper.toDTO(product);
    }

    @GetMapping("/my-products")
    @Transactional(readOnly = true)
    public List<ProductDTO> findMyProducts() {
        return this.serviceManager.findMyProducts()
                .stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product updated = this.serviceManager.update(id, product);
        return ResponseEntity.ok(ProductMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.serviceManager.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}