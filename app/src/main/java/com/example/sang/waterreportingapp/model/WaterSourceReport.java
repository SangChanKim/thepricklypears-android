package com.example.sang.waterreportingapp.model;

import com.example.sang.waterreportingapp.WaterCondition;
import com.example.sang.waterreportingapp.WaterType;

import java.util.Date;

/**
 * Created by Valerie on 10/11/2016.
 *
 * Represents a water source report
 */
public class WaterSourceReport {

    private String username;
    private int reportNumber;
    private Date date;
    private Location location;
    private WaterType waterType;
    private WaterCondition waterCondition;

    /**
     * Make a new water source report
     * @param username      the user's name
     * @param reportNumber  the report number for this report
     * @param date          date/time of water report
     * @param location      location of water source
     * @param waterType     type of water source
     * @param waterCondition    condition of water
     */
    public WaterSourceReport(String username, int reportNumber, Date date,
                             Location location, WaterType waterType, WaterCondition waterCondition) {
        this.username = username;
        this.reportNumber = reportNumber;
        this.date = date;
        this.location = location;
        this.waterType = waterType;
        this.waterCondition = waterCondition;
    }


    /*
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof WaterSourceReport)) {
            return false;
        }
        WaterSourceReport other = (WaterSourceReport) o;
        return (this.getUsername().equals(other.getUsername()))
                && (this.getReportNumber().equals(other.getReportNumber()))
                && (this.getDate().equals(other.getDate()))
                && (this.getLocation().equals(other.getLocation()))
                && (this.getWaterType().equals(other.getWaterType()))
                && (this.getWaterCondition().equals(other.getWaterCondition()));
    }
*/

    public Location getLocation() {
        return location;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }

    public void setWaterCondition(WaterCondition waterCondition) {
        this.waterCondition = waterCondition;
    }

    public String getUsername() {
        return username;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public Date getDate() {
        return date;
    }

    public WaterType getWaterType() {
        return waterType;
    }

    public WaterCondition getWaterCondition() {
        return waterCondition;
    }
}
