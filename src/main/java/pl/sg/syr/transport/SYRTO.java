package pl.sg.syr.transport;

import java.time.Year;

public class SYRTO<T extends SYRTO> {
    protected int id;
    protected Year year;
    protected Integer peak;
    protected Integer average;
    protected Integer averagePreviousYear;
    protected Integer percentIncrease;
    protected Integer baptized;
    protected Integer averageAuxiliaryPioneers;
    protected Integer averagePioneers;
    protected Integer numberOfCongregations;
    protected Integer totalHours;
    protected Integer averageBibleStudies;
    protected Integer memorialAttendance;

    public SYRTO() {
    }

    public SYRTO(int id, Year year, Integer numberOfCountries, Integer peak, Integer average, Integer averagePreviousYear, Integer percentIncrease, Integer baptized, Integer averageAuxiliaryPioneers, Integer averagePioneers, Integer numberOfCongregations, Integer totalHours, Integer averageBibleStudies, Integer memorialAttendance) {
        this.id = id;
        this.year = year;
        this.peak = peak;
        this.average = average;
        this.averagePreviousYear = averagePreviousYear;
        this.percentIncrease = percentIncrease;
        this.baptized = baptized;
        this.averageAuxiliaryPioneers = averageAuxiliaryPioneers;
        this.averagePioneers = averagePioneers;
        this.numberOfCongregations = numberOfCongregations;
        this.totalHours = totalHours;
        this.averageBibleStudies = averageBibleStudies;
        this.memorialAttendance = memorialAttendance;
    }

    public int getId() {
        return id;
    }

    public T setId(int id) {
        this.id = id;
        return (T) this;
    }

    public Year getYear() {
        return year;
    }

    public T setYear(Year year) {
        this.year = year;
        return (T) this;
    }

    public Integer getPeak() {
        return peak;
    }

    public T setPeak(Integer peak) {
        this.peak = peak;
        return (T) this;
    }

    public Integer getAverage() {
        return average;
    }

    public T setAverage(Integer average) {
        this.average = average;
        return (T) this;
    }

    public Integer getAveragePreviousYear() {
        return averagePreviousYear;
    }

    public T setAveragePreviousYear(Integer averagePreviousYear) {
        this.averagePreviousYear = averagePreviousYear;
        return (T) this;
    }

    public Integer getPercentIncrease() {
        return percentIncrease;
    }

    public T setPercentIncrease(Integer percentIncrease) {
        this.percentIncrease = percentIncrease;
        return (T) this;
    }

    public Integer getBaptized() {
        return baptized;
    }

    public T setBaptized(Integer baptized) {
        this.baptized = baptized;
        return (T) this;
    }

    public Integer getAverageAuxiliaryPioneers() {
        return averageAuxiliaryPioneers;
    }

    public T setAverageAuxiliaryPioneers(Integer averageAuxiliaryPioneers) {
        this.averageAuxiliaryPioneers = averageAuxiliaryPioneers;
        return (T) this;
    }

    public Integer getAveragePioneers() {
        return averagePioneers;
    }

    public T setAveragePioneers(Integer averagePioneers) {
        this.averagePioneers = averagePioneers;
        return (T) this;
    }

    public Integer getNumberOfCongregations() {
        return numberOfCongregations;
    }

    public T setNumberOfCongregations(Integer numberOfCongregations) {
        this.numberOfCongregations = numberOfCongregations;
        return (T) this;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public T setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
        return (T) this;
    }

    public Integer getAverageBibleStudies() {
        return averageBibleStudies;
    }

    public T setAverageBibleStudies(Integer averageBibleStudies) {
        this.averageBibleStudies = averageBibleStudies;
        return (T) this;
    }

    public Integer getMemorialAttendance() {
        return memorialAttendance;
    }

    public T setMemorialAttendance(Integer memorialAttendance) {
        this.memorialAttendance = memorialAttendance;
        return (T) this;
    }
}
