package com.huiyi.huiyi_fanny.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lw on 2017/11/1.
 */

public class GoodsListEntry implements Serializable{

    private String x_number;//交货单号

    //判断 是否有这个交货单号,有就加入到该交货单号. 就是加入的这个gonglist
    //比如超威和蚊香 是根据是否同一单号的
    private List<GoodEntry> goodlist;

    public static  class  GoodEntry implements Serializable{
        private String c_number;//客户编码
        private String x_id;
        private String p_number;
        private String p_name;
        private String p_standard;
        private String c_name;
        private String c_address;
        private String x_xquantity;
        private String x_yquantity;

        //get set方法可以进行写入或读取


        public String getX_id() {
            return x_id;
        }

        public void setX_id(String x_id) {
            this.x_id = x_id;
        }

        public String getC_number() {
            return c_number;
        }

        public void setC_number(String c_number) {
            this.c_number = c_number;
        }

        public String getP_number() {
            return p_number;
        }

        public void setP_number(String p_number) {
            this.p_number = p_number;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }

        public String getP_standard() {
            return p_standard;
        }

        public void setP_standard(String p_standard) {
            this.p_standard = p_standard;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }

        public String getC_address() {
            return c_address;
        }

        public void setC_address(String c_address) {
            this.c_address = c_address;
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


    //--------------------------------------------------
    //--------------------------------------------------
    public String getX_number() {
        return x_number;
    }

    public void setX_number(String x_number) {
        this.x_number = x_number;
    }



    public List<GoodEntry> getGoodlist() {
        return goodlist;
    }

    public void setGoodlist(List<GoodEntry> goodlist) {
        this.goodlist = goodlist;
    }

    @Override
    public String toString() {
        return "GoodsListEntry{" +
                "x_number='" + x_number + '\'' +
                ", goodlist=" + goodlist +
                '}';
    }
}

