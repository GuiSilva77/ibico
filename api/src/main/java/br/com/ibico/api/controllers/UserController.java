package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.payload.UserPayload;
import br.com.ibico.api.entities.queries.UserQuery;
import br.com.ibico.api.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserDto> findUserByCpf(@RequestBody UserQuery userQuery) {
        UserDto user = userService.findUserByCpf(userQuery.cpf());

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserPayload userPayload) {
        UserDto savedUser = userService.saveUser(userPayload);

        return ResponseEntity.ok(savedUser);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserPayload userPayload) {

        UserDto updatedUser = userService.updateUser(userPayload);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@RequestBody UserQuery query) {
        userService.deactivateUser(query.cpf());

        return ResponseEntity.ok("Usuário desativado com sucesso!");
    }
}
