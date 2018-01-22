package loginapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        Scene scene=new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.setTitle("School Management SYstem");
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:fbIcon.png"));
        System.out.println("sdgfdsgsgsg");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
