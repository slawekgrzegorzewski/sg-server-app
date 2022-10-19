package pl.sg.cubes.transport;

import pl.sg.application.security.annotations.AddZoneIdOffsetDuringDeserialization;
import pl.sg.application.api.DomainSimple;
import pl.sg.application.api.WithDomain;
import pl.sg.cubes.model.CubesType;

import java.time.LocalDateTime;

public class CubeRecord implements WithDomain {

    private Integer id;
    private CubesType cubesType;
    private double time;
    private String scramble;
    @AddZoneIdOffsetDuringDeserialization
    private LocalDateTime recordTime;
    private DomainSimple domain;

    @Override
    public Integer getId() {
        return id;
    }

    public CubeRecord setId(Integer id) {
        this.id = id;
        return this;
    }

    public CubesType getCubesType() {
        return cubesType;
    }

    public CubeRecord setCubesType(CubesType cubesType) {
        this.cubesType = cubesType;
        return this;
    }

    public double getTime() {
        return time;
    }

    public CubeRecord setTime(double time) {
        this.time = time;
        return this;
    }

    public String getScramble() {
        return scramble;
    }

    public CubeRecord setScramble(String scramble) {
        this.scramble = scramble;
        return this;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public CubeRecord setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
        return this;
    }

    @Override
    public DomainSimple getDomain() {
        return domain;
    }

    public CubeRecord setDomain(DomainSimple domain) {
        this.domain = domain;
        return this;
    }
}
