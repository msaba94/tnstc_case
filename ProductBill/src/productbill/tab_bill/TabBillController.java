/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.tab_bill;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author iCT-3
 */
public class TabBillController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TabPane mainTabPane;

    @FXML
    private JFXButton addNewTab;

    public static Tab selectedTab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        Platform.runLater(() -> {
            addNewTab.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.N, KeyCombination.ALT_DOWN), () -> {
                addNewTab.fire();
            });
        });

        Tab tab = new Tab("New Bill");
        tab.setClosable(true);
        tab.setId(UUID.randomUUID().toString());
        mainTabPane.getTabs().add(tab);
        selectedTab = tab;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/productbill/tab_bill/TabDetails.fxml"));
        AnchorPane tabAnchor = null;
        try {
            tabAnchor = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(TabBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tab.setContent(tabAnchor);

        mainTabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                selectedTab = (Tab) newValue;
                System.out.println("Selected Tab : " + selectedTab.getId());
            }
        });

    }

    public void addNewTab() throws IOException {
        int numTabs = mainTabPane.getTabs().size();
        Tab tab = new Tab("New Bill");
        tab.setClosable(true);
        tab.setId(UUID.randomUUID().toString());
        mainTabPane.getTabs().add(tab);
        mainTabPane.getSelectionModel().select(tab);
        mainTabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("TabDetails.fxml"));
        AnchorPane tabAnchor = loader.load();
        tab.setContent(tabAnchor);
    }

}
