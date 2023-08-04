package ru.maxima.springboottest.ProjectSpringBoot1.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthentificationDTO {

    @NotEmpty(message = "Username should not to be empty")
    @Size(min = 4,max = 20, message = "You need to write correct username")
    private String username;

    private String password;
}
