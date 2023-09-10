package com.nextflix.app.controllers.user;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.user.UserAdminResponseDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.enums.UserRole;
import com.nextflix.app.services.interfaces.user.UserService;

@RestController
@RequestMapping("/api/v1/admin/user")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @PutMapping("/updaterole")
    public ResponseEntity<?> updateUserRole(@RequestBody UserDto userDto) {

        if (userDto.getRole() != UserRole.ADMIN && userDto.getRole() != UserRole.USER)
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            UserDto updateUserDto = userService.getUserByEmail(email);
            updateUserDto.setRole(userDto.getRole());
            userService.updateUser(updateUserDto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto) {

        try {

            userService.updateUser(userDto);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(3000).build();
        }
    }

    @GetMapping("/getallusers")
    public ResponseEntity<?> getAllUsers() {

        try {
            List<UserAdminResponseDto> allUsers = userService.getAllUsers();
            return ResponseEntity.ok(allUsers); 
            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/deleteuser")
    public ResponseEntity<?> deleteUser(@RequestBody UserDto user){
        try{
            userService.deleteUser(user);
             return ResponseEntity.status(200).build();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return ResponseEntity.notFound().build();
    }

}