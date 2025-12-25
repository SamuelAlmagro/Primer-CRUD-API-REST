package net.miPrimerCRUD.app.CRUD.repositories;

import net.miPrimerCRUD.app.CRUD.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
