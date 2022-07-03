package pl.sg.banks.integrations.nodrigen.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class NodrigenAccess {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(columnDefinition="TEXT")
    private String accessToken;
    private Long accessExpires;
    private LocalDateTime accessExpiresAt;
    @Column(columnDefinition="TEXT")
    private String refreshToken;
    private Long refreshExpires;
    private LocalDateTime refreshExpiresAt;
    private LocalDateTime archivedAt;

    public Integer getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getAccessExpires() {
        return accessExpires;
    }

    public void setAccessExpires(Long accessExpires) {
        this.accessExpires = accessExpires;
    }

    public LocalDateTime getAccessExpiresAt() {
        return accessExpiresAt;
    }

    public void setAccessExpiresAt(LocalDateTime accessExpiresAt) {
        this.accessExpiresAt = accessExpiresAt;
    }

    public Optional<String> getRefreshToken() {
        return Optional.ofNullable(refreshToken);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Optional<Long> getRefreshExpires() {
        return Optional.ofNullable(refreshExpires);
    }

    public void setRefreshExpires(Long refreshExpires) {
        this.refreshExpires = refreshExpires;
    }

    public Optional<LocalDateTime> getRefreshExpiresAt() {
        return Optional.ofNullable(refreshExpiresAt);
    }

    public void setRefreshExpiresAt(LocalDateTime refreshExpiresAt) {
        this.refreshExpiresAt = refreshExpiresAt;
    }

    public Optional<LocalDateTime> getArchivedAt() {
        return Optional.ofNullable(archivedAt);
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }
}
