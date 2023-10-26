package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.OportunityDto;
import br.com.ibico.api.entities.enums.OportunityStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Indexed
@Table(
        name= "oportunities"
)
public class Oportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @FullTextField
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @FullTextField
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @NotNull
    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @NotNull
    @Column(name = "time_load", nullable = false)
    private String timeLoad;

    @NotNull
    @Column(name = "local", nullable = false)
    private String local;

    @NotNull
    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "opportuniy_skills", joinColumns = @JoinColumn(name = "id_opportunity", foreignKey = @ForeignKey(name = "FK_OPPORTUNITY_SKILLS_USERS")), inverseJoinColumns = @JoinColumn(name = "id_skills", foreignKey = @ForeignKey(name = "FK_OPPORTUNITY_SKILLS_SKILLS")))
    private Set<Skill> necessarySkills = new LinkedHashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OportunityStatus status;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;


    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "opportunity_status", nullable = false)
    private OportunityStatus oportunityStatus;

    public Oportunity() {
    }

    public Oportunity(String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime, String timeLoad, String local, BigDecimal value, Set<Skill> necessarySkills, OportunityStatus status) {
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.timeLoad = timeLoad;
        this.local = local;
        this.value = value;
        this.necessarySkills = necessarySkills;
        this.status = status;
    }

    public Oportunity(UUID oportunityId) {
        this.id = oportunityId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getTimeLoad() {
        return timeLoad;
    }

    public void setTimeLoad(String timeLoad) {
        this.timeLoad = timeLoad;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Set<Skill> getNecessarySkills() {
        return necessarySkills;
    }

    public void setNecessarySkills(Set<Skill> necessarySkills) {
        this.necessarySkills = necessarySkills;
    }

    public OportunityStatus getStatus() {
        return status;
    }

    public void setStatus(OportunityStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oportunity that = (Oportunity) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(startDateTime, that.startDateTime) && Objects.equals(endDateTime, that.endDateTime) && Objects.equals(timeLoad, that.timeLoad) && Objects.equals(local, that.local) && Objects.equals(value, that.value) && Objects.equals(necessarySkills, that.necessarySkills) && status == that.status && Objects.equals(createdAt, that.createdAt) && Objects.equals(postedBy, that.postedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, startDateTime, endDateTime, timeLoad, local, value, necessarySkills, status, createdAt, postedBy);
    }

    public OportunityDto toOportunityDto() {
        return new OportunityDto(
                this.id,
                this.title,
                this.description,
                this.startDateTime,
                this.endDateTime,
                this.timeLoad,
                this.local,
                this.value,
                this.necessarySkills.stream()
                        .map(Skill::toSkillDto)
                        .collect(Collectors.toSet()),
                this.status,
                this.createdAt,
                new OportunityDto.UserDto(
                        this.postedBy.getName(),
                        this.postedBy.getUsername(),
                        this.postedBy.getImgURL())
        );
    }
}
