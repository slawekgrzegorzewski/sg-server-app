package pl.sg.checker.engine;

import java.util.List;

public class TaskResult {
    private final Result result;
    private final List<String> messages;

    public TaskResult(Result result, List<String> messages) {
        this.result = result;
        this.messages = messages;
    }

    public Result getResult() {
        return result;
    }

    public List<String> getMessages() {
        return messages;
    }
}
