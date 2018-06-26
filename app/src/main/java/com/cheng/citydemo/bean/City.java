package com.cheng.citydemo.bean;

import java.io.Serializable;

/**
 * 注：此类作为用户用车选择过的历史城市数据记录bean类
 * 其中属性修改时，需要连同其它引用属性名字当做查询条件的地方一并修改
 */
public class City implements Serializable {

    public int id;
    public String name;//鞍山市
    public int cityid;//64
    public int open_didi;//1
    public int open_zhuanche;//0
    public int open_kuaiche;//1
    public long save_time;

    public City(){

    }

    public City(String name){
        this.name = name;
    }

    public City(String name, int id){
        this.name = name;
        this.cityid = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public void setOpen_didi(int open_didi) {
        this.open_didi = open_didi;
    }

    public void setOpen_zhuanche(int open_zhuanche) {
        this.open_zhuanche = open_zhuanche;
    }

    public void setOpen_kuaiche(int open_kuaiche) {
        this.open_kuaiche = open_kuaiche;
    }

    public void setSave_time(long save_time) {
        this.save_time = save_time;
    }

}
