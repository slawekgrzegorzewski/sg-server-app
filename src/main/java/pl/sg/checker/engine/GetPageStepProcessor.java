package pl.sg.checker.engine;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import pl.sg.checker.PageFetcher;
import pl.sg.checker.model.GetPageContentStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetPageStepProcessor implements StepProcessor<GetPageContentStep> {
    private final CheckerContext context;
    private final PageFetcher fetcher;
    private final List<String> messages = new ArrayList<>();
    private Result result;

    public GetPageStepProcessor(CheckerContext context, PageFetcher fetcher) {
        this.context = context;
        this.fetcher = fetcher;
    }

    public void process(GetPageContentStep step) {
        Binding binding = new Binding(this.context.getVariables());
        String pageUrl = (String) new GroovyShell(binding).evaluate(step.getPageUrlExpression());

        Optional<String> page = this.fetcher.getPage(pageUrl);
        if (page.isEmpty()) {
            result = Result.ERROR;
            messages.add("Invalid url or page is empty");
        } else {
            this.context.setVariable(step.getVariableName(), page.get());
        }
    }

    public Result getResult() {
        return result;
    }

    public List<String> getMessages() {
        return messages;
    }
}
