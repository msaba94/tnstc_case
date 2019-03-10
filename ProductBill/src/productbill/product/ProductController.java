/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import productbill.category.Category;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import productbill.category.CategoryController;
import productbill.database.SQLHelper;
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
public class ProductController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXComboBox<Category> categoryCB;

    @FXML
    private JFXTextField titleTF;

    @FXML
    private JFXTextField despTF;

    @FXML
    private JFXTextField codeTF;

    @FXML
    private JFXTextField purchasedPriceTF;

    @FXML
    private JFXTextField sellingPriceTF;

    @FXML
    private JFXTextField hsnCodeTF;

    @FXML
    private JFXTextField mrpTF;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXTreeTableView<Product> productTV;

    private SQLHelper sQLHelper;
    private ObservableList<Product> products;
    private ObservableList<Category> categorys;
    private Product selectedProduct;
    private TreeItem<Product> cuTreeItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sQLHelper = new SQLHelper();

        products = sQLHelper.getAllProduct();

        Utils.doubleTextField(purchasedPriceTF);
        Utils.doubleTextField(sellingPriceTF);
        Utils.doubleTextField(mrpTF);

        categorys = sQLHelper.getAllCategory();
        categoryCB.setItems(categorys);
        categoryCB.setStyle("-fx-font: 14px \"Montserrat Regular\";");
        categoryCB.getSelectionModel().selectFirst();

        categoryCB.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {

            @Override
            public ListCell<Category> call(ListView<Category> p) {

                final ListCell<Category> cell = new ListCell<Category>() {

                    @Override
                    protected void updateItem(Category t, boolean bln) {
                        super.updateItem(t, bln);

                        if (t != null) {
                            setText(t.getName());
                        } else {
                            setText(null);
                        }
                    }

                };

                return cell;
            }
        });
        initTable();

    }

    public void insertOrUpdateProduct() {
        boolean isCheck = true;
        String title = "", desp = "", purchasedPrice = "", sellingPrice = "",
                mrp = "", code = "", hsnCode = "";

        if (Utils.isValied(titleTF.getText())) {
            title = titleTF.getText();
        } else {
            isCheck = false;
        }

        if (Utils.isValied(purchasedPriceTF.getText())) {
            purchasedPrice = purchasedPriceTF.getText();
        } else {
            isCheck = false;
        }

        if (Utils.isValied(sellingPriceTF.getText())) {
            sellingPrice = sellingPriceTF.getText();
        } else {
            isCheck = false;
        }

        if (Utils.isValied(mrpTF.getText())) {
            mrp = mrpTF.getText();
        } else {
            isCheck = false;
        }

        if (Utils.isValied(codeTF.getText())) {
            code = codeTF.getText();
        } else {
            isCheck = false;
        }

        if (Utils.isValied(hsnCodeTF.getText())) {
            hsnCode = hsnCodeTF.getText();
        } else {
            isCheck = false;
        }

        if (isCheck) {
            Category category = (Category) categoryCB.getValue();
            desp = despTF.getText();
            if (addBtn.getText().equals("Update")) {
                if (selectedProduct != null && selectedProduct.getId() != null) {
                    selectedProduct.setCategoryId(category.getId());
                    selectedProduct.setTitle(title);
                    selectedProduct.setDesc(desp);
                    selectedProduct.setPurchasedPrice(purchasedPrice);
                    selectedProduct.setSellingPrice(sellingPrice);
                    selectedProduct.setMrp(mrp);
                    selectedProduct.setCode(code);
                    selectedProduct.setHsnCode(hsnCode);
                    if (sQLHelper.updateProduct(selectedProduct)) {
                        System.err.println("Updated!");
                        products = sQLHelper.getAllProduct();
                        cuTreeItem = new RecursiveTreeItem<>(products, RecursiveTreeObject::getChildren);
                        productTV.setRoot(cuTreeItem);
                        clearData();
                    } else {
                        System.err.println("Not Update");
                    }
                }
            } else {
                Product product = new Product(null, title, desp, category.getId(),
                        code, null, purchasedPrice, null, hsnCode, sellingPrice, mrp,
                        null, null, null, null, null);

                if (sQLHelper.insertProduct(product)) {
                    System.out.println("Insert!");
                    products = sQLHelper.getAllProduct();
                    cuTreeItem = new RecursiveTreeItem<>(products, RecursiveTreeObject::getChildren);
                    productTV.setRoot(cuTreeItem);
                    clearData();
                } else {
                    System.out.println("Not Insert");
                }
            }

        }
    }

    public void clearData() {
        titleTF.setText("");
        despTF.setText("");
        despTF.setText("");
        purchasedPriceTF.setText("");
        sellingPriceTF.setText("");
        mrpTF.setText("");
        codeTF.setText("");
        hsnCodeTF.setText("");
        categoryCB.getSelectionModel().selectFirst();
        addBtn.setText("Add");
    }

    public void tvSelectedRow() {
        Product product = productTV.getSelectionModel().getSelectedItem().getValue();
        if (product != null) {
            selectedProduct = product;
            setData(selectedProduct);
            addBtn.setText("Update");
        }
    }

    public void deleteProduct() {
        if (selectedProduct != null) {
            if (sQLHelper.deleteProduct(selectedProduct.getId())) {
                System.out.println("Deleted");
                refresh();
            } else {
                System.out.println("Delete Fail");
            }
        }
    }

    private void setData(Product product) {
        titleTF.setText(product.getTitle());
        despTF.setText(product.getDesc());
        purchasedPriceTF.setText(product.getPurchasedPrice());
        sellingPriceTF.setText(product.getSellingPrice());
        mrpTF.setText(product.getMrp());
        codeTF.setText(product.getCode());
        hsnCodeTF.setText(product.getHsnCode());
        categorys.stream().filter((category) -> (category.getId().equals(product.getCategoryId()))).forEachOrdered((category) -> {
            categoryCB.getSelectionModel().select(category);
        });
    }

    public void refresh() {
        products = sQLHelper.getAllProduct();
        cuTreeItem = new RecursiveTreeItem<>(products, RecursiveTreeObject::getChildren);
        productTV.setRoot(cuTreeItem);
        clearData();
    }

    public void insertMyProduct(Row row, String id) {
        Product oldProduct = sQLHelper.getProduct(row.getCell(3).toString());
        if (oldProduct == null) {
            Product selectedProduct = new Product(null,
                    row.getCell(0).toString(),
                    row.getCell(1).toString(),
                    id,
                    row.getCell(3).toString(),
                    row.getCell(4).toString(),
                    row.getCell(5).toString(),
                    row.getCell(6).toString(),
                    row.getCell(7).toString(),
                    row.getCell(8).toString(),
                    row.getCell(9).toString(),
                    row.getCell(10).toString(),
                    row.getCell(11).toString(),
                    row.getCell(12).toString(),
                    "1",
                    null);
            if (sQLHelper.insertProduct(selectedProduct)) {
                System.out.println("Insert!");
            } else {
                System.out.println("Not Insert");
            }
        }
    }

    public void importExcelSheet(File file) {
        System.out.println("Importing Product Excel Sheet");
        try {
            FileInputStream fileIn = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                Category selectedCategory = sQLHelper.getCategory(row.getCell(2).toString());
                if (selectedCategory == null) {
                    Category insertCategory = new Category(null, row.getCell(2).toString(), "0", null);
                    int ID = sQLHelper.insertCategoryReturnID(insertCategory);
                    if (ID > 0) {
                        System.out.println("Inserted Category");
                    } else {
                        System.out.println("Not Insert Category");
                    }
                    insertMyProduct(row, ID + "");
                } else {
                    insertMyProduct(row, selectedCategory.getId());
                }
            }
            products = sQLHelper.getAllProduct();
            cuTreeItem = new RecursiveTreeItem<>(products, RecursiveTreeObject::getChildren);
            productTV.setRoot(cuTreeItem);
            categorys = sQLHelper.getAllCategory();
            categoryCB.setItems(categorys);
            clearData();
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

    @FXML
    void fileChooser(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        if (Utils.stage != null) {
            File file = chooser.showOpenDialog(Utils.stage);
            if (file != null) {
                importExcelSheet(file);
            }
        }
    }

    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Product Import Successfully");
        alert.showAndWait();
    }

    private void initTable() {
        JFXTreeTableColumn<Product, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setPrefWidth(100);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().title);

        JFXTreeTableColumn<Product, String> despCol = new JFXTreeTableColumn<>("Desp");
        despCol.setPrefWidth(100);
        despCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().desc);

        JFXTreeTableColumn<Product, String> codeCol = new JFXTreeTableColumn<>("Code");
        codeCol.setPrefWidth(100);
        codeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().code);

        JFXTreeTableColumn<Product, String> purchasedPriceCol = new JFXTreeTableColumn<>("Purchased Price");
        purchasedPriceCol.setPrefWidth(100);
        purchasedPriceCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().purchasedPrice);

        JFXTreeTableColumn<Product, String> sellingPriceCol = new JFXTreeTableColumn<>("Selling Price");
        sellingPriceCol.setPrefWidth(100);
        sellingPriceCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().sellingPrice);

        JFXTreeTableColumn<Product, String> hsnCodeCol = new JFXTreeTableColumn<>("HSN Code");
        hsnCodeCol.setPrefWidth(100);
        hsnCodeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().hsnCode);

        JFXTreeTableColumn<Product, String> mrpCol = new JFXTreeTableColumn<>("MRP");
        mrpCol.setPrefWidth(100);
        mrpCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Product, String> param) -> param.getValue().getValue().mrp);

        cuTreeItem = new RecursiveTreeItem<>(products, RecursiveTreeObject::getChildren);
        productTV.setRoot(cuTreeItem);
        productTV.getColumns().setAll(nameCol, despCol, codeCol, purchasedPriceCol, sellingPriceCol, hsnCodeCol,
                mrpCol);
        productTV.setShowRoot(false);
    }

}
