package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.CandidatureDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "candidatures")
public class Candidature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "candidature_date", nullable = false)
    private LocalDateTime candidatureDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User candidate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Opportunity opportunity;

    public Candidature() {
    }

    public Candidature(User candidate, Opportunity opportunity) {
        this.candidate = candidate;
        this.opportunity = opportunity;
    }

    @PrePersist
    public void prePersist() {
        if (this.candidatureDate == null) this.candidatureDate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCandidatureDate() {
        return candidatureDate;
    }

    public void setCandidatureDate(LocalDateTime candidatureDate) {
        this.candidatureDate = candidatureDate;
    }

    public User getCandidate() {
        return candidate;
    }

    public void setCandidate(User candidate) {
        this.candidate = candidate;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidature that = (Candidature) o;
        return Objects.equals(id, that.id) && Objects.equals(candidatureDate, that.candidatureDate) && Objects.equals(candidate, that.candidate) && Objects.equals(opportunity, that.opportunity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, candidatureDate, candidate, opportunity);
    }

    public CandidatureDto toCandidatureDto() {
        return new CandidatureDto(
                this.id,
                this.candidatureDate,
                this.candidate.getName(),
                this.candidate.getUsername(),
                this.candidate.getImgURL(),
                this.opportunity.getId());
    }
}
