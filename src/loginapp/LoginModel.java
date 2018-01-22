package loginapp;

import dbUtil.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    Connection connection;

    public LoginModel(){
        try{
            this.connection = dbConnection.getConnection();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (this.connection == null){
            System.exit(1);
        }
    }

    public boolean isDatabaseConnected(){
        return this.connection!=null;
    }

    public boolean isLogin(String user, String pw, String opt) throws SQLException {
        System.out.println("isLogin");
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM login where username=? and password=? and division=?";

        try{
            System.out.println("isLogin2");
            pr = this.connection.prepareStatement(sql);
            System.out.println("isLogin3");
            pr.setString(1,user);
            pr.setString(2, pw);
            pr.setString(3,opt);
            System.out.println("isLogin4");
            rs = pr.executeQuery();

            if(rs.next()){
                return true;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        finally {

                pr.close();
                rs.close();

        }
        return false;
    }
}
