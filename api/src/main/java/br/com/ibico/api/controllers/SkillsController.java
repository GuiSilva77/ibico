package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.SkillDto;
import br.com.ibico.api.services.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/skills")
public class SkillsController {

    private final SkillService skillService;

    public SkillsController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<Response<SkillDto>> getSkills(
            @RequestParam(value = "query", defaultValue = "", required = true) String query,
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {

        return ResponseEntity.ok(skillService.findSkills(query, pageSize, pageNo, sortBy, sortDir));
    }

    @PostMapping
    public ResponseEntity<SkillDto> addSkill(@Valid @RequestBody SkillDto skillDto) {
        return ResponseEntity.ok(skillService.addSkill(skillDto));
    }

    @PutMapping
    public ResponseEntity<SkillDto> updateSkill(@Valid @RequestBody SkillDto skillDto) {
        return ResponseEntity.ok(skillService.updateSkill(skillDto));
    }
}
