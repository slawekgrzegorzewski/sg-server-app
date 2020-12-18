package pl.sg.checker.engine;

import pl.sg.checker.model.StoreResultStep;

import java.util.ArrayList;
import java.util.List;

public class StoreResultStepProcessor implements StepProcessor<StoreResultStep> {
    private final CheckerContext context;
    private final List<String> messages = new ArrayList<>();
    private Result result;

    public StoreResultStepProcessor(CheckerContext context) {
        this.context = context;
    }

    public void process(StoreResultStep step) {
        this.context.setVariable("result", this.context.getVariable(step.getGetResultFromVariable()));
    }

    public Result getResult() {
        return result;
    }

    public List<String> getMessages() {
        return messages;
    }
}
