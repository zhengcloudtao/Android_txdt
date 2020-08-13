package com.example.android_txdt;


public class search_list {
    private String name;//地址名称
    private String add;//地址
    private double lat;//纬度
    private double lng;//经度
    //构造器方法
    public search_list(String name, String add, double lat, double lng) {
        this.name = name;
        this.add = add;
        this.lat = lat;
        this.lng = lng;
    }
    //对应set和get方法
    public String getName() {
        return name;
    }

    public String getAdd() {
        return add;
    }

    public double getlat() {
        return lat;
    }

    public double getlng() {
        return lng;
    }
}
