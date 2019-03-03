package application.add_new;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.jfoenix.controls.JFXSnackbar;

import application.database.SQLHelper;
import application.model.CaseDetail;
import application.utils.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class AddNewController implements Initializable {

	@FXML
	private AnchorPane root;

	@FXML
	private TextField fipNoTxt;

	@FXML
	private TextField dateOfAccTxt;

	@FXML
	private TextField timeTxt;

	@FXML
	private TextField placeTxt;

	@FXML
	private TextField drNameTxt;

	@FXML
	private TextField crNameTxt;

	@FXML
	private TextField vehNoTxt;

	@FXML
	private TextField routeTxt;

	@FXML
	private TextField branchTxt;

	@FXML
	private TextField typeOfRoadTxt;

	@FXML
	private TextField ghTxt;

	@FXML
	private TextField policeStationTxt;

	@FXML
	private TextField petNameTxt;

	@FXML
	private TextField mactTxt;

	@FXML
	private TextField mcopTxt;

	@FXML
	private TextField firstHearTxt;

	@FXML
	private TextField epTxt;

	@FXML
	private TextField firTxt;

	@FXML
	private TextField dateOfWarrentTxt;

	@FXML
	private TextField natureOfAccTxt;

	@FXML
	private TextArea punishmentTxt;

	@FXML
	private TextArea darTxt;

	@FXML
	private Button saveBtn;

	@FXML
	private Button resetBtn;

	private SQLHelper helper;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		helper = new SQLHelper();

		fipNoTxt.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					CaseDetail caseDetail = helper.getCaseDetailByFipNo(fipNoTxt.getText());
					setDataToTxt(caseDetail);
				}
			}
		});
	}

	private void setDataToTxt(CaseDetail caseDetail) {
		if (caseDetail != null) {

			saveBtn.setText("Edit");

			dateOfAccTxt.setText(caseDetail.getDateOfAcc());
			timeTxt.setText(caseDetail.getTime());
			placeTxt.setText(caseDetail.getPlace());
			drNameTxt.setText(caseDetail.getDrName());
			crNameTxt.setText(caseDetail.getCrName());
			vehNoTxt.setText(caseDetail.getVehNo());
			ghTxt.setText(caseDetail.getGh());
			policeStationTxt.setText(caseDetail.getPoliceStation());
			petNameTxt.setText(caseDetail.getPetName());
			mactTxt.setText(caseDetail.getMact());
			mcopTxt.setText(caseDetail.getMcop());
			firstHearTxt.setText(caseDetail.getFirstHear());
			epTxt.setText(caseDetail.getEpNo());
			firTxt.setText(caseDetail.getFir());
			dateOfWarrentTxt.setText(caseDetail.getDateOfWarrent());
			natureOfAccTxt.setText(caseDetail.getNature());
			punishmentTxt.setText(caseDetail.getPunishment());
			darTxt.setText(caseDetail.getDar());
			branchTxt.setText(caseDetail.getBranch());
			typeOfRoadTxt.setText(caseDetail.getTypeOfRoad());
			routeTxt.setText(caseDetail.getRoute());
			fipNoTxt.setText(caseDetail.getFip());
		} else {
			JFXSnackbar bar = new JFXSnackbar(root);
			bar.enqueue(new JFXSnackbar.SnackbarEvent("Case Not Found for FIP NO: " + fipNoTxt.getText() + " Invalid"));
			resetAll();
		}
	}

	public void saveCase() {

		if (StringUtils.equals("Save", saveBtn.getText())) {
			if (Utils.isValied(fipNoTxt.getText())) {
				CaseDetail caseDetail = new CaseDetail(0, dateOfAccTxt.getText(), timeTxt.getText(), placeTxt.getText(),
						drNameTxt.getText(), crNameTxt.getText(), vehNoTxt.getText(), ghTxt.getText(),
						policeStationTxt.getText(), petNameTxt.getText(), mactTxt.getText(), mcopTxt.getText(),
						firstHearTxt.getText(), epTxt.getText(), firTxt.getText(), dateOfWarrentTxt.getText(),
						natureOfAccTxt.getText(), punishmentTxt.getText(), darTxt.getText(), fipNoTxt.getText(),
						branchTxt.getText(), typeOfRoadTxt.getText(), routeTxt.getText());

				CaseDetail existCaseDetail = helper.getCaseDetailByFipNo(fipNoTxt.getText());
				if (existCaseDetail != null) {
					JFXSnackbar bar = new JFXSnackbar(root);
					bar.enqueue(new JFXSnackbar.SnackbarEvent(
							"Already exist FIP NO: " + fipNoTxt.getText() + " for other case"));
					return;
				}

				if (helper.insertCategory(caseDetail)) {
					resetAll();
				}
			} else {
				JFXSnackbar bar = new JFXSnackbar(root);
				bar.enqueue(new JFXSnackbar.SnackbarEvent("FIP NO is Empty!"));
			}
		} else if (StringUtils.equals("Edit", saveBtn.getText())) {
			if (Utils.isValied(fipNoTxt.getText())) {
				CaseDetail existCaseDetail = helper.getCaseDetailByFipNo(fipNoTxt.getText());
				if (existCaseDetail != null) {
					CaseDetail caseDetail = new CaseDetail(existCaseDetail.getId(), dateOfAccTxt.getText(),
							timeTxt.getText(), placeTxt.getText(), drNameTxt.getText(), crNameTxt.getText(),
							vehNoTxt.getText(), ghTxt.getText(), policeStationTxt.getText(), petNameTxt.getText(),
							mactTxt.getText(), mcopTxt.getText(), firstHearTxt.getText(), epTxt.getText(),
							firTxt.getText(), dateOfWarrentTxt.getText(), natureOfAccTxt.getText(),
							punishmentTxt.getText(), darTxt.getText(), fipNoTxt.getText(), branchTxt.getText(),
							typeOfRoadTxt.getText(), routeTxt.getText());

					if (helper.updateCaseDetails(caseDetail)) {
						JFXSnackbar bar = new JFXSnackbar(root);
						bar.enqueue(new JFXSnackbar.SnackbarEvent("Case Details Updated"));
						resetAll();
					}
				} else {
					JFXSnackbar bar = new JFXSnackbar(root);
					bar.enqueue(new JFXSnackbar.SnackbarEvent(
							"Case Not Found for FIP NO: " + fipNoTxt.getText() + " Invalid"));
				}
			}
		}

	}

	public void resetAll() {
		saveBtn.setText("Save");
		dateOfAccTxt.setText("");
		timeTxt.setText("");
		placeTxt.setText("");
		drNameTxt.setText("");
		crNameTxt.setText("");
		vehNoTxt.setText("");
		ghTxt.setText("");
		policeStationTxt.setText("");
		petNameTxt.setText("");
		mactTxt.setText("");
		mcopTxt.setText("");
		firstHearTxt.setText("");
		epTxt.setText("");
		firTxt.setText("");
		dateOfWarrentTxt.setText("");
		natureOfAccTxt.setText("");
		punishmentTxt.setText("");
		darTxt.setText("");
		branchTxt.setText("");
		typeOfRoadTxt.setText("");
		routeTxt.setText("");
		fipNoTxt.setText("");
	}

}
