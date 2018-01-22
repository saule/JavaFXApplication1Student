package Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentData {

    private final StringProperty id;
    private final StringProperty fname;
    private final StringProperty lname;
    private final StringProperty email;
    private final StringProperty dob;
    private final StringProperty hobbies;
    private final StringProperty gender;


    public StudentData(String id, String fname, String lname, String email, String dob, String hobbies, String gender){

        this.id = new SimpleStringProperty(id);
        this.fname=new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.email = new SimpleStringProperty(email);
        this.dob = new SimpleStringProperty(dob);

        this.hobbies = new SimpleStringProperty(hobbies);
        this.gender = new SimpleStringProperty(gender);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getFname() {
        return fname.get();
    }

    public StringProperty fnameProperty() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public String getLname() {
        return lname.get();
    }

    public StringProperty lnameProperty() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getDob() {
        return dob.get();
    }

    public StringProperty dobProperty() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }

    public String getHobbies() {
        return hobbies.get();
    }

    public StringProperty hobbiesProperty() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies.set(hobbies);
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }
}
