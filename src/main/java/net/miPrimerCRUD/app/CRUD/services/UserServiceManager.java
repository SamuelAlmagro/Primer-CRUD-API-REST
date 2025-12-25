package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.DTO.UserDTO;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.mapper.UserMapper;
import net.miPrimerCRUD.app.CRUD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceManager implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return (List<User>) this.repository.findAll();
    }

    @Override
    public User findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @Override
    public User save(User user) {
        // Encriptar la contraseña antes de guardar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.repository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        User user1 = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        // Solo actualizar password si viene uno nuevo
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user1.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return this.repository.save(user1);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
        this.repository.deleteById(id);
    }
}