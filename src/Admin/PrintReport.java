/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import dbUtil.dbConnection;
import java.awt.Desktop;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

/**,
 *
 * @author Lagrange-Support
 */
public class PrintReport extends JFrame  {
    dbUtil.dbConnection dbCon;
    PreparedStatement pst;
    ResultSet rs;
    
    public PrintReport(){
    }
    
    
    
    
    public void showReport(){
        try{
            
             System.out.println("show report inside print report ");
             
            Connection dbCon = dbConnection.getConnection();
            System.out.println("show report inside print report after connectiom ");
            
            String reportPath="C:\\Users\\Lagrange-Support\\Documents\\NetBeansProjects\\"
                    + "JavaFXApplication1Student\\src\\reportTest.jrxml";
            
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null,dbCon);
            JRViewer viewer = new JRViewer(jasperPrint);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            
            this.add(viewer);
            this.setSize(900,500);
            this.setVisible(true);
            
          System.out.println("print report end end");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    
    }

    

}
