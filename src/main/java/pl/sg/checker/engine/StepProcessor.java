package pl.sg.checker.engine;

import pl.sg.checker.model.CheckerStep;

import java.util.List;

public interface StepProcessor<T extends CheckerStep> {

    void process(T step);

    Result getResult();

    List<String> getMessages();
}
