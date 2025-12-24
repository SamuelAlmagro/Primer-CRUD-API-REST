package net.miPrimerCRUD.app.CRUD.services;

import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio {@link ProductService}.
 *
 * Esta clase contiene la lógica de negocio para gestionar productos.
 * Actúa como capa intermedia entre los controladores (web) y el repositorio (acceso a datos).
 *
 * En este CRUD básico, la lógica es mínima (solo delega al repositorio),
 * pero tener esta capa separada permite agregar fácilmente validaciones,
 * transacciones, conversiones o reglas de negocio en el futuro.
 *
 * @author TuNombre
 * @version 1.0
 */
@Service // Indica a Spring que esta clase es un componente de servicio (bean gestionado por el contenedor)
public class ProductServiceManager implements ProductService{
    /**
     * Inyección de dependencia del repositorio.
     * @Autowired le dice a Spring: "Busca un bean de tipo ProductRepository e inyéctalo aquí".
     * Spring lo crea automáticamente porque la interfaz extiende CrudRepository.
     */
    @Autowired
    private ProductRepository repository;

    /**
     * Devuelve todos los productos de la base de datos.
     * findAll() del repositorio devuelve un Iterable<Product>,
     * por eso lo convertimos a List<Product> para que coincida con la interfaz.
     * @return Lista con todos los productos (vacía si no hay ninguno)
     */
    @Override
    public List<Product> findAll() {
        return (List<Product>) this.repository.findAll();
    }
    /**
     * Busca un producto por su ID.
     * Nota importante: usamos .get() directamente sobre el Optional.
     * Esto lanza una excepción si el producto no existe (NoSuchElementException).
     * Recomendación futura: usar .orElseThrow() con una excepción personalizada
     * para manejar mejor el error (ej: ProductNotFoundException).
     * @param id ID del producto a buscar
     * @return El producto encontrado
     */
    @Override
    public Product findById(Long id) {
        return this.repository.findById(id).get();
    }
    /**
     * Guarda un producto en la base de datos.
     * - Si el producto es nuevo (id == null), lo inserta y genera el ID automáticamente.
     * - Si el producto ya existe (id no null), lo actualiza.
     * @param product El producto a guardar (no debe ser null)
     * @return El producto guardado, con el ID actualizado si era nuevo
     */
    @Override
    public Product save(Product product) {
        return this.repository.save(product);
    }
    /**
     * Actualiza un producto existente identificándolo por su ID.
     *
     * Busca el producto en la base de datos, modifica sus campos name y price
     * con los valores recibidos, y lo guarda de nuevo.
     *
     * @param id      ID del producto que se quiere actualizar
     * @param product Objeto que contiene los nuevos valores para name y price
     * @return El producto ya actualizado con los nuevos datos
     * Exception si no existe un producto con ese ID
     */
    @Override
    public Product update(Long id, Product product) {
        // Buscamos el producto existente (si no existe, lanza excepción)
        Product prod = this.repository.findById(id).get();
        // Actualizamos los campos modificables
        prod.setName(product.getName());
        prod.setPrice(product.getPrice());
        // Guardamos los cambios en la base de datos
        return this.repository.save(prod);
    }

    /**
     * Elimina un producto de la base de datos por su ID.
     * Primero verifica que el producto exista (para lanzar error temprano si no),
     * y luego lo borra definitivamente.
     * @param id ID del producto a eliminar
     * Exception si no existe un producto con ese ID
     */
    @Override
    public void deleteById(Long id) {
        // Verificamos que el producto exista (lanza excepción si no)
        this.repository.findById(id);
        // Procedemos a eliminarlo de la base de datos
        this.repository.deleteById(id);
    }

}
