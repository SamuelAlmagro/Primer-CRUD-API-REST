package net.miPrimerCRUD.app.CRUD.controllers;

import jakarta.validation.Valid;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.services.UserServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceManager serviceManager;

    @GetMapping
    @Transactional(readOnly = true)
    public List<User> findAllUsers(){return this.serviceManager.findAll();}

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public User findByidUser(@PathVariable Long id){return this.serviceManager.findById(id);}

    @PostMapping
    @Transactional
    public User save(@Valid @RequestBody User user){return this.serviceManager.save(user);}

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody User user){
        User updated = this.serviceManager.update(id, user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        this.serviceManager.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
