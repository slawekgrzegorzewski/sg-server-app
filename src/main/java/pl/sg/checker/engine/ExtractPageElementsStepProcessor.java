package pl.sg.checker.engine;

import org.jsoup.nodes.Element;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.model.ExtractPageElementsStep;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExtractPageElementsStepProcessor implements StepProcessor<ExtractPageElementsStep> {
    private final CheckerContext context;
    private final PageElementExtractor pageElementExtractor;
    private final List<String> messages = new ArrayList<>();
    private Result result;

    public ExtractPageElementsStepProcessor(CheckerContext context, PageElementExtractor pageElementExtractor) {
        this.context = context;
        this.pageElementExtractor = pageElementExtractor;
    }

    public void process(ExtractPageElementsStep step) {
        String pageContent = this.context.getVariable(step.getPageContentVariable());
        if (pageContent == null) {
            messages.add(String.format("No page in \"%s\"", step.getPageContentVariable()));
            result = Result.ERROR;
        } else {
            List<String> elements = this.pageElementExtractor.getElements(pageContent, step.getCssQuery())
                    .stream()
                    .map(Element::toString)
                    .collect(Collectors.toUnmodifiableList());
            this.context.setVariable(step.getResultVariable(), elements);
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
