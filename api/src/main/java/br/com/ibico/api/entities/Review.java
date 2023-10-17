package br.com.ibico.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "review", nullable = false, columnDefinition = "TEXT")
    private String review;

    @NotNull
    @Column(name = "rating", nullable = false)
    private int rating;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User reviewer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oporunity_id")
    private Oportunity oportunity;

    public Review() {
    }

    public Review(String review, int rating, Oportunity oportunity) {
        this.review = review;
        this.rating = rating;
        this.oportunity = oportunity;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public Oportunity getOportunity() {
        return oportunity;
    }

    public void setOportunity(Oportunity oportunity) {
        this.oportunity = oportunity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review1 = (Review) o;
        return rating == review1.rating && Objects.equals(id, review1.id) && Objects.equals(review, review1.review) && Objects.equals(createdAt, review1.createdAt) && Objects.equals(reviewer, review1.reviewer) && Objects.equals(oportunity, review1.oportunity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, review, rating, createdAt, reviewer, oportunity);
    }
}
