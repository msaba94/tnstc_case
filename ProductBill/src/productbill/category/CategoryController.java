package productbill.category;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import productbill.database.SQLHelper;
import productbill.utils.Utils;
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
public class CategoryController implements Initializable {

    @FXML
    private JFXTextField nameTF;

    @FXML
    private JFXComboBox<Category> parentCB;

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXTreeTableView<Category> categoryTable;

    private SQLHelper sQLHelper;
    private ObservableList<Category> categorys;
    private ObservableList<Category> categorysFromDB;
    private Category selectedCategory;
    private TreeItem<Category> cuTreeItem;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        sQLHelper = new SQLHelper();

        initWithNone();

        categorysFromDB = sQLHelper.getAllCategory();

        parentCB.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {

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
        parentCB.setStyle("-fx-font: 14px \"Montserrat Regular\";");

    }

    public void insertOrUpdateCategory() {
        boolean isCheck = true;
        String name = "";
        String type = "";

        if (Utils.isValied(nameTF.getText())) {
            name = nameTF.getText();
        } else {
            isCheck = false;
        }

        if (isCheck) {

            if (addBtn.getText().equals("Update")) {
                if (selectedCategory != null) {
                    selectedCategory.setName(name);
                    Category parent = (Category) parentCB.getValue();
                    selectedCategory.setParentCategory(parent.getId());
                    if (sQLHelper.updateCategory(selectedCategory)) {
                        System.out.println("Updated");
                        initWithNone();
                        categorysFromDB = sQLHelper.getAllCategory();
                        cuTreeItem = new RecursiveTreeItem<>(categorysFromDB, RecursiveTreeObject::getChildren);
                        categoryTable.setRoot(cuTreeItem);
                        clearData();
                    } else {
                        System.out.println("Not Update");
                    }
                }
            } else {
                Category parent = (Category) parentCB.getValue();
                Category category = new Category(null, name, parent.getId(), null);
                if (sQLHelper.insertCategory(category)) {
                    System.out.println("Inserted");
                    initWithNone();
                    categorysFromDB = sQLHelper.getAllCategory();
                    cuTreeItem = new RecursiveTreeItem<>(categorysFromDB, RecursiveTreeObject::getChildren);
                    categoryTable.setRoot(cuTreeItem);
                    clearData();
                } else {
                    System.out.println("Not Insert");
                }
            }
        }

    }

    public void clearData() {
        nameTF.setText("");
        addBtn.setText("Add");
    }

    public void refresh() {
        categorysFromDB = sQLHelper.getAllCategory();
        cuTreeItem = new RecursiveTreeItem<>(categorysFromDB, RecursiveTreeObject::getChildren);
        categoryTable.setRoot(cuTreeItem);
        initWithNone();
        clearData();
    }

    public void tvSelectedRow() {
        Category category = categoryTable.getSelectionModel().getSelectedItem().getValue();
        if (category != null) {
            selectedCategory = category;
            setData(selectedCategory);
        }
    }

    public void deleteCategory() {
        if (selectedCategory != null && selectedCategory.getId() != null) {
            if (sQLHelper.deleteCategory(selectedCategory.getId())) {
                System.out.println("Deleted");
                refresh();
            } else {
                System.out.println("Delete Fail");
            }
        }
    }

    private void setData(Category category) {
        nameTF.setText(category.getName());
        for (Category cate : categorysFromDB) {
            if (cate.getId().equals(category.getParentCategory())) {
                parentCB.getSelectionModel().select(cate);
                break;
            }else{
                parentCB.getSelectionModel().selectFirst();
            }
        }
        addBtn.setText("Update");
    }

    private void initWithNone() {
        categorys = FXCollections.observableArrayList();
        Category noneCategory = new Category("0", "None", "0", null);
        categorys.add(noneCategory);
        categorys.addAll(sQLHelper.getAllCategory());
        parentCB.setItems(categorys);
        parentCB.getSelectionModel().selectFirst();
    }

    public void importExcelSheet(File file) {
        System.out.println("Importing Category Excel Sheet");
        try {
            FileInputStream fileIn = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                Category selectedCategory = sQLHelper.getCategory(row.getCell(0).toString());
                Category selectedParentCategory = null;
                if (row.getCell(1) != null) {
                    selectedParentCategory = sQLHelper.getCategory(row.getCell(1).toString());
                }
                String parentID = "";
                if (selectedCategory == null) {
                    if (selectedParentCategory != null) {
                        parentID = selectedParentCategory.getId();
                    } else {
                        parentID = "0";
                    }

                    Category insertCategory = new Category(null, row.getCell(0).toString(), parentID, null);

                    if (sQLHelper.insertCategory(insertCategory)) {
                        System.out.println("Inserted");
                    } else {
                        System.out.println("Not Insert");
                    }
                } else {
                    System.out.println("Not Inserted :" + row.getCell(0));
                }
            }
            initWithNone();
            categorysFromDB = sQLHelper.getAllCategory();
            cuTreeItem = new RecursiveTreeItem<>(categorysFromDB, RecursiveTreeObject::getChildren);
            categoryTable.setRoot(cuTreeItem);
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
    void chooseFile(ActionEvent event) {
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
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Success Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Category Import Successfully");
        alert.showAndWait();
    }

    private void initTable() {
        JFXTreeTableColumn<Category, String> nameCol = new JFXTreeTableColumn<>("Name");
        nameCol.setPrefWidth(200);
        nameCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, String> param) -> param.getValue().getValue().name);

        JFXTreeTableColumn<Category, String> parentCal = new JFXTreeTableColumn<>("Parent Category");
        parentCal.setPrefWidth(200);
        parentCal.setCellValueFactory((TreeTableColumn.CellDataFeatures<Category, String> param) -> param.getValue().getValue().parentName);

        cuTreeItem = new RecursiveTreeItem<>(categorysFromDB, RecursiveTreeObject::getChildren);
        categoryTable.setRoot(cuTreeItem);
        categoryTable.getColumns().setAll(nameCol, parentCal);
        categoryTable.setShowRoot(false);
    }

}
