package ru.maxima.springboottest.ProjectSpringBoot1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Эта DTO-сушность необходима для регистрации пользователей через REST-запросы
 * */
@Data
public class PersonDTO {
    @NotEmpty(message = "Name should not to be empty")
    @Size(min = 3, max = 30, message = "Name should be between 2 and 30 characters")
    public String name;

    @Min(value = 1, message = "Age should be more than 0")
    public int age;

    @NotEmpty(message = "Password should not to be empty")
    public String password;
}
