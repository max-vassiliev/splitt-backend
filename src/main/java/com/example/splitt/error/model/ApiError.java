package com.example.splitt.error.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private HttpStatus status;
    private String reason;
    private String message;
    private String timestamp;

    public ApiError(HttpStatus status, String timestamp) {
        this.status = status;
        this.timestamp = timestamp;
    }
}
