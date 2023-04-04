package com.example.projekat;

public class ModelCartItem {

    String id, uid, name, priceEach, priceTotal, quantity;

    public ModelCartItem() { }

    public ModelCartItem(String id, String uid, String name, String priceEach, String priceTotal, String quantity) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.priceEach = priceEach;
        this.priceTotal = priceTotal;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceEach() {
        return priceEach;
    }

    public void setPriceEach(String priceEach) {
        this.priceEach = priceEach;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
