/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.hsn;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import productbill.database.SQLHelper;
import productbill.utils.Utils;

/**
 * FXML Controller class
 *
 * @author iCT-3
 */
public class HSNController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXTextField hsnCode;

    @FXML
    private JFXTextField gstPercentage;

    @FXML
    private JFXTextField description;

    @FXML
    private JFXRadioButton goodsRadio;

    @FXML
    private JFXRadioButton servicesRadio;
    
    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXTreeTableView<HSN> hsnTV;

    private SQLHelper sQLHelper;

    private ObservableList<HSN> hsnList;
    private HSN selectedHSN;
    private TreeItem<HSN> cuTreeItem;
    final ToggleGroup group = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        goodsRadio.setToggleGroup(group);
        servicesRadio.setToggleGroup(group);
        sQLHelper = new SQLHelper();

        hsnList = sQLHelper.getAllHSN();
        initTableView();

    }

    private void setDataValues(HSN hsn) {
        if (hsn != null) {
            HSN hsnData = sQLHelper.getHSN(hsn.getId());
            if (hsn != null) {
                hsnCode.setText(hsnData.getHSN());
                description.setText(hsnData.getDescription());
                gstPercentage.setText(hsnData.getGST());
                addBtn.setText("Update");
            } else {
                clearAll();
            }
        }
    }

    @FXML
    void clearAll() {
        hsnCode.setText("");
        gstPercentage.setText("");
        description.setText("");
        addBtn.setText("Add");
        System.out.println(group.getSelectedToggle().equals(goodsRadio));
        group.selectToggle(goodsRadio);
        group.getSelectedToggle().setSelected(false);
    }

    @FXML
    void insertOrUpdate(ActionEvent event) {
        if (Utils.isValied(hsnCode.getText())) {
            if (addBtn.getText().equals("Update")) {
                HSN hsnValue = new HSN(selectedHSN.getId(), hsnCode.getText(), gstPercentage.getText(), description.getText(), (group.getSelectedToggle().equals(goodsRadio)) ? "1" : "0",null);
                if (sQLHelper.updateHSN(hsnValue)) {
                    System.out.println("HSN Updated!");
                    hsnList = sQLHelper.getAllHSN();
                    cuTreeItem = new RecursiveTreeItem<>(hsnList, RecursiveTreeObject::getChildren);
                    hsnTV.setRoot(cuTreeItem);
                } else {
                    System.out.println("HSN Not Updated!");
                }
            } else {
                HSN hsnValue = new HSN(null, hsnCode.getText(), gstPercentage.getText(), description.getText(), (group.getSelectedToggle().equals(goodsRadio)) ? "1" : "0",null);
                if (sQLHelper.insertHSN(hsnValue)) {
                    System.out.println("HSN Inserted!");
                    hsnList = sQLHelper.getAllHSN();
                    cuTreeItem = new RecursiveTreeItem<>(hsnList, RecursiveTreeObject::getChildren);
                    hsnTV.setRoot(cuTreeItem);
                    clearAll();
                } else {
                    System.out.println("HSN Not Inserted!");
                }
            }
        }
    }

    @FXML
    void deleteHSN(ActionEvent event) {
        selectedHSN = hsnTV.getSelectionModel().getSelectedItem().getValue();
        if (selectedHSN != null) {
            if (sQLHelper.deleteHSN(selectedHSN)) {
                System.out.println("Deleted");
                hsnList = sQLHelper.getAllHSN();
                cuTreeItem = new RecursiveTreeItem<>(hsnList, RecursiveTreeObject::getChildren);
                hsnTV.setRoot(cuTreeItem);
                clearAll();
            } else {
                System.out.println("Not Deleted");
            }
        }
        
    }

    @FXML
    void tableViewSelected(MouseEvent event) {
        selectedHSN = hsnTV.getSelectionModel().getSelectedItem().getValue();
        if (selectedHSN != null) {
            hsnCode.setText(selectedHSN.getHSN());
            gstPercentage.setText(selectedHSN.getGST());
            description.setText(selectedHSN.getDescription());
            if(selectedHSN.getType().equals("1")){
                group.selectToggle(goodsRadio);
            }else{ 
                group.selectToggle(servicesRadio);
            }
            addBtn.setText("Update");
        } else {
            clearAll();
        }
    }

    public void exportExcelSheet() throws IOException {
        hsnList = sQLHelper.getAllHSN();
        if (hsnList.size() > 0) {
            System.out.println("Exporting HSN Excel");
            try {
                XSSFWorkbook wb = new XSSFWorkbook();
                XSSFSheet sheet = wb.createSheet("Stock Details");
                XSSFRow header = sheet.createRow(0);
                header.createCell(0).setCellValue("type");
                header.createCell(1).setCellValue("description");
                header.createCell(1).setCellValue("rate");
                header.createCell(1).setCellValue("hsn");

                for (int i = 0; i < hsnList.size(); i++) {
                    XSSFRow headerOfI = sheet.createRow(i + 1);
                    if(hsnList.get(i).getType() == "1"){
                        headerOfI.createCell(0).setCellValue("Goods");
                    }else{
                        headerOfI.createCell(0).setCellValue("Services");
                    }
                    headerOfI.createCell(1).setCellValue(hsnList.get(i).getDescription());
                    headerOfI.createCell(2).setCellValue(Float.parseFloat(hsnList.get(i).getGST()));
                    headerOfI.createCell(3).setCellValue(hsnList.get(i).getHSN());
                }

                FileOutputStream fileOut = new FileOutputStream("D:\\HSNDetails.xlsx");
                wb.write(fileOut);
                fileOut.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(HSNController.class.getName()).log(Level.SEVERE, null, ex);
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
        System.out.println("Importing HSN Excel Sheet");
        try {
            FileInputStream fileIn = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                String value1 = Utils.defaultFloatInt(row.getCell(3).toString());
                String value2 = Utils.defaultFloatInt(row.getCell(2).toString());
                String value3= "1";
                if((!value1.equals("0"))&&(!value2.equals("0"))){
                    value1 = value1.replace(".0","");
                    value2 = value2.replace(".0","");
                    if(row.getCell(0).toString().equals("service")){
                        value3="0";
                    }
                    HSN hsn = new HSN(null, value1,value2,row.getCell(1).toString(),value3,null);
                    if (sQLHelper.checkHSN(row.getCell(0).toString())) {
                        if (sQLHelper.updateHSNWithCode(hsn)) {
                            System.out.println("Updated");
                        } else {
                            System.out.println("Not Updated");
                        }
                    } else {
                        if (sQLHelper.insertHSN(hsn)) {
                            System.out.println("Inserted");
                        } else {
                            System.out.println("Not Inserted");
                        }
                    }
                }else{
                    System.out.println("Omitted "+row.getCell(3).toString());
                }
            }
            hsnList = sQLHelper.getAllHSN();
            cuTreeItem = new RecursiveTreeItem<>(hsnList, RecursiveTreeObject::getChildren);
            hsnTV.setRoot(cuTreeItem);
            wb.close();
            fileIn.close();
            showSuccessDialog();
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            Logger.getLogger(HSNController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println(ex);
            Logger.getLogger(HSNController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success Dialog");
        alert.setHeaderText(null);
        alert.setContentText("HSN Import Successfully");
        alert.showAndWait();
    }

    private void initTableView() {

        JFXTreeTableColumn<HSN, String> nameCol = new JFXTreeTableColumn<>("HSN Code");
        nameCol.setPrefWidth(100);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HSN, String> param) -> param.getValue().getValue().hsn);

        JFXTreeTableColumn<HSN, String> gstCol = new JFXTreeTableColumn<>("GST");
        gstCol.setPrefWidth(100);
        gstCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HSN, String> param) -> param.getValue().getValue().gst);

        JFXTreeTableColumn<HSN, String> descriptionCol = new JFXTreeTableColumn<>("Description");
        descriptionCol.setPrefWidth(100);
        descriptionCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HSN, String> param) -> param.getValue().getValue().description);

        JFXTreeTableColumn<HSN, String> typeCol = new JFXTreeTableColumn<>("Type");
        typeCol.setPrefWidth(100);
        typeCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HSN, String> param) -> param.getValue().getValue().typeName);

        cuTreeItem = new RecursiveTreeItem<>(hsnList, RecursiveTreeObject::getChildren);
        hsnTV.setRoot(cuTreeItem);
        hsnTV.getColumns().setAll(nameCol, gstCol, descriptionCol, typeCol);
        hsnTV.setShowRoot(false);

    }

}
