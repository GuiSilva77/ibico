package br.com.ibico.api.entities;

import br.com.ibico.api.entities.dto.PasswordResetRequestDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "password_reset_requests",
    uniqueConstraints = {
            @UniqueConstraint(name = "UQ_PASSWORD_RESET_REQUEST_ACCESS_TOKEN", columnNames =  "access_token")})
public class PasswordResetRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID requestId;

    @JoinColumn(name = "code_id", referencedColumnName = "requestId")
    @OneToOne(fetch = FetchType.LAZY)
    private PasswordResetCode passwordResetCode;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;
    
    public static final int EXPIRATION_TIME = 10;

    @Column(name = "used", nullable = false)
    private boolean used;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(PasswordResetCode passwordResetCode, String accessToken, LocalDateTime createdAt, LocalDateTime expirationDate, boolean used) {
        this.passwordResetCode = passwordResetCode;
        this.accessToken = accessToken;
        this.createdAt = createdAt;
        this.expirationDate = expirationDate;
        this.used = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public PasswordResetCode getPasswordResetCode() {
        return passwordResetCode;
    }

    public void setPasswordResetCode(PasswordResetCode passwordResetCode) {
        this.passwordResetCode = passwordResetCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public PasswordResetRequestDto toDto() {
        return new PasswordResetRequestDto(
                accessToken, expirationDate
        );
    }
}
