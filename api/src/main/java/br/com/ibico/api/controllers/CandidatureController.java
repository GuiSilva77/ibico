package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.CandidatureDto;
import br.com.ibico.api.services.CandidatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/candidatures")
public class CandidatureController {

    private final CandidatureService candidatureService;

    public CandidatureController(CandidatureService candidatureService) {
        this.candidatureService = candidatureService;
    }

    @Operation(summary = "Busca todas as candidaturas")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma lista de candidaturas, com paginação e ordenação"),
            @ApiResponse(responseCode = "404", description = "Usuário ou oportunidade não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response<CandidatureDto>> findAllCandidatures(@RequestParam(value = "query", defaultValue = "", required = false) String query,
                                                                        @RequestParam(value = "findBy", defaultValue = "self", required = true) String findBy,
                                                                        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                        @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                        @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {

        if (findBy.equals("self")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String cpf = (String) authentication.getPrincipal();

            return ResponseEntity.ok(candidatureService.findCandidatures(cpf, pageNo, pageSize, sortBy, sortDir));
        }

        if (findBy.equals("oportunity")) {
            return ResponseEntity.ok(candidatureService.findCandidaturesByOportunityId(query, pageNo, pageSize, sortBy, sortDir));
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Busca uma candidatura pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma candidatura", content = @Content(schema = @Schema(implementation = CandidatureDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidatura não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    }
    )
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<CandidatureDto> findCandidatureById(@Validated @PathVariable(name = "id") String candidatureId) {
        return ResponseEntity.ok(candidatureService.findCandidatureById(candidatureId));
    }

    @Operation(summary = "Cria uma candidatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidatura criada", content = @Content(schema = @Schema(implementation = CandidatureDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou oportunidade não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    }
    )
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CandidatureDto> createCandidature(@RequestParam(value = "oportunityId", defaultValue = "", required = true) String oportunityId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = (String) authentication.getPrincipal();

        return ResponseEntity.ok(candidatureService.createCandidature(cpf, oportunityId));
    }

    @Operation(summary = "Deleta uma candidatura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidatura deletada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping("{id}")
    public ResponseEntity deleteCandidature(@Validated @PathVariable(name = "id") String id) {

        candidatureService.deleteCandidature(id);

        return ResponseEntity.ok("Candidatura Deletada");
    }
}
