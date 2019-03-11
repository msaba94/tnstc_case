/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.stock;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author iCT-3
 */
public class Stock extends RecursiveTreeObject<Stock> {
    
    public final SimpleStringProperty id;
    public final SimpleStringProperty productId;
    public final SimpleStringProperty productName;
    public final SimpleStringProperty quantity;

    public Stock(String id, String productId, String productName, String quantity) {
        this.id = new SimpleStringProperty(id);
        this.productName = new SimpleStringProperty(productName);
        this.productId = new SimpleStringProperty(productId);
        this.quantity = new SimpleStringProperty(quantity);
    }
    
    
    

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getProductName() {
        return productName.get();
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }
    
    public String getProductId() {
        return productId.get();
    }

    public void setProductId(String productName) {
        this.productId.set(productName);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }
    
    
}
