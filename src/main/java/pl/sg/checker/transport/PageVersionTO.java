package pl.sg.checker.transport;

import java.time.LocalDateTime;

public class PageVersionTO {
    private int id;
    private String content;
    private LocalDateTime versionTime;

    public PageVersionTO() {
    }

    public PageVersionTO(int id, String content, LocalDateTime versionTime) {
        this.id = id;
        this.content = content;
        this.versionTime = versionTime;
    }

    public int getId() {
        return id;
    }

    public PageVersionTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PageVersionTO setContent(String content) {
        this.content = content;
        return this;
    }

    public LocalDateTime getVersionTime() {
        return versionTime;
    }

    public PageVersionTO setVersionTime(LocalDateTime versionTime) {
        this.versionTime = versionTime;
        return this;
    }
}
