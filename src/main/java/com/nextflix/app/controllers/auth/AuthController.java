package com.nextflix.app.controllers.auth;

import java.security.Principal;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextflix.app.dtos.auth.LoginDto;
import com.nextflix.app.dtos.user.UserDto;
import com.nextflix.app.dtos.user.UserRegisterDto;
import com.nextflix.app.enums.UserRole;
import com.nextflix.app.services.interfaces.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
 
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpServletRequest request) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getEmail(),
                            loginDto.getPassword()));

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);

            return ResponseEntity.ok().build();

        } catch (AuthenticationException e) {
            SecurityContextHolder.getContext().setAuthentication(null);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto registrationDto, HttpSession httpSession) {
        try {
        
            if (!EmailValidator.getInstance().isValid(registrationDto.getEmail()))
                throw new Exception("Not a valid email address.");
                
            UserDto newUser = new UserDto();
            newUser.setEmail(registrationDto.getEmail());
            newUser.setFirstName(registrationDto.getFirstName());
            newUser.setLastName(registrationDto.getLastName());
            newUser.setPassword(registrationDto.getPassword());

            userService.registerUser(newUser);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/authenticated")
    public ResponseEntity<?> authenticated(Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && principal != null) {
            UserRole role = userService.getUserByEmail(principal.getName()).getRole();
            return ResponseEntity.status(200).body(role);
        } else {
            return ResponseEntity.status(400).build();
        }
    }

 

}