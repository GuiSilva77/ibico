package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.OportunityDto;
import br.com.ibico.api.entities.payload.OportunityPayload;
import br.com.ibico.api.services.OportunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/oportunities")
public class OportunityController {

    private final OportunityService oportunityService;

    public OportunityController(OportunityService oportunityService) {
        this.oportunityService = oportunityService;
    }

    @Operation(summary = "Busca todas as vagas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vagas encontradas"),
            @ApiResponse(responseCode = "404", description = "Vagas não encontradas", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response<OportunityDto>> findOportunityByName(@RequestParam(value = "query", defaultValue = "", required = false) String query,
                                                            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
                                                            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {
        Response<OportunityDto> oportunity = oportunityService.findOportunities(query, pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.ok(oportunity);
    }

    @Operation(summary = "Busca uma vaga pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga encontrada"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<OportunityDto> findOportunityById(@Validated @PathVariable(name="id") String id) {
        OportunityDto oportunity = oportunityService.findOportunityById(id);

        return ResponseEntity.ok(oportunity);
    }

    @Operation(summary = "Cria uma nova vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<OportunityDto> saveOportunity(@Valid @RequestBody OportunityPayload oportunityPayload) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = (String)authentication.getPrincipal();
        OportunityDto savedOportunity = oportunityService.saveOportunity(oportunityPayload, cpf);

        return ResponseEntity.ok(savedOportunity);
    }

    @Operation(summary = "Atualiza uma vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PutMapping(path = "{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OportunityDto> updateOportunity(@Validated @PathVariable(name="id") String id, @Valid @RequestBody OportunityPayload oportunityPayload) {

        OportunityDto updatedOportunity = oportunityService.updateOportunity(oportunityPayload, id);

        return ResponseEntity.ok(updatedOportunity);
    }

    @Operation(summary = "Deleta uma vaga")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga deletada com sucesso", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Erro na requisição", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<String> deleteOportunity(@Validated @PathVariable(name="id") String id) {
        oportunityService.deactivateOportunity(id);

        return ResponseEntity.ok("Vaga apagada com sucesso!");
    }
}
