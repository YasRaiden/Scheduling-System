package controller;

import DAO.CountryDaoImpl;
import DAO.CustomerDaoImp;
import DAO.DivisionDaoImpl;
import DAO.UserDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class to control customer form window for scheduling system.*/
public class CustomerController implements Initializable {
    Stage stage;
    Parent scene;


    @FXML
    private TextField custAddressTxt;

    @FXML
    private TextField custIdTxt;

    @FXML
    private TextField custNameTxt;

    @FXML
    private TextField custPhoneTxt;

    @FXML
    private TextField custPostalCodeTxt;

    @FXML
    private ComboBox<Country> countryCbx;

    @FXML
    private ComboBox<Division> divisionCbx;

    /** Filters divisions combo box based on country selected and enables combo box to be editable.
     * @param event selected country from combo box.
     * @throws SQLException handle exceptions
     */
    @FXML
    public void onActionCountrySelect(ActionEvent event) throws SQLException {
        if(countryCbx.getSelectionModel().getSelectedItem() != null) {
            Country selectedCountry = countryCbx.getSelectionModel().getSelectedItem();
            divisionCbx.setItems(DivisionDaoImpl.filterDivisions(selectedCountry.getId()));
            divisionCbx.setDisable(false);
        }
    }

    /** Saves or Updates currently populated customer to database.
     * Populated data is validated for logical errors and prompts are presented to take corrective action.
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException, SQLException {
        Customer customer;
        try {
            customer = storeCustomer();
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Entry");
            alert.setContentText("Data is not entered in all fields!");
            alert.showAndWait();
            return;
        }

        int affectedRows;
        if(customer.getNewRecord())
            CustomerDaoImp.insert(customer, UserDaoImpl.getLoginUser());
        else
            CustomerDaoImp.update(customer, UserDaoImpl.getLoginUser());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/MainView.fxml"));
        loader.load();
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /** Method changes current form to Main Form. Confirmation will display voiding any entered information.
     * @param event take action on event of pressing Cancel button.
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

    /** Method for creating customer object when pressing save button. */
    public Customer storeCustomer(){
        int id;
        try {
            id = Integer.parseInt(custIdTxt.getText());
        }
        catch (NumberFormatException e){
            id = -1;
        }
        String name = custNameTxt.getText();
        String address = custAddressTxt.getText();
        String postalCode = custPostalCodeTxt.getText();
        String phone = custPhoneTxt.getText();
        String userName = UserDaoImpl.getLoginUser().getUserName();
        int divisionId = divisionCbx.getSelectionModel().getSelectedItem().getId();
        Customer storedCustomer  = new Customer(id, name, address, postalCode, phone, divisionId);
        return storedCustomer;
    }

    /** Used to transfer customer information to current form for pre-populating fields.
     * @param customer customer object received from other forms
     */
    public void sendCustomer(Customer customer) throws Exception {
        custIdTxt.setText(String.valueOf(customer.getId()));
        custNameTxt.setText(customer.getName());
        custAddressTxt.setText(customer.getAddress());
        custPostalCodeTxt.setText(customer.getPostalCode());
        custPhoneTxt.setText(customer.getPhone());


        Division selectedDivision = DivisionDaoImpl.getDivision(customer.getDivisionId());
        Country selectedCountry = CountryDaoImpl.getCountry(selectedDivision.getCountryId());

        countryCbx.setValue(selectedCountry);
        divisionCbx.setItems(DivisionDaoImpl.filterDivisions(selectedCountry.getId()));
        divisionCbx.setValue(selectedDivision);
        divisionCbx.setDisable(false);

    }


    /** Initializes data in country and division combo boxes based on information available from database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custIdTxt.setDisable(true);
        divisionCbx.setDisable(true);
        try {
            countryCbx.setItems(CountryDaoImpl.getAllCountries());
            divisionCbx.setItems(DivisionDaoImpl.getAllDivisions());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
