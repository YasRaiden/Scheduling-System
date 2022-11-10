package controller;

import DAO.AppointmentDaoImpl;
import DAO.UserDaoImpl;
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
import model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** Class to control login form window for scheduling system.*/
public class LoginController implements Initializable {
    Stage stage;
    Parent scene;
    // Uncomment line below to manually translate login page to French. Comment out line after.
    //ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.FRANCE);
    ResourceBundle rb = ResourceBundle.getBundle("Nat", Locale.getDefault());

    @FXML
    private Label invalidAuthLbl;

    @FXML
    private Label usernameLbl;

    @FXML
    private Label passwordLbl;

    @FXML
    private Button loginBtn;

    @FXML
    private Label zoneIdLbl;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private TextField usernameTxt;

    @FXML
    private Label zoneValueLbl;

    /** Used for checking user login credentials against database
     * @param event take action on event of button pressed
     * @throws IOException
     */
    @FXML
    public void onActionLogin(ActionEvent event) throws IOException {
            int apptNotifyThreshold = 15;

            invalidAuthLbl.setVisible(false);
            try {
                User loginUser = UserDaoImpl.authUser(usernameTxt.getText(), passwordTxt.getText());
                File file = new File("Login_activity.txt");
                FileWriter fw = new FileWriter(file, true);

                if (loginUser != null) {
                    fw.write(Timestamp.valueOf(LocalDateTime.now()) + "\t:\tUsername:" + usernameTxt.getText() +
                            "\t" + "LOGIN SUCCESSFUL" + "\n");

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/MainView.fxml"));
                    loader.load();
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Parent scene = loader.getRoot();
                    stage.setScene(new Scene(scene));
                    stage.show();
                    Boolean upcomingAppointment = false;
                    ObservableList<Appointment> allAppointments = AppointmentDaoImpl.getAllAppointments();
                    for(Appointment appointment : allAppointments){
                        LocalDateTime appointmentStart = appointment.getStart();
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        long timeDifference = ChronoUnit.MINUTES.between(currentDateTime, appointmentStart);
                        if(timeDifference > 0 && timeDifference < apptNotifyThreshold) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Upcoming Appointment");
                            alert.setContentText("Upcoming Appointment: [ID-" + appointment.getId() + "] " + appointment.getStart().toLocalDate() + " @ " + appointment.getStart().toLocalTime());
                            upcomingAppointment = true;
                            alert.showAndWait();
                        }
                    }
                    if(!(upcomingAppointment)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Upcoming Appointment");
                        alert.setContentText("No Upcoming Appointments!");
                        alert.showAndWait();
                    }

                }
                else{
                    fw.write(Timestamp.valueOf(LocalDateTime.now()) + "\t:\tUsername:" + usernameTxt.getText() +
                            "\t" + "LOGIN FAILED" + "\n");
                    invalidAuthLbl.setVisible(true);
                }
                fw.flush();
                fw.close();

            }
            catch (NullPointerException | SQLException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                try{
                    alert.setTitle(rb.getString("LoginError"));
                    alert.setContentText(rb.getString("LoginErrorMessage"));
                } catch(MissingResourceException re) {
                    System.out.println("Unable to translate text due to missing resource file or phrase: " + re);
                }
                alert.showAndWait();

            }

    }

    /** Initializes text fields with specified text to translate in English or French
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            usernameLbl.setText(rb.getString("Username"));
            passwordLbl.setText(rb.getString("Password"));
            loginBtn.setText(rb.getString("Login"));
            invalidAuthLbl.setText(rb.getString("InvalidUsr&Pass"));
            zoneIdLbl.setText(rb.getString("ZoneID"));
        } catch(MissingResourceException e) {
            System.out.println("Unable to translate text due to missing resource file or phrase: " + e);
        }


        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        zoneValueLbl.setText(localZoneId.toString());

    }
}
