package com.wamufi.particulates.models;

public class SearchModel {

    private String sidoName; // 시도 명
    private String sggName; // 시군구 명
    private String umdName; // 읍면동 명
    private String tmX; // x 좌표
    private String tmY; // y 좌표

    public SearchModel() {
        super();
    }

    public String getSidoName() {
        return sidoName;
    }

    public void setSidoName(String sidoName) {
        this.sidoName = sidoName;
    }

    public String getSggName() {
        return sggName;
    }

    public void setSggName(String sggName) {
        this.sggName = sggName;
    }

    public String getUmdName() {
        return umdName;
    }

    public void setUmdName(String umdName) {
        this.umdName = umdName;
    }

    public String getTmX() {
        return tmX;
    }

    public void setTmX(String tmX) {
        this.tmX = tmX;
    }

    public String getTmY() {
        return tmY;
    }

    public void setTmY(String tmY) {
        this.tmY = tmY;
    }
}
