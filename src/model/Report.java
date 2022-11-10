package model;

import java.time.Month;

/** Class utilized for managing first level divisions.*/
public class Report {
    private String label;
    private int count;
    private Month month;


    /** Constructor for generating report objects containing String and Integer
     * @param label String variable describing count
     * @param count Cound of item
     */
    public Report(String label, int count) {
        this.label = label;
        this.count = count;
    }

    /** Constructor for generating report objects containing String and month
     * @param month month variable describing count using months of the year
     * @param count Count of month item
     */
    public Report(Month month, int count) {
        this.month = month;
        this.count = count;
    }

    /**
     * @return string describing unique item in report
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return month describing unique item in report
     */
    public Month getMonth() {
        return month;
    }

    /**
     * @return count for item describing addition item
     */
    public int getCount() {
        return count;
    }


}
