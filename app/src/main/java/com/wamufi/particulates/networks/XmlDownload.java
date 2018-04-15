package com.wamufi.particulates.networks;

import android.os.AsyncTask;
import android.util.Log;

import com.wamufi.particulates.models.MeasureModel;
import com.wamufi.particulates.models.StationModel;

import java.util.ArrayList;

public class XmlDownload {

    public static String TAG = "XmlDownload";

    private static XmlDownload instance;

//    public XmlDownload() {
//        instance = this;
//    }
//
//    public static XmlDownload get() {
//        return instance;
//    }

    public static class DownloadStationXml extends AsyncTask<String, Void, ArrayList<StationModel>> {

        @Override
        protected ArrayList<StationModel> doInBackground(String... strings) {
            return new XmlParserUtil().StationInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<StationModel> stationModels) {
            Log.v(TAG, "get 0 " + String.valueOf(stationModels.get(0).getStationName()));
            Log.v(TAG, "get 1 " + String.valueOf(stationModels.get(1).getStationName()));
        }
    }

    public static class DownloadMeasureXml extends AsyncTask<String, Void, ArrayList<MeasureModel>> {

        @Override
        protected ArrayList<MeasureModel> doInBackground(String... strings) {
            return new XmlParserUtil().RealtimeMeasure(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<MeasureModel> measureModels) {
            Log.v(TAG, "get 0 " + String.valueOf(measureModels.get(0).getDateTime()));
            Log.v(TAG, "get 1 " + String.valueOf(measureModels.get(1).getDateTime()));
        }
    }
}
