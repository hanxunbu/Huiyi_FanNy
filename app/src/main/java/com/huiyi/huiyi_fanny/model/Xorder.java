package com.huiyi.huiyi_fanny.model;

/**
 * Created by lw on 2017/9/26.
 */

public class Xorder {

    private Integer x_customerID;
    private String x_datetime;
    private Integer x_id;
    private String x_number;
    private Integer x_productID;
    private String x_xquantity;
    private String x_yquantity;

    public Xorder(Integer x_customerID, String x_datetime, Integer x_id, String x_number, Integer x_productID, String x_xquantity, String x_yquantity) {
        this.x_customerID = x_customerID;
        this.x_datetime = x_datetime;
        this.x_id = x_id;
        this.x_number = x_number;
        this.x_productID = x_productID;
        this.x_xquantity = x_xquantity;
        this.x_yquantity = x_yquantity;
    }

    public Integer getX_customerID() {
        return x_customerID;
    }

    public void setX_customerID(Integer x_customerID) {
        this.x_customerID = x_customerID;
    }

    public String getX_datetime() {
        return x_datetime;
    }

    public void setX_datetime(String x_datetime) {
        this.x_datetime = x_datetime;
    }

    public Integer getX_id() {
        return x_id;
    }

    public void setX_id(Integer x_id) {
        this.x_id = x_id;
    }

    public String getX_number() {
        return x_number;
    }

    public void setX_number(String x_number) {
        this.x_number = x_number;
    }

    public Integer getX_productID() {
        return x_productID;
    }

    public void setX_productID(Integer x_productID) {
        this.x_productID = x_productID;
    }

    public String getX_xquantity() {
        return x_xquantity;
    }

    public void setX_xquantity(String x_xquantity) {
        this.x_xquantity = x_xquantity;
    }

    public String getX_yquantity() {
        return x_yquantity;
    }

    public void setX_yquantity(String x_yquantity) {
        this.x_yquantity = x_yquantity;
    }
}
