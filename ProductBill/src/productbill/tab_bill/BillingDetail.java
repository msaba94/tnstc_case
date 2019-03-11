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
public class BillingDetail extends RecursiveTreeObject<BillingDetail> {
    
    public final SimpleStringProperty id;
    public final SimpleStringProperty productId;
    public final SimpleStringProperty productName;
    public final SimpleStringProperty customerId;
    public final SimpleStringProperty categoryName;
    public final SimpleStringProperty quantity;
    public final SimpleStringProperty price;
    public final SimpleStringProperty total;
    public String gst;
    public String productCode;

    public BillingDetail(String id, String productId, String productName, 
            String customerId, String categoryName, String quantity, String price, String total) {
        this.id = new SimpleStringProperty(id);
        this.productId = new SimpleStringProperty(productId);
        this.productName = new SimpleStringProperty(productName);
        this.categoryName = new SimpleStringProperty(categoryName);
        this.customerId = new SimpleStringProperty(customerId);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
        this.total = new SimpleStringProperty(total);
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    
    
    public String getId() {
        return this.id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getProductId() {
        return this.productId.get();
    }

    public void setProductId(String productId) {
        this.productId.set(productId);
    }

    public String getCustomerId() {
        return this.customerId.get();
    }

    public void setCustomerId(String customerId) {
        this.customerId.set(customerId);
    }

    public String getQuantity() {
        return this.quantity.get();
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getPrice() {
        return this.price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getTotal() {
        return this.total.get();
    }

    public void setTotal(String total) {
        this.total.set(total);
    }
    
    public String getProductName() {
        return this.productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }
    
    public String getCategoryName() {
        return this.categoryName.get();
    }

    public void setCategoryName(String categoryName) {
        this.categoryName.set(categoryName);
    }
    
    
}
