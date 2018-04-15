package com.wamufi.particulates.models;

public class StationModel {

    private String stationName; // 측정소 명
    private String addr; // 측정소 주소

    public StationModel() {
        super();
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
