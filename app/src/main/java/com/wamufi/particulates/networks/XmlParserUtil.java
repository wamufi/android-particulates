package com.wamufi.particulates.networks;

import android.content.Context;

import com.wamufi.particulates.models.MeasureModel;
import com.wamufi.particulates.models.SearchModel;
import com.wamufi.particulates.models.StationModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class XmlParserUtil {

    private Context mContext;

    private XmlPullParserFactory mXmlPullParserFactory;
    private XmlPullParser mXmlPullParser;

//    public XmlParserUtil(Context context) {
//        mContext = context;
//    }

    /**
     * 측정소별 실시간 측정정보 조회
     *
     * @param urlString
     * @return
     */
    public ArrayList<MeasureModel> RealtimeMeasure(String urlString) {
        ArrayList<MeasureModel> arrayList = new ArrayList<MeasureModel>();
        MeasureModel measureModel = null;

        String tagName = null;
        boolean isItemTag = false;
        String text = null;

        try {
//            InputStream inputStream = downloadUrl(urlString);
            String result = new UrlFetch().getUrl(urlString);

            mXmlPullParserFactory = XmlPullParserFactory.newInstance();
            mXmlPullParser = mXmlPullParserFactory.newPullParser();
            mXmlPullParser.setInput(new StringReader(result));
//            mXmlPullParser.setInput(inputStream, null);
//            Log.v("XmlParserUtil", "xmlData: "+inputStream);

            int eventType = mXmlPullParser.getEventType();
            while (eventType != mXmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = mXmlPullParser.getName();
                        if (tagName.equals("item")) {
                            isItemTag = true;
                            measureModel = new MeasureModel();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (isItemTag) {
                            text = mXmlPullParser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tagName = mXmlPullParser.getName();
                        if (isItemTag) {
                            if (tagName.equals("dataTime"))
                                measureModel.setDataTime(text);
                            else if (tagName.equals("so2Value"))
                                measureModel.setSo2Value(text);
                            else if (tagName.equals("coValue"))
                                measureModel.setCoValue(text);
                            else if (tagName.equals("o3Value"))
                                measureModel.setO3Value(text);
                            else if (tagName.equals("no2Value"))
                                measureModel.setNo2Value(text);
                            else if (tagName.equals("pm10Value"))
                                measureModel.setPm10Value(text);
                            else if (tagName.equals("pm25Value"))
                                measureModel.setPm25Value(text);
                            else if (tagName.equals("khaiValue"))
                                measureModel.setKhaiValue(text);
                            else if (tagName.equals("khaiGrade"))
                                measureModel.setKhaiGrade(text);
                            else if (tagName.equals("so2Grade"))
                                measureModel.setSo2Grade(text);
                            else if (tagName.equals("coGrade"))
                                measureModel.setCoGrade(text);
                            else if (tagName.equals("o3Grade"))
                                measureModel.setO3Grade(text);
                            else if (tagName.equals("no2Grade"))
                                measureModel.setNo2Grade(text);
                            else if (tagName.equals("pm10Grade"))
                                measureModel.setPm10Grade(text);
                            else if (tagName.equals("pm25Grade"))
                                measureModel.setPm25Grade(text);
                            else if (tagName.equals("pm10Grade1h"))
                                measureModel.setPm10Grade1h(text);
                            else if (tagName.equals("pm25Grade1h")) {
                                measureModel.setPm25Grade1h(text);
                                arrayList.add(measureModel);
                            }
                        }

                        if (tagName.equals("item")) {
                            isItemTag = false;
                        }
                        break;
                    default:
                        break;
                }
                eventType = mXmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    /**
     * 근접측정소 목록 조회
     *
     * @param urlString
     * @return
     */
    public ArrayList<StationModel> StationInfo(String urlString) {
        ArrayList<StationModel> arrayList = new ArrayList<StationModel>();
        StationModel stationModel = null;

        String tagName = null;
        boolean isItemTag = false;
        String text = null;

        try {
//            InputStream inputStream = downloadUrl(urlString);
            String result = new UrlFetch().getUrl(urlString);

            mXmlPullParserFactory = XmlPullParserFactory.newInstance();
            mXmlPullParser = mXmlPullParserFactory.newPullParser();
            mXmlPullParser.setInput(new StringReader(result));
//            mXmlPullParser.setInput(inputStream, null);
//            Log.v("XmlParserUtil", "xmlData: "+inputStream);

            int eventType = mXmlPullParser.getEventType();
            while (eventType != mXmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = mXmlPullParser.getName();
                        if (tagName.equals("item")) {
                            isItemTag = true;
                            stationModel = new StationModel();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (isItemTag) {
                            text = mXmlPullParser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tagName = mXmlPullParser.getName();
                        if (isItemTag) {
                            if (tagName.equals("stationName"))
                                stationModel.setStationName(text);
                            else if (tagName.equals("addr")) {
                                stationModel.setAddr(text);
                                arrayList.add(stationModel);
                            }
                        }

                        if (tagName.equals("item")) {
                            isItemTag = false;
                        }
                        break;
                    default:
                        break;
                }
                eventType = mXmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    /**
     * TM 기준좌표 조회
     *
     * @param urlString
     * @return
     */
    public ArrayList<SearchModel> SearchCoordinate(String urlString) {
        ArrayList<SearchModel> arrayList = new ArrayList<SearchModel>();
        SearchModel searchModel = null;

        String tagName = null;
        boolean isItemTag = false;
        String text = null;

        try {
//            InputStream inputStream = downloadUrl(urlString);
            String result = new UrlFetch().getUrl(urlString);

            mXmlPullParserFactory = XmlPullParserFactory.newInstance();
            mXmlPullParser = mXmlPullParserFactory.newPullParser();
            mXmlPullParser.setInput(new StringReader(result));
//            mXmlPullParser.setInput(inputStream, null);
//            Log.v("XmlParserUtil", "xmlData: "+inputStream);

            int eventType = mXmlPullParser.getEventType();
            while (eventType != mXmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = mXmlPullParser.getName();
                        if (tagName.equals("item")) {
                            isItemTag = true;
                            searchModel = new SearchModel();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (isItemTag) {
                            text = mXmlPullParser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tagName = mXmlPullParser.getName();
                        if (isItemTag) {
                            if (tagName.equals("sidoName"))
                                searchModel.setSidoName(text);
                            else if (tagName.equals("sggName"))
                                searchModel.setSggName(text);
                            else if (tagName.equals("umdName"))
                                searchModel.setUmdName(text);
                            else if (tagName.equals("tmX"))
                                searchModel.setTmX(text);
                            else if (tagName.equals("tmY")) {
                                searchModel.setTmY(text);
                                arrayList.add(searchModel);
                            }
                        }

                        if (tagName.equals("item")) {
                            isItemTag = false;
                        }
                        break;
                    default:
                        break;
                }
                eventType = mXmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000); // milliseconds
        connection.setConnectTimeout(15000); // milliseconds
        connection.setRequestMethod("GET");
        connection.setDoInput(true);

        // Starts the query
        connection.connect();
        return connection.getInputStream();
    }

//    private class DownloadXmlTask extends AsyncTask<String, Void, InputStream> {
//        @Override
//        protected InputStream doInBackground(String... strings) {
//            try {
//                return downloadUrl(strings[0]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
