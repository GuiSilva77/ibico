package br.com.ibico.api.services;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.dto.ReviewDto;
import br.com.ibico.api.entities.payload.ReviewPayload;

public interface ReviewService {

    Response<ReviewDto> findReviews(String query, int pageNo, int pageSize, String sortBy, String sortDir);
    ReviewDto findReviewById(String Id);
    ReviewDto saveReview(ReviewPayload payload, String cpf);
    ReviewDto updateReview(ReviewPayload reviewDto, String id, String cpf);
    void deactivateReview(String id, String cpf);
}
