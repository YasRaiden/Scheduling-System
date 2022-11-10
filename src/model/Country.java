package model;

/** Class utilized for managing countries.*/
public class Country {
    private int id;
    private String countryName;

    /**
     * @param id country id from countries table in database
     * @param countryName country name from countries table in database
     */
    public Country(int id, String countryName) {
        this.id = id;
        this.countryName = countryName;
    }

    /**
     * @return id for country
     */
    public int getId() {
        return id;
    }

    /**
     * @return name for country
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * @return overload text return when string is requested for country object
     */
    public String toString(){
        return countryName;
    }
}
