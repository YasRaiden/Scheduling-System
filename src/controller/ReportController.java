package controller;

import DAO.AppointmentDaoImpl;
import DAO.ContactDaoImpl;
import DAO.CustomerDaoImp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.Report;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Month;
import java.util.Collections;
import java.util.ResourceBundle;

/** Class to control report form window for scheduling system.*/
public class ReportController implements Initializable {
    Stage stage;
    Parent scene;

    @FXML
    private ComboBox<Customer> apptCustomerCbx;

    @FXML
    private ComboBox<Contact> apptContactCbx;

    @FXML
    private TableColumn<Appointment, String> apptDescCustCol;

    @FXML
    private TableColumn<Appointment, Appointment> apptEndCustCol;

    @FXML
    private TableColumn<Appointment, Integer> apptIdCustCol;

    @FXML
    private TableColumn<Report, String> apptMonthNameCol;

    @FXML
    private TableColumn<Report, Integer> apptMonthTotalCol;

    @FXML
    private TableColumn<Appointment, Timestamp> apptStartCustCol;

    @FXML
    private TableView<Appointment> apptCustTableView;

    @FXML
    private TableColumn<Appointment, String> apptTitleCustCol;

    @FXML
    private TableColumn<Appointment, String> apptTypeCustCol;

    @FXML
    private TableColumn<Report, String> apptTypeNameCol;

    @FXML
    private TableColumn<Report, Integer> apptTypeTotalCol;

    @FXML
    private TableColumn<Appointment, Integer> apptCustCustCol;

    @FXML
    private TableColumn<Appointment, Integer> apptCustContactCol;

    @FXML
    private TableColumn<Appointment, String> apptDescContactCol;

    @FXML
    private TableColumn<Appointment, Timestamp> apptEndContactCol;

    @FXML
    private TableColumn<Appointment, Integer> apptIdContactCol;

    @FXML
    private TableColumn<Appointment, Timestamp> apptStartContactCol;

    @FXML
    private TableColumn<Appointment, String> apptTitleContactCol;

    @FXML
    private TableColumn<Appointment, String> apptTypeContactCol;

    @FXML
    private TableColumn<Appointment, Integer> apptContactContactCol;

    @FXML
    private TableView<Report> apptTypeTotalTableView;

    @FXML
    private TableView<Report> apptMonthTotalTableView;

    @FXML
    private TableView<Appointment> apptContactTableView;

    /** Changes current form to Main Form.
     * @param event take action on event of pressing Close button
     * @throws IOException handle exceptions
     */
    @FXML
    void onActionClose(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/MainView.fxml"));
        loader.load();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Updates currently listed appointments based on selected customer from customer report.
     * Used Lambda to carve out filtered appointment list from selected contact.
     * @param event selected contact from combo box.
     * @throws SQLException handle exceptions
     */
    @FXML
    void onActionCustomerSelected(ActionEvent event) throws SQLException {
        ObservableList<Appointment> allAppts = AppointmentDaoImpl.getAllAppointments();
        ObservableList<Appointment> customerAppts = FXCollections.observableArrayList();
        if(apptCustomerCbx.getSelectionModel().getSelectedItem() != null) {
            Customer customer = apptCustomerCbx.getSelectionModel().getSelectedItem();
            allAppts.forEach(appt -> {if(appt.getCustomerID() == customer.getId()) customerAppts.add(appt);});
            apptCustTableView.setItems(customerAppts);

        }
    }

    /** Updates currently listed appointments based on selected contact from contact report.
     * Used Lambda to carve out filtered appointment list from selected contact.
     * @param event selected contact from combo box.
     * @throws SQLException handle exceptions
     */
    @FXML
    void onActionContactSelected(ActionEvent event) throws SQLException {
        ObservableList<Appointment> allAppts = AppointmentDaoImpl.getAllAppointments();
        ObservableList<Appointment> contactAppts = FXCollections.observableArrayList();
        if(apptContactCbx.getSelectionModel().getSelectedItem() != null) {
            Contact contact = apptContactCbx.getSelectionModel().getSelectedItem();
            allAppts.forEach(appt -> {if(appt.getContactID() == contact.getId()) contactAppts.add(appt);});
            apptContactTableView.setItems(contactAppts);
        }
    }

    /** Populates tables in under all tabs with data from database when window is initialized.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Appointment> allAppointments = null;
        try {
            allAppointments = AppointmentDaoImpl.getAllAppointments();

            apptCustomerCbx.setItems(CustomerDaoImp.getAllCustomers());
            apptContactCbx.setItems(ContactDaoImpl.getAllContacts());
            apptCustTableView.setItems(allAppointments);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        apptIdCustCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptTitleCustCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCustCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptTypeCustCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCustCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCustCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustCustCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        apptIdContactCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apptTitleContactCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescContactCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptTypeContactCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartContactCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndContactCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustContactCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptContactContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));


        apptTypeNameCol.setCellValueFactory(new PropertyValueFactory<>("label"));
        apptTypeTotalCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        apptMonthNameCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        apptMonthTotalCol.setCellValueFactory(new PropertyValueFactory<>("count"));


        ObservableList<String> listApptTypes = FXCollections.observableArrayList();
        ObservableList<String> uniqueApptTypes = FXCollections.observableArrayList();
        ObservableList<Report> apptTypeCount = FXCollections.observableArrayList();

        ObservableList<Month> listApptMonths = FXCollections.observableArrayList();
        ObservableList<Month> uniqueApptMonths = FXCollections.observableArrayList();
        ObservableList<Report> apptMonthCount = FXCollections.observableArrayList();

        for (Appointment appointment : allAppointments){
            listApptTypes.add(appointment.getType());
            listApptMonths.add(appointment.getStart().getMonth());
            if(!(uniqueApptTypes.contains(appointment.getType())))
                uniqueApptTypes.add(appointment.getType());
            if(!(uniqueApptMonths.contains(appointment.getStart().getMonth())))
                uniqueApptMonths.add(appointment.getStart().getMonth());
        }
        for (String apptType : uniqueApptTypes){
            int typeTotal = Collections.frequency(listApptTypes, apptType);
            Report apptTypeCountReport = new Report(apptType, typeTotal);
            apptTypeCount.add(apptTypeCountReport);
            apptTypeTotalTableView.setItems(apptTypeCount);
        }
        for (Month month : uniqueApptMonths){
            int monthTotal = Collections.frequency(listApptMonths, month);
            Report apptMonthCountReport = new Report(month, monthTotal);
            apptMonthCount.add(apptMonthCountReport);
            apptMonthTotalTableView.setItems(apptMonthCount);
        }



    }
}
