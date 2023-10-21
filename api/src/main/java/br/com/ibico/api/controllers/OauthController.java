package br.com.ibico.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/v1/oauth")
public class OauthController {

    /**
     * {@link br.com.ibico.api.filters.JWTTokenGeneratorFilter} will handle this request
     */
    @Operation(summary = "Gera um token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token gerado"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping(path = "token", produces = "application/json")
    public void getToken() {
    }


}
