package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.UserDto;
import br.com.ibico.api.entities.dto.UserGetDto;
import br.com.ibico.api.entities.dto.UserPutDto;
import br.com.ibico.api.exceptions.ResourceNotValidException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Indexed
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "user_uq_cpf", columnNames = "cpf"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id", nullable = false)
    private UUID id;

    @Size(max = 11)
    @NotNull
    @KeywordField
    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @NotNull
    @FullTextField
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @FullTextField
    @Column(name = "username", nullable = false)
    private String username;

    @Size(min = 8)
    @Column(name = "passwd", nullable = false)
    private String passwd;

    @NotNull
    @Column(name = "date_of_creation", nullable = false)
    private LocalDateTime dateOfCreation;

    @NotNull
    @Column(name = "img_url", nullable = false)
    private String imgURL;

    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_skills", joinColumns = @JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "FK_USER_SKILLS_USERS")), inverseJoinColumns = @JoinColumn(name = "id_skills", foreignKey = @ForeignKey(name = "FK_USER_SKILLS_SKILLS")))
    private Set<Skill> skills = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tb_user_role", joinColumns = @JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "FK_USER_ROLES_USERS")), inverseJoinColumns = @JoinColumn(name = "id_role", foreignKey = @ForeignKey(name = "FK_USER_ROLES_ROLES")))
    private Set<Role> roles = new LinkedHashSet<>();

    @IndexedEmbedded
    @OneToMany(mappedBy = "postedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Oportunity> oportunitiesPosted = new LinkedHashSet<>();

    @IndexedEmbedded
    @OneToMany(mappedBy = "reviewer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews = new LinkedHashSet<>();


    public User() {
    }

    public User(String cpf, String name, String passwd, LocalDateTime dateOfCreation, String imgURL, boolean active, String telephone, Set<Skill> skills, Set<Role> roles) {
        this.cpf = cpf;
        this.name = name;
        this.passwd = passwd;
        this.dateOfCreation = dateOfCreation;
        this.imgURL = imgURL;
        this.active = active;
        this.telephone = telephone;
        this.skills = skills;
        this.roles = roles;
    }

    public User(String cpf, String name, String username, LocalDateTime dateOfCreation, String imgURL, boolean active, String telephone, Set<Skill> skills) {
        this.cpf = cpf;
        this.name = name;
        this.username = username;
        this.dateOfCreation = dateOfCreation;
        this.imgURL = imgURL;
        this.active = active;
        this.telephone = telephone;
        this.skills = skills;
    }

    public User(String name, String username, LocalDateTime dateOfCreation, String imgURL, boolean active, String telephone) {
        this.name = name;
        this.username = username;
        this.dateOfCreation = dateOfCreation;
        this.imgURL = imgURL;
        this.active = active;
        this.telephone = telephone;
    }

    public User(String cpf, String name, String username, String passwd, String imgURL, boolean active, String telephone, Set<Skill> skills) {
        this.cpf = cpf;
        this.name = name;
        this.username = username;
        this.passwd = passwd;
        this.imgURL = imgURL;
        this.active = active;
        this.telephone = telephone;
        this.skills = skills;
    }

    public User(String cpf, String name, String username, String passwd, String imgURL, boolean active, String telephone) {
        this.cpf = cpf;
        this.name = name;
        this.username = username;
        this.passwd = passwd;
        this.imgURL = imgURL;
        this.active = active;
        this.telephone = telephone;
    }

    public static boolean validateCpf(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        // Verify if CPF has 11 digits
        if (cpf.length() != 11) {
            return false;
        }

        // Verify if all digits are equal
        boolean todosDigitosIguais = cpf.matches("(\\d)\\1{10}");
        if (todosDigitosIguais) {
            return false;
        }

        // Calculate the first verification digit
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito > 9) {
            primeiroDigito = 0;
        }

        // verify the first verification digit
        if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito) {
            return false;
        }

        // Calculate the second verification digit
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito > 9) {
            segundoDigito = 0;
        }

        // Verify the second verification digit
        return Character.getNumericValue(cpf.charAt(10)) == segundoDigito;
    }

    @PrePersist
    public void prePersist() {
        if (!validateCpf(this.cpf)) throw new ResourceNotValidException("CPF is not valid");

        if (!this.active && dateOfCreation == null) this.active = true;

        if (this.dateOfCreation == null) this.dateOfCreation = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public UserDto toUserDto() {
        return new UserDto(this.cpf, this.name, this.username, this.dateOfCreation, this.imgURL, this.active, this.telephone, this.skills.stream().map(Skill::toSkillDto).collect(Collectors.toSet()));
    }

    public UserPutDto toUserPutDto() {
        return new UserPutDto(this.name, this.username, this.dateOfCreation, this.imgURL, this.active, this.telephone, this.skills.stream().map(Skill::toSkillDto).collect(Collectors.toSet()));
    }

    public UserDto toUserDtoMinusCPF() {
        return new UserDto("", this.name, this.username, this.dateOfCreation, this.imgURL, this.active, this.telephone, this.skills.stream().map(Skill::toSkillDto).collect(Collectors.toSet()));
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public UserGetDto toUserGetDto() {
        return new UserGetDto(
                name, username, dateOfCreation,
                imgURL, active, telephone,
                skills.stream()
                        .map(Skill::toSkillDto)
                        .collect(Collectors.toSet()),
                oportunitiesPosted.stream()
                        .map(Oportunity::toOportunityDto)
                        .collect(Collectors.toSet()),
                reviews.stream()
                        .map(Review::toReviewDto)
                        .collect(Collectors.toSet()));
    }
}
