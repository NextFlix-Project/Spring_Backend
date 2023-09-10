package com.nextflix.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nextflix.app.dtos.user.UserResponseDto;
import com.nextflix.app.dtos.user.UserAdminResponseDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.enums.UserRole;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column 
    private String stripeCustomerId;    

    @Column String stripePaymentMethodId;
    
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Subscription subscription;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<WatchList> watchList = new ArrayList<>();

    public User(UserAdminResponseDto userResponseDto) {
        this.id = userResponseDto.getId() != null ? userResponseDto.getId() : null;
        this.firstName = userResponseDto.getFirstName() != null ? userResponseDto.getFirstName() : null;
        this.lastName = userResponseDto.getLastName() != null ? userResponseDto.getLastName() : null;
        this.email = userResponseDto.getEmail() != null ? userResponseDto.getEmail() : null;
        this.role = userResponseDto.getRole() != null ? userResponseDto.getRole() : null;
    }

    public User(UserResponseDto userResponseDto) {
        this.id = userResponseDto.getId() != null ? userResponseDto.getId() : null;
        this.firstName = userResponseDto.getFirstName() != null ? userResponseDto.getFirstName() : null;
        this.lastName = userResponseDto.getLastName() != null ? userResponseDto.getLastName() : null;
        this.email = userResponseDto.getEmail() != null ? userResponseDto.getEmail() : null;
    }

    public User(UserDto user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}