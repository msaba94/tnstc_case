/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mactapplication.main_menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import mactapplication.database.SQLHelper;

/**
 * FXML Controller class
 *
 * @author Sabapathi
 */
public class MainMenu implements Initializable {

    @FXML
    private AnchorPane mainAnchor;

    private SQLHelper helper;

    @FXML
    public void switchAddNew() throws IOException {
        ScrollPane pane = FXMLLoader.load(getClass().getResource("/mactapplication/add_new/add_new.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    @FXML
    public void switchViewAll() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/mactapplication/view_all/view_all.fxml"));
        mainAnchor.getChildren().setAll(pane);
        pane.prefWidthProperty().bind(mainAnchor.widthProperty());
        pane.prefHeightProperty().bind(mainAnchor.heightProperty());
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            helper = new SQLHelper();
            helper.createTables();
            switchAddNew();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
