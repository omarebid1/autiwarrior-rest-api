package com.autiwarrior.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    // Getters and setters
    private String message;
    // You can add more fields like timestamp, error code, etc.

    public ErrorResponse(String message) {
        this.message = message;
    }
}
