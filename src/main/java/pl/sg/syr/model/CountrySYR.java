package pl.sg.syr.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class CountrySYR extends SYR<CountrySYR> {
    @NotNull
    @ManyToOne
    private Country country;
    private Integer population;
    private Integer peak;
    private Integer ratio1PublisherTo;
    private Integer average;
    private Integer averagePreviousYear;
    private Integer percentIncrease;
    private Integer baptized;
    private Integer averageAuxiliaryPioneers;
    private Integer averagePioneers;
    private Integer numberOfCongregations;
    private Integer totalHours;
    private Integer averageBibleStudies;
    private Integer memorialAttendance;

    public CountrySYR() {
    }

    public CountrySYR(@NotNull Country country, Integer population, Integer peak, Integer ratio1PublisherTo, Integer average, Integer percentIncrease, Integer baptized, Integer averageAuxiliaryPioneers, Integer averagePioneers, Integer numberOfCongregations, Integer totalHours, Integer averageBibleStudies, Integer memorialAttendance) {
        this.country = country;
        this.population = population;
        this.peak = peak;
        this.ratio1PublisherTo = ratio1PublisherTo;
        this.average = average;
        this.percentIncrease = percentIncrease;
        this.baptized = baptized;
        this.averageAuxiliaryPioneers = averageAuxiliaryPioneers;
        this.averagePioneers = averagePioneers;
        this.numberOfCongregations = numberOfCongregations;
        this.totalHours = totalHours;
        this.averageBibleStudies = averageBibleStudies;
        this.memorialAttendance = memorialAttendance;
    }

    public Country getCountry() {
        return country;
    }

    public CountrySYR setCountry(Country country) {
        this.country = country;
        return this;
    }

    public Integer getPopulation() {
        return population;
    }

    public CountrySYR setPopulation(Integer population) {
        this.population = population;
        return this;
    }

    public Integer getPeak() {
        return peak;
    }

    public CountrySYR setPeak(Integer peak) {
        this.peak = peak;
        return this;
    }

    public Integer getRatio1PublisherTo() {
        return ratio1PublisherTo;
    }

    public CountrySYR setRatio1PublisherTo(Integer ratio1PublisherTo) {
        this.ratio1PublisherTo = ratio1PublisherTo;
        return this;
    }

    public Integer getAverage() {
        return average;
    }

    public CountrySYR setAverage(Integer average) {
        this.average = average;
        return this;
    }

    public Integer getAveragePreviousYear() {
        return averagePreviousYear;
    }

    public CountrySYR setAveragePreviousYear(Integer averagePreviousYear) {
        this.averagePreviousYear = averagePreviousYear;
        return this;
    }

    public Integer getPercentIncrease() {
        return percentIncrease;
    }

    public CountrySYR setPercentIncrease(Integer percentIncrease) {
        this.percentIncrease = percentIncrease;
        return this;
    }

    public Integer getBaptized() {
        return baptized;
    }

    public CountrySYR setBaptized(Integer baptized) {
        this.baptized = baptized;
        return this;
    }

    public Integer getAverageAuxiliaryPioneers() {
        return averageAuxiliaryPioneers;
    }

    public CountrySYR setAverageAuxiliaryPioneers(Integer averageAuxiliaryPioneers) {
        this.averageAuxiliaryPioneers = averageAuxiliaryPioneers;
        return this;
    }

    public Integer getAveragePioneers() {
        return averagePioneers;
    }

    public CountrySYR setAveragePioneers(Integer averagePioneers) {
        this.averagePioneers = averagePioneers;
        return this;
    }

    public Integer getNumberOfCongregations() {
        return numberOfCongregations;
    }

    public CountrySYR setNumberOfCongregations(Integer numberOfCongregations) {
        this.numberOfCongregations = numberOfCongregations;
        return this;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public CountrySYR setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
        return this;
    }

    public Integer getAverageBibleStudies() {
        return averageBibleStudies;
    }

    public CountrySYR setAverageBibleStudies(Integer averageBibleStudies) {
        this.averageBibleStudies = averageBibleStudies;
        return this;
    }

    public Integer getMemorialAttendance() {
        return memorialAttendance;
    }

    public CountrySYR setMemorialAttendance(Integer memorialAttendance) {
        this.memorialAttendance = memorialAttendance;
        return this;
    }
}
