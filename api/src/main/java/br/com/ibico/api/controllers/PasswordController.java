package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.dto.PasswordResetCodeDto;
import br.com.ibico.api.entities.dto.PasswordResetRequestDto;
import br.com.ibico.api.entities.payload.PasswordResetCodePayload;
import br.com.ibico.api.entities.payload.PasswordResetPayload;
import br.com.ibico.api.services.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Operation(summary = "Gera um código de validação para reset de senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código de validação gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "CPF inválido", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(path = "generateCode/{cpf}", produces = "application/json")
    public ResponseEntity<PasswordResetCodeDto> requestPasswordResetCode(@Validated @PathVariable String cpf) {
        PasswordResetCodeDto passwordResetCodeDto = passwordService.generatePasswordResetCode(cpf);
        return ResponseEntity.ok(passwordResetCodeDto);
    }

    @Operation(summary = "Valida o código de reset de senha e retorna um token de acesso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Código de validação gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Código de validação inválido", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(path = "validateCode", produces = "application/json", consumes = "application/json")
    public ResponseEntity<PasswordResetRequestDto> validatePasswordResetCode(@Valid @RequestBody PasswordResetCodePayload payload) {
        PasswordResetRequestDto passwordResetRequestDtoResponse = passwordService.validatePasswordResetCode(payload);
        return ResponseEntity.ok(passwordResetRequestDtoResponse);
    }

    @Operation(summary = "Redefine a senha do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Token de acesso inválido", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(path = "resetPassword", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetPayload payload) {
        passwordService.resetPassword(payload.accessToken(), payload.newPassword());

        return ResponseEntity.ok("Password Reset successfully");
    }


}
