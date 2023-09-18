package com.nextflix.app.dtos.user;

import java.util.Optional;

import com.nextflix.app.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Not a valid email")
    private String email;

    public UserRegisterDto(User user) {
        this.password = user.getPassword() != null ? user.getPassword() : null;
        this.firstName = user.getFirstName() != null ? user.getFirstName() : null;
        this.lastName = user.getLastName() != null ? user.getLastName() : null;
        this.email = user.getEmail() != null ? user.getEmail() : null;
    }

    public UserRegisterDto(Optional<User> user) {

        user.ifPresent(usr -> {
            this.password = usr.getPassword() != null ? usr.getPassword() : null;
            this.firstName = usr.getFirstName() != null ? usr.getFirstName() : null;
            this.lastName = usr.getLastName() != null ? usr.getLastName() : null;
            this.email = usr.getEmail() != null ? usr.getEmail() : null;

        });
    }
}
