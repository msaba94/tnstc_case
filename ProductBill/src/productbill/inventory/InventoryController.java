/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.inventory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import productbill.database.SQLHelper;
import productbill.report.ReportController;
import productbill.stock.Stock;
import productbill.utils.Utils;
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
public class InventoryController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private JFXTextField minQuantity;

    @FXML
    private JFXTextField maxQuantity;

    @FXML
    private JFXTextField searchBy;

    @FXML
    private JFXTreeTableView<Stock> inventoryTV;

    private SQLHelper sQLHelper;
    private ObservableList<Stock> stocks;
    private TreeItem<Stock> cuTreeItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        Utils.doubleTextField(maxQuantity);
        Utils.doubleTextField(minQuantity);

        sQLHelper = new SQLHelper();
        stocks = sQLHelper.getAllProductStock();
        initTale();

    }

    @FXML
    void clearAll(ActionEvent event) {
        maxQuantity.setText("");
        minQuantity.setText("");
        searchBy.setText("");
    }

    @FXML
    void searchByTable(ActionEvent event) {
        boolean isCheck = true;
        String min = "", max = "";
        if (Utils.isValied(minQuantity.getText())) {
            min = minQuantity.getText();
        } else {
            min = "0";
            isCheck = false;
        }

        if (Utils.isValied(maxQuantity.getText())) {
            max = maxQuantity.getText();
        } else {
            max = "200";
            isCheck = false;
        }
        stocks = sQLHelper.getSelectedInventoryStock(min, max, searchBy.getText());
        cuTreeItem = new RecursiveTreeItem<>(stocks, RecursiveTreeObject::getChildren);
        inventoryTV.setRoot(cuTreeItem);

    }

    @FXML
    void tableSelected(MouseEvent event) {

    }

    @FXML
    void exportXl(ActionEvent event) throws IOException {
        if (stocks.size() > 0) {
            System.out.println("Exporting Excel");
            try {
                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet("Stock Details");
                XSSFRow header = sheet.createRow(0);
                header.createCell(0).setCellValue("Product Name");
                header.createCell(1).setCellValue("Total Quantity");

                for (int i = 0; i < stocks.size(); i++) {
                    XSSFRow headerOfI = sheet.createRow(i + 1);
                    headerOfI.createCell(0).setCellValue(stocks.get(i).getProductName());
                    headerOfI.createCell(1).setCellValue(stocks.get(i).getQuantity());
                }

                FileOutputStream fileOut = new FileOutputStream("D:\\StockDetails.xlsx");
                wb.write(fileOut);
                fileOut.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initTale() {
        JFXTreeTableColumn<Stock, String> nameCol = new JFXTreeTableColumn<>("Product Name");
        nameCol.setPrefWidth(200);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Stock, String> param) -> param.getValue().getValue().productName);

        JFXTreeTableColumn<Stock, String> availableCol = new JFXTreeTableColumn<>("Available");
        availableCol.setPrefWidth(200);
        availableCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Stock, String> param) -> param.getValue().getValue().quantity);

        cuTreeItem = new RecursiveTreeItem<>(stocks, RecursiveTreeObject::getChildren);
        inventoryTV.setRoot(cuTreeItem);
        inventoryTV.getColumns().setAll(nameCol, availableCol);
        inventoryTV.setShowRoot(false);
    }

}
