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

        try {
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

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public UserDto getUserByEmail(String email) {

        try {
            UserDto userDto = new UserDto(userRepository.findByEmail(email));

            return userDto;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public UserDto getUserById(Long id) {

        try {
            Optional<User> user = userRepository.findById(id);

            return new UserDto(user);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<UserAdminResponseDto> getAllUsers() {

        try {
            List<User> users = userRepository.findAllUsers();

            return users.stream().map((user) -> new UserAdminResponseDto(user)).collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User updateUser(UserDto userDto) {

        try {
            User user = userRepository.findByEmail(userDto.getEmail());

            user.setFirstName(userDto.getFirstName() != null ? userDto.getFirstName() : user.getFirstName());
            user.setLastName(userDto.getLastName() != null ? userDto.getLastName() : user.getLastName());
            user.setEmail(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail());
            user.setPassword(user.getPassword());
            user.setStripeCustomerId(user.getStripeCustomerId());
            user.setStripePaymentMethodId(user.getStripePaymentMethodId());

            UserRole role = userDto.getRole();
            if (role == UserRole.ADMIN || role == UserRole.USER)
                user.setRole(role);

            return userRepository.saveAndFlush(user);

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteUser(UserDto user) {

        try {
            userRepository.delete(new User(getUserByEmail(user.getEmail())));

            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }

    }

}
