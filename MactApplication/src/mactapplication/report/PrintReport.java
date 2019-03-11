/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mactapplication.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import javax.swing.JFrame;
import mactapplication.database.SQLConnection;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author sabapathy
 */
public class PrintReport extends JFrame {

    Connection conn;
    PreparedStatement statement;
    ResultSet resultSet;

    public PrintReport() {
        conn = SQLConnection.connection();
    }

    public void showReport(Map parametersMap) {
        if (conn != null) {
            System.out.println("Connected");
        }

        try {
            System.out.print(parametersMap);

//            JasperReport jasperReport = JasperCompileManager.compileReport("/Users/sabapathy/Sabapathi/tnstc_case/MactApplication/src/mactapplication/report/CaseReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport("/Users/sabapathy/Sabapathi/tnstc_case/MactApplication/src/mactapplication/report/CaseReport.jrxml");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, conn);
            JRViewer jView = new JRViewer(jasperPrint);
            jView.setVisible(true);
            jView.setOpaque(true);

            this.add(jView);
            this.setSize(900, 500);
            this.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
