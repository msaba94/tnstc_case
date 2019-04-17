/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.tab_bill;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import productbill.database.SQLHelper;
import productbill.jasper_report.PrintReport;
import productbill.utils.Utils;

/**
 * FXML Controller class
 *
 * @author iCT-3
 */
public class ConfirmBillingController implements Initializable {

	/**
	 * Initializes the controller class.
	 */
	@FXML
	private Label customerAmount;

	@FXML
	private Label totalBillAmount;

	@FXML
	private Label balance;

	@FXML
	private Button printBtn;

	@FXML
	private Button cancelBtn;

	private SQLHelper sQLHelper;

	@FXML
	void closeDialog(ActionEvent event) {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	void printBill(ActionEvent event) {
		if (deleteDB()) {
			System.out.println("Deleted!");
		} else {
			System.out.println("Not Deleted!");
		}

		if (Utils.billDetails.size() > 0) {

			String customerName = "";
			ArrayList<String> productIds = new ArrayList<>();
			TotalBill bill = new TotalBill(null, Utils.total, Utils.billDetails.get(0).getCustomerId(), customerName,
					null, null);
			int billId = sQLHelper.insertTotalBill(bill);
			if (billId > 0) {
				System.out.println("Inserted");
				for (BillingDetail detail : Utils.billDetails) {

					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setId(null);
					orderDetail.setBillId(billId);
					System.out.println("Bill Id: " + billId);
					productIds.add(detail.getProductId());
					System.out.println("Product Ids: " + productIds);
					orderDetail.setProductId(Utils.defaultInt(detail.getProductId()));
					orderDetail.setQuantity(Utils.defaultInt(detail.getQuantity()));
					orderDetail.setExchangeCount(0);

					if (sQLHelper.insertOrderDetail(orderDetail)) {
						System.out.println("Bill Inserted!");
					} else {
						System.out.println("Bill Not Inserted!");
					}
				}
				if (sQLHelper.insertOrderArray(billId, productIds)) {
					System.out.println("Order Array");
				}
				createdReport(billId);
			} else {
				System.out.println("Not Inserted!");
			}

		}
		cancelBtn.fire();
	}

	private boolean deleteDB() {
		return sQLHelper.deleteBillDetails();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO

		sQLHelper = new SQLHelper();
		customerAmount.setText("Rs : " + Utils.cusAmount);
		totalBillAmount.setText("Rs : " + Utils.total);
		double bal = Utils.defaultDouble(Utils.cusAmount) - Utils.defaultDouble(Utils.total);
		balance.setText("Rs : " + bal);

		printBtn.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					printBtn.fire();
				}
			}
		});

		cancelBtn.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					cancelBtn.fire();
				}
			}
		});

	}

	private void createdReport(int billID) {
		Map parametersMap = new HashMap();
		String totalBeforeGst = Utils
				.defaultString((Utils.defaultDouble(Utils.total) - Utils.defaultDouble(Utils.totalGst)));
		parametersMap.put("grand_total", Utils.total);
		parametersMap.put("total_gst", Utils.totalGst);
		parametersMap.put("total_before_gst", totalBeforeGst);
		parametersMap.put("bill_id", billID);

		PrintReport report = new PrintReport();
		report.showReport(parametersMap);
	}

}
