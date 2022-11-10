package model;

import java.time.LocalDateTime;

/** Class utilized for managing appointment associated with customers. */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;
    private Boolean newRecord;

    /**
     * @param id appointment id from users table in database
     * @param title appointment tile from users table in database
     * @param description appointment description from users table in database
     * @param location appointment location from users table in database
     * @param type appointment type from users table in database
     * @param start appointment start date time from users table in database via local date time
     * @param end appointment end date time from users table in database via local date time
     * @param customerID appointment customer id from users table in database
     * @param userID appointment user id from users table in database
     * @param contactID appointment contact id from users table in database
     */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start,
                       LocalDateTime end, int customerID, int userID, int contactID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        if(this.id < 0)
            this.newRecord = true;
        else
            this.newRecord = false;

    }

    /**
     * @return id for appointment
     */
    public int getId() {
        return id;
    }

    /**
     * @return title for appointment
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return description for appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return location for appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return type for appointment
     */
    public String getType() {
        return type;
    }

    /**
     * @return start date time for appointment via local date time
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * @return end date time for appointment via local date time
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * @return customer id for appointment
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return user id for appointment
     */
    public int getUserID() {
        return userID;
    }

    /** Determine if active record is new or current
     * @return determines if record is new with id being less than 0
     */
    public Boolean getNewRecord() {
        return newRecord;
    }

    /**
     * @return contact id for appointment
     */
    public int getContactID() {
        return contactID;
    }

}
