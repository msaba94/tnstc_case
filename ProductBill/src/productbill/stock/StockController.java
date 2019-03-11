/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.stock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import productbill.category.CategoryController;
import productbill.database.SQLHelper;
import productbill.product.Product;
import productbill.product.ProductAC;
import productbill.utils.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
 * @author iCT-3
 */
public class StockController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXComboBox<Product> productCB;

    @FXML
    private JFXTextField avaiableTF;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXTreeTableView<Stock> stockTV;

    private SQLHelper sQLHelper;

    private ObservableList<Product> products;
    private ObservableList<Stock> stocks;
    private Product selectedProduct;
    private TreeItem<Stock> cuTreeItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        sQLHelper = new SQLHelper();

        products = sQLHelper.getAllProduct();
        stocks = sQLHelper.getAllProductStock();

        Utils.doubleTextField(avaiableTF);
        productCB.setItems(products);
        productCB.setStyle("-fx-font: 14px \"Montserrat Regular\";");
        ProductAC productAC = new ProductAC(productCB);

        productCB.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedProduct = (Product) newValue;
                setDataValues(selectedProduct);
            }
        });

        initTable();

    }

    private void setDataValues(Product product) {
        if (product != null) {
            Stock stock = sQLHelper.getProductStock(product.getId());
            if (stock != null) {
                avaiableTF.setText(stock.getQuantity());
                addBtn.setText("Update");
            } else {
                avaiableTF.setText("");
                addBtn.setText("Add");
            }
        }
    }

    @FXML
    void clearAll(ActionEvent event) {
        avaiableTF.setText("");
        addBtn.setText("Add");
    }

    @FXML
    void insertOrUpdate(ActionEvent event) {
        if (selectedProduct != null) {
            if (Utils.isValied(avaiableTF.getText())) {
                Stock stock = new Stock(null, selectedProduct.getId(), null, avaiableTF.getText());
                if (addBtn.getText().equals("Update")) {
                    if (sQLHelper.updateProductStock(stock)) {
                        System.out.println("Stock Updated!");
                        stocks = sQLHelper.getAllProductStock();
                        cuTreeItem = new RecursiveTreeItem<>(stocks, RecursiveTreeObject::getChildren);
                        stockTV.setRoot(cuTreeItem);
                    } else {
                        System.out.println("Stock Not Updated!");
                    }
                } else {
                    if (sQLHelper.insertStock(stock)) {
                        System.out.println("Stock Inserted!");
                        stocks = sQLHelper.getAllProductStock();
                        cuTreeItem = new RecursiveTreeItem<>(stocks, RecursiveTreeObject::getChildren);
                        stockTV.setRoot(cuTreeItem);
                    } else {
                        System.out.println("Stock Not Inserted!");
                    }
                }

            }
        }

    }

    @FXML
    void tableViewSelected(MouseEvent event) {
        Stock stock = stockTV.getSelectionModel().getSelectedItem().getValue();
        if (stock != null) {
            products.stream().filter((product) -> (stock.getProductId().equals(product.getId()))).forEachOrdered((product) -> {
                productCB.getSelectionModel().select(product);
            });
            avaiableTF.setText(stock.getQuantity());
            addBtn.setText("Update");
        } else {
            avaiableTF.setText("");
            addBtn.setText("Add");
        }
    }

    public void exportExcelSheet() throws IOException {
        stocks = sQLHelper.getAllProductStockDetails();
        if (stocks.size() > 0) {
            System.out.println("Exporting Stock Excel");
            try {
                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet("Stock Details");
                XSSFRow header = sheet.createRow(0);
                header.createCell(0).setCellValue("Product ID");
                header.createCell(1).setCellValue("Product Name");
                header.createCell(2).setCellValue("Quantity");

                for (int i = 0; i < stocks.size(); i++) {
                    XSSFRow headerOfI = sheet.createRow(i + 1);
                    headerOfI.createCell(0).setCellValue(Float.parseFloat(stocks.get(i).getId()));
                    headerOfI.createCell(1).setCellValue(stocks.get(i).getProductName());
                    if (stocks.get(i).getQuantity() != null) {
                        headerOfI.createCell(2).setCellValue(Float.parseFloat(stocks.get(i).getQuantity()));
                    } else {
                        headerOfI.createCell(2).setCellValue(Float.parseFloat("0"));
                    }
                }

                FileOutputStream fileOut = new FileOutputStream("D:\\StockDetails.xlsx");
                wb.write(fileOut);
                fileOut.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void importExcelSheet() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        if (Utils.stage != null) {
            File file = chooser.showOpenDialog(Utils.stage);
            if (file != null) {
                importExcelSheetToDB(file);
            }
        }
    }

    public void importExcelSheetToDB(File file) {
        System.out.println("Importing Stock Excel Sheet");
        try {
            FileInputStream fileIn = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                if (sQLHelper.updateProductStockWithID(row.getCell(0).toString(), row.getCell(2).toString())) {
                    System.out.println("Updated");
                } else {
                    Stock myStock = new Stock(null, row.getCell(0).toString(), null, row.getCell(2).toString());
                    if (sQLHelper.insertStock(myStock)) {
                        System.out.println("Inserted");
                    } else {
                        System.out.println("Not Inserted");
                    }
                }
            }
            stocks = sQLHelper.getAllProductStock();
            cuTreeItem = new RecursiveTreeItem<>(stocks, RecursiveTreeObject::getChildren);
            stockTV.setRoot(cuTreeItem);
            wb.close();
            fileIn.close();
            showSuccessDialog();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println(ex);
            Logger.getLogger(CategoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Stock Import Successfully");
        alert.showAndWait();
    }

    private void initTable() {
        JFXTreeTableColumn<Stock, String> nameCol = new JFXTreeTableColumn<>("Product Name");
        nameCol.setPrefWidth(200);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Stock, String> param) -> param.getValue().getValue().productName);

        JFXTreeTableColumn<Stock, String> availableCol = new JFXTreeTableColumn<>("Avaialable");
        availableCol.setPrefWidth(200);
        availableCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Stock, String> param) -> param.getValue().getValue().quantity);

        cuTreeItem = new RecursiveTreeItem<>(stocks, RecursiveTreeObject::getChildren);
        stockTV.setRoot(cuTreeItem);
        stockTV.getColumns().setAll(nameCol, availableCol);
        stockTV.setShowRoot(false);
    }

}
