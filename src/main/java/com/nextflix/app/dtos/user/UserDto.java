package com.nextflix.app.dtos.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nextflix.app.entities.User;
import com.nextflix.app.entities.WatchList;
import com.nextflix.app.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Not a valid email")
    private String email;
    private String stripeCustomerId;
    private String stripePaymentMethodId;
    private UserRole role;

    private List<WatchList> watchList = new ArrayList<>();

    public UserDto(User user) {
        this.id = user.getId() != null ? user.getId() : null;
        this.password = user.getPassword() != null ? user.getPassword() : null;
        this.firstName = user.getFirstName() != null ? user.getFirstName() : null;
        this.lastName = user.getLastName() != null ? user.getLastName() : null;
        this.email = user.getEmail() != null ? user.getEmail() : null;
        this.stripeCustomerId = user.getStripeCustomerId() != null ? user.getStripeCustomerId() : null;
        this.stripePaymentMethodId = user.getStripePaymentMethodId() != null ? user.getStripePaymentMethodId() : null;

        this.role = user.getRole() != null ? user.getRole() : null;
    }

    public UserDto(Optional<User> user) {

        user.ifPresent(usr -> {
            this.id = usr.getId() != null ? usr.getId() : null;
            this.password = usr.getPassword() != null ? usr.getPassword() : null;
            this.firstName = usr.getFirstName() != null ? usr.getFirstName() : null;
            this.lastName = usr.getLastName() != null ? usr.getLastName() : null;
            this.email = usr.getEmail() != null ? usr.getEmail() : null;
            this.stripeCustomerId = usr.getStripeCustomerId() != null ? usr.getStripeCustomerId() : null;
            this.stripePaymentMethodId = usr.getStripePaymentMethodId() != null ? usr.getStripePaymentMethodId() : null;

            this.role = usr.getRole() != null ? usr.getRole() : null;
        });
    }
}
