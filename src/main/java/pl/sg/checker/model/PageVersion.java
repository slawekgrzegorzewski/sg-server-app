package pl.sg.checker.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class PageVersion {
    @Id
    @GeneratedValue
    private int id;
    @Column(length = 200_000)
    private String content;
    private LocalDateTime versionTime;
    @ElementCollection
    @Column(length = 200_000)
    private List<String> elementsAdded;
    @ElementCollection()
    @Column(length = 200_000)
    private List<String> elementsRemoved;

    public PageVersion() {
    }

    public PageVersion(int id, String content, LocalDateTime versionTime, List<String> elementsAdded, List<String> elementsRemoved) {
        this.id = id;
        this.content = content;
        this.versionTime = versionTime;
        this.elementsAdded = elementsAdded;
        this.elementsRemoved = elementsRemoved;
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

    public List<String> getElementsAdded() {
        return elementsAdded;
    }

    public PageVersion setElementsAdded(List<String> elementsAdded) {
        this.elementsAdded = elementsAdded;
        return this;
    }

    public List<String> getElementsRemoved() {
        return elementsRemoved;
    }

    public PageVersion setElementsRemoved(List<String> elementsRemoved) {
        this.elementsRemoved = elementsRemoved;
        return this;
    }
}
