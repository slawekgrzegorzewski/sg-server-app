package pl.sg.checker.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import pl.sg.checker.PageElementExtractor;
import pl.sg.checker.model.PageVersion;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class PageVersionsService {
    private final PageVersionRepository pageVersionRepository;
    private final PageElementExtractor pageElementExtractor;

    public PageVersionsService(PageVersionRepository pageVersionRepository, PageElementExtractor pageElementExtractor) {
        this.pageVersionRepository = pageVersionRepository;
        this.pageElementExtractor = pageElementExtractor;
    }

    public List<PageVersion> getAllVersions() {
        return this.pageVersionRepository.findAll();
    }

    public void updateVersion(String content) {
        Optional<PageVersion> lastVersion = this.pageVersionRepository.getTopByOrderByVersionTime();
        List<Element> currentValue;
        if (lastVersion.isEmpty()) {
            currentValue = List.of();
        } else {
            currentValue = this.pageElementExtractor.getElements(lastVersion.get().getContent(), "p");
        }
        List<Element> newValue = this.pageElementExtractor.getElements(content, "p");
        currentValue = filterNoOfVisitsOut(currentValue);
        newValue = filterNoOfVisitsOut(newValue);

        List<Element> added = findAddedElements(currentValue, newValue);
        List<Element> removed = findRemovedElements(currentValue, newValue);
        if (added.size() > 0 || removed.size() > 0) {
            PageVersion newVersion = new PageVersion()
                    .setElementsAdded(added.stream().map(Element::html).collect(Collectors.toUnmodifiableList()))
                    .setElementsRemoved(removed.stream().map(Element::html).collect(Collectors.toUnmodifiableList()))
                    .setContent(content)
                    .setVersionTime(LocalDateTime.now());
            this.pageVersionRepository.save(newVersion);
            sendEmail(added, removed);
        }
    }

    private void sendEmail(List<Element> added, List<Element> removed) {
        Email from = new Email("admin@grzegorzewski.org");
        String subject = "Coś się zmieniło na stronie www.szkockiewrzosowisko.pl";
        Email to = new Email("alicja.grzegorzewska@gmail.com");
        Email cc = new Email("slawek.grz@gmail.com");

        StringBuilder contentBuilder = new StringBuilder("<div>Hej Alicja, na twojej stronie zmieniły się przed chwilą")
                .append(" następujące rzeczy</div>")
                .append("<div style=\"background-color: #b7e1cd;\">");
        added.stream()
                .map(Element::html)
                .map(html -> "<p>" + html + "</p>")
                .forEach(contentBuilder::append);
        contentBuilder.append("</div>");
        contentBuilder.append("<<div style=\"background-color: #f4c7c3; \">");
        removed.stream()
                .map(Element::html)
                .map(html -> "<p>" + html + "</p>")
                .forEach(contentBuilder::append);
        contentBuilder.append("</div>");

        Content content = new Content("text/html", contentBuilder.toString());
        Mail mail = new Mail(from, subject, to, content);
        Personalization personalization = new Personalization();
        personalization.addTo(to);
        personalization.addCc(cc);
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

    private List<Element> filterNoOfVisitsOut(List<Element> currentValue) {
        Pattern visitsPattern = Pattern.compile("Wizyt: [0-9]{6,}");
        return currentValue.stream()
                .filter(e -> !visitsPattern.matcher(e.text()).find())
                .collect(Collectors.toUnmodifiableList());
    }

    private List<Element> findAddedElements(List<Element> currentValue, List<Element> newValue) {
        return listsDifference(currentValue, newValue);
    }

    private List<Element> findRemovedElements(List<Element> currentValue, List<Element> newValue) {
        return listsDifference(newValue, currentValue);
    }

    private List<Element> listsDifference(List<Element> currentValue, List<Element> newValue) {
        return newValue.stream()
                .filter(e -> currentValue.stream()
                        .map(Element::text)
                        .noneMatch(e.text()::equals))
                .collect(Collectors.toList());
    }
}
