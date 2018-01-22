package Admin;

import dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AdminController implements Initializable{

    @FXML
    private TextField id;

    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField email;
    @FXML
    private DatePicker dob;

    @FXML
    private TableView<StudentData> studentDataTableView;
    @FXML
    private TableColumn<StudentData, String> idCol;
    @FXML
    private TableColumn<StudentData, String> fnameCol;
    @FXML
    private TableColumn<StudentData, String> lnameCol;
    @FXML
    private TableColumn<StudentData, String> emailCol;
    @FXML
    private TableColumn<StudentData, String> dobCol;
    @FXML
    private TableColumn<StudentData,String> hobbiesCol;
    @FXML
    private TableColumn<StudentData,String> genderCol;
    @FXML
    private CheckBox playing;
    @FXML
    private CheckBox dancing;
    @FXML
    private CheckBox singing;
    @FXML
    private RadioButton female;
    @FXML
    private RadioButton male;
    @FXML
    private TextField searchField;

    private FilteredList<StudentData> filteredStudents;
    private dbConnection dbCon;
    private ObservableList<StudentData> studentData;
    private ObservableList<String> studentHobbies;
    private String studentGender;

    private String sql = "Select * from students";

    private FileChooser imageChooser;
    private File imageFile;

    @FXML
    private Button browseImage;
    @FXML
    private TextArea filePathTextArea;

    @FXML
    private ImageView studentImage;

    private Image image1;
    private Desktop desktop;

    private FileInputStream fis;
    private InputStream inputStream;
    
    public void initialize(URL url, ResourceBundle resourceBundle){
       // this.dbCon = new dbConnection();
        this.studentHobbies = FXCollections.observableArrayList();


        try {
            selectQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        filteredStudents=new FilteredList<>(studentData, p->true);

        imageChooser=new FileChooser();
        imageChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All FIles", "*.*"));

        desktop=Desktop.getDesktop();

      /*  Stage stage=new Stage();
        stage.getIcons().add(new Image("file:fbIcon.png"));

        Scene scene=this.browseImage.getScene();
        stage.setScene(scene);
*/
    }

    @FXML
    private void loadStudentData(ActionEvent actionEvent) throws SQLException{
            selectQuery();
    }

    private void selectQuery() throws SQLException{
        try {
            Connection dbCon = dbConnection.getConnection();

            this.studentData = FXCollections.observableArrayList();

            ResultSet resultSet =dbCon.createStatement().executeQuery(sql);

            while (resultSet.next()){
                this.studentData.add(new StudentData(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7)));

            }
            dbCon.close();

        } catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        }

        this.idCol.setCellValueFactory(new PropertyValueFactory<StudentData, String>("id"));
        this.fnameCol.setCellValueFactory(new PropertyValueFactory<StudentData, String>("fname"));
        this.lnameCol.setCellValueFactory(new PropertyValueFactory<StudentData, String>("lname"));
        this.emailCol.setCellValueFactory(new PropertyValueFactory<StudentData, String>("email"));
        this.dobCol.setCellValueFactory(new PropertyValueFactory<StudentData, String>("dob"));
        this.hobbiesCol.setCellValueFactory(new PropertyValueFactory<StudentData, String>("hobbies"));
        this.genderCol.setCellValueFactory(new PropertyValueFactory<StudentData, String>("gender"));

        this.studentDataTableView.setItems(null);
        this.studentDataTableView.setItems(studentData);

    }

    @FXML
    private void playingChecked(ActionEvent actionEvent){
        studentHobbies.add(this.playing.getText());
    }

    @FXML
    private void dancingChecked(ActionEvent actionEvent){
        studentHobbies.add(this.dancing.getText());
    }

    @FXML
    private void singingChecked(ActionEvent actionEvent){
        studentHobbies.add(this.singing.getText());
    }

    @FXML
    private void femaleSelected(ActionEvent actionEvent){
        studentGender="female";
        this.male.setSelected(false);
        System.out.println(studentGender);
    }

    @FXML
    private void maleSelected(ActionEvent actionEvent){
        studentGender="male";
        this.female.setSelected(false);
    }

    @FXML
    private void addStudent(ActionEvent actionEvent) throws SQLException {
        if (validateDataBeforeEntry()) {
            insertQuery();
        }
    }

    private void insertQuery() throws SQLException {
        String sqlInsert = "INSERT INTO students" +
                "(id, fname, lname, email, dob, hobbies, gender, image)" +
                " VALUES(?,?,?,?,?,?,?,?) ";

        try {
            Connection dbCon = dbConnection.getConnection();
            PreparedStatement preparedStatement = dbCon.prepareStatement(sqlInsert);

            preparedStatement.setString(1, this.id.getText());
            preparedStatement.setString(2, this.fname.getText());
            preparedStatement.setString(3, this.lname.getText());
            preparedStatement.setString(4, this.email.getText());
            preparedStatement.setString(5, this.dob.getEditor().getText());
            preparedStatement.setString(6, this.studentHobbies.toString());
            preparedStatement.setString(7, this.studentGender);

            fis=new FileInputStream(this.imageFile);
            preparedStatement.setBinaryStream(8,(InputStream) this.fis, (int)this.imageFile.length());
            preparedStatement.execute();
            preparedStatement.close();
            dbCon.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        selectQuery();
        // clearFormAfterCRUD();
    }
    @FXML
    private void updateStudent(ActionEvent actionEvent) throws SQLException {
        if (validateDataBeforeEntry()) {
            updateQuery();
        }
    }

    private void updateQuery() throws SQLException {
        String sqlUpdate = "UPDATE students SET id=?, fname=?, lname=?, email=?, dob=?, hobbies=?, gender=?, image=?" +
                "WHERE id='" + this.id.getText() + "'  ";

        try {
            Connection dbCon = dbConnection.getConnection();
            PreparedStatement preparedStatement = dbCon.prepareStatement(sqlUpdate);

            preparedStatement.setString(1, this.id.getText());
            preparedStatement.setString(2, this.fname.getText());
            preparedStatement.setString(3, this.lname.getText());
            preparedStatement.setString(4, this.email.getText());
            preparedStatement.setString(5, this.dob.getEditor().getText());
            preparedStatement.setString(6, this.studentHobbies.toString());
            preparedStatement.setString(7,this.studentGender);

           fis=new FileInputStream(this.imageFile);

            preparedStatement.setBinaryStream(8,(InputStream) fis, (int)this.imageFile.length());

            preparedStatement.execute();
            preparedStatement.close();
            dbCon.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        selectQuery();
        // clearFormAfterCRUD();
    }

    private boolean validateDataBeforeEntry(){
        boolean validate=true;
       if(!(this.playing.isSelected()|this.dancing.isSelected()|this.singing.isSelected())){

           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Data validation");
           alert.setHeaderText(null);
           alert.setContentText("please select at least one hobby");
           alert.showAndWait();
           validate=false;
       }
        if(!(this.female.isSelected()|this.male.isSelected())){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Data validation");
            alert.setHeaderText(null);
            alert.setContentText("please select gender");
            alert.showAndWait();
            validate=false;
        }

        return validate;
    }


    @FXML
    private void clearForm(ActionEvent actionEvent){
        System.out.println("clear forms action");
        this.id.setText("");
        this.fname.setText("");
        this.lname.setText("");
        this.email.setText("");
        this.dob.setValue(null);
        this.playing.setSelected(false);
        this.dancing.setSelected(false);
        this.singing.setSelected(false);
        this.female.setSelected(false);
        this.male.setSelected(false);
        this.studentHobbies.clear();
    }


    private void clearFormAfterCRUD(){
        System.out.println("clear forms after crud");
        this.id.setText("");
        this.fname.setText("");
        this.lname.setText("");
        this.email.setText("");
        this.dob.setValue(null);
        this.playing.setSelected(false);
        this.dancing.setSelected(false);
        this.singing.setSelected(false);
        this.studentHobbies.clear();
    }

    @FXML
    private void deleteStudent(ActionEvent actionEvent) throws SQLException {
       if (confirmDelete()) {
           deleteQuery();
       }
    }

    private void deleteQuery() throws SQLException {
        String query = "DELETE FROM students WHERE id=?  ";
        try {
            Connection dbCon = dbConnection.getConnection();
            PreparedStatement preparedStatement = dbCon.prepareStatement(query);
            preparedStatement.setString(1, this.id.getText());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            dbCon.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        selectQuery();
    }

    private boolean confirmDelete(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation dialog");
        alert.setHeaderText(null);
        alert.setContentText("You are going to delete student record");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    @FXML
    private void fetchStudentInfoWhenTableClicked(MouseEvent mouseEvent){
        System.out.println("fetchStudMouseClick");
        StudentData studentData = studentDataTableView.getSelectionModel().getSelectedItem();
        String query = "SELECT * FROM students WHERE id = ?";


        try {
            Connection dbCon = dbConnection.getConnection();
            this.studentData = FXCollections.observableArrayList();

            PreparedStatement preparedStatement = dbCon.prepareStatement(query);
            preparedStatement.setString(1,studentData.getId());
           // System.out.println("ID is === "+studentData.getId());
            ResultSet resultSet=preparedStatement.executeQuery();

            while (resultSet.next()){
                this.id.setText(resultSet.getString("id"));
                this.fname.setText(resultSet.getString("fname"));
                this.lname.setText(resultSet.getString("lname"));
                this.email.setText(resultSet.getString("email"));
                this.dob.getEditor().setText(resultSet.getString("dob"));

                String gender="";

                if (null != resultSet.getString("gender")) {
                    gender = resultSet.getString("gender");

                    if (gender.equals("female")) {
                        this.female.setSelected(true);
                        this.male.setSelected(false);
                    } else {
                        this.female.setSelected(false);
                        this.male.setSelected(true);
                    }
                }
                else{
                    this.female.setSelected(false);
                    this.male.setSelected(false);
                }

                //combobox values
                dancing.setSelected(false);
                playing.setSelected(false);
                singing.setSelected(false);

                if (null != resultSet.getString("hobbies")) {
                    String hobbiesStr =
                            resultSet.getString("hobbies").replace("[","");
                    hobbiesStr=hobbiesStr.replace("]","");

                    System.out.println(hobbiesStr);
                    List<String> hobbiesList= Arrays.asList(hobbiesStr.split("\\s*,\\s*"));

                    System.out.println(hobbiesList);



                    for (String hobbi: hobbiesList){
                        System.out.println(hobbi);
                        switch (hobbi){
                            case "playing": this.playing.setSelected(true);
                            System.out.println("rrr00");
                                           // break;
                            case "dancing": playing.setSelected(true);
                                            //break;
                            case "singing": playing.setSelected(true);
                                            //break;
                                            default: ;
                        }
                    }
                }




                    File imageFile1 = new File("photo.jpg");
                if (null != resultSet.getBinaryStream("image")) {
                    InputStream is = resultSet.getBinaryStream("image");
                    OutputStream os = new FileOutputStream(imageFile1);

                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = is.read(content)) != -1) {
                        os.write(content, 0, size);
                    }
                    is.close();
                    os.close();

                    //System.out.println("is os end");

                }

                this.image1 = new Image("file:photo.jpg");
                studentImage.setImage(image1);

            }
            dbCon.close();
            resultSet.close();

//            preparedStatement.close();

        } catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void fetchStudentInfoWhenKeyPressed(KeyEvent keyEvent){
        System.out.println("key presssesd");

        if(keyEvent.getCode()== KeyCode.UP || keyEvent.getCode()==KeyCode.DOWN) {
            StudentData studentData = studentDataTableView.getSelectionModel().getSelectedItem();
            String query = "SELECT * FROM students WHERE id = ?";


            try {
                Connection dbCon = dbConnection.getConnection();
                this.studentData = FXCollections.observableArrayList();

                PreparedStatement preparedStatement = dbCon.prepareStatement(query);
                preparedStatement.setString(1, studentData.getId());
                System.out.println("ID is === " + studentData.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    this.id.setText(resultSet.getString("id"));
                    this.fname.setText(resultSet.getString("fname"));
                    this.lname.setText(resultSet.getString("lname"));
                    this.email.setText(resultSet.getString("email"));
                    this.dob.getEditor().setText(resultSet.getString("dob"));

                    String gender = "";
                    if (null != resultSet.getString("gender")) {
                        gender = resultSet.getString("gender");

                        if (gender.equals("female")) {
                            this.female.setSelected(true);
                            this.male.setSelected(false);
                        } else {
                            this.female.setSelected(false);
                            this.male.setSelected(true);
                        }
                    } else {
                        this.female.setSelected(false);
                        this.male.setSelected(false);
                    }


                    File imageFile1 = new File("photo.jpg");
                    if (null != resultSet.getBinaryStream("image")) {
                        InputStream is = resultSet.getBinaryStream("image");
                        OutputStream os = new FileOutputStream(imageFile1);

                        byte[] content = new byte[1024];
                        int size = 0;
                        while ((size = is.read(content)) != -1) {
                            os.write(content, 0, size);
                        }
                        is.close();
                        os.close();

                        System.out.println("is os end");

                    }

                    this.image1 = new Image("file:photo.jpg");
                    studentImage.setImage(image1);

                }
                dbCon.close();
//            preparedStatement.close();

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void searchStudent(KeyEvent keyEvent){
        System.out.println("key is presssed");

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            filteredStudents.setPredicate((Predicate<? super StudentData>) searchStudent -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (searchStudent.getId().contains(lowerCaseFilter))
                    return true;

                if (searchStudent.getFname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (searchStudent.getLname().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false;
            });
        });
        SortedList<StudentData> sortedStudents = new SortedList<>(filteredStudents);
        sortedStudents.comparatorProperty().bind(this.studentDataTableView.comparatorProperty());
        this.studentDataTableView.setItems(sortedStudents);

    }

    @FXML
    private void broseImage(ActionEvent actionEvent) throws MalformedURLException {
        //imageFile=imageChooser.showOpenDialog((Stage)this.browseImage.getScene().getWindow());
        imageFile=imageChooser.showOpenDialog(new Stage());
        if(imageFile != null){

                this.filePathTextArea.setText(imageFile.getAbsolutePath());
                String filepath=this.imageFile.toURI().toURL().toString();
                this.image1=new Image(filepath);
                this.studentImage.setImage(this.image1);
                this.studentImage.setVisible(true);
                System.out.println("image end");

        }
    }

    @FXML
    private void exportToExcel(ActionEvent actionEvent){
        System.out.println("export");
        try {
            Connection dbCon = dbConnection.getConnection();

            this.studentData = FXCollections.observableArrayList();

            ResultSet resultSet =dbCon.createStatement().executeQuery(sql);

            XSSFWorkbook wb=new XSSFWorkbook();

            XSSFSheet sheet = wb.createSheet("Students list");
            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("fname");
            header.createCell(2).setCellValue("lname");
            header.createCell(3).setCellValue("email");
            header.createCell(4).setCellValue("dob");
            header.createCell(5).setCellValue("hobbies");
            header.createCell(6).setCellValue("gender");
            header.createCell(7).setCellValue("image");

            int index=1;
            while (resultSet.next()){
                XSSFRow row=sheet.createRow(index);
                row.createCell(0).setCellValue(resultSet.getString("ID"));
                row.createCell(1).setCellValue(resultSet.getString("fname"));
                row.createCell(2).setCellValue(resultSet.getString("lname"));
                row.createCell(3).setCellValue(resultSet.getString("email"));
                row.createCell(4).setCellValue(resultSet.getString("dob"));
                row.createCell(5).setCellValue(resultSet.getString("hobbies"));
                row.createCell(6).setCellValue(resultSet.getString("gender"));
                index++;

            }
            FileOutputStream fileOut = new FileOutputStream("studentsList.xlsx");
            wb.write(fileOut);
            fileOut.close();

            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);
            alert.setContentText("file has been succesfully created");
            alert.showAndWait();

            dbCon.close();

        } catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void importExcelDb() throws SQLException {
        String sqlInsert = "INSERT INTO students" +
                "(id, fname, lname, email)" +
                " VALUES(?,?,?,?) ";

        try {
            Connection dbCon = dbConnection.getConnection();
            PreparedStatement preparedStatement = dbCon.prepareStatement(sqlInsert);

          FileInputStream fileIn = new FileInputStream(new File("studentsList.xlsx"));
            XSSFWorkbook wb=new XSSFWorkbook(fileIn);

            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;

            for(int i=1; i<=sheet.getLastRowNum(); i++){
                row=sheet.getRow(i);
                preparedStatement.setString(1, row.getCell(0).getStringCellValue());
                preparedStatement.setString(2, row.getCell(2).getStringCellValue());
                preparedStatement.setString(3, row.getCell(3).getStringCellValue());
                preparedStatement.setString(4, row.getCell(4).getStringCellValue());
                preparedStatement.execute();
            }

            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information dialog");
            alert.setHeaderText(null);
            alert.setContentText("data have been  imported");
            alert.showAndWait();

            preparedStatement.close();
            dbCon.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectQuery();
    }
    
    @FXML
    private void showIReport(ActionEvent actionEvent){
        System.out.println("show Ireport function start ");

        PrintReport printReport=new PrintReport();
        printReport.showReport();
    System.out.println("show Ireport function end ");
    }
}
