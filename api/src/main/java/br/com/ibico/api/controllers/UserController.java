package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.payload.UserPayload;
import br.com.ibico.api.entities.queries.UserQuery;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 *   TODO: Implement User search from his UserDetails
 *   TODO: Implement password reset
 *   TODO: Alter DELETE method to get user from UserDetails
 */

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Response<UserDto>> findUserByName(@RequestParam(value = "query", defaultValue = "", required = false) String query,
                                                           @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                           @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                           @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        Response<UserDto> user = userService.findUsers(query, pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserPayload userPayload) {
        UserDto savedUser = userService.saveUser(userPayload);

        return ResponseEntity.ok(savedUser);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserPayload userPayload) {

        UserDto updatedUser = userService.updateUser(userPayload);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@Valid @RequestBody UserQuery query) {
        userService.deactivateUser(query.cpf());

        return ResponseEntity.ok("Usuário desativado com sucesso!");
    }
}
