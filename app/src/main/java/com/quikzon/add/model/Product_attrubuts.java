package com.quikzon.add.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Product_attrubuts {
    String ad_author_id;
    String ad_id;
    String ad_date;
    String ad_title;
    String ad_desc;
    String ad_cats_name;
    String ad_views;
    String phone;
    String poster_name;
    String ad_condition;
    Boolean is_feature;
    ArrayList<Adstatus_model> ad_status = new ArrayList<>();
    ArrayList<Adcat_model> ad_cats = new ArrayList<>();
    ArrayList<Adimg_model> ad_images = new ArrayList<>();
    ArrayList<Adlocation_model> ad_location = new ArrayList<>();
    ArrayList<Ad_price> ad_price = new ArrayList<>();
    ArrayList<Ad_video> ad_video = new  ArrayList<>();
    ArrayList<Ad_timer> ad_timer = new ArrayList<>();
    ArrayList<Ad_saved> ad_saved = new ArrayList<>();

    public String getAd_author_id() {
        return ad_author_id;
    }

    public void setAd_author_id(String ad_author_id) {
        this.ad_author_id = ad_author_id;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_date() {
        return ad_date;
    }

    public void setAd_date(String ad_date) {
        this.ad_date = ad_date;
    }

    public String getAd_title() {
        return ad_title;
    }

    public void setAd_title(String ad_title) {
        this.ad_title = ad_title;
    }

    public String getAd_desc() {
        return ad_desc;
    }

    public void setAd_desc(String ad_desc) {
        this.ad_desc = ad_desc;
    }

    public String getAd_cats_name() {
        return ad_cats_name;
    }

    public void setAd_cats_name(String ad_cats_name) {
        this.ad_cats_name = ad_cats_name;
    }

    public String getAd_views() {
        return ad_views;
    }

    public void setAd_views(String ad_views) {
        this.ad_views = ad_views;
    }

    public ArrayList<Adstatus_model> getAd_status() {
        return ad_status;
    }

    public void setAd_status(ArrayList<Adstatus_model> ad_status) {
        this.ad_status = ad_status;
    }

    public ArrayList<Adcat_model> getAd_cats() {
        return ad_cats;
    }

    public void setAd_cats(ArrayList<Adcat_model> ad_cats) {
        this.ad_cats = ad_cats;
    }

    public ArrayList<Adimg_model> getAd_images() {
        return ad_images;
    }

    public void setAd_images(ArrayList<Adimg_model> ad_images) {
        this.ad_images = ad_images;
    }

    public ArrayList<Adlocation_model> getAd_location() {
        return ad_location;
    }

    public void setAd_location(ArrayList<Adlocation_model> ad_location) {
        this.ad_location = ad_location;
    }

    public ArrayList<Ad_price> getAd_price() {
        return ad_price;
    }

    public void setAd_price(ArrayList<Ad_price> ad_price) {
        this.ad_price = ad_price;
    }

    public ArrayList<Ad_video> getAd_video() {
        return ad_video;
    }

    public void setAd_video(ArrayList<Ad_video> ad_video) {
        this.ad_video = ad_video;
    }

    public ArrayList<Ad_timer> getAd_timer() {
        return ad_timer;
    }

    public void setAd_timer(ArrayList<Ad_timer> ad_timer) {
        this.ad_timer = ad_timer;
    }

    public ArrayList<Ad_saved> getAd_saved() {
        return ad_saved;
    }

    public void setAd_saved(ArrayList<Ad_saved> ad_saved) {
        this.ad_saved = ad_saved;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPoster_name() {
        return poster_name;
    }

    public void setPoster_name(String poster_name) {
        this.poster_name = poster_name;
    }
    public String getAd_condition() {
        return ad_condition;
    }

    public void setAd_condition(String ad_condition) {
        this.ad_condition = ad_condition;
    }

    public Boolean getIs_feature() {
        return is_feature;
    }

    public void setIs_feature(Boolean is_feature) {
        this.is_feature = is_feature;
    }
}
