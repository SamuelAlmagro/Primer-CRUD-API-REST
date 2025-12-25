package net.miPrimerCRUD.app.CRUD.services;

import jakarta.persistence.EntityNotFoundException;
import net.miPrimerCRUD.app.CRUD.entities.User;
import net.miPrimerCRUD.app.CRUD.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceManager implements UserService {
    @Autowired
    private UserRepository repository;

    @Override
    public List<User> findAll() {return (List<User>) this.repository.findAll();}

    @Override
    public User findById(Long id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @Override
    public User save(User user){return this.repository.save(user);}

    @Override
    public User update(Long id, User user) {
        User user1 = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        return this.repository.save(user1);
    }

    @Override
    public void deleteById(Long id) {
        this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
        this.repository.deleteById(id);
    }

}
