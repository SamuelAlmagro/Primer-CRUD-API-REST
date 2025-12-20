package net.miPrimerCRUD.app.CRUD.services;

import net.miPrimerCRUD.app.CRUD.entities.Product;

import java.util.List;

/**
 * Interfaz del servicio para la entidad {@link Product}.
 *
 * Define las operaciones de negocio básicas para gestionar productos.
 * Esta capa actúa como intermediaria entre los controladores (web) y los repositorios (base de datos),
 * permitiendo agregar lógica adicional en el futuro (validaciones, cálculos, transacciones, etc.).
 *
 * En aplicaciones simples como este primer CRUD, el servicio suele ser una "capa delgada"
 * que delega directamente al repositorio, pero es una buena práctica tenerlo separado
 * para mantener el código limpio y escalable.
 *
 */

public interface ProductService {
    /**
     * Devuelve todos los productos almacenados en la base de datos.
     * @return Lista de todos los productos. Si no hay ninguno, devuelve una lista vacía.
     */
    List<Product> findAll();

    /**
     * Busca un producto por su identificador único (ID).
     * @param id El ID del producto a buscar. No puede ser null.
     * @return El producto encontrado.
     * @throws RuntimeException (o excepción personalizada) si no existe un producto con ese ID.
     */
    Product findByID(Long id);

    /**
     * Guarda un producto en la base de datos.
     * Sirve tanto para crear uno nuevo como para actualizar uno existente.
     * - Si el producto es nuevo (id = null), se inserta y la base de datos genera el ID.
     * - Si el producto ya existe (id no null), se actualiza.
     * @param product El producto a guardar. No puede ser null.
     * @return El producto guardado, con el ID asignado (en caso de ser nuevo).
     */
    Product save(Product product);

}
