package com.wamufi.particulates.models;

public class DbModel {

    private String searchName;
    private String searchAddr;
    private String updateTime;
    private String stationName;

    public DbModel() {
        super();
    }

    public String getSearchName() {
        return searchName;
    }

    public String getSearchAddr() {
        return searchAddr;
    }

    public void setSearchAddr(String searchAddr) {
        this.searchAddr = searchAddr;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
