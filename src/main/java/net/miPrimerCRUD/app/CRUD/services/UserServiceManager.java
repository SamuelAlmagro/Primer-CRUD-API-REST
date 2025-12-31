package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public User findByIdWithValidation(Long id) {
        User user = findById(id);
        validateUserAccess(user);
        return user;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        return repository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario actual no encontrado"));
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.repository.save(user);
    }

    @Override
    public User update(Long id, User user) {
        User user1 = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));

        validateUserAccess(user1);

        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user1.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return this.repository.save(user1);
    }

    @Override
    public void deleteById(Long id) {
        User user = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));

        validateUserAccess(user);

        this.repository.deleteById(id);
    }

    private void validateUserAccess(User targetUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = auth.getName();

        User currentUser = repository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario actual no encontrado"));

        if (currentUser.getRole().equals("ADMIN")) {
            return;
        }

        if (!targetUser.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("No tienes permiso para acceder a este usuario");
        }
    }
}