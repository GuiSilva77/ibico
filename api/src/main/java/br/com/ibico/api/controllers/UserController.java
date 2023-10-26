package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.dto.UserGetDto;
import br.com.ibico.api.entities.dto.UserPutDto;
import br.com.ibico.api.entities.payload.UserPayload;
import br.com.ibico.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Busca informações do usuário logado ou de uma busca por nome")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de usuários, com paginação e ordenação"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    }
    )
    @GetMapping(produces = {"application/json","application/xml"})
    public ResponseEntity<Response<UserDto>> findUserByName(@RequestParam(value = "query", defaultValue = "", required = false) String query,
                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {

        Response<UserDto> user;

        if (query.isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDto userDto = userService.findUserByCpf((String)authentication.getPrincipal());
            user = new Response<>(List.of(userDto), 1, 1, 1, 1, true, true);
        } else {
            user = userService.findUsers(query, pageNo, pageSize, sortBy, sortDir);
        }
        return ResponseEntity.ok(user);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Busca um usuário pelo username")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    }
    )
    @GetMapping(path = "{username}", produces = "application/json")
    public ResponseEntity<UserGetDto> findUserByUsername(@PathVariable("username") String username) {
        UserGetDto user = userService.findUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Salva um usuário")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "201", description = "Retorna o usuário salvo"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    }
    )
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserPayload userPayload) {
        UserDto savedUser = userService.saveUser(userPayload);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualiza um usuário")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Retorna o usuário atualizado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserPutDto> updateUser(@Valid @RequestBody UserPayload userPayload) {

        UserPutDto updatedUser = userService.updateUser(userPayload);

        return ResponseEntity.ok(updatedUser);
    }

    @SecurityRequirement(name = "bearerAuth")

    @Operation(summary = "Desativa um usuário")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma mensagem de sucesso", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = (String)authentication.getPrincipal();

        userService.deactivateUser(cpf);

        return ResponseEntity.ok("Usuário desativado com sucesso!");
    }
}
