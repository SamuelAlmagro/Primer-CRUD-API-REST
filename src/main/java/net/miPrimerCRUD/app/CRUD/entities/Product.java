package net.miPrimerCRUD.app.CRUD.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class Product {
    // ==================== Variables ====================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // ==================== GETTERS Y SETTERS ====================
    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Double getPrice() {return price;}
    public void setPrice(Double price) {this.price = price;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    // ==================== CONSTRUCTORES ====================
    public Product() {}
    public Product(User user, Double price, String name, long id) {
        this.user = user;
        this.price = price;
        this.name = name;
        this.id = id;
    }
}