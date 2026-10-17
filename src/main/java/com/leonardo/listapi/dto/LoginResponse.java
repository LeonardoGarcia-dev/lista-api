
package com.leonardo.listapi.dto;

public record LoginResponse(
        String token,
        String tokenType
) {}
