package pl.sg.checker.engine;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import pl.sg.checker.model.PageVersion;
import pl.sg.checker.model.SaveResultStep;
import pl.sg.checker.service.PageVersionsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SaveResultStepProcessor implements StepProcessor<SaveResultStep> {
    private final CheckerContext context;
    private final PageVersionsService pageVersionsService;
    private final List<String> messages = new ArrayList<>();
    private Result result;

    public SaveResultStepProcessor(CheckerContext context, PageVersionsService pageVersionsService) {
        this.context = context;
        this.pageVersionsService = pageVersionsService;
    }

    public void process(SaveResultStep step) {
        List<String> content = this.context.getVariable(step.getResultVariableName());
        Optional<PageVersion> pageVersion = this.pageVersionsService.updateVersion(this.context.getTask(), content);
        if (step.isSendEmailNotification() && pageVersion.isPresent()) {
            PageVersion version = pageVersion.get();
            sendEmail(version.getElementsAdded(), version.getElementsRemoved(), step.getEmailTitle(), step.getEmailHeader(), step.getTo(), step.getCc());
        }
        result = Result.OK;
    }

    private void sendEmail(List<String> added, List<String> removed, String subject, String header, Map<String, String> to, Map<String, String> cc) {
        Email from = new Email("admin@grzegorzewski.org");

        StringBuilder contentBuilder = new StringBuilder(header).append("<div style=\"background-color: #b7e1cd;\">");
        added.stream()
                .map(html -> "<p>" + html + "</p>")
                .forEach(contentBuilder::append);
        contentBuilder.append("</div>");
        contentBuilder.append("<<div style=\"background-color: #f4c7c3; \">");
        removed.stream()
                .map(html -> "<p>" + html + "</p>")
                .forEach(contentBuilder::append);
        contentBuilder.append("</div>");

        Content content = new Content("text/html", contentBuilder.toString());
        List<Email> tos = to.entrySet().stream()
                .map(entry -> new Email(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
        Email firstTo = tos.get(0);
        Mail mail = new Mail(from, subject, firstTo, content);
        Personalization personalization = new Personalization();
        tos.forEach(personalization::addTo);
        cc.entrySet().stream()
                .map(entry -> new Email(entry.getValue(), entry.getKey()))
                .forEach(personalization::addCc);
        mail.addPersonalization(personalization);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            System.out.println("Could not send email");
        }
    }

    public Result getResult() {
        return result;
    }

    public List<String> getMessages() {
        return messages;
    }
}
