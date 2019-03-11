/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.hsn;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author iCT-3
 */
public class HSN extends RecursiveTreeObject<HSN> {

    public final SimpleStringProperty id;
    public final SimpleStringProperty hsn;
    public final SimpleStringProperty gst;
    public final SimpleStringProperty description;
    public final SimpleStringProperty type;
    public final SimpleStringProperty typeName;

    public HSN(String id, String hsn, String gst, String description, String type, String typeName) {
        this.id = new SimpleStringProperty(id);
        this.hsn = new SimpleStringProperty(hsn);
        this.gst = new SimpleStringProperty(gst);
        this.description = new SimpleStringProperty(description);
        this.type = new SimpleStringProperty(type);
        this.typeName = new SimpleStringProperty(typeName);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getHSN() {
        return hsn.get();
    }

    public void setHSN(String hsn) {
        this.hsn.set(hsn);
    }

    public String getGST() {
        return gst.get();
    }

    public void setGST(String gst) {
        this.gst.set(gst);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getTypeName() {
        return typeName.get();
    }

    public void setTypeName(String typeName) {
        this.typeName.set(typeName);
    }


}
