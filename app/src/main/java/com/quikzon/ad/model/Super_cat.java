package com.quikzon.ad.model;

import java.util.ArrayList;
import java.util.List;

public class Super_cat {
    String cat_id;
    String name;
    String img;
    String cat_banner;

    public String getCat_banner() {
        return cat_banner;
    }

    public void setCat_banner(String cat_banner) {
        this.cat_banner = cat_banner;
    }

    private List<Super_sub_Cat> sub_cat = new ArrayList<Super_sub_Cat>();
    public String getCat_id() {
        return cat_id;
    }
    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Super_sub_Cat> getSub_cat() {
        return sub_cat;
    }
    public void setSub_cat(List<Super_sub_Cat> sub_cat) {
        this.sub_cat = sub_cat;
    }
}
