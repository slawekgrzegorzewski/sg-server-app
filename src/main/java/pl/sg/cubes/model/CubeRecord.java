package pl.sg.cubes.model;

import pl.sg.application.model.Domain;
import pl.sg.application.model.WithDomain;

import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class CubeRecord implements WithDomain<CubeRecord> {

    @Id
    
    @SequenceGenerator(
            name = "commonIdGenerator",
            sequenceName = "hibernate_sequence",
            allocationSize = 1
    )
    @GeneratedValue(generator = "commonIdGenerator")
    private Integer id;
    @Enumerated(EnumType.STRING)
    private CubesType cubesType;
    private Duration time;
    private String scramble;
    private LocalDateTime recordTime;
    @ManyToOne
    private Domain domain;

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

    public Duration getTime() {
        return time;
    }

    public CubeRecord setTime(Duration time) {
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
    public Domain getDomain() {
        return domain;
    }

    @Override
    public CubeRecord setDomain(Domain domain) {
        this.domain = domain;
        return this;
    }
}
