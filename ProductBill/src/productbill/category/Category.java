/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productbill.category;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author iCT-3
 */
public class Category extends RecursiveTreeObject<Category> {
    
    public final SimpleStringProperty id;
    public final SimpleStringProperty name;
    public final SimpleStringProperty parentCategory;
    public final SimpleStringProperty parentName;

    public Category(String id, String name, String parentCategory, String parentName) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.parentCategory = new SimpleStringProperty(parentCategory);
        this.parentName = new SimpleStringProperty(parentName);
    }

  
    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }
    
    public String getParentCategory(){
        return parentCategory.get();
    }
    
    public void setId(String id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }
    
    public void setParentCategory(String parentCategory) {
        this.parentCategory.set(parentCategory);
    }

    public String getParentName() {
        return parentName.get();
    }

    public void setParentName(String parentName) {
        this.parentName.set(parentName);
    }
    

    @Override
    public String toString() {
        return this.name.get(); 
    }
    
}
