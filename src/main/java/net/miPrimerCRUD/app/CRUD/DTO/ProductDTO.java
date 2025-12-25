package net.miPrimerCRUD.app.CRUD.DTO;

import jakarta.validation.constraints.*;

public class ProductDTO {
    private Long id;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(min = 2, max = 200, message = "El nombre debe tener entre 2 y 200 caracteres")
    private String name;

    @NotNull(message = "El precio es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double price;

    private Long userId;
    private String userName;

    // Constructores
    public ProductDTO() {}

    public ProductDTO(Long id, String name, Double price, Long userId, String userName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.userId = userId;
        this.userName = userName;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
