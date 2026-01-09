package org.lab.dto;

import org.lab.model.Role;

public record UserRegistrationRequest(
        String login,
        String password,
        Role role
) {
}
