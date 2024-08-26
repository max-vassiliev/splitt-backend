package com.example.splitt.user.dto;

import com.example.splitt.util.validation.annotations.AtLeastOneFieldNotNull;
import com.example.splitt.util.validation.annotations.ValidAvatar;
import jakarta.validation.constraints.Email;
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
@AtLeastOneFieldNotNull(
        fields = {"name", "email", "password", "avatar"},
        message = "Either of these fields must be not null: {fields}"
)
public class UserUpdateDto {

    private String name;

    @Email(message = "Email Not Well-Formed. Must be a well-formed email address.")
    private String email;

    @Size(min = 8, max = 50, message = "Password Size Error. " +
            "Password must be between {min} and {max} characters long.")
    private String password;

    @ValidAvatar
    @Size(max = 50, message = "User Avatar Name Size Exceeded. Must not exceed {max} characters.")
    private String avatar;

}
