import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DAO.Database;

import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/** Main class start application for scheduling appointments to customers */
public class Main extends Application {

    /** Creates starting point of application pulling up login screen */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("C195-Task 1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /** Main method starts connection to database and runs application.
     * @param args
     */
    public static void main(String[] args) {
        Database.openConnection();
        launch(args);
        Database.closeConnection();
    }
}
