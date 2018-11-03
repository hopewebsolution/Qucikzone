package com.quikzon.ad.model;

import java.util.ArrayList;

public class Ad_post_view {
    String field_type,field_type_name,field_val,field_name,title,has_page_number,is_required,has_cat_template;
    ArrayList<Post_values> values=new ArrayList<>();
    public String getField_type() {
        return field_type;
    }

    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getField_type_name() {
        return field_type_name;
    }

    public void setField_type_name(String field_type_name) {
        this.field_type_name = field_type_name;
    }

    public String getField_val() {
        return field_val;
    }

    public void setField_val(String field_val) {
        this.field_val = field_val;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHas_page_number() {
        return has_page_number;
    }

    public void setHas_page_number(String has_page_number) {
        this.has_page_number = has_page_number;
    }

    public String getIs_required() {
        return is_required;
    }

    public void setIs_required(String is_required) {
        this.is_required = is_required;
    }

    public String getHas_cat_template() {
        return has_cat_template;
    }

    public void setHas_cat_template(String has_cat_template) {
        this.has_cat_template = has_cat_template;
    }

    public ArrayList<Post_values> getValues() {
        return values;
    }

    public void setValues(ArrayList<Post_values> values) {
        this.values = values;
    }
}
