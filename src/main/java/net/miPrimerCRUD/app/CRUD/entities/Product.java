package net.miPrimerCRUD.app.CRUD.entities;
import jakarta.persistence.*;

// @Entity indica que esta clase es una "entidad" de JPA.
// Eso significa que representa una tabla en la base de datos y que
// los objetos de esta clase se pueden guardar, actualizar, borrar y buscar
// automáticamente usando un EntityManager o un Repository de Spring Data.
@Entity

// @Table especifica el nombre exacto de la tabla en la base de datos.
@Table(name = "products")
public class Product {

    // @Id marca este campo como la clave primaria (primary key) de la tabla.
    // Es el campo que identifica de forma única cada registro.
    @Id

    // @GeneratedValue indica que el valor de este campo se genera automáticamente.
    // strategy = GenerationType.IDENTITY significa que la base de datos se encarga
    // de generar el ID usando una columna auto-incremental (por ejemplo: 1, 2, 3, 4...).
    // Muy común en MySQL, PostgreSQL, SQL Server, etc.
    // Cuando guardas un nuevo producto, no necesitas ponerle un id manualmente.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                  // Clave primaria de tipo long (número grande)

    // Estos campos se convierten automáticamente en columnas de la tabla y no son clave primaria.
    private String name;
    private Double price;

    // ==================== GETTERS Y SETTERS ====================
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }

    // ==================== CONSTRUCTORES ====================

    // Constructor vacío (sin parámetros)
    // ES OBLIGATORIO en entidades JPA porque el framework necesita poder crear
    // instancias vacías cuando recupera datos de la base de datos.
    public Product() {
    }

    // Constructor con todos los parámetros
    // Útil cuando quieres crear un producto directamente en código
    // (aunque normalmente al guardar uno nuevo solo pasas name y price,
    //  porque el id lo genera la BD).
    public Product(long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}