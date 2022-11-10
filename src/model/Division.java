package model;

/** Class utilized for managing first level divisions.*/
public class Division {
    private int id;
    private String divisionName;
    private int countryId;

    /**
     * @param id division id from first_level_divisions table in database
     * @param divisionName division name from first_level_divisions table in database
     * @param countryId division country id from first_level_divisions table in database
     */
    public Division(int id, String divisionName, int countryId) {
        this.id = id;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * @return id for division
     */
    public int getId() {
        return id;
    }

    /**
     * @return name for division
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @return country id for division
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * @return overload text return when string is requested for division object
     */
    public String toString(){
        return divisionName;
    }
}
