package pl.sg.integrations.nodrigen.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class NodrigenAccess {
    @Id
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    @Column(columnDefinition="TEXT")
    private String accessToken;
    private LocalDateTime accessExpiresAt;
    @Column(columnDefinition="TEXT")
    private String refreshToken;
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
