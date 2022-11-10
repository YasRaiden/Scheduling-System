package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Class for getting country information from countries table in database */
public class CountryDaoImpl {

    /** Used for receiving all countries in countries table database
     * @return all countries listed in countries table in database
     * @throws SQLException
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        ResultSet countryList = Database.selectStatement("SELECT * FROM countries");
        Country selectedCountry;
        while (countryList.next()){
            int id = countryList.getInt("Country_ID");
            String countryName = countryList.getString("Country");
            selectedCountry = new Country(id, countryName);
            allCountries.add(selectedCountry);
        }
        return allCountries;
    }

    /** Used for receiving single country object in countries table in database
     * @param searchCountryID country ID to search on
     * @return single country object from country ID
     * @throws SQLException
     * @throws Exception
     */
    public static Country getCountry(int searchCountryID) throws SQLException, Exception{
        ResultSet countryList = Database.selectStatement("SELECT * FROM countries WHERE " +
                "Country_ID  = '" + searchCountryID+ "'");
        Country selectedCountry;
        while(countryList.next()){
            int id = countryList.getInt("Country_ID");
            String countryName = countryList.getString("Country");
            selectedCountry = new Country(id, countryName);
            return selectedCountry;
        }
        return null;
    }
}
