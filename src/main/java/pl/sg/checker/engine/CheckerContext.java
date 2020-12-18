package pl.sg.checker.engine;

import pl.sg.checker.model.CheckerTask;

import java.util.Map;

public interface CheckerContext {

    CheckerTask getTask();

    Map<String, Object> getVariables();

    <T> T getVariable(String name);

    <T> void setVariable(String name, T value);
}
