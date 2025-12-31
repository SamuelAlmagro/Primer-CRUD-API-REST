package net.miPrimerCRUD.app.CRUD.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.services.ProductServiceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductServiceManager productService;

    private Product testProduct;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@email.com");
        testUser.setRole("USER");
        testUser.setProducts(new ArrayList<>());

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(99.99);
        testProduct.setUser(testUser);
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testCreateProduct_WithAuth_Success() throws Exception {
        // Arrange
        when(productService.save(any(Product.class))).thenReturn(testProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    void testCreateProduct_WithoutAuth_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllProducts_AsAdmin_Success() throws Exception {
        // Arrange
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(149.99);
        product2.setUser(testUser);

        when(productService.findAll()).thenReturn(Arrays.asList(testProduct, product2));

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllProducts_AsUser_Forbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testGetProductById_WithAuth_Success() throws Exception {
        // Arrange
        when(productService.findById(1L)).thenReturn(testProduct);

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testUpdateProduct_WithAuth_Success() throws Exception {
        // Arrange
        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(199.99);
        updatedProduct.setUser(testUser);

        when(productService.update(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(199.99));
    }

    @Test
    @WithMockUser(username = "test@email.com", roles = "USER")
    void testDeleteProduct_WithAuth_Success() throws Exception {
        // Arrange
        doNothing().when(productService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProduct_WithoutAuth_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isUnauthorized());
    }
}