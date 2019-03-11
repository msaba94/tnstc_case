/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.customer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import productbill.database.SQLHelper;
import productbill.utils.Utils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

/**
 * FXML Controller class
 *
 * @author iCT-3
 */
public class CustomerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField nameTF;

    @FXML
    private JFXTextField addressTA;

    @FXML
    private JFXTextField mobileTF;

    @FXML
    private JFXRadioButton maleRB;

    @FXML
    private ToggleGroup genderGroup;

    @FXML
    private JFXRadioButton femaleRB;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXTreeTableView<Customer> customerTV;

    private SQLHelper sQLHelper;
    private ObservableList<Customer> customers;
    private Customer selectedCustomer;
    private TreeItem<Customer> cuTreeItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sQLHelper = new SQLHelper();

        customers = sQLHelper.getAllCustomer();
        initTreeTV();
        maleRB.setSelected(true);
    }

    public void insertOrUpdatedCustomer() {

        boolean isCheck = true;

        String name = "", address = "", mobileNo = "", gender = "";

        if (Utils.isValied(nameTF.getText())) {
            name = nameTF.getText();
        } else {
            isCheck = false;
        }

        if (isCheck) {
            address = addressTA.getText();
            mobileNo = mobileTF.getText();
            if (maleRB.isSelected()) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            if (addBtn.getText().equals("Update")) {
                if (selectedCustomer != null) {
                    selectedCustomer.setName(name);
                    selectedCustomer.setAddress(address);
                    selectedCustomer.setMobile(mobileNo);
                    selectedCustomer.setGender(gender);
                    if (sQLHelper.updateCustomer(selectedCustomer)) {
                        System.out.println("Updated!");
                        refresh();
                    } else {
                        System.out.println("Not Update!");
                    }
                }
            } else {

                Customer customer = new Customer(null, name, mobileNo, address, gender);
                if (sQLHelper.insertCustomer(customer)) {
                    refresh();
                    System.out.println("Insert!");
                } else {
                    System.out.println("Not Insert!");
                }
            }
        }

    }

    public void tvSelectedRow() {
        Customer customer = customerTV.getSelectionModel().getSelectedItem().getValue();
        if (customer != null) {
            selectedCustomer = customer;
            setData(selectedCustomer);
        }
    }

    public void deleteCustomer() {
        if (selectedCustomer != null && selectedCustomer.getId() != null) {
            if (sQLHelper.deleteCustomer(selectedCustomer.getId())) {
                System.out.println("Deleted");
                refresh();
            } else {
                System.out.println("Delete Fail");
            }
        }
    }

    private void setData(Customer customer) {
        nameTF.setText(customer.getName());
        addressTA.setText(customer.getAddress());
        mobileTF.setText(customer.getMobile());
        if (customer.getGender().equals("Male")) {
            maleRB.setSelected(true);
        } else {
            femaleRB.setSelected(true);
        }
        addBtn.setText("Update");
    }

    public void refresh() {
        customers = sQLHelper.getAllCustomer();
        cuTreeItem = new RecursiveTreeItem<>(customers, RecursiveTreeObject::getChildren);
        customerTV.setRoot(cuTreeItem);
        clearAll();
    }

    private void clearAll() {
        nameTF.setText("");
        addressTA.setText("");
        mobileTF.setText("");

        addBtn.setText("Add");
    }
    
    private void initTreeTV() {
        JFXTreeTableColumn<Customer, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setPrefWidth(200);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) -> param.getValue().getValue().name);

        JFXTreeTableColumn<Customer, String> addressCol = new JFXTreeTableColumn<>("Address");
        addressCol.setPrefWidth(200);
        addressCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) -> param.getValue().getValue().address);

        JFXTreeTableColumn<Customer, String> mobileNoCol = new JFXTreeTableColumn<>("Mobile No");
        mobileNoCol.setPrefWidth(200);
        mobileNoCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) -> param.getValue().getValue().mobile);

        JFXTreeTableColumn<Customer, String> genderCol = new JFXTreeTableColumn<>("Gender");
        genderCol.setPrefWidth(100);
        genderCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Customer, String> param) -> param.getValue().getValue().gender);

        cuTreeItem = new RecursiveTreeItem<>(customers, RecursiveTreeObject::getChildren);
        customerTV.setRoot(cuTreeItem);
        customerTV.getColumns().setAll(nameCol, addressCol, mobileNoCol, genderCol);
        customerTV.setShowRoot(false);

    }

}
