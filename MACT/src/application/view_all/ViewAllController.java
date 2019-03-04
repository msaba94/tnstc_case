package application.view_all;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import application.database.SQLHelper;
import application.model.CaseDetail;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;

public class ViewAllController implements Initializable {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXTreeTableView<CaseDetail> caseDetailTV;

	@FXML
	private JFXButton refreshBtn;

	@FXML
	private JFXButton deleteBtn;

	private SQLHelper helper;
	private ObservableList<CaseDetail> caseDetails;
	private TreeItem<CaseDetail> caseDetailTI;
	private CaseDetail selectedCase;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		helper = new SQLHelper();

		caseDetails = helper.getAllCaseDetails();
		initTableData();
	}

	public void tvSelectedRow() {
		CaseDetail caseDetail = caseDetailTV.getSelectionModel().getSelectedItem().getValue();
		if (caseDetail != null) {
			selectedCase = caseDetail;
		}
	}

	public void deleteCaseDetail() {
		if (selectedCase != null) {
			if (helper.deleteCaseDetail(selectedCase)) {
				JFXSnackbar bar = new JFXSnackbar(root);
				bar.enqueue(new JFXSnackbar.SnackbarEvent("Case Detail Deleted!"));

				caseDetails = helper.getAllCaseDetails();
				initTableData();
			}
		}
	}

	public void refreshData() {
		caseDetails = helper.getAllCaseDetails();
		initTableData();
	}

	@SuppressWarnings("unchecked")
	private void initTableData() {
		
		JFXTreeTableColumn<CaseDetail, String> fipNoCol = new JFXTreeTableColumn<>("FIP No");
		fipNoCol.setPrefWidth(100);
		fipNoCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().fip);
		
		JFXTreeTableColumn<CaseDetail, String> dateOfAccCol = new JFXTreeTableColumn<>("Date Of Acc");
		dateOfAccCol.setPrefWidth(100);
		dateOfAccCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().dateOfAcc);

		JFXTreeTableColumn<CaseDetail, String> timeCol = new JFXTreeTableColumn<>("Time");
		timeCol.setPrefWidth(100);
		timeCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().time);

		JFXTreeTableColumn<CaseDetail, String> placeCol = new JFXTreeTableColumn<>("Place");
		placeCol.setPrefWidth(100);
		placeCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().place);

		JFXTreeTableColumn<CaseDetail, String> drNameCol = new JFXTreeTableColumn<>("DR Name");
		drNameCol.setPrefWidth(100);
		drNameCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().drName);

		JFXTreeTableColumn<CaseDetail, String> crNameCol = new JFXTreeTableColumn<>("CR Name");
		crNameCol.setPrefWidth(100);
		crNameCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().crName);

		JFXTreeTableColumn<CaseDetail, String> vehNoCol = new JFXTreeTableColumn<>("Veh No");
		vehNoCol.setPrefWidth(100);
		vehNoCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().vehNo);

		JFXTreeTableColumn<CaseDetail, String> ghCol = new JFXTreeTableColumn<>("GH");
		ghCol.setPrefWidth(100);
		ghCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().gh);

		JFXTreeTableColumn<CaseDetail, String> policeStationCol = new JFXTreeTableColumn<>("Police Station");
		policeStationCol.setPrefWidth(100);
		policeStationCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param
				.getValue().getValue().policeStation);

		JFXTreeTableColumn<CaseDetail, String> petNameCol = new JFXTreeTableColumn<>("PET Name");
		petNameCol.setPrefWidth(100);
		petNameCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().petName);

		JFXTreeTableColumn<CaseDetail, String> mactCol = new JFXTreeTableColumn<>("MACT");
		mactCol.setPrefWidth(100);
		mactCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().mact);

		JFXTreeTableColumn<CaseDetail, String> mcopCol = new JFXTreeTableColumn<>("MCOP");
		mcopCol.setPrefWidth(100);
		mcopCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().mcop);

		JFXTreeTableColumn<CaseDetail, String> firCol = new JFXTreeTableColumn<>("FIR");
		firCol.setPrefWidth(100);
		firCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().fir);

		JFXTreeTableColumn<CaseDetail, String> epNoCol = new JFXTreeTableColumn<>("EP No");
		epNoCol.setPrefWidth(100);
		epNoCol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<CaseDetail, String> param) -> param.getValue().getValue().epNo);

		caseDetailTI = new RecursiveTreeItem<>(caseDetails, RecursiveTreeObject::getChildren);
		caseDetailTV.setRoot(caseDetailTI);
		caseDetailTV.getColumns().setAll(fipNoCol, dateOfAccCol, timeCol, placeCol, drNameCol, crNameCol, vehNoCol, ghCol,
				policeStationCol, petNameCol, mactCol, mcopCol, firCol, epNoCol);
		caseDetailTV.setShowRoot(false);

	}

}
