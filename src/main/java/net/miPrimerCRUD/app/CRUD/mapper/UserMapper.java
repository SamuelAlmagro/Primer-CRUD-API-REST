package net.miPrimerCRUD.app.CRUD.mapper;

import net.miPrimerCRUD.app.CRUD.DTO.UserDTO;
import net.miPrimerCRUD.app.CRUD.entities.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}