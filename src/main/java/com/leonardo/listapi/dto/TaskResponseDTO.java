
package com.leonardo.listapi.dto;

public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        boolean completed
) {
}
