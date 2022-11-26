package pl.sg.ip.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ip.api.TimeRecord;
import pl.sg.ip.api.TimeRecordData;
import pl.sg.ip.service.TimeRecordService;

@RestController
@RequestMapping("/time-record")
public class TimeRecordControllerImpl implements TimeRecordController {

    private final ModelMapper modelMapper;
    private final TimeRecordService timeRecordService;

    public TimeRecordControllerImpl(ModelMapper modelMapper, TimeRecordService timeRecordService) {
        this.modelMapper = modelMapper;
        this.timeRecordService = timeRecordService;
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = "IPR")
    public TimeRecord create(@RequestHeader("domainId") int domainId,
                             @RequestBody TimeRecordData taskData) {
        return modelMapper.map(

                timeRecordService.createWithoutTask(domainId, taskData),
                TimeRecord.class);
    }
}
