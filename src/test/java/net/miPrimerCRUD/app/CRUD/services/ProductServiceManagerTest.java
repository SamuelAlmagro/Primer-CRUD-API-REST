package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.entities.Product;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.repositories.ProductRepository;
import net.miPrimerCRUD.app.CRUD.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceManagerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
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
    void testFindAll_ReturnsProductList() {
        // Arrange
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");
        product2.setPrice(149.99);
        product2.setUser(testUser);

        List<Product> products = Arrays.asList(testProduct, product2);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ProductExists_ReturnsProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act
        Product result = productService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(99.99, result.getPrice());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ProductNotExists_ThrowsException() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.findById(999L);
        });

        assertTrue(exception.getMessage().contains("999"));
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_WithUser_SavesSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.save(testProduct);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testSave_UserNotExists_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        testProduct.getUser().setId(999L);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.save(testProduct);
        });
    }

    @Test
    void testUpdate_UpdatesProductFields() {
        // Arrange
        Product updatedData = new Product();
        updatedData.setName("Updated Product");
        updatedData.setPrice(199.99);
        updatedData.setUser(testUser);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // Act
        Product result = productService.update(1L, updatedData);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(testProduct);
    }

    @Test
    void testUpdate_ProductNotExists_ThrowsException() {
        // Arrange
        Product updatedData = new Product();
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.update(999L, updatedData);
        });
    }

    @Test
    void testDeleteById_ProductExists_DeletesSuccessfully() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).deleteById(1L);

        // Act
        productService.deleteById(1L);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ProductNotExists_ThrowsException() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteById(999L);
        });
        verify(productRepository, times(1)).findById(999L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}