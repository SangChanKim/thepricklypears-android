package com.example.sang.waterreportingapp.model;

/**
 * Created by Sang on 10/26/16.
 *
 * Represents water quality conditions
 */
public enum QualityCondition {

    SAFE ("Safe"),
    TREATABLE ("Treatable"),
    UNSAFE ("Unsafe");

    /** the full string representation of the water quality condition */
    private final String condition;


    /**
     * Constructor for the enumeration
     *
     * @param condition   the water condition
     */
    QualityCondition(String condition) {
        this.condition = condition;
    }

    /**
     *
     * @return   the water quality condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     *
     * @return the display string representation
     */
    public String toString() {
        return condition;
    }
}
