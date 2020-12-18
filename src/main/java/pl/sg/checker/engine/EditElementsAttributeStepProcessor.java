package pl.sg.checker.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.model.EditElementsAttributeStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditElementsAttributeStepProcessor implements StepProcessor<EditElementsAttributeStep> {
    private final CheckerContext context;
    private final PageElementExtractor pageElementExtractor;
    private final List<String> messages = new ArrayList<>();
    private Result result;

    public EditElementsAttributeStepProcessor(CheckerContext context, PageElementExtractor pageElementExtractor) {
        this.context = context;
        this.pageElementExtractor = pageElementExtractor;
    }

    public void process(EditElementsAttributeStep step) {
        String pageContent = this.context.getVariable(step.getPageContentVariable());
        if (pageContent == null) {
            messages.add(String.format("No page in \"%s\"", step.getPageContentVariable()));
            result = Result.ERROR;
        } else {
            String newContent = this.pageElementExtractor.visitElements(pageContent, step.getCssQuery(), element -> {
                Optional<Object> oldVariable = Optional.ofNullable(this.context.getVariables().get(step.getAttributeVariableNameInExpression()));
                this.context.getVariables().put(step.getAttributeVariableNameInExpression(), element.attr(step.getAttribute()));
                Binding binding = new Binding(this.context.getVariables());
                element.attr(
                        step.getAttribute(),
                        (String) new GroovyShell(binding).evaluate(step.getOperationExpression())
                );
                if (oldVariable.isPresent()) {
                    this.context.getVariables().put(step.getAttributeVariableNameInExpression(), oldVariable.get());
                } else {
                    this.context.getVariables().remove(step.getAttributeVariableNameInExpression());
                }
            });
            this.context.setVariable(step.getResultVariable(), newContent);
            result = Result.OK;
        }
    }

    public Result getResult() {
        return result;
    }

    public List<String> getMessages() {
        return messages;
    }
}
