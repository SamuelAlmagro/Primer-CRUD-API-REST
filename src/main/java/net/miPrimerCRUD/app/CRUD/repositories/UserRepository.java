package net.miPrimerCRUD.app.CRUD.repositories;

import net.miPrimerCRUD.app.CRUD.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
