package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceManagerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceManager userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@email.com");
        testUser.setPassword("password123");
        testUser.setRole("USER");
        testUser.setProducts(new ArrayList<>());
    }

    @Test
    void testFindAll_ReturnsUserList() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        user2.setEmail("user2@email.com");
        user2.setRole("USER");
        user2.setProducts(new ArrayList<>());

        List<User> users = Arrays.asList(testUser, user2);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_UserExists_ReturnsUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test User", result.getName());
        assertEquals("test@email.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_UserNotExists_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findById(999L);
        });

        assertTrue(exception.getMessage().contains("999"));
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_EncryptsPassword() {
        // Arrange
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$encrypted");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.save(testUser);

        // Assert
        assertNotNull(result);
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdate_UpdatesUserFields() {
        // Arrange
        User updatedData = new User();
        updatedData.setName("Updated Name");
        updatedData.setEmail("updated@email.com");
        updatedData.setPassword("newpassword");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("$2a$10$newencrypted");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        User result = userService.update(1L, updatedData);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(passwordEncoder, times(1)).encode("newpassword");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testUpdate_UserNotExists_ThrowsException() {
        // Arrange
        User updatedData = new User();
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.update(999L, updatedData);
        });
    }

    @Test
    void testDeleteById_UserExists_DeletesSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.deleteById(1L);

        // Assert
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_UserNotExists_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteById(999L);
        });
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}