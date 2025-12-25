package net.miPrimerCRUD.app.CRUD.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
public class User {
    // ==================== Variables ====================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2,max = 100,message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    @Size(max = 100, message = "El email no puede tener mas de 100 caracteres")
    private String email;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();
    // ==================== GETTERS Y SETTERS ====================
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public List<Product> getProducts() {return products;}
    public void setProducts(List<Product> products) {this.products = products;}
    // ==================== CONSTRUCTORES ====================
    public User() {}
    public User(String email, List<Product> products, String name, Long id) {
        this.email = email;
        this.products = products;
        this.name = name;
        this.id = id;
    }
}
