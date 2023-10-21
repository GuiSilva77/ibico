package br.com.ibico.api.services.impl;

import br.com.ibico.api.entities.Response;
import br.com.ibico.api.entities.Review;
import br.com.ibico.api.entities.dto.ReviewDto;
import br.com.ibico.api.entities.payload.ReviewPayload;
import br.com.ibico.api.exceptions.ResourceNotFoundException;
import br.com.ibico.api.repositories.OportunityRepository;
import br.com.ibico.api.repositories.ReviewRepository;
import br.com.ibico.api.repositories.UserRepository;
import br.com.ibico.api.services.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OportunityRepository oportunityRepository;


    public ReviewServiceImpl(ReviewRepository reviewRepository, UserRepository userRepository, OportunityRepository oportunityRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.oportunityRepository = oportunityRepository;
    }

    @Override
    public Response<ReviewDto> findReviewsByOportunityId(String id, int pageNo, int pageSize, String sortBy, String sortDir) {
        Page<Review> page = reviewRepository.findAllByOportunity_Id(UUID.fromString(id), PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.valueOf(sortDir), sortBy)));

        if (page.isEmpty()) {
            return new Response<ReviewDto>(
                    null,
                    page.getNumber(),
                    page.getSize(),
                    page.getNumberOfElements(),
                    page.getTotalPages(),
                    page.isLast(),
                    false
            );
        }

        return new Response<ReviewDto>(
                page.getContent().stream()
                        .map(Review::toReviewDto)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages(),
                page.isLast(),
                false
        );
    }

    @Override
    public ReviewDto findReviewById(String Id) {
        return reviewRepository.findById(UUID.fromString(Id))
                .orElseThrow(() -> new ResourceNotFoundException("Review", "Id", Id))
                .toReviewDto();
    }

    @Override
    public ReviewDto saveReview(ReviewPayload payload, String cpf) {
        Review review = payload.toReview();

        review.setReviewer(userRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User", "", "")));

        review.setOportunity(oportunityRepository.findById(UUID.fromString(payload.oportunityId()))
                .orElseThrow(() -> new ResourceNotFoundException("Oportunity", "", "")));

        return reviewRepository.save(review).toReviewDto();
    }

    @Override
    public ReviewDto updateReview(ReviewPayload reviewDto, String id, String cpf) {
        Review review = reviewDto.toReview();

        review.setReviewer(userRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("User", "", "")));

        review.setOportunity(oportunityRepository.findById(UUID.fromString(reviewDto.oportunityId()))
                .orElseThrow(() -> new ResourceNotFoundException("Oportunity", "", "")));

        return reviewRepository.save(review).toReviewDto();
    }

    @Override
    public void deactivateReview(String id, String cpf) {
        Review review = reviewRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("Review", "Id", id));

        if (!review.getReviewer().getCpf().equals(cpf)) {
            throw new ResourceNotFoundException("Review", "Id", id);
        }

        reviewRepository.delete(review);
    }
}
