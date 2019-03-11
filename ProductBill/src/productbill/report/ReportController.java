/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import productbill.database.SQLHelper;
import productbill.tab_bill.TotalBill;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

/**
 * FXML Controller class
 *
 * @author iCT_01
 */
public class ReportController implements Initializable {

    @FXML
    private JFXDatePicker fromDate;

    @FXML
    private JFXDatePicker toDate;

    @FXML
    private JFXTextField searchBy;

    @FXML
    private JFXTreeTableView<TotalBill> reportTV;

    private String pattern = "yyyy-MM-dd";
    private ObservableList<TotalBill> totalBills;
    private SQLHelper sQLHelper;
    private TreeItem<TotalBill> cuTreeItem;

    @FXML
    void searchByTable(ActionEvent event) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        String fromDateStr = dateFormatter.format(fromDate.getValue());
        String toDateStr = dateFormatter.format(toDate.getValue());
        String searchByStr = searchBy.getText();

        totalBills = sQLHelper.getAllSelectedDateTotalBill(fromDateStr, toDateStr, searchByStr);
        cuTreeItem = new RecursiveTreeItem<>(totalBills, RecursiveTreeObject::getChildren);
        reportTV.setRoot(cuTreeItem);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sQLHelper = new SQLHelper();
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
        fromDate.setStyle("-fx-font: 14px \"Montserrat Regular\";");
        toDate.setStyle("-fx-font: 14px \"Montserrat Regular\";");

        totalBills = sQLHelper.getAllTotalBill();
        initTable();

    }

    @FXML
    void clearFromDate(ActionEvent event) {

        fromDate.setValue(null);
    }

    @FXML
    void clearToDate(ActionEvent event) {

        toDate.setValue(null);
    }

    public void exportCSVFile() throws IOException {
        if (totalBills.size() > 0) {
            System.out.println("Exporting Excel");
            try {
                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet("User Details");
                XSSFRow header = sheet.createRow(0);
                header.createCell(0).setCellValue("Bill NO");
                header.createCell(1).setCellValue("Customer Name");
                header.createCell(2).setCellValue("Total Amount");

                for (int i = 0; i < totalBills.size(); i++) {
                    XSSFRow headerOfI = sheet.createRow(i + 1);
                    headerOfI.createCell(0).setCellValue(totalBills.get(i).getId());
                    headerOfI.createCell(1).setCellValue(totalBills.get(i).getCustomerName());
                    headerOfI.createCell(2).setCellValue(totalBills.get(i).getTotalAmount());
                }

                FileOutputStream fileOut = new FileOutputStream("D:\\UserDetails.xlsx");
                wb.write(fileOut);
                fileOut.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void csvFile() throws IOException {
        Writer writer = null;
        try {
            File file = new File("D:\\UserDetails.csv.");
            writer = new BufferedWriter(new FileWriter(file));
            String text = "Harish Kumar , Rathina Sabapathi, Kishore\n";
            writer.write(text);
            String text2 = "Harish Kumar , Rathina Sabapathi, Kishore\n";
            writer.write(text2);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }

    private void initTable() {
        JFXTreeTableColumn<TotalBill, String> billIDCol = new JFXTreeTableColumn<>("Bill No");
        billIDCol.setPrefWidth(100);
        billIDCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<TotalBill, String> param) -> param.getValue().getValue().id);

        JFXTreeTableColumn<TotalBill, String> customerCol = new JFXTreeTableColumn<>("Customer Name");
        customerCol.setPrefWidth(250);
        customerCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<TotalBill, String> param) -> param.getValue().getValue().customerName);

        JFXTreeTableColumn<TotalBill, String> totalAmountCol = new JFXTreeTableColumn<>("Total Amount");
        totalAmountCol.setPrefWidth(200);
        totalAmountCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<TotalBill, String> param) -> param.getValue().getValue().totalAmount);

        cuTreeItem = new RecursiveTreeItem<>(totalBills, RecursiveTreeObject::getChildren);
        reportTV.setRoot(cuTreeItem);
        reportTV.getColumns().setAll(billIDCol, customerCol, totalAmountCol);
        reportTV.setShowRoot(false);
    }

}
