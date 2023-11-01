package com.example.splitt.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateDto {

    private String name;

    @Email(message = "Email Not Well-Formed. Must be a well-formed email address.")
    private String email;

    @Size(min = 8, max = 50, message = "Password Size Error. " +
            "Password must be between {min} and {max} characters long.")
    private String password;

}
