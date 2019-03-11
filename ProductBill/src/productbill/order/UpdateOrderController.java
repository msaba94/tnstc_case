/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.order;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import productbill.category.Category;
import productbill.database.SQLHelper;
import productbill.jasper_report.PrintReport;
import productbill.product.Product;
import productbill.tab_bill.BillingDetail;
import productbill.tab_bill.OrderDetail;
import productbill.tab_bill.TotalBill;
import productbill.utils.Utils;

/**
 * FXML Controller class
 *
 * @author iCT-3
 */
public class UpdateOrderController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField customerName;

    @FXML
    private JFXTextField productCode;

    @FXML
    private JFXTextField productName;

    @FXML
    private JFXTextField categoryName;

    @FXML
    private JFXTextField price;

    @FXML
    private JFXTextField quantity;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXTreeTableView<BillingDetail> productTV;

    @FXML
    private JFXTextField totalAmount;

    @FXML
    private JFXButton deleteBtn;
    
    @FXML
    private Label exchangeCount;

    private TreeItem<BillingDetail> cuTreeItem;
    private BillOrder billOrder;
    private BillingDetail selectedBillingDetail;
    private Product selectedProduct;
    private ObservableList<BillingDetail> billDetails;
    private SQLHelper sQLHelper;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        billOrder = Utils.billOrder;
        sQLHelper = new SQLHelper();

        customerName.setText(billOrder.customerName);

        Utils.numericTextField(quantity);
        billDetails = sQLHelper.getAllProductByBillID(billOrder.getBillId(), billOrder.getCustomerId());
        exchangeCount.setText("Exchange Count : " + billOrder.getExchangeCount());
        
        initOrderTable();
        calculateTotal();

        productCode.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    Product product = sQLHelper.getProduct(productCode.getText());
                    if (product != null) {
                        selectedProduct = product;
                        setDataValues(product);
                    }
                }
            }
        });
        
        quantity.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    addOrUpdateAction();
                }
            }
        });
        
        
    }

    @FXML
    void addOrUpdateAction() {
        if (Utils.isValied(quantity.getText())) {
            if (addBtn.getText().equals("Add")) {
                if (selectedProduct != null) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setBillId(Utils.defaultInt(billOrder.getBillId()));
                    orderDetail.setId(null);
                    orderDetail.setProductId(Utils.defaultInt(selectedProduct.getId()));
                    orderDetail.setQuantity(Utils.defaultInt(quantity.getText()));
                    if (sQLHelper.insertOrderDetail(orderDetail)) {
                        System.out.println("New Order Inserted!");
                        clearAll();
                    } else {
                        System.out.println("New Order not Inserted!");
                    }
                }
            } else if (addBtn.getText().equals("Update")) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setBillId(Utils.defaultInt(billOrder.getBillId()));
                orderDetail.setId(Utils.defaultInt(selectedBillingDetail.getId()));
                orderDetail.setProductId(Utils.defaultInt(selectedBillingDetail.getProductId()));
                orderDetail.setQuantity(Utils.defaultInt(quantity.getText()));
                if (sQLHelper.updateOrderDetil(orderDetail)) {
                    System.out.println("New Order Updated!");
                    clearAll();
                } else {
                    System.out.println("New Order not Updated!");
                }
            }
        }

    }

    @FXML
    void clearAll() {
        productCode.setText("");
        productName.setText("");
        categoryName.setText("");
        price.setText("");
        quantity.setText("");
        addBtn.setText("Add");
        billDetails = sQLHelper.getAllProductByBillID(billOrder.getBillId(), billOrder.getCustomerId());
        cuTreeItem = new RecursiveTreeItem<>(billDetails, RecursiveTreeObject::getChildren);
        productTV.setRoot(cuTreeItem);
        calculateTotal();
    }

    @FXML
    void deleteProduct() {
        if (selectedBillingDetail != null) {
            if (sQLHelper.deleteOrderDetail(selectedBillingDetail.getId())) {
                System.out.println("New Order Deleted!");
            } else {
                System.out.println("New Order Not Deleted!");
            }
            clearAll();
        }
    }

    @FXML
    void proceedAction() {
        TotalBill totalBill = new TotalBill(billOrder.getBillId(),
                totalAmount.getText(), billOrder.getCustomerId(), null, null, null);
        totalBill.setExchangeCount(billOrder.getExchangeCount());
        if (sQLHelper.updateBill(totalBill)){
            System.out.println("New Order Update!");
            billOrder = sQLHelper.getBillOrder(billOrder.getBillId());
            exchangeCount.setText("Exchange Count : " + billOrder.getExchangeCount());
            createdReport(totalBill);
        }else{
            System.out.println("New Order not Update!");
        }
    }

    @FXML
    void selectedTVRow(MouseEvent event) {
        BillingDetail billingDetail = productTV.getSelectionModel().getSelectedItem().getValue();
        if (billingDetail != null) {
            selectedBillingDetail = billingDetail;
            setDataToUI(billingDetail);
        }
    }

    private void setDataToUI(BillingDetail billingDetail) {
        if (billingDetail != null) {
            productCode.setText(billingDetail.getProductCode());
            productName.setText(billingDetail.getProductName());
            categoryName.setText(billingDetail.getCategoryName());
            price.setText(billingDetail.getPrice());
            quantity.setText(billingDetail.getQuantity());
            addBtn.setText("Update");
        }
    }

    private void setDataValues(Product product) {
        if (product != null) {
            productName.setText(product.getTitle());
            price.setText(product.getSellingPrice());
            ObservableList<Category> category = sQLHelper.searchCategory(product.getCategoryId());
            if (category.size() > 0) {
                categoryName.setText(category.get(0).getName());
            } else {
                categoryName.setText("Undefined");
            }
            quantity.requestFocus();
            addBtn.setText("Add");
        }
    }

    private void initOrderTable() {

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
        productTV.setRoot(cuTreeItem);
        productTV.getColumns().setAll(nameCol, categoryNameCol, quantityCol, priceCol, totalCol);
        productTV.setShowRoot(false);

    }

    private void calculateTotal() {
        double total = 0;
        for (BillingDetail detail : billDetails) {
            total = total + Utils.defaultDouble(detail.getTotal());
        }
        totalAmount.setText("" + total);
    }

    private void createdReport(TotalBill totalBill) {        
        double totalGst = 0;
        for (BillingDetail detail : billDetails) {
            int quantity  = Utils.defaultInt(detail.getQuantity());
            double price = Utils.defaultDouble(detail.getPrice());
            double total = quantity * price;
            double gstWithTotal = total + (Utils.defaultDouble(detail.getGst()) / 100);
            totalGst = totalGst + gstWithTotal;
        }
        
        double totalBeforeGst = Utils.defaultDouble(totalBill.getTotalAmount()) - totalGst;
        
        Map parametersMap = new HashMap();
        parametersMap.put("grand_total", totalBill.getTotalAmount());
        parametersMap.put("total_gst", Utils.defaultString(totalBeforeGst));
        parametersMap.put("total_before_gst", Utils.defaultString(totalGst));
        parametersMap.put("bill_id", Utils.defaultInt(totalBill.getId()));

        PrintReport report = new PrintReport();
        report.showReport(parametersMap);
    }

}
