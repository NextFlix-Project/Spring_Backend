package com.nextflix.app.services.implementations.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nextflix.app.dtos.user.UserAdminResponseDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.entities.User;
import com.nextflix.app.enums.UserRole;
import com.nextflix.app.repositories.user.UserRepository;
import com.nextflix.app.services.interfaces.user.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDto userDto) throws Exception {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user != null) {
            throw new Exception("User already exists with this email.");
        }

        User newUser = new User();

        newUser.setEmail(userDto.getEmail());
        newUser.setPassword("{bcrypt}" + passwordEncoder.encode(userDto.getPassword()));
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setRole(UserRole.USER);

        return userRepository.save(newUser);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserDto userDto = new UserDto(userRepository.findByEmail(email));

        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return new UserDto(user);
    }

    @Override
    public List<UserAdminResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map((user) -> new UserAdminResponseDto(user)).collect(Collectors.toList());
    }

    @Override
    public User updateUser(UserDto userDto) {

        User user = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        UserRole role = userDto.getRole();
        if (role == UserRole.ADMIN || role == UserRole.USER)
            user.setRole(role);

        return userRepository.saveAndFlush(user);
    }

}
