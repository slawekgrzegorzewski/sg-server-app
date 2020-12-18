package pl.sg.checker.model;

import com.google.gson.Gson;
import pl.sg.checker.engine.Result;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class CheckerTaskHistory {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    CheckerTask task;
    @Enumerated(EnumType.STRING)
    Result result;
    LocalDateTime runTime;
    @Column(length = 10_000)
    String messages;

    public CheckerTaskHistory() {
    }

    public CheckerTaskHistory(int id, CheckerTask task, Result result, LocalDateTime runTime, String messages) {
        this.id = id;
        this.task = task;
        this.result = result;
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public CheckerTask getTask() {
        return task;
    }

    public CheckerTaskHistory setTask(CheckerTask task) {
        this.task = task;
        return this;
    }

    public Result getResult() {
        return result;
    }

    public CheckerTaskHistory setResult(Result result) {
        this.result = result;
        return this;
    }

    public LocalDateTime getRunTime() {
        return runTime;
    }

    public CheckerTaskHistory setRunTime(LocalDateTime runTime) {
        this.runTime = runTime;
        return this;
    }

    public List<String> getMessages() {
        return new Gson().fromJson(messages, List.class);
    }

    public CheckerTaskHistory setMessages(List<String> messages) {
        this.messages = new Gson().toJson(messages);
        return this;
    }
}
