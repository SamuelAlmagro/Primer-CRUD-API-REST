package net.miPrimerCRUD.app.CRUD.services;

import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Product findByID(Long id) {
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
}
