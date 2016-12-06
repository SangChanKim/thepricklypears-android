package com.example.sang.waterreportingapp.model;


import java.util.Date;

/**
 * Created by Sang on 10/24/16.
 *
 * Represents a water quality report
 */
public class WaterQualityReport implements Comparable<WaterQualityReport> {
    private String username;
    private int reportNumber;
    private Date date;
    private Location location;
    private QualityCondition qualityCondition;
    private int virusPPM;
    private int contaminantPPM;

    /**
     * Make a new water source report
     * @param username          the user's name
     * @param reportNumber      the report number for this report
     * @param date              date/time of water report
     * @param location          location of water source
     * @param qualityCondition  quality condition of water
     * @param virusPPM          the virus PPM
     * @param contaminantPPM    contaminant ppm
     */
    public WaterQualityReport(String username, int reportNumber, Date date,
                              Location location, QualityCondition qualityCondition, int virusPPM, int contaminantPPM) {
        this.username = username;
        this.reportNumber = reportNumber;
        this.date = date;
        this.location = location;
        this.qualityCondition = qualityCondition;
        this.virusPPM = virusPPM;
        this.contaminantPPM = contaminantPPM;
    }


    @Override
    public int compareTo(WaterQualityReport waterQualityReport) {
        return 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public QualityCondition getQualityCondition() {
        return qualityCondition;
    }

    public void setQualityCondition(QualityCondition qualityCondition) {
        this.qualityCondition = qualityCondition;
    }

    public int getVirusPPM() {
        return virusPPM;
    }

    public void setVirusPPM(int virusPPM) {
        this.virusPPM = virusPPM;
    }

    public int getContaminantPPM() {
        return contaminantPPM;
    }

    public void setContaminantPPM(int contaminantPPM) {
        this.contaminantPPM = contaminantPPM;
    }
}
