package net.miPrimerCRUD.app.CRUD.controllers;

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
    /**
     * Inyección de dependencia del servicio.
     * Spring crea automáticamente un bean de ProductServiceManager y lo inyecta aquí.
     */
    @Autowired
    private ProductServiceManager serviceManager;

    /**
     * GET /api/products
     * Lista todos los productos existentes.
     *
     * @return Lista de productos en formato JSON
     */
    @GetMapping()
    @Transactional(readOnly = true)
    public List<Product> findAllProducts(){
        return this.serviceManager.findAll();
    }

    /**
     * POST /api/products
     * Crea un nuevo producto.
     * @param product Objeto Product recibido en el cuerpo de la petición (JSON)
     * @return El producto creado con su ID generado por la BD
     */
    @PostMapping()
    @Transactional
    public Product save(@RequestBody Product product) {
        return this.serviceManager.save(product);
    }

    /**
     * GET /api/products/{id}
     * Busca un producto por su ID.
     * @param id ID del producto (viene en la URL)
     * @return El producto encontrado
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public Product getByIdProduct(@PathVariable Long id){
        return this.serviceManager.findById(id);
    }

    /**
     * PUT /api/products/{id}
     * Actualiza un producto existente.
     * @param id      ID del producto a actualizar (en la URL)
     * @param product Datos nuevos recibidos en el cuerpo JSON
     * @return Respuesta con el producto actualizado o 404 si no existe
     */
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id,@RequestBody Product product){
        // Optional.of() lanza excepción si es null → aquí no es necesario porque findById ya lo hace
        Optional<Product> product1 = Optional.of(this.serviceManager.findById(id));

        if(product1.isPresent()){
            Product newProduct = product1.get();
            newProduct.setName(product.getName());
            newProduct.setPrice(product.getPrice());

            return ResponseEntity.status(HttpStatus.CREATED).body(this.serviceManager.update(id,newProduct));
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE /api/products/{id}
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar
     * @return 204 No Content si se borró correctamente
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        this.serviceManager.findById(id);
        this.serviceManager.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
