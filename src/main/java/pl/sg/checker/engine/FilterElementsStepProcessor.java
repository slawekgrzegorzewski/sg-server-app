package pl.sg.checker.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import pl.sg.checker.model.FilterElementsStep;

import java.util.*;
import java.util.stream.Collectors;

public class FilterElementsStepProcessor implements StepProcessor<FilterElementsStep> {
    private final CheckerContext context;
    private final List<String> messages = new ArrayList<>();
    private Result result;

    public FilterElementsStepProcessor(CheckerContext context) {
        this.context = context;
    }

    public void process(FilterElementsStep step) {
        List<String> elements = this.context.getVariable(step.getElementsVariable());
        Optional<String> oldVariable = Optional.ofNullable((String) this.context.getVariables().get(step.getElementVariableNameInExpression()));
        Binding binding = new Binding(this.context.getVariables());
        GroovyShell groovyShell = new GroovyShell(binding);
        List<String> filtered = elements.stream()
                .filter(element -> {
                    binding.setVariable(step.getElementVariableNameInExpression(), element);
                    return (Boolean) groovyShell.evaluate(step.getFilterExpression());
                })
                .collect(Collectors.toUnmodifiableList());
        this.context.setVariable(step.getResultVariable(), filtered);
        if (oldVariable.isPresent()) {
            this.context.setVariable(step.getElementVariableNameInExpression(), oldVariable.get());
        } else {
            this.context.getVariables().remove(step.getElementVariableNameInExpression());
        }
        result = Result.OK;
    }

    public Result getResult() {
        return result;
    }

    public List<String> getMessages() {
        return messages;
    }
}
