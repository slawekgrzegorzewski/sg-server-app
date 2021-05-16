package pl.sg.cubes.transport;

import pl.sg.application.transport.DomainTO;
import pl.sg.application.transport.WithDomainTO;
import pl.sg.cubes.model.CubesType;

import java.time.LocalDateTime;

public class CubeRecordTO implements WithDomainTO {

    private Integer id;
    private CubesType cubesType;
    private double time;
    private String scramble;
    private LocalDateTime recordTime;
    private DomainTO domain;

    @Override
    public Integer getId() {
        return id;
    }

    public CubeRecordTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public CubesType getCubesType() {
        return cubesType;
    }

    public CubeRecordTO setCubesType(CubesType cubesType) {
        this.cubesType = cubesType;
        return this;
    }

    public double getTime() {
        return time;
    }

    public CubeRecordTO setTime(double time) {
        this.time = time;
        return this;
    }

    public String getScramble() {
        return scramble;
    }

    public CubeRecordTO setScramble(String scramble) {
        this.scramble = scramble;
        return this;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public CubeRecordTO setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
        return this;
    }

    @Override
    public DomainTO getDomain() {
        return domain;
    }

    public CubeRecordTO setDomain(DomainTO domain) {
        this.domain = domain;
        return this;
    }
}
