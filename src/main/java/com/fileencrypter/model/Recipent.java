package com.fileencrypter.model;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

@Data
public class Recipent {
    //TODO: create other necessairy fields
    private SimpleStringProperty name;

    public Recipent() {}

    public Recipent(String name) {
        this.name = new SimpleStringProperty(name);
    }
}
