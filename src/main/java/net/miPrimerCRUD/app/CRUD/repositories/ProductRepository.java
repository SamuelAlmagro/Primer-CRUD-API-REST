package net.miPrimerCRUD.app.CRUD.repositories;

import net.miPrimerCRUD.app.CRUD.entities.Product;
import org.springframework.data.repository.CrudRepository;

//en: CrudRepository<Product, Long>
// - Primera parte: entidad = Product
// - Segunda parte: tipo del ID = Long (porque id es long)
public interface ProductRepository extends CrudRepository<Product, Long> {

    /**
     * No es necesario agregar métodos aquí para un CRUD básico.
     *
     * Al extender CrudRepository<Product, Long>, automáticamente disponemos de los siguientes métodos:
     *
     * - save(Product) → Guarda o actualiza un producto
     * - findById(Long) → Busca por ID (devuelve Optional<Product>)
     * - findAll() → Devuelve todos los productos
     * - count() → Cuenta el número total de productos
     * - deleteById(Long) → Elimina por ID
     * - delete(Product) → Elimina una entidad específica
     * - existsById(Long) → Verifica si existe un producto con ese ID
     *
     * Si en el futuro necesitas consultas personalizadas (por nombre, precio, etc.),
     * puedes agregar métodos aquí siguiendo las reglas de nombrado de Spring Data
     * (ej: findByNameContainingIgnoreCase(String name)).
     */

}
