package pl.sg.checker.engine;

import org.jsoup.nodes.Element;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.model.PageElementExtractorStep;

import java.util.ArrayList;
import java.util.List;

public class PageElementExtractorStepProcessor implements StepProcessor<PageElementExtractorStep> {
    private final CheckerContext context;
    private final PageElementExtractor pageElementExtractor;
    private final List<String> messages = new ArrayList<>();
    private Result result;

    public PageElementExtractorStepProcessor(CheckerContext context, PageElementExtractor pageElementExtractor) {
        this.context = context;
        this.pageElementExtractor = pageElementExtractor;
    }

    public void process(PageElementExtractorStep step) {
        String page = this.context.getVariable(step.getPageContentVariable());
        if (page == null) {
            messages.add(String.format("No page in \"%s\"", step.getPageContentVariable()));
            result = Result.ERROR;
        } else {
            List<Element> elements = this.pageElementExtractor.getElements(page, step.getCssQuery());
            if (elements.size() <= step.getElementIndex()) {
                result = Result.WARNING;
                messages.add("Number of elements with specified selector is less than requested index.");
            } else {
                Element element = elements.get(step.getElementIndex());
                if (step.getAttribute() != null) {
                    context.setVariable(step.getResultVariable(), element.attr(step.getAttribute()));
                } else {
                    context.setVariable(step.getResultVariable(), element);
                }
                result = Result.OK;
            }
        }
    }

    public Result getResult() {
        return result;
    }

    public List<String> getMessages() {
        return messages;
    }
}
