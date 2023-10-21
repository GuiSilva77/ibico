package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.services.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/skills")
public class SkillsController {

    private final SkillService skillService;

    public SkillsController(SkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(summary = "Busca todas as habilidades cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habilidades encontradas, com paginação e ordenação"),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response<SkillDto>> getSkills(
            @RequestParam(value = "query", defaultValue = "", required = true) String query,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {

        return ResponseEntity.ok(skillService.findSkills(query, pageSize, pageNo, sortBy, sortDir));
    }

    @Operation(summary = "Adiciona uma nova habilidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habilidade adicionada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<SkillDto> addSkill(@Valid @RequestBody SkillDto skillDto) {
        return ResponseEntity.ok(skillService.addSkill(skillDto));
    }


    @Operation(summary = "Atualiza uma habilidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habilidade atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PutMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<SkillDto> updateSkill(@Valid @RequestBody SkillDto skillDto) {
        return ResponseEntity.ok(skillService.updateSkill(skillDto));
    }
}
