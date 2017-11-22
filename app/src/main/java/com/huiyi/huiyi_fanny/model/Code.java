package com.huiyi.huiyi_fanny.model;

import java.io.Serializable;

/**
 * Created by lw on 2017/9/26.
 */

public class Code implements Serializable {

    private int id;
    private String code;
    private String productcode;
    private String chilerenamount;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getChilerenamount() {
        return chilerenamount;
    }

    public void setChilerenamount(String chilerenamount) {
        this.chilerenamount = chilerenamount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
