package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.OpportunityDto;
import br.com.ibico.api.entities.payload.OpportunityPayload;
import br.com.ibico.api.services.OpportunityService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/opportunities")
public class OpportunityController {

    private final OpportunityService opportunityService;

    public OpportunityController(OpportunityService opportunityService) {
        this.opportunityService = opportunityService;
    }

    @Operation(summary = "Busca todas as vagas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vagas encontradas"),
            @ApiResponse(responseCode = "404", description = "Vagas não encontradas", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response<OpportunityDto>> findOpportunityByName(@RequestParam(value = "query", defaultValue = "", required = false) String query,
                                                                         @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                         @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                                         @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        Response<OpportunityDto> opportunity = opportunityService.findOpportunities(query, pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(opportunity);
    }

    @Operation(summary = "Busca uma vaga pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga encontrada"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<OpportunityDto> findOpportunityById(@Validated @PathVariable(name="id") String id) {
        OpportunityDto opportunity = opportunityService.findOpportunityById(id);

        return ResponseEntity.ok(opportunity);
    }

    @Operation(summary = "Cria uma nova vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vaga criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<OpportunityDto> saveOpportunity(@Valid @RequestBody OpportunityPayload opportunityPayload) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = (String)authentication.getPrincipal();
        OpportunityDto savedOpportunity = opportunityService.saveOpportunity(opportunityPayload, cpf);

        return new ResponseEntity<>(savedOpportunity, HttpStatus.CREATED);
    }

    @Operation(summary = "Atualiza uma vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PutMapping(path = "{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OpportunityDto> updateOpportunity(@Validated @PathVariable(name="id") String id, @Valid @RequestBody OpportunityPayload opportunityPayload) {

        OpportunityDto updatedOpportunity = opportunityService.updateOpportunity(opportunityPayload, id);

        return ResponseEntity.ok(updatedOpportunity);
    }

    @Operation(summary = "Deleta uma vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga deletada com sucesso", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<String> deleteOpportunity(@Validated @PathVariable(name="id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = (String)authentication.getPrincipal();

        opportunityService.deactivateOpportunity(id, cpf);

        return ResponseEntity.ok("Vaga apagada com sucesso!");
    }

    @Operation(summary = "Seleciona um candidato para uma vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato selecionado com sucesso", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping(path = "{opportunityId}/candidates/{candidateUsername}", produces = "plain/text")
    public ResponseEntity<String> selectCandidate(@Validated @PathVariable(name="opportunityId") String opportunityId,
                                                  @Validated @PathVariable(name="candidateUsername") String candidateUsername) {
        opportunityService.selectCandidate(opportunityId, candidateUsername);

        return ResponseEntity.ok("Candidato selecionado com sucesso!");
    }
}
