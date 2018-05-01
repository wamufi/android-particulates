package com.wamufi.particulates.models;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class MeasureModel implements Serializable {

    private String dataTime;
    private String so2Value; // 아황산가스 농도
    private String coValue; // 일산화탄소 농도
    private String o3Value; // 오존 농도
    private String no2Value; // 이산화질소 농도
    private String pm10Value; // 미세먼지 농도
    private String pm25Value; // 초미세먼지 농도
    private String khaiValue; // 통합대기환경 수치
    private String khaiGrade; // 통합대기환경 지수
    private String so2Grade; // 아황산가스 지수
    private String coGrade; // 일산화탄소 지수
    private String o3Grade; // 오존 지수
    private String no2Grade; // 이산화질소 지수
    private String pm10Grade; // 미세먼지 24시간 등급
    private String pm25Grade; // 초미세먼지 24시간 등급
    private String pm10Grade1h; // 미세먼지 1시간 등급
    private String pm25Grade1h; // 초미세먼지 1시간 등급


    public MeasureModel() {
        super();
    }

    public String getDataTime() {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
            dataTime = dataTime.substring(dataTime.indexOf(" "), dataTime.length());
            Date date = timeFormat.parse(dataTime);
            String timeOut = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
            return timeOut;
        } catch (Exception e) {
            return dataTime;
        }
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getSo2Value() {
        return so2Value;
    }

    public void setSo2Value(String so2Value) {
        this.so2Value = so2Value;
    }

    public String getCoValue() {
        return coValue;
    }

    public void setCoValue(String coValue) {
        this.coValue = coValue;
    }

    public String getO3Value() {
        return o3Value;
    }

    public void setO3Value(String o3Value) {
        this.o3Value = o3Value;
    }

    public String getNo2Value() {
        return no2Value;
    }

    public void setNo2Value(String no2Value) {
        this.no2Value = no2Value;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    public String getKhaiValue() {
        return khaiValue;
    }

    public void setKhaiValue(String khaiValue) {
        this.khaiValue = khaiValue;
    }

    public String getKhaiGrade() {
        return khaiGrade;
    }

    public void setKhaiGrade(String khaiGrade) {
        this.khaiGrade = khaiGrade;
    }

    public String getSo2Grade() {
        return so2Grade;
    }

    public void setSo2Grade(String so2Grade) {
        this.so2Grade = so2Grade;
    }

    public String getCoGrade() {
        return coGrade;
    }

    public void setCoGrade(String coGrade) {
        this.coGrade = coGrade;
    }

    public String getO3Grade() {
        return o3Grade;
    }

    public void setO3Grade(String o3Grade) {
        this.o3Grade = o3Grade;
    }

    public String getNo2Grade() {
        return no2Grade;
    }

    public void setNo2Grade(String no2Grade) {
        this.no2Grade = no2Grade;
    }

    public String getPm10Grade() {
        return pm10Grade;
    }

    public void setPm10Grade(String pm10Grade) {
        this.pm10Grade = pm10Grade;
    }

    public String getPm25Grade() {
        return pm25Grade;
    }

    public void setPm25Grade(String pm25Grade) {
        this.pm25Grade = pm25Grade;
    }

    public String getPm10Grade1h() {
        return pm10Grade1h;
    }

    public void setPm10Grade1h(String pm10Grade1h) {
        this.pm10Grade1h = pm10Grade1h;
    }

    public String getPm25Grade1h() {
        return pm25Grade1h;
    }

    public void setPm25Grade1h(String pm25Grade1h) {
        this.pm25Grade1h = pm25Grade1h;
    }
}
