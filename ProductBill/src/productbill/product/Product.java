
package productbill.product;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class Product extends RecursiveTreeObject<Product> {
    
    public final SimpleStringProperty id;
    public final SimpleStringProperty title;
    public final SimpleStringProperty desc;
    public final SimpleStringProperty categoryId;
    public final SimpleStringProperty code;
    public final SimpleStringProperty measureType;
    public final SimpleStringProperty purchasedPrice;
    public final SimpleStringProperty purchasedFrom;
    public final SimpleStringProperty hsnCode;
    public final SimpleStringProperty sellingPrice;
    public final SimpleStringProperty mrp;
    public final SimpleStringProperty color;
    public final SimpleStringProperty tags;
    public final SimpleStringProperty vendorId;
    public final SimpleStringProperty active;
    public final SimpleStringProperty gst;
    public int quantity;

    public Product(String id, String title, String desc, String categoryId, String code, 
            String measureType, String purchasedPrice, String purchasedFrom, 
            String hsnCode, String sellingPrice, String mrp, String color, 
            String tags, String vendorId, String active, String gst) {
        
        this.id = new SimpleStringProperty(id);
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
        this.categoryId = new SimpleStringProperty(categoryId);
        this.code = new SimpleStringProperty(code);
        this.measureType = new SimpleStringProperty(measureType);
        this.purchasedPrice = new SimpleStringProperty(purchasedPrice);
        this.purchasedFrom = new SimpleStringProperty(purchasedFrom);
        this.hsnCode = new SimpleStringProperty(hsnCode);
        this.sellingPrice = new SimpleStringProperty(sellingPrice);
        this.mrp = new SimpleStringProperty(mrp);
        this.color = new SimpleStringProperty(color);
        this.tags = new SimpleStringProperty(tags);
        this.vendorId = new SimpleStringProperty(vendorId);
        this.active = new SimpleStringProperty(active);
        this.gst = new SimpleStringProperty(gst);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDesc() {
        return desc.get();
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public String getCategoryId() {
        return categoryId.get();
    }

    public void setCategoryId(String categoryId) {
        this.categoryId.set(categoryId);
    }

    public String getCode() {
        return code.get();
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getMeasureType() {
        return measureType.get();
    }

    public void setMeasureType(String measureType) {
        this.measureType.set(measureType);
    }

    public String getPurchasedPrice() {
        return purchasedPrice.get();
    }

    public void setPurchasedPrice(String purchasedPrice) {
        this.purchasedPrice.set(purchasedPrice);
    }

    public String getPurchasedFrom() {
        return purchasedFrom.get();
    }

    public void setPurchasedFrom(String purchasedFrom) {
        this.purchasedFrom.set(purchasedFrom);
    }

    public String getHsnCode() {
        return hsnCode.get();
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode.set(hsnCode);
    }

    public String getSellingPrice() {
        return sellingPrice.get();
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice.set(sellingPrice);
    }

    public String getMrp() {
        return mrp.get();
    }

    public void setMrp(String mrp) {
        this.mrp.set(mrp);
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public String getTags() {
        return tags.get();
    }

    public void setTags(String tags) {
        this.tags.set(tags);
    }

    public String getVendorId() {
        return vendorId.get();
    }

    public void setVendorId(String vendorId) {
        this.vendorId.set(vendorId);
    }

    public String getActive() {
        return active.get();
    }

    public void setActive(String active) {
        this.active.set(active);
    }
    
    public String getGst() {
        return gst.get();
    }

    public void setGst(String gst) {
        this.gst.set(gst);
    }

    @Override
    public String toString() {
        return this.title.get();
    }
    
    
    
}
