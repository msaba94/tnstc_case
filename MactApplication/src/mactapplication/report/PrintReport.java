/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mactapplication.report;

import java.io.InputStream;
import java.sql.Connection;
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
 * @author Sabapathi
 */
public class PrintReport extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection conn;

	public PrintReport() {
		conn = SQLConnection.connection();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void showReport(Map parametersMap) throws Throwable {
		if (conn == null) {
			System.out.println("Connection Failed");
			return;
		}

		String reportSource = "/mactapplication/report/CaseReport.jrxml";
		InputStream is = null;

		try {
			is = getClass().getResourceAsStream(reportSource);
			// JasperReport jasperReport =
			// JasperCompileManager.compileReport("/Users/sabapathy/Sabapathi/tnstc_case/MactApplication/src/mactapplication/report/CaseReport.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(is);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametersMap, conn);
			JRViewer jView = new JRViewer(jasperPrint);
			jView.setVisible(true);
			jView.setOpaque(true);

			this.add(jView);
			this.setSize(900, 500);
			this.setVisible(true);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception();
		}
	}

}
