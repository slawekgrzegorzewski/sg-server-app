package pl.sg.checker.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PageVersion {
    @Id
    @GeneratedValue
    private int id;
    @Column(length = 200_000)
    private String content;
    private LocalDateTime versionTime;

    public PageVersion() {
    }

    public PageVersion(int id, String content, LocalDateTime versionTime) {
        this.id = id;
        this.content = content;
        this.versionTime = versionTime;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public PageVersion setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getVersionTime() {
        return versionTime;
    }

    public PageVersion setVersionTime(LocalDateTime versionTime) {
        this.versionTime = versionTime;
        return this;
    }
}
