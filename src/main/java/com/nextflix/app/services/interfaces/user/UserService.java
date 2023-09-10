package com.nextflix.app.services.interfaces.user;

import java.util.List;

import com.nextflix.app.dtos.user.UserAdminResponseDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.entities.User;

public interface UserService {
        User registerUser(UserDto user) throws Exception;

        UserDto getUserByEmail(String email);

        UserDto getUserById(Long id);

        List<UserAdminResponseDto> getAllUsers();

        User updateUser(UserDto user);
        
        boolean deleteUser(UserDto user);
}
