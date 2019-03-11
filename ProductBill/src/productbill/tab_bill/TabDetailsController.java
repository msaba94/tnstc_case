/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.tab_bill;

import java.io.IOException;
import productbill.customer.CustomerAC;
import productbill.customer.Customer;
import productbill.product.ProductAC;
import productbill.product.Product;
import productbill.category.Category;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import productbill.database.SQLHelper;
import productbill.utils.Utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author iCT-3
 */
public class TabDetailsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane root;

    @FXML
    private JFXComboBox<Customer> customerCB;

    @FXML
    private JFXTextField productCodeTF;

    @FXML
    private JFXTextField productNameTF;

    @FXML
    private JFXTextField categoryTF;

    @FXML
    private JFXTextField priceTF;

    @FXML
    private JFXTextField quantityTF;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton proceedBtn;

    @FXML
    private JFXTreeTableView<BillingDetail> billTV;

    @FXML
    private JFXTextField totalTF;

    @FXML
    private JFXTextField customerAmount;

    private SQLHelper sQLHelper;
    private ObservableList<Product> products;
    private ObservableList<Customer> customers;
    private CustomerAC customerAC;
    private ProductAC productAC;
    private Product selectedProduct;
    private Customer selectedCustomer;
    private ObservableList<BillingDetail> billDetails;
    private BillingDetail selectedBill;
    private String tabId;
    private TreeItem<BillingDetail> cuTreeItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        Platform.runLater(() -> {
            proceedBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN), () -> {
                proceedBtn.fire();
            });
        });

        sQLHelper = new SQLHelper();
        productCodeTF.requestFocus();

        if (deleteDB()) {
            System.out.println("Deleted!");
        } else {
            System.out.println("Not Deleted!");
        }
        billDetails = FXCollections.observableArrayList();
        initTable();
        products = sQLHelper.getAllProduct();
        customers = sQLHelper.getAllCustomer();

        customerCB.setItems(customers);
        customerCB.setStyle("-fx-font: 14px \"Montserrat Regular\";");

        customerAC = new CustomerAC(customerCB);

        Utils.numericTextField(quantityTF);
        Utils.numericTextField(customerAmount);

        productCodeTF.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    Product product = sQLHelper.getProduct(productCodeTF.getText());
                    if (product != null) {
                        selectedProduct = product;
                        setDataValues(product);
                    }
                }
            }
        });

        customerCB.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedCustomer = (Customer) newValue;
            }
        });

        quantityTF.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {

                    insertToTable();

                }
            }
        });

        addBtn.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    insertToTable();
                }
            }
        });

        customerAmount.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    try {
                        showConfirmDialog();
                    } catch (IOException ex) {
                        Logger.getLogger(TabDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    private void setDataValues(Product selectedProduct) {
        if (selectedProduct != null) {
            productNameTF.setText(selectedProduct.getTitle());
            priceTF.setText(selectedProduct.getSellingPrice());
            ObservableList<Category> category = sQLHelper.searchCategory(selectedProduct.getCategoryId());
            if (category.size() > 0) {
                categoryTF.setText(category.get(0).getName());
            } else {
                categoryTF.setText("Undefined");
            }
            quantityTF.requestFocus();
        }
    }

    public void insertToTable() {
        if (selectedProduct.getQuantity() >= Utils.defaultInt(quantityTF.getText())) {
            if (Utils.isValied(quantityTF.getText())) {
                if (selectedProduct != null) {
                    System.out.println("Tab Id : " + TabBillController.selectedTab.getId());
                    if (selectedCustomer != null || TabBillController.selectedTab.getId() != null) {
                        double total = calculateTotal(selectedProduct.getSellingPrice(), quantityTF.getText());
                        double gstAmount = (total * Utils.defaultDouble(selectedProduct.getGst())) / 100;
                        String totalStr = Utils.defaultString((total + gstAmount));

                        String billID = "";
                        if (selectedCustomer != null) {
                            billID = selectedCustomer.getId();
                        } else {
                            billID = TabBillController.selectedTab.getId();
                        }

                        BillingDetail detail = new BillingDetail(null, selectedProduct.getId(),
                                selectedProduct.getTitle(), billID,
                                categoryTF.getText(), quantityTF.getText(),
                                selectedProduct.getSellingPrice(), totalStr);
                        detail.setGst(Utils.defaultString(gstAmount));

                        billDetails.add(detail);
                        calculateSubTotal();
                        refresh();
                    }
                }
            }
        } else {
            JFXSnackbar bar = new JFXSnackbar(root);
            bar.enqueue(new JFXSnackbar.SnackbarEvent("Out of Stock"));
        }
    }

    private void calculateSubTotal() {
        double total = 0;
        for (BillingDetail detail : billDetails) {
            total = total + Utils.defaultDouble(detail.getTotal());
        }
        totalTF.setText("" + total);
    }

    public void tvSelectedRow() {
        BillingDetail billingDetail = billTV.getSelectionModel().getSelectedItem().getValue();
        if (billingDetail != null) {
            selectedBill = billingDetail;
        }
    }

    public void deleteBill() {
        if (selectedBill != null) {
            billDetails.remove(selectedBill);
            calculateSubTotal();
            refresh();
        }
    }

    private double calculateTotal(String priceStr, String quantityStr) {

        double prince = Utils.defaultDouble(priceStr);
        double quantity = Utils.defaultDouble(quantityStr);

        return prince * quantity;

    }

    public void refresh() {

        cuTreeItem = new RecursiveTreeItem<>(billDetails, RecursiveTreeObject::getChildren);
        billTV.setRoot(cuTreeItem);

        productNameTF.setText("");
        priceTF.setText("");
        categoryTF.setText("");
        quantityTF.setText("");
        productCodeTF.setText("");
        productCodeTF.requestFocus();
    }

    public void showConfirmDialog() throws IOException {
        if (Utils.isValied(customerAmount.getText())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/productbill/tab_bill/ConfirmBilling.fxml"));

            //ConfirmBillingController cbc = loader.getController();
            Utils.totalGst = getTotalGst(billDetails);
            Utils.billDetails = billDetails;
            Utils.total = totalTF.getText();
            Utils.cusAmount = customerAmount.getText();

            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 250);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Need Customer Amount");
            alert.setContentText("Please, enter Customer amount to proceed next!");

            alert.showAndWait();
            customerAmount.requestFocus();
        }
    }

    public void printDB() {
        if (deleteDB()) {
            System.out.println("Deleted!");
        } else {
            System.out.println("Not Deleted!");
        }

        if (billDetails.size() > 0) {
            billDetails.forEach((detail) -> {
                if (sQLHelper.insertBill(detail)) {
                    System.out.println("Bill Inserted!");
                } else {
                    System.out.println("Bill Not Inserted!");
                }
            });
//            PrintReport report = new PrintReport();
//            report.showReport(totalTF.getText());
        }
    }

    private boolean deleteDB() {
        return sQLHelper.deleteBillDetails();
    }

    private String getTotalGst(ObservableList<BillingDetail> billDetails) {

        double totalGst = 0.0;
        for (BillingDetail detail : billDetails) {
            totalGst = totalGst + Utils.defaultDouble(detail.getGst());
        }
        return Utils.defaultString(totalGst);

    }

    private void initTable() {
        JFXTreeTableColumn<BillingDetail, String> nameCol = new JFXTreeTableColumn<>("Product Name");
        nameCol.setPrefWidth(200);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BillingDetail, String> param) -> param.getValue().getValue().productName);

        JFXTreeTableColumn<BillingDetail, String> categoryNameCol = new JFXTreeTableColumn<>("Category Name");
        categoryNameCol.setPrefWidth(200);
        categoryNameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BillingDetail, String> param) -> param.getValue().getValue().categoryName);

        JFXTreeTableColumn<BillingDetail, String> quantityCol = new JFXTreeTableColumn<>("Quantity");
        quantityCol.setPrefWidth(120);
        quantityCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BillingDetail, String> param) -> param.getValue().getValue().quantity);

        JFXTreeTableColumn<BillingDetail, String> priceCol = new JFXTreeTableColumn<>("Price");
        priceCol.setPrefWidth(120);
        priceCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BillingDetail, String> param) -> param.getValue().getValue().price);

        JFXTreeTableColumn<BillingDetail, String> totalCol = new JFXTreeTableColumn<>("Total");
        totalCol.setPrefWidth(150);
        totalCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<BillingDetail, String> param) -> param.getValue().getValue().total);

        cuTreeItem = new RecursiveTreeItem<>(billDetails, RecursiveTreeObject::getChildren);
        billTV.setRoot(cuTreeItem);
        billTV.getColumns().setAll(nameCol, categoryNameCol, quantityCol, priceCol, totalCol);
        billTV.setShowRoot(false);
    }

}
