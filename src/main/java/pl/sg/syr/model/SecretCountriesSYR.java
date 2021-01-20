package pl.sg.syr.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class SecretCountriesSYR extends SYR<SecretCountriesSYR> {
    @NotNull
    private Integer numberOfCountries;
    private Integer peak;
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

    public SecretCountriesSYR() {
    }

    public SecretCountriesSYR(Integer numberOfCountries, Integer peak, Integer average, Integer percentIncrease, Integer baptized, Integer averageAuxiliaryPioneers, Integer averagePioneers, Integer numberOfCongregations, Integer totalHours, Integer averageBibleStudies, Integer memorialAttendance) {
        this.numberOfCountries = numberOfCountries;
        this.peak = peak;
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

    public Integer getNumberOfCountries() {
        return numberOfCountries;
    }

    public SecretCountriesSYR setNumberOfCountries(Integer numberOfCountries) {
        this.numberOfCountries = numberOfCountries;
        return this;
    }

    public Integer getPeak() {
        return peak;
    }

    public SecretCountriesSYR setPeak(Integer peak) {
        this.peak = peak;
        return this;
    }

    public Integer getAverage() {
        return average;
    }

    public SecretCountriesSYR setAverage(Integer average) {
        this.average = average;
        return this;
    }

    public Integer getAveragePreviousYear() {
        return averagePreviousYear;
    }

    public SecretCountriesSYR setAveragePreviousYear(Integer averagePreviousYear) {
        this.averagePreviousYear = averagePreviousYear;
        return this;
    }

    public Integer getPercentIncrease() {
        return percentIncrease;
    }

    public SecretCountriesSYR setPercentIncrease(Integer percentIncrease) {
        this.percentIncrease = percentIncrease;
        return this;
    }

    public Integer getBaptized() {
        return baptized;
    }

    public SecretCountriesSYR setBaptized(Integer baptized) {
        this.baptized = baptized;
        return this;
    }

    public Integer getAverageAuxiliaryPioneers() {
        return averageAuxiliaryPioneers;
    }

    public SecretCountriesSYR setAverageAuxiliaryPioneers(Integer averageAuxiliaryPioneers) {
        this.averageAuxiliaryPioneers = averageAuxiliaryPioneers;
        return this;
    }

    public Integer getAveragePioneers() {
        return averagePioneers;
    }

    public SecretCountriesSYR setAveragePioneers(Integer averagePioneers) {
        this.averagePioneers = averagePioneers;
        return this;
    }

    public Integer getNumberOfCongregations() {
        return numberOfCongregations;
    }

    public SecretCountriesSYR setNumberOfCongregations(Integer numberOfCongregations) {
        this.numberOfCongregations = numberOfCongregations;
        return this;
    }

    public Integer getTotalHours() {
        return totalHours;
    }

    public SecretCountriesSYR setTotalHours(Integer totalHours) {
        this.totalHours = totalHours;
        return this;
    }

    public Integer getAverageBibleStudies() {
        return averageBibleStudies;
    }

    public SecretCountriesSYR setAverageBibleStudies(Integer averageBibleStudies) {
        this.averageBibleStudies = averageBibleStudies;
        return this;
    }

    public Integer getMemorialAttendance() {
        return memorialAttendance;
    }

    public SecretCountriesSYR setMemorialAttendance(Integer memorialAttendance) {
        this.memorialAttendance = memorialAttendance;
        return this;
    }
}
