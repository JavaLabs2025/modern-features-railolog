package org.lab.controller;

import org.lab.dto.UserRegistrationRequest;
import org.lab.model.User;
import org.lab.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody UserRegistrationRequest request) {
        try {
            User registeredUser = userService.registerUser(
                    request.login(),
                    request.password(),
                    request.role()
            );

            UserResponse response = new UserResponse(
                    registeredUser.getId(),
                    registeredUser.getLogin(),
                    registeredUser.getRole()
            );

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Registration failed"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @PathVariable Long id
    ) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserResponse response = new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getRole()
        );

        return ResponseEntity.ok(response);
    }

    public static class UserResponse {
        private Long id;
        private String login;
        private org.lab.model.Role role;

        public UserResponse(Long id, String login, org.lab.model.Role role) {
            this.id = id;
            this.login = login;
            this.role = role;
        }

        public Long getId() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public org.lab.model.Role getRole() {
            return role;
        }
    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}