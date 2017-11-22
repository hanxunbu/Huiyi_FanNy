package com.huiyi.huiyi_fanny.model;

/**
 * Created by lw on 2017/9/28.
 */

public class Kube {

    private Integer kb_id;
    private String kb_name;

    public Kube(Integer kb_id, String kb_name) {
        this.kb_id = kb_id;
        this.kb_name = kb_name;
    }

    public Integer getKb_id() {
        return kb_id;
    }

    public void setKb_id(Integer kb_id) {
        this.kb_id = kb_id;
    }

    public String getKb_name() {
        return kb_name;
    }

    public void setKb_name(String kb_name) {
        this.kb_name = kb_name;
    }
}
