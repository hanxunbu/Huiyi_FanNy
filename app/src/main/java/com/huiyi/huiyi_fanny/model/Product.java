package com.huiyi.huiyi_fanny.model;

/**
 * Created by lw on 2017/9/26.
 */

public class Product {

    private Integer p_id;
    private String p_name;
    private String p_number;
    private String p_standard;

    public Product(String p_name, Integer p_id, String p_number, String p_standard) {
        this.p_name = p_name;
        this.p_id = p_id;
        this.p_number = p_number;
        this.p_standard = p_standard;
    }

    public Integer getP_id() {
        return p_id;
    }

    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_number() {
        return p_number;
    }

    public void setP_number(String p_number) {
        this.p_number = p_number;
    }

    public String getP_standard() {
        return p_standard;
    }

    public void setP_standard(String p_standard) {
        this.p_standard = p_standard;
    }
}
