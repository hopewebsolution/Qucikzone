package com.quikzon.ad.model;

import java.util.ArrayList;

public class Products extends Product_attrubuts {
    String text;
    ArrayList<Product_attrubuts> ads=new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Product_attrubuts> getAds() {
        return ads;
    }
    public void setAds(ArrayList<Product_attrubuts> ads) {
        this.ads = ads;
    }
}