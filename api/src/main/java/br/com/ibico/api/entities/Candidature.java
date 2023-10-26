package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.CandidatureDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "candidatorships")
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
    private Oportunity oportunity;

    public Candidature() {
    }

    public Candidature(User candidate, Oportunity oportunity) {
        this.candidate = candidate;
        this.oportunity = oportunity;
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
        Candidature that = (Candidature) o;
        return Objects.equals(id, that.id) && Objects.equals(candidatureDate, that.candidatureDate) && Objects.equals(candidate, that.candidate) && Objects.equals(oportunity, that.oportunity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, candidatureDate, candidate, oportunity);
    }

    public CandidatureDto toCandidatureDto() {
        return new CandidatureDto(
                this.id,
                this.candidatureDate,
                this.candidate.getName(),
                this.candidate.getUsername(),
                this.candidate.getImgURL(),
                this.oportunity.getId());
    }
}
