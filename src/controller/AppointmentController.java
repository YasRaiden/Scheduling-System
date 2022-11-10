package controller;

import DAO.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class to control appointment form window for scheduling system.*/
public class AppointmentController implements Initializable {
    Stage stage;
    Parent scene;
    private LocalTime officeStart = LocalTime.of(8, 0, 0);
    private LocalTime officeEnd = LocalTime.of(22, 0, 0);


    @FXML
    private TextField apptIdTxt;

    @FXML
    private TextField apptTitleTxt;

    @FXML
    private TextField apptDescriptionTxt;

    @FXML
    private TextField apptLocationTxt;

    @FXML
    private TextField apptTypeTxt;

    @FXML
    private ComboBox<Contact> apptContactCbx;

    @FXML
    private ComboBox<User> apptUserCbx;

    @FXML
    private ComboBox<Customer> apptCustomerCbx;

    @FXML
    private DatePicker startDateDp;

    @FXML
    private ComboBox<LocalTime> startTimeCbx;

    @FXML
    private DatePicker endDateDp;

    @FXML
    private ComboBox<LocalTime> endTimeCbx;


    /** Saves or Updates currently populated appointment to database.
     * Populated data is validated for logical errors and prompts are presented to take corrective action.
     * Used Lambda to carve out filtered appointment list from selected customer.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException, SQLException {
        Appointment appointment;
        try{
            appointment = storeAppointment();
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Entry");
            alert.setContentText("Data is not entered in all fields!");
            alert.showAndWait();
            return;
        }


        ObservableList<Appointment> allAppts = AppointmentDaoImpl.getAllAppointments();
        ObservableList<Appointment> customerAppts = FXCollections.observableArrayList();
        if(apptCustomerCbx.getSelectionModel().getSelectedItem() != null) {
            Customer customer = apptCustomerCbx.getSelectionModel().getSelectedItem();
            allAppts.forEach(appt -> {if(appt.getCustomerID() == customer.getId()) customerAppts.add(appt);});

        }

        //LocalDate officeStartDay = appointment.getStart().toLocalDate();
        //LocalDateTime officeStartDT = LocalDateTime.of(officeStartDay, officeStartTime);
        //LocalDateTime officeEndDT = LocalDateTime.of(officeStartDay, officeEndTime);

        ZonedDateTime localZoneStart = ZonedDateTime.of(appointment.getStart(), ZoneId.systemDefault());
        ZonedDateTime appStartETC = localZoneStart.withZoneSameInstant(ZoneId.of("America/New_York"));

        ZonedDateTime localZoneEnd = ZonedDateTime.of(appointment.getEnd(), ZoneId.systemDefault());
        ZonedDateTime appEndETC = localZoneEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

        System.out.println(appStartETC.toLocalDate());
        System.out.println(appEndETC.toLocalDate());
        System.out.println(appStartETC.toLocalTime());
        System.out.println(appEndETC.toLocalTime());
        System.out.println(officeStart.minusSeconds(1));
        System.out.println(officeEnd.plusSeconds(1));

        if (!(appStartETC.toLocalDate().isEqual(appEndETC.toLocalDate()) &&
                appStartETC.toLocalTime().isAfter(officeStart.minusSeconds(1)) &&
                appStartETC.toLocalTime().isBefore(officeEnd.plusSeconds(1)) &&
                appEndETC.toLocalTime().isAfter(officeStart.minusSeconds(1)) &&
                appEndETC.toLocalTime().isBefore(officeEnd.plusSeconds(1))))
                {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Time Scheduled");
            alert.setContentText("Start time or end time is not within business hours!");
            alert.showAndWait();
            return;
        }
        if (appointment.getStart().isAfter(appointment.getEnd())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Time Scheduled");
            alert.setContentText("Start time is after appointment end time!");
            alert.showAndWait();
            return;
        }
        if (appointment.getStart().isEqual(appointment.getEnd())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Time Scheduled");
            alert.setContentText("Appointment has same start and end time!");
            alert.showAndWait();
            return;
        }



        for (Appointment appointmentList : customerAppts) {
            LocalDateTime appointmentStart = appointmentList.getStart();
            LocalDateTime appointmentEnd = appointmentList.getEnd();

            if (appointmentList.getId() != appointment.getId()) {
                if (appointment.getStart().isEqual(appointmentStart) ||
                        (appointment.getStart().isAfter(appointmentStart) && appointment.getStart().isBefore(appointmentEnd)) ||
                        (appointment.getEnd().isBefore(appointmentEnd) && appointment.getEnd().isAfter(appointmentStart))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Appointment Overlap");
                    alert.setContentText("Customers appointment overlaps with existing appointment");
                    alert.showAndWait();
                    return;
                }
            }
        }

        int affectedRows;
        if (appointment.getNewRecord())
            affectedRows = AppointmentDaoImpl.insert(appointment, UserDaoImpl.getLoginUser());
        else {
            int id = Integer.parseInt(apptIdTxt.getText());
            affectedRows = AppointmentDaoImpl.update(appointment, UserDaoImpl.getLoginUser());
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/MainView.fxml"));
        loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Changes current form to Main Form. Confirmation will display voiding any entered information.
     * @param event take action on event of pressing Cancel button
     * @throws IOException handle exceptions
     */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Changes will not be saved! Would you like to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/MainView.fxml"));
            loader.load();
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** Used to transfer appointment information to current form for pre-populating fields.
     * @param appointment appointment object received from other form.
     */
    public void sendAppointment(Appointment appointment) throws Exception {
        apptIdTxt.setText(String.valueOf(appointment.getId()));
        apptTitleTxt.setText(appointment.getTitle());
        apptDescriptionTxt.setText(appointment.getDescription());
        apptLocationTxt.setText(appointment.getLocation());
        apptTypeTxt.setText(appointment.getType());
        apptCustomerCbx.setValue(CustomerDaoImp.getCustomer(appointment.getCustomerID()));
        apptUserCbx.setValue(UserDaoImpl.getUser(appointment.getUserID()));
        apptContactCbx.setValue(ContactDaoImpl.getContact(appointment.getContactID()));
        startDateDp.setValue(appointment.getStart().toLocalDate());
        startTimeCbx.setValue(appointment.getStart().toLocalTime());
        endDateDp.setValue(appointment.getEnd().toLocalDate());
        endTimeCbx.setValue(appointment.getEnd().toLocalTime());

    }

    /** Used for creating appointment object from data populated on form. */
    public Appointment storeAppointment(){
        int id;
        try {
            id = Integer.parseInt(apptIdTxt.getText());
        }
        catch (NumberFormatException e){
            id = -1;
        }
        LocalDate startDate = startDateDp.getValue();
        LocalTime startTime = startTimeCbx.getSelectionModel().getSelectedItem();
        LocalDate endDate = endDateDp.getValue();
        LocalTime endTime = endTimeCbx.getSelectionModel().getSelectedItem();

        String title = apptTitleTxt.getText();
        String description = apptDescriptionTxt.getText();
        String location = apptLocationTxt.getText();
        String type = apptTypeTxt.getText();
        LocalDateTime start = LocalDateTime.of(startDate, startTime);
        LocalDateTime end = LocalDateTime.of(endDate, endTime);
        int customerID = apptCustomerCbx.getSelectionModel().getSelectedItem().getId();
        int userID = apptUserCbx.getSelectionModel().getSelectedItem().getUserID();
        int contactID = apptContactCbx.getSelectionModel().getSelectedItem().getId();

        Appointment storedAppointment  = new Appointment(id, title, description, location, type, start, end,
                customerID, userID, contactID);
        return storedAppointment;
    }

    /** Used to transfer customer information to current form to pre-populate customer field.
     * @param customer appointment object received from other form.
     */
    public void sendCustomer(Customer customer) {
        apptCustomerCbx.setValue(customer);
    }

    /** Initializes data for combo box for start time, end time, contacts, customers, and users.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate today = LocalDate.now();



        LocalTime start = LocalTime.of(0,0);
        LocalTime end = LocalTime.of(23,29);

        System.out.println(start);
        apptIdTxt.setDisable(true);
        while (start.isBefore(end.plusSeconds(1))){
            startTimeCbx.getItems().add(start);
            endTimeCbx.getItems().add(start);
            start = start.plusMinutes(30);
        }
        try {
            apptContactCbx.setItems(ContactDaoImpl.getAllContacts());
            apptCustomerCbx.setItems(CustomerDaoImp.getAllCustomers());
            apptUserCbx.setItems(UserDaoImpl.getAllUsers());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
