package com.rakesh.splitwise.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
    private String error;
    private String message;
}