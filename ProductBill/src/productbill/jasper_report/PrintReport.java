/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.jasper_report;

import productbill.database.SQLConnection;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;

/**
 *
 * @author iCT-3
 */
public class PrintReport extends JFrame {

    Connection conn;

    public PrintReport() {
        conn = SQLConnection.connection();
    }

    public void showReport(Map parametersMap) {
        if (conn != null) {
            System.out.println("Connected");
        }

        try {

            //JasperReport jasperReport = JasperCompileManager.compileReport("/Users/sabapathy/Sabapathi/tnstc_case/ProductBill/src/productbill/jasper_report/CustomerBill.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport("/Users/sabapathy/Sabapathi/tnstc_case/ProductBill/src/productbill/mini_bill_report/mini_bill.jrxml");
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
