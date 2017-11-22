package com.huiyi.huiyi_fanny.model;

/**
 * Created by lw on 2017/10/10.
 */

@SuppressWarnings("serial")//去掉警告
public class Dict_Product  {

    private Integer id;
    private String text;

    public Dict_Product(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString(){
        return text;
    }

}
