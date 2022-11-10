package model;

/** Class utilized for managing customers.*/
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private int divisionId;
    private Boolean newRecord;

    /**
     * @param id customer id from customers table in database
     * @param name customer name from customers table in database
     * @param address customer address from customers table in database
     * @param postalCode customer postal code from customers table in database
     * @param phone customer phone from customers table in database
     * @param divisionId customer division id from customers table in database
     */
    public Customer(int id, String name, String address, String postalCode, String phone, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        if(this.id < 0)
            this.newRecord = true;
        else
            this.newRecord = false;

    }

    /**
     * @return id for customer
     */
    public int getId() {
        return id;
    }

    /**
     * @return name for customer
     */
    public String getName() {
        return name;
    }

    /**
     * @return address for customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return postal code for customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return phone for customer
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return division id for customer
     */
    public int getDivisionId() {
        return divisionId;
    }

    /** Determine if active record is new or current
     * @return determines if record is new with id being less than 0
     */
    public Boolean getNewRecord() {
        return newRecord;
    }

    /**
     * @return overload text return when string is requested for customer object
     */
    public String toString(){
        return name;
    }
}
