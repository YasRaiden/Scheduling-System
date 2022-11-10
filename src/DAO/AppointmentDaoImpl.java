package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/** Class for adding, getting, updating and deleting appointment information from appointments table in database */
public class AppointmentDaoImpl {

    /** Used for receiving all appointments in appointments table in database
     * @return all appointments listed in appointments table in database
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet appointmentList = Database.selectStatement("SELECT * FROM appointments");
        Appointment selectedAppointment;
        while (appointmentList.next()){
            int id = appointmentList.getInt("Appointment_ID");
            String title = appointmentList.getString("Title");
            String description = appointmentList.getString("Description");
            String location = appointmentList.getString("Location");
            String type = appointmentList.getString("Type");
            LocalDateTime start = appointmentList.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = appointmentList.getTimestamp("End").toLocalDateTime();
            int customerID = appointmentList.getInt("Customer_ID");
            int userID = appointmentList.getInt("User_ID");
            int contactID = appointmentList.getInt("Contact_ID");

            ZoneId currentZoneId = ZoneId.systemDefault();
            ZoneId utcZoneId = ZoneId.of("UTC");

            ZonedDateTime utcStartZDT = ZonedDateTime.of(start, utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.of(end, utcZoneId);

            ZonedDateTime currentStartZDT = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), currentZoneId);
            ZonedDateTime currentEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), currentZoneId);

            start = currentStartZDT.toLocalDateTime();
            end = currentEndZDT.toLocalDateTime();


            selectedAppointment = new Appointment(id, title, description, location, type, start, end, customerID,
                    userID, contactID);
            allAppointments.add(selectedAppointment);
        }
        return allAppointments;
    }

    /** Used for receiving single appointment object in appointments table in database
     * @param searchAppointmentID appointment ID to search on
     * @return single appointment object from appointment ID
     * @throws SQLException
     * @throws Exception
     */
    public static Appointment getAppointment(int searchAppointmentID) throws SQLException, Exception{
        ResultSet appointmentList = Database.selectStatement("SELECT * FROM appointments WHERE " +
                "Contact_ID  = '" + searchAppointmentID+ "'");
        Appointment selectedAppointment;
        while(appointmentList.next()){
            int id = appointmentList.getInt("Appointment_ID");
            String title = appointmentList.getString("Title");
            String description = appointmentList.getString("Description");
            String location = appointmentList.getString("Location");
            String type = appointmentList.getString("Type");
            LocalDateTime start = appointmentList.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = appointmentList.getTimestamp("End").toLocalDateTime();
            int customerID = appointmentList.getInt("Customer_ID");
            int userID = appointmentList.getInt("User_ID");
            int contactID = appointmentList.getInt("Contact_ID");

            ZoneId currentZoneId = ZoneId.systemDefault();
            ZoneId utcZoneId = ZoneId.of("UTC");

            ZonedDateTime utcStartZDT = ZonedDateTime.of(start, utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.of(end, utcZoneId);

            ZonedDateTime currentStartZDT = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), currentZoneId);
            ZonedDateTime currentEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), currentZoneId);

            start = currentStartZDT.toLocalDateTime();
            end = currentEndZDT.toLocalDateTime();


            selectedAppointment = new Appointment(id, title, description, location, type, start, end, customerID,
                    userID, contactID);
            return selectedAppointment;
        }
        return null;
    }

    /**
     * @param filter
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointment> getFilteredAppointments(String filter) throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        ResultSet appointmentList = Database.selectStatement("SELECT * FROM appointments WHERE " +
                filter + "(Start) = " + filter + "(now()) AND year(Start) = year(now());");
        Appointment selectedAppointment;
        while (appointmentList.next()){
            int id = appointmentList.getInt("Appointment_ID");
            String title = appointmentList.getString("Title");
            String description = appointmentList.getString("Description");
            String location = appointmentList.getString("Location");
            String type = appointmentList.getString("Type");
            LocalDateTime start = appointmentList.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = appointmentList.getTimestamp("End").toLocalDateTime();
            int customerID = appointmentList.getInt("Customer_ID");
            int userID = appointmentList.getInt("User_ID");
            int contactID = appointmentList.getInt("Contact_ID");

            ZoneId currentZoneId = ZoneId.systemDefault();
            ZoneId utcZoneId = ZoneId.of("UTC");



            ZonedDateTime utcStartZDT = ZonedDateTime.of(start, utcZoneId);
            ZonedDateTime utcEndZDT = ZonedDateTime.of(end, utcZoneId);

            ZonedDateTime currentStartZDT = ZonedDateTime.ofInstant(utcStartZDT.toInstant(), currentZoneId);
            ZonedDateTime currentEndZDT = ZonedDateTime.ofInstant(utcEndZDT.toInstant(), currentZoneId);

            start = currentStartZDT.toLocalDateTime();
            end = currentEndZDT.toLocalDateTime();


            selectedAppointment = new Appointment(id, title, description, location, type, start, end, customerID,
                    userID, contactID);
            allAppointments.add(selectedAppointment);
        }
        return allAppointments;
    }


    /** Used for adding appointment record in appointments table in database
     * @param selectedAppointment pass appointment object to create appointment
     * @param currentUser current user who entered record for database auditing
     * @return amount of rows updated in database indicating add was successful
     * @throws SQLException
     */
    public static int insert(Appointment selectedAppointment, User currentUser) throws SQLException {

        ZoneId currentZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");

        ZonedDateTime currentStartZDT = ZonedDateTime.of(selectedAppointment.getStart(), currentZoneId);
        ZonedDateTime currentEndZDT = ZonedDateTime.of(selectedAppointment.getEnd(), currentZoneId);

        ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(currentStartZDT.toInstant(), utcZoneId);
        ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(currentEndZDT.toInstant(), utcZoneId);

        Timestamp startTimeStamp = Timestamp.valueOf(utcStartZDT.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(utcEndZDT.toLocalDateTime());

        String statement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End," +
                "Create_Date, Created_By, Last_Update, Last_Updated_By, " +
                "Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,NOW(),?,NOW(),?,?,?,?)";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(statement);
        preparedStatement.setString(1, selectedAppointment.getTitle());
        preparedStatement.setString(2, selectedAppointment.getDescription());
        preparedStatement.setString(3, selectedAppointment.getLocation());
        preparedStatement.setString(4, selectedAppointment.getType());
        preparedStatement.setTimestamp(5, startTimeStamp);
        preparedStatement.setTimestamp(6, endTimeStamp);
        preparedStatement.setString(7, currentUser.getUserName());
        preparedStatement.setString(8, currentUser.getUserName());
        preparedStatement.setInt(9, selectedAppointment.getCustomerID());
        preparedStatement.setInt(10, selectedAppointment.getUserID());
        preparedStatement.setInt(11, selectedAppointment.getContactID());
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }

    /** Used for updating appointment record in appointments table in database
     * @param selectedAppointment pass appointment object to update appointment
     * @param currentUser current user who updated record for database auditing
     * @return amount of rows updated in database indicating update was successful
     * @throws SQLException
     */
    public static int update(Appointment selectedAppointment, User currentUser) throws SQLException {

        ZoneId currentZoneId = ZoneId.systemDefault();
        ZoneId utcZoneId = ZoneId.of("UTC");

        ZonedDateTime currentStartZDT = ZonedDateTime.of(selectedAppointment.getStart(), currentZoneId);
        ZonedDateTime currentEndZDT = ZonedDateTime.of(selectedAppointment.getEnd(), currentZoneId);

        ZonedDateTime utcStartZDT = ZonedDateTime.ofInstant(currentStartZDT.toInstant(), utcZoneId);
        ZonedDateTime utcEndZDT = ZonedDateTime.ofInstant(currentEndZDT.toInstant(), utcZoneId);

        Timestamp startTimeStamp = Timestamp.valueOf(utcStartZDT.toLocalDateTime());
        Timestamp endTimeStamp = Timestamp.valueOf(utcEndZDT.toLocalDateTime());

        String statement = "UPDATE appointments SET Title = ?, Description = ?, Location = ?," +
                "Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = ?," +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(statement);
        preparedStatement.setString(1, selectedAppointment.getTitle());
        preparedStatement.setString(2, selectedAppointment.getDescription());
        preparedStatement.setString(3, selectedAppointment.getLocation());
        preparedStatement.setString(4, selectedAppointment.getType());
        preparedStatement.setTimestamp(5, startTimeStamp);
        preparedStatement.setTimestamp(6, endTimeStamp);
        preparedStatement.setString(7, currentUser.getUserName());
        preparedStatement.setInt(8, selectedAppointment.getCustomerID());
        preparedStatement.setInt(9, selectedAppointment.getUserID());
        preparedStatement.setInt(10, selectedAppointment.getContactID());
        preparedStatement.setInt(11, selectedAppointment.getId());
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }

    /** Used for deleting appointment from appointments table in database
     * @param id of appointment record
     * @return amount of rows effected for delete indicating if delete was successful
     * @throws SQLException
     */
    public static int delete(int id) throws SQLException {

        String statement = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement preparedStatement = Database.connection.prepareStatement(statement);
        preparedStatement.setInt(1, id);
        int rowsAffected = preparedStatement.executeUpdate();

        return rowsAffected;

    }
}
