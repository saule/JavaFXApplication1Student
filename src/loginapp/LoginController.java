package loginapp;

import Admin.AdminController;
import Student.StudentsController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    LoginModel loginModel=new LoginModel();

    @FXML
    private Label dbstatus;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ComboBox<option> combobox;

    @FXML
    private Button loginButton;
    @FXML
    private Label loginstatus;

    public void initialize(URL url, ResourceBundle rb){
        if(this.loginModel.isDatabaseConnected()==true){
            this.dbstatus.setText("DB is connected");
        }
        else{
            this.dbstatus.setText("Db is not connected");
        }
        this.combobox.setItems(FXCollections.observableArrayList(option.values()));
    }


    public void Login(ActionEvent actionEvent) {
        System.out.println("11");
        try{
            if(this.loginModel.isLogin(this.username.getText(),this.password.getText(),((option)this.combobox.getValue()).toString() )){
               System.out.println("autentification is ok");
                Stage stage = (Stage)this.loginButton.getScene().getWindow();
                stage.close();
                switch (this.combobox.getValue().toString()){
                    case "adm":
                        adminLogin();
                        break;
                    case "stud":
                        studentLogin();
                        break;

                }


            }else{
                this.loginstatus.setText("wrong credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void studentLogin(){
        try{
            Stage userStage=new Stage();
            FXMLLoader loader = new FXMLLoader();

            Pane root=loader.load(getClass().getResource("/Student/studentsFxml.fxml").openStream());

            StudentsController studentsController=loader.getController();

            Scene scene =  new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("student page");
            userStage.setResizable(false);
            userStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adminLogin(){
        try{
            Stage adminStage = new Stage();
            FXMLLoader fxmlAdminLoader= new FXMLLoader();
            Pane adminroot = fxmlAdminLoader.load(getClass().getResource("/Admin/admin.fxml").openStream());

            AdminController adminController = fxmlAdminLoader.getController();


            Scene scene = new Scene(adminroot);
            adminStage.setScene(scene);
            adminStage.setTitle("Admin Dashboard");

            adminStage.getIcons().add(new Image("file:fbIcon.png"));
            adminStage.setResizable(false);
            adminStage.show();
          System.out.println("end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
