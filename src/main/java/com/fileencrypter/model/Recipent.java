package com.fileencrypter.model;

import javafx.beans.property.SimpleStringProperty;

public class Recipent {
    //TODO: create other necessairy fields
    private SimpleStringProperty name;

    private String adress;

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Recipent(SimpleStringProperty name, String adress) {
        this.name = name;
        this.adress = adress;
    }

    public Recipent() {}

    public Recipent(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public SimpleStringProperty getName() {
        return this.name;
    }

    public void setName(SimpleStringProperty name) {
        this.name = name;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Recipent)) return false;
        final Recipent other = (Recipent) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Recipent;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        return result;
    }

    public String toString() {
        return "Recipent(name=" + this.getName() + ")";
    }
}
