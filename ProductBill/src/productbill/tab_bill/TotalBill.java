/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.tab_bill;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author iCT-3
 */
public class TotalBill extends RecursiveTreeObject<TotalBill>{
    
    public final SimpleStringProperty id;
    public final SimpleStringProperty totalAmount;
    public final SimpleStringProperty customerId;
    public final SimpleStringProperty customerName;
    public final SimpleStringProperty createdOn;
    public final SimpleStringProperty modifyOn;
    public int exchangeCount;

    public TotalBill(String id, String totalAmount, String customerId,String customerName, String createdOn, String modifyOn) {
        this.id = new SimpleStringProperty(id);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        this.customerId = new SimpleStringProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.modifyOn = new SimpleStringProperty(modifyOn);
    }

    public int getExchangeCount() {
        return exchangeCount;
    }

    public void setExchangeCount(int exchangeCount) {
        this.exchangeCount = exchangeCount;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public String getCustomerId() {
        return customerId.get();
    }

    public void setCustomerId(String customerId) {
        this.customerId.set(customerId);
    }
    
    public String getCustomerName() {
        return customerName.get();
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }
    
    public String getCreatedOn() {
        return createdOn.get();
    }

    public void setCreatedOn(String customerId) {
        this.createdOn.set(customerId);
    }
    
    public String getModifyOn() {
        return modifyOn.get();
    }

    public void setModifyOn(String customerId) {
        this.modifyOn.set(customerId);
    }
    
    
}
