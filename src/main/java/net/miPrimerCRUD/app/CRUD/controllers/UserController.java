package net.miPrimerCRUD.app.CRUD.controllers;

import jakarta.validation.Valid;
import net.miPrimerCRUD.app.CRUD.DTO.UserDTO;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.mapper.UserMapper;
import net.miPrimerCRUD.app.CRUD.services.UserServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceManager serviceManager;

    @GetMapping
    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers() {
        return this.serviceManager.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public UserDTO findByIdUser(@PathVariable Long id) {
        User user = this.serviceManager.findByIdWithValidation(id);;
        return UserMapper.toDTO(user);
    }

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        User user = this.serviceManager.getCurrentUser();
        return UserMapper.toDTO(user);
    }

    @PostMapping
    @Transactional
    public UserDTO save(@Valid @RequestBody User user) {
        User savedUser = this.serviceManager.save(user);
        return UserMapper.toDTO(savedUser);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody User user) {
        User updated = this.serviceManager.update(id, user);
        return ResponseEntity.ok(UserMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.serviceManager.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}