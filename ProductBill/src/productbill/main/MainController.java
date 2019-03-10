/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.main;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import productbill.database.SQLHelper;
import productbill.order.BillOrder;
import productbill.utils.Utils;

/**
 *
 * @author iCT-3
 */
public class MainController implements Initializable {

    @FXML
    AnchorPane mainAnchor;

    @FXML
    private JFXButton billBtn;

    @FXML
    private JFXButton categoryBtn;

    @FXML
    private JFXButton productBtn;

    @FXML
    private JFXButton customerBtn;

    @FXML
    private JFXButton stockBtn;

    @FXML
    private JFXButton reportBtn;

    @FXML
    private JFXButton inventoryBtn;

    @FXML
    private JFXButton hsnBtn;

    @FXML
    private JFXButton orderBtn;

    private SQLHelper helper;

    public void switchCategory() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/category/Category.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    public void switchProduct() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/product/Product.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    public void switchCustomer() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/customer/Customer.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    public void switchTabBill() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/tab_bill/TabBill.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    public void switchReport() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/report/Report.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    @FXML
    void switchStock(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/stock/Stock.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    @FXML
    void switchInventory(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/inventory/Inventory.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    @FXML
    void switchHSN(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/hsn/HSN.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    @FXML
    void switchOrder(ActionEvent event) throws IOException {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Search By Bill No");
        dialog.setHeaderText("Please Enter Bill NO");
        dialog.setContentText("Bill No:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            BillOrder billOrder = helper.getBillOrder(result.get());
            if (billOrder != null) {
                switchToBillDetails(billOrder);
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Bill Not Found");
                alert.setContentText("Ooops, there was an error!");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(() -> {

            billBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN), () -> {
                billBtn.fire();
            });

            categoryBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), () -> {
                categoryBtn.fire();
            });

            customerBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN), () -> {
                customerBtn.fire();
            });

            productBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN), () -> {
                productBtn.fire();
            });

            stockBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), () -> {
                stockBtn.fire();
            });

            reportBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), () -> {
                reportBtn.fire();
            });

            inventoryBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN), () -> {
                inventoryBtn.fire();
            });

            hsnBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN), () -> {
                hsnBtn.fire();
            });

            orderBtn.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN), () -> {
                orderBtn.fire();
            });

        });

        try {

            helper = new SQLHelper();
            helper.createTables();
            switchTabBill();

//            if (helper.deleteAllTable()) {
//                System.out.println("Delete");
//            } else {
//                System.out.println("Not Delete");
//            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void switchToBillDetails(BillOrder billOrder) throws IOException {
        Utils.billOrder = billOrder;
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/productbill/order/UpdateOrder.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

}
