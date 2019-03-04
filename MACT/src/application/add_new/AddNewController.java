package application.add_new;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import application.database.SQLHelper;
import application.model.CaseDetail;
import application.utils.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class AddNewController implements Initializable {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXTextField fipNoTxt;

	@FXML
	private JFXTextField dateOfAccTxt;

	@FXML
	private JFXTextField timeTxt;

	@FXML
	private JFXTextField placeTxt;

	@FXML
	private JFXTextField drNameTxt;

	@FXML
	private JFXTextField crNameTxt;

	@FXML
	private JFXTextField vehNoTxt;

	@FXML
	private JFXTextField routeTxt;

	@FXML
	private JFXTextField branchTxt;

	@FXML
	private JFXTextField typeOfRoadTxt;

	@FXML
	private JFXTextField ghTxt;

	@FXML
	private JFXTextField policeStationTxt;

	@FXML
	private JFXTextField petNameTxt;

	@FXML
	private JFXTextField mactTxt;

	@FXML
	private JFXTextField mcopTxt;

	@FXML
	private JFXTextField firstHearTxt;

	@FXML
	private JFXTextField epTxt;

	@FXML
	private JFXTextField firTxt;

	@FXML
	private JFXTextField dateOfWarrentTxt;

	@FXML
	private JFXTextField natureOfAccTxt;

	@FXML
	private JFXTextArea punishmentTxt;

	@FXML
	private JFXTextArea darTxt;

	@FXML
	private JFXButton saveBtn;

	@FXML
	private JFXButton resetBtn;

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

			saveBtn.setText("UPDATE");

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

		if (StringUtils.equals("SAVE", saveBtn.getText())) {
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
		} else if (StringUtils.equals("UPDATE", saveBtn.getText())) {
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
		saveBtn.setText("SAVE");
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
