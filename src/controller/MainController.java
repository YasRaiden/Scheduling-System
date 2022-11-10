package controller;

import DAO.AppointmentDaoImpl;
import DAO.CustomerDaoImp;
import DAO.UserDaoImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.IsoFields;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class to control main form window for scheduling system.*/
public class MainController implements Initializable {
    Stage stage;
    Parent scene;


    @FXML
    private Label usernameLbl;

    @FXML
    private TableColumn<Appointment, Integer> apptIDCol;

    @FXML
    private TableView<Appointment> apptTableView;

    @FXML
    private TableColumn<Appointment, String> apptTitleCol;

    @FXML
    private TableColumn<Appointment, String> apptDescCol;

    @FXML
    private TableColumn<Appointment, String> apptLocationCol;

    @FXML
    private TableColumn<Appointment, String> apptTypeCol;

    @FXML
    private TableColumn<Appointment, Timestamp> apptStartCol;

    @FXML
    private TableColumn<Appointment, Timestamp> apptEndCol;

    @FXML
    private TableColumn<Appointment, Integer> apptCustCol;

    @FXML
    private TableColumn<Appointment, Integer> apptUserCol;

    @FXML
    private TableColumn<Customer, Integer> custIDCol;

    @FXML
    private TableColumn<Customer, String> custNameCol;

    @FXML
    private TableColumn<Customer, String> custAddressCol;

    @FXML
    private TableColumn<Customer, String> custPostalCodeCol;

    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    @FXML
    private TableColumn<Customer, Integer> custDivisionCol;

    @FXML
    private TableView<Customer> custTableView;

    @FXML
    private RadioButton apptMonthRbtn;

    @FXML
    private RadioButton apptWeekRbtn;

    @FXML
    private RadioButton apptAllRbtn;

    /** Returns to login form and marks login user as null.
     * @param event take action on event of pressing logout button.
     * @throws IOException
     */
    @FXML
    void onActionLogout(ActionEvent event) throws IOException {
            UserDaoImpl.clearLoginUser();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
            loader.load();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();

    }

    /** Opens appointment window to ADD an appointment.
     * @param event take action on event of pressing add button from appointment tab.
     * @throws IOException
     */
    @FXML
    void onActionAddAppt(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AppointmentView.fxml"));
        loader.load();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Opens appointment window to UPDATE currently selected appointment.
     * @param event take action on event of pressing update button from appointment tab.
     * @throws IOException
     */
    @FXML
    void onActionUpdateAppt(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentView.fxml"));
            loader.load();
            AppointmentController ADMController = loader.getController();
            ADMController.sendAppointment(apptTableView.getSelectionModel().getSelectedItem());
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();



        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No appointment Selected! Select an appointment and try again.");
            alert.showAndWait();
        }
    }

    /** DELETES currently selected appointment and prompts for confirmation.
     * @param event take action on event of pressing delete button from appointment tab.
     * @throws IOException
     */
    @FXML
    void onActionDeleteAppt(ActionEvent event) throws SQLException {
        Appointment selectedAppointment = apptTableView.getSelectionModel().getSelectedItem();
        int selectedApptID = selectedAppointment.getId();
        String selectedApptType = selectedAppointment.getType();
        if(selectedAppointment != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment record will be deleted! Would you like to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                if(AppointmentDaoImpl.delete(selectedAppointment.getId()) < 1){
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Delete Failed");
                    alert.setContentText("Unable to delete appointment.");
                    alert.showAndWait();
                }
                else{
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Cancelled");
                    alert.setContentText("Following appointment cancelled successfully!\n ID: "+selectedApptID+
                            "\nType: " + selectedApptType);
                    alert.showAndWait();
                }
            reloadAppointments();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No Item Selected! Select an item and try again.");
            alert.showAndWait();
        }
    }

    /** Opens customers window to ADD a customer.
     * @param event take action on event of pressing add button from customers tab.
     * @throws IOException
     */
    @FXML
    void onActionAddCust(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/CustomerView.fxml"));
        loader.load();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Opens customers window to UPDATE currently selected customer.
     * @param event take action on event of pressing update button from customers tab.
     * @throws IOException
     */
    @FXML
    void onActionUpdateCust(ActionEvent event) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/CustomerView.fxml"));
            loader.load();
            CustomerController ADMController = loader.getController();
            ADMController.sendCustomer(custTableView.getSelectionModel().getSelectedItem());
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No customer Selected! Select an customer and try again.");
            alert.showAndWait();
        }
    }

    /** DELETES currently selected customer and prompts for confirmation.
     * Used Lambda to carve out filtered appointment list from selected customer.
     * @param event take action on event of pressing delete button from customers tab.
     * @throws IOException
     */
    @FXML
    void onActionDeleteCust(ActionEvent event) throws SQLException {
        Customer selectedCustomer = custTableView.getSelectionModel().getSelectedItem();
        if(selectedCustomer != null){

            ObservableList<Appointment> allAppts = AppointmentDaoImpl.getAllAppointments();
            ObservableList<Appointment> customerAppts = FXCollections.observableArrayList();
            allAppts.forEach(appt -> {if(selectedCustomer.getId() == appt.getCustomerID()) customerAppts.add(appt);});


            if(customerAppts.size() > 0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Foreign Key Constraint");
                alert.setContentText("Appointments exist for customer. Appointments must be removed prior to continuing!");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Customer record will be deleted! Would you like to continue?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK)
                    if (CustomerDaoImp.delete(selectedCustomer.getId()) < 1) {
                        alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Delete Failed");
                        alert.setContentText("Unable to delete customer.");
                        alert.showAndWait();
                    }
                    else{
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Record Deleted");
                        alert.setContentText("Customer record delete successfully!");
                        alert.showAndWait();
                    }
                reloadCustomers();
            }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No Item Selected! Select an item and try again.");
            alert.showAndWait();
        }

    }

    /** Opens appointment window to ADD an appointment with currently selected customer pre-populating form.
     * @param event take action on event of pressing add appointment button from customers tab.
     * @throws IOException
     */
    @FXML
    void onActionAddApptByCust(ActionEvent event) throws IOException {
        Customer selectedCustomer = custTableView.getSelectionModel().getSelectedItem();
        if(selectedCustomer != null) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/AppointmentView.fxml"));
            loader.load();
            AppointmentController ADMController = loader.getController();
            ADMController.sendCustomer(selectedCustomer);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("No customer selected! Select a customer and try again.");
            alert.showAndWait();

        }

    }

    /** Opens reports window.
     * @param event take action on event of pressing reports button.
     * @throws IOException
     */
    @FXML
    void onActionOpenReports(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ReportView.fxml"));
        loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Resets filters on appointments listed on appointments tab to display all appointments
     * @param event take action on event of pressing all radio button.
     * @throws IOException
     */
    @FXML
    void onActionApptAllSelected(ActionEvent event) throws SQLException {
        apptTableView.setItems(AppointmentDaoImpl.getAllAppointments());
    }

    /** Filters appointments listed on appointments tab to display all appointments for current month
     * @param event take action on event of pressing month radio button.
     * @throws IOException
     */
    @FXML
    void onActionApptMonthSelected(ActionEvent event) throws SQLException {
        //apptTableView.setItems(AppointmentDaoImpl.getFilteredAppointments("MONTH"));


        ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        Month currentMonth = currentDateTime.getMonth();
        int currentYear = currentDateTime.getYear();

        ObservableList<Appointment> allAppointments = AppointmentDaoImpl.getAllAppointments();
        ObservableList<Appointment> currMonthAppointments = FXCollections.observableArrayList();

        allAppointments.forEach(appointment -> {
            if(appointment.getStart().getYear() == currentYear && appointment.getStart().getMonth() == currentMonth)
                currMonthAppointments.add(appointment);});

        apptTableView.setItems(currMonthAppointments);
    }

    /** Filters appointments listed on appointments tab to display all appointments for current week
     * @param event take action on event of pressing week radio button.
     * @throws IOException
     */
    @FXML
    void onActionApptWeekSelected(ActionEvent event) throws SQLException {
        //apptTableView.setItems(AppointmentDaoImpl.getFilteredAppointments("MONTH"));
        ZonedDateTime currentDateTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        int currentWeek = currentDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        int currentYear = currentDateTime.getYear();
        ObservableList<Appointment> allAppointments = AppointmentDaoImpl.getAllAppointments();
        ObservableList<Appointment> currWeekAppointments = FXCollections.observableArrayList();


        for (Appointment appointment : allAppointments) {
            ZonedDateTime apptDateTime = ZonedDateTime.of(appointment.getStart(), ZoneId.systemDefault());
            int apptWeek = apptDateTime.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            int apptYear = appointment.getStart().getYear();
            if(currentYear == apptYear && currentWeek == apptWeek)
                currWeekAppointments.add(appointment);
        }
        apptTableView.setItems(currWeekAppointments);
    }

    /** Refreshes listed customers to pull up to date information from database after changes. */
    public void reloadCustomers() {
        try {
            custTableView.setItems(CustomerDaoImp.getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** Refreshes listed appointments to pull up to date information from database after changes. */
    public void reloadAppointments() {
        try {
            apptTableView.setItems(AppointmentDaoImpl.getAllAppointments());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /** Populates appointment table and customer table when window is initialized.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            apptTableView.setItems(AppointmentDaoImpl.getAllAppointments());
            custTableView.setItems(CustomerDaoImp.getAllCustomers());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        usernameLbl.setText(UserDaoImpl.getLoginUser().getUserName());
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        custIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        custDivisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionId"));

        apptAllRbtn.setSelected(true);
    }
}
