package br.com.ibico.api.controllers;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.ReviewDto;
import br.com.ibico.api.entities.payload.ReviewPayload;
import br.com.ibico.api.services.ReviewService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @Operation(summary = "Busca uma avaliação pelo id e pela oportunidade")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação encontrada"),
            @ApiResponse(responseCode = "404", description = "Avaliação ou Oportunidade não encontrada", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Response<ReviewDto>> getReviews(@RequestParam(value = "id", defaultValue = "", required = true) String id,
                                                          @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "id", required = true) String sortBy,
                                                          @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir) {

        if(sortBy.equals("id")) {
            Response<ReviewDto> review = new Response<>(List.of(reviewService.findReviewById(id)), 1, 1, 1, 1, true, true);
            return ResponseEntity.ok(review);
        }

        Response<ReviewDto> review = reviewService.findReviewsByOportunityId(id, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(review);
    }

    @Operation(summary = "Salva uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação salva"),
            @ApiResponse(responseCode = "400", description = "Avaliação inválida", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReviewDto> saveReview(@Valid @RequestBody ReviewPayload reviewPayload) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(reviewService.saveReview(reviewPayload, (String)authentication.getPrincipal()));
    }

    @Operation(summary = "Atualiza uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada"),
            @ApiResponse(responseCode = "400", description = "Avaliação inválida", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content(schema = @Schema(hidden = true))),
    })
    @PutMapping(path = "{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReviewDto> updateReview(@Valid @RequestBody ReviewPayload reviewPayload,
                                                  @PathVariable(name = "id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(reviewService.updateReview(reviewPayload, id, (String)authentication.getPrincipal()));
    }

    @Operation(summary = "Deleta uma avaliação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação deletada com sucesso", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Usuário não autenticado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Usuário não autorizado", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "Avaliação não encontrada", content = @Content(schema = @Schema(hidden = true))),
    })
    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<Void> deactivateReview(@PathVariable(name = "id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        reviewService.deactivateReview(id, (String)authentication.getPrincipal());
        return ResponseEntity.ok().build();
    }
}
