package application.main_menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import application.database.SQLHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class MainMenu implements Initializable {

	@FXML
	private ScrollPane mainAnchor;

	private SQLHelper helper;

	@FXML
	public void switchAddNew() throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/add_new/add_new.fxml"));
		mainAnchor.setContent(pane);
		pane.prefWidthProperty().bind(mainAnchor.widthProperty());
		pane.prefHeightProperty().bind(mainAnchor.heightProperty());
	}

	@FXML
	public void switchViewAll() throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/application/view_all/view_all.fxml"));
		mainAnchor.setContent(pane);
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
