package com.demo.utils;

/**
 * This class is used as a constant class
 * where constants used across application
 * are defined.
 */
public class Constants {

    private Constants() {}

    public static final String CUBE_NODE = "Cube";
    public static final String TIME_ATTRIBUTE = "time";
    public static final String CURRENCY_ATTRIBUTE = "currency";
    public static final String RATE_ATTRIBUTE = "rate";

    // Message Exception Constants
    public static final String DATE_RATES_NOT_FOUND = "Could not found rates for date: ";
    public static final String CURRENCY_RATE_NOT_FOUND  = "Rate not found for currency: ";
    public static final String DAILY_RATES_NOT_RETRIEVED = "Daily rates could not be retrieved";
    public static final String RATE_DATE_MALFORMED_FORMAT = "Rate date should be of form 'YYYY-MM-DD'";
}
