package com.wamufi.particulates.networks;

public class UrlUtil {

    public static final String STATION_URL = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/"; // 측정소정보 조회 서비스
    public static final String STATION_TM_X = "getNearbyMsrstnList?pageNo=1&numOfRows=10&tmX="; // 근접측정소 목록 조회
    public static final String STATION_TM_Y = "&tmY=";
    public static final String STATION_SEARCH = "getTMStdrCrdnt?pageNo=1&numOfRows=25&umdName="; // TM 기준좌표 조회

    public static final String AIR_POLLUTION_URL = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/"; // 대기오염정보조회 서비스
    public static final String AP_REALTIME_MEASURE = "getMsrstnAcctoRltmMesureDnsty?dataTerm=daily&ver=1.3&stationName="; // 측정소별 실시간 측정정보 조회
}

