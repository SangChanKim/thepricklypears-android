package com.example.sang.waterreportingapp;

/**
 * Created by Valerie on 10/11/2016.
 *
 * Represents all the water source conditions
 */
public enum WaterCondition {

    WASTE ("Waste"),
    TREATABLE_CLEAR ("Treatable-Clear"),
    TREATABLE_MUDDY ("Treatable-Muddy"),
    POTABLE ("Potable");

    /** the full string representation of the water condition */
    private final String condition;


    /**
     * Constructor for the enumeration
     *
     * @param condition   the water condition
     */
    WaterCondition(String condition) {
        this.condition = condition;
    }

    /**
     *
     * @return   the water condition
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
