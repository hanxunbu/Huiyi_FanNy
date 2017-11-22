package com.huiyi.huiyi_fanny.model;

/**
 * Created by lw on 2017/9/26.
 */

public class Tuigoods {

    private String t_datetime;
    private Integer t_id;
    private String t_remarks;
    private String t_xscode;

    public Tuigoods(String t_datetime, Integer t_id, String t_remarks, String t_xscode) {
        this.t_datetime = t_datetime;
        this.t_id = t_id;
        this.t_remarks = t_remarks;
        this.t_xscode = t_xscode;
    }

    public String getT_datetime() {
        return t_datetime;
    }

    public void setT_datetime(String t_datetime) {
        this.t_datetime = t_datetime;
    }

    public Integer getT_id() {
        return t_id;
    }

    public void setT_id(Integer t_id) {
        this.t_id = t_id;
    }

    public String getT_remarks() {
        return t_remarks;
    }

    public void setT_remarks(String t_remarks) {
        this.t_remarks = t_remarks;
    }

    public String getT_xscode() {
        return t_xscode;
    }

    public void setT_xscode(String t_xscode) {
        this.t_xscode = t_xscode;
    }
}
