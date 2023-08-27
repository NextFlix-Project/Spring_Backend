package com.nextflix.app.dtos.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nextflix.app.entities.User;
import com.nextflix.app.entities.WatchList;
import com.nextflix.app.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAdminResponseDto {

    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    private List<WatchList> watchList = new ArrayList<>();

    public UserAdminResponseDto(User user) {
        this.id = user.getId() != null ? user.getId() : null;
        this.firstName = user.getFirstName() != null ? user.getFirstName() : null;
        this.lastName = user.getLastName() != null ? user.getLastName() : null;
        this.email = user.getEmail() != null ? user.getEmail() : null;
        this.role = user.getRole() != null ? user.getRole() : null;
    }

    public UserAdminResponseDto(Optional<User> user) {

        user.ifPresent(usr -> {
            this.id = usr.getId() != null ? usr.getId() : null;
            this.firstName = usr.getFirstName() != null ? usr.getFirstName() : null;
            this.lastName = usr.getLastName() != null ? usr.getLastName() : null;
            this.email = usr.getEmail() != null ? usr.getEmail() : null;
            this.role = usr.getRole() != null ? usr.getRole() : null;
        });
    }
}
