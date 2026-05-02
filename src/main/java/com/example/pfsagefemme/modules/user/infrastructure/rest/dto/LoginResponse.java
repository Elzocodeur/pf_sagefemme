package com.example.pfsagefemme.modules.user.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resultat d'une authentification reussie")
public record LoginResponse(
        @Schema(description = "JWT a passer dans 'Authorization: Bearer <token>'") String token,
        @Schema(description = "Type du token") String tokenType,
        @Schema(description = "Duree de validite en secondes") long expiresIn,
        UserResponse user
) {
    public static LoginResponse of(String token, long expiresIn, UserResponse user) {
        return new LoginResponse(token, "Bearer", expiresIn, user);
    }
}
