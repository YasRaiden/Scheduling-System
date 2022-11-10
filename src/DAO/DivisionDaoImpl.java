package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.ResultSet;
import java.sql.SQLException;

/** Class for getting division information from first_level_divisions table in database */
public class DivisionDaoImpl {

    /** Used for receiving all division in first_level_divisions table database
     * @return all divisions listed in first_level_divisions table in database
     * @throws SQLException
     */
    public static ObservableList<Division> getAllDivisions() throws SQLException {
        ObservableList<Division> allDivisions = FXCollections.observableArrayList();
        ResultSet divisionList = Database.selectStatement("SELECT * FROM first_level_divisions");
        Division selectedDivision;
        while (divisionList.next()){
            int id = divisionList.getInt("Division_ID");
            String divisionName = divisionList.getString("Division");
            int countryId = divisionList.getInt("Country_ID");
            selectedDivision = new Division(id, divisionName, countryId);
            allDivisions.add(selectedDivision);
        }
        return allDivisions;
    }

    /** Used for receiving single division object in first_level_divisions table in database
     * @param searchDivisionID division ID to search on
     * @return single division object from division ID
     * @throws SQLException
     * @throws Exception
     */
    public static Division getDivision(int searchDivisionID) throws SQLException, Exception{
        ResultSet divisionList = Database.selectStatement("SELECT * FROM first_level_divisions WHERE " +
                "Division_ID  = '" + searchDivisionID+ "'");
        Division selectedDivision;
        while(divisionList.next()){
            int id = divisionList.getInt("Division_ID");
            String divisionName = divisionList.getString("Division");
            int countryId = divisionList.getInt("Country_ID");
            selectedDivision = new Division(id, divisionName, countryId);
            return selectedDivision;
        }
        return null;
    }

    /** Used for receiving filtered division in first_level_divisions table in database based on country id
     * @param country country ID for filtering
     * @return list of division with specified country ID
     * @throws SQLException
     */
    public static ObservableList<Division> filterDivisions(int country) throws SQLException {
        ObservableList<Division> filteredDivisions = FXCollections.observableArrayList();
        ResultSet divisionList = Database.selectStatement("SELECT * FROM first_level_divisions WHERE " +
                "Country_ID = " + country);
        Division selectedDivision;
        while (divisionList.next()){
            int id = divisionList.getInt("Division_ID");
            String divisionName = divisionList.getString("Division");
            int countryId = divisionList.getInt("Country_ID");
            selectedDivision = new Division(id, divisionName, countryId);
            filteredDivisions.add(selectedDivision);
        }
        return filteredDivisions;
    }
}
