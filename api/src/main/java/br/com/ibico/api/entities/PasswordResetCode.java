package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.PasswordResetCodeDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "password_reset_code")
public class PasswordResetCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID requestId;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public final static int EXPIRATION_TIME = 10;

    public PasswordResetCode() {
    }

    public PasswordResetCode(User user, String code, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.user = user;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetCode that = (PasswordResetCode) o;
        return Objects.equals(requestId, that.requestId) && Objects.equals(user, that.user) && Objects.equals(code, that.code) && Objects.equals(createdAt, that.createdAt) && Objects.equals(expiresAt, that.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, user, code, createdAt, expiresAt);
    }

    public PasswordResetCodeDto toDto() {
        return new PasswordResetCodeDto(requestId, code, expiresAt);
    }
}
