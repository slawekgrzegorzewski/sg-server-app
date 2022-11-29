package pl.sg.ip.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ip.api.TimeRecord;
import pl.sg.ip.api.TimeRecordData;

import java.util.List;


public interface TimeRecordController {

    List<TimeRecord> getUnassociatedTimeRecords(int domainId);

    TimeRecord create(int domainId, TimeRecordData createData);

    void update(int domainId, int timeRecordId, TimeRecordData updateData);

    void delete(int domainId, int timeRecordId);
}
