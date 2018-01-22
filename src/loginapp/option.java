package loginapp;

public enum option {
    adm, stud;

    public String value(){
        return name();
    }

    public static option fromvalue(String v){
        return valueOf(v);
    }
}
