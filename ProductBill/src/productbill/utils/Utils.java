/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import productbill.order.BillOrder;
import productbill.tab_bill.BillingDetail;

/**
 *
 * @author iCT-3
 */
public class Utils {

    public static ObservableList<BillingDetail> billDetails;
    public static String total;
    public static String totalGst;
    public static String cusAmount;
    public static Stage stage;
    public static BillOrder billOrder;

    public static boolean isValied(String str) {
        if (str == null || "null".equals(str)) {
            return false;
        }

        if (str.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static void doubleTextField(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    textField.setText(oldValue);
                }
            }
        });
    }

    public static void numericTextField(TextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public static double defaultDouble(String str) {
        if (str == null || str.equals("null")) {
            return 0.0;
        }
        double val = 0.0;
        try {
            val = Double.parseDouble(str);
            return val;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static int defaultInt(String str) {

        if (str == null || str.equals("null")) {
            return 0;
        }
        int val = 0;
        try {
            val = Integer.parseInt(str.trim());
            return val;
        } catch (Exception e) {
            return 0;
        }

    }

    public static String defaultString(Integer total) {
        if (total == null) {
            return "0";
        } else {
            String val = "0";
            try {
                val = Integer.toString(total);
            } catch (Exception ex) {
                val = "0";
            }
            return val;
        }
    }

    public static String defaultFloatInt(String total) {
        if (total == null) {
            return "0";
        } else {
            String val = "0";
            try {
                Float floatVal = Float.parseFloat(total);
                val =String.valueOf(floatVal);
            } catch (Exception ex) {
                val = "0";
            }
            return val;
        }
    }

    public static String defaultString(Double total) {
        if (total == null) {
            return "0";
        } else {
            String val = "0";
            try {
                val = Double.toString(total);
            } catch (Exception ex) {
                val = "0";
            }
            return val;
        }
    }

}
