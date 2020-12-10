package pl.sg.checker.transport;

import java.time.LocalDateTime;
import java.util.List;

public class PageVersionTO {
    private int id;
    private String content;
    private LocalDateTime versionTime;
    private List<String> elementsAdded;
    private List<String> elementsRemoved;

    public PageVersionTO() {
    }

    public PageVersionTO(int id, String content, LocalDateTime versionTime, List<String> elementsAdded, List<String> elementsRemoved) {
        this.id = id;
        this.content = content;
        this.versionTime = versionTime;
        this.elementsAdded = elementsAdded;
        this.elementsRemoved = elementsRemoved;
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

    public List<String> getElementsAdded() {
        return elementsAdded;
    }

    public PageVersionTO setElementsAdded(List<String> elementsAdded) {
        this.elementsAdded = elementsAdded;
        return this;
    }

    public List<String> getElementsRemoved() {
        return elementsRemoved;
    }

    public PageVersionTO setElementsRemoved(List<String> elementsRemoved) {
        this.elementsRemoved = elementsRemoved;
        return this;
    }
}
