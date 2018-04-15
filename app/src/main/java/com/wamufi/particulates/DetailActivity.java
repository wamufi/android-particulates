package com.wamufi.particulates;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wamufi.particulates.databases.DbManager;
import com.wamufi.particulates.models.DbModel;
import com.wamufi.particulates.models.MeasureModel;
import com.wamufi.particulates.models.StationModel;
import com.wamufi.particulates.networks.UrlUtil;
import com.wamufi.particulates.networks.XmlParserUtil;
import com.wamufi.particulates.utils.ColorResources;
import com.wamufi.particulates.utils.GradeString;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private Context mContext;

    private MeasureModel mMeasureModel;

    private String mSearchName;
    private String mStationName;

    private DbManager mDbManager;
    private ArrayList<DbModel> dbModels = null;

    private ColorResources mColorResources;

    @Override
    public Resources getResources() {
        if (mColorResources == null) {
            mColorResources = new ColorResources(super.getResources());
        }
        return mColorResources;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        if (getIntent().getBooleanExtra("search", true)) {
            mDbManager = new DbManager(mContext);

            String urlString = getIntent().getStringExtra("stationUrl");
            mSearchName = getIntent().getStringExtra("searchName");
            Log.v("DetailActivity", "onActivityResult: mSearchName - " + mSearchName);
            Log.v("DetailActivity", "onActivityResult: urlString - " + urlString);
            loadStationPage(urlString);
        } else {
            mMeasureModel = (MeasureModel) getIntent().getSerializableExtra("measure_model");
            mSearchName = getIntent().getStringExtra("station_name");
            mStationName = getIntent().getStringExtra("measure_name");
            Log.v("DetailActivity", "mStationName - " + mStationName);
            initView(false);
        }
    }

//    private void initView() {
    private void initView(boolean bSearch) {
//        getSupportActionBar().setTitle(getIntent().getStringExtra("station_name"));
        getSupportActionBar().setTitle(mSearchName);

        mColorResources.setColor(Integer.parseInt(mMeasureModel.getKhaiGrade()));


        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.detail_layout);
//        relativeLayout.setBackground(gradientBackground(getResources().getColor(R.color.grade_one_first), getResources().getColor(R.color.grade_one_second)));
        relativeLayout.setBackground(gradientBackground(mColorResources.setGradientColor(mMeasureModel.getKhaiGrade())));

        GradeString gradeString = new GradeString(this);

        TextView searchText = (TextView) findViewById(R.id.detail_search);
//        searchText.setText(getIntent().getStringExtra("station_name"));
        searchText.setText(mStationName);
        TextView dateText = (TextView) findViewById(R.id.detail_date);
        dateText.setText(mMeasureModel.getDateTime());

        TextView khaiValueText = (TextView) findViewById(R.id.detail_khai_value);
        khaiValueText.setText(mMeasureModel.getKhaiValue());
        TextView khaiGradeText = (TextView) findViewById(R.id.detail_khai_grade);
        khaiGradeText.setText(gradeString.getString(mMeasureModel.getKhaiGrade()));

        TextView pm10ValueText = (TextView) findViewById(R.id.detail_pm10_value);
        pm10ValueText.setText(mMeasureModel.getPm10Value());
        TextView pm10GradeText = (TextView) findViewById(R.id.detail_pm10_grade);
//        pm10GradeText.setText(mMeasureModel.getPm10Grade1h());
        pm10GradeText.setText(gradeString.getString(mMeasureModel.getPm10Grade1h()));

        TextView pm25ValueText = (TextView) findViewById(R.id.detail_pm25_value);
        pm25ValueText.setText(mMeasureModel.getPm25Value());
        TextView pm25GradeText = (TextView) findViewById(R.id.detail_pm25_grade);
        pm25GradeText.setText(gradeString.getString(mMeasureModel.getPm25Grade1h()));

        TextView o3ValueText = (TextView) findViewById(R.id.detail_o3_value);
        o3ValueText.setText(mMeasureModel.getO3Value());
        TextView o3GradeText = (TextView) findViewById(R.id.detail_o3_grade);
        o3GradeText.setText(gradeString.getString(mMeasureModel.getO3Grade()));

        TextView coValueText = (TextView) findViewById(R.id.detail_co_value);
        coValueText.setText(mMeasureModel.getCoValue());
        TextView coGradeText = (TextView) findViewById(R.id.detail_co_grade);
        coGradeText.setText(gradeString.getString(mMeasureModel.getCoGrade()));

        TextView so2ValueText = (TextView) findViewById(R.id.detail_so2_value);
        so2ValueText.setText(mMeasureModel.getSo2Value());
        TextView so2GradeText = (TextView) findViewById(R.id.detail_so2_grade);
        so2GradeText.setText(gradeString.getString(mMeasureModel.getSo2Grade()));

        TextView no2ValueText = (TextView) findViewById(R.id.detail_no2_value);
        no2ValueText.setText(mMeasureModel.getNo2Value());
        TextView no2GradeText = (TextView) findViewById(R.id.detail_no2_grade);
        no2GradeText.setText(gradeString.getString(mMeasureModel.getNo2Grade()));

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.detail_fab_add);
        Log.v("DetailActivity", "mSearchName - "+mSearchName);
        Log.v("DetailActivity", "bSearch - "+bSearch);

        if (bSearch) {
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDbManager.insertStation(mSearchName, mStationName);
                    Toast.makeText(mContext, "Added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            floatingActionButton.setVisibility(View.GONE);
        }
    }

    /**
     * Gradient
     */
    private Drawable gradientBackground(int[] colors) {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        return drawable;
    }

    /**
     * Connect network
     */
    private void loadStationPage(String urlString) { // 측정소정보 조회
        Log.v("DetailActivity", "loadStationPage: URL - " + urlString);
        new DownloadStationXml().execute(urlString);
    }

    private void loadMeasurePage(String urlString) { // 측정정보 조회
        Log.v("DetailActivity", "loadMeasurePage: URL - " + urlString);
        new DownloadMeasureXml().execute(urlString);
    }

    private class DownloadStationXml extends AsyncTask<String, Void, ArrayList<StationModel>> { // 측정소정보 조회
        @Override
        protected ArrayList<StationModel> doInBackground(String... strings) {
            return new XmlParserUtil().StationInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<StationModel> stationModels) {
            mStationName = String.valueOf(stationModels.get(0).getStationName());
            Log.v("DetailActivity", "DownloadStationXml: mSearchName - " + mSearchName);
            Log.v("DetailActivity", "DownloadStationXml: searchStationName - " + mStationName);
//            if (!mSearchName.equals("")) {
//                mDbManager.insertStation(mSearchName, String.valueOf(stationModels.get(0).getStationName()));
//            }

//            dbModels = mDbManager.selectAllStations();
//            Log.v("DetailActivity", "DownloadStationXml: dbModels - " + dbModels);

            String measureUrlString = UrlUtil.AIR_POLLUTION_URL + UrlUtil.AP_REALTIME_MEASURE + mStationName + getResources().getString(R.string.api_key);
            loadMeasurePage(measureUrlString);

//            int i = 0;
//            while (i < dbModels.size()) {
//                Log.v("DetailActivity", "DownloadStationXml: dbModels - " + dbModels.get(i).getSearchName());
//                measureUrlString = UrlUtil.AIR_POLLUTION_URL + UrlUtil.AP_REALTIME_MEASURE + dbModels.get(i).getStationName() + getResources().getString(R.string.api_key);
//                loadMeasurePage(measureUrlString);
//                DOWNLOAD_MEASURE_XML_COUNT += 1;
//                i++;
//            }
//            Log.v("DetailActivity", "DownloadStationXml: DOWNLOAD_MEASURE_XML_COUNT - " + DOWNLOAD_MEASURE_XML_COUNT);
        }
    }

    private class DownloadMeasureXml extends AsyncTask<String, Void, ArrayList<MeasureModel>> { // 측정정보 조회
        @Override
        protected ArrayList<MeasureModel> doInBackground(String... strings) {
            return new XmlParserUtil().RealtimeMeasure(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<MeasureModel> measureModels) {
//            stationList.add(measureModels);
//            Log.v("DetailActivity", "DownloadMeasureXml: stationList - " + stationList);
//            Log.v("DetailActivity", "DownloadMeasureXml: stationList.size() - " + stationList.size());
            mMeasureModel = measureModels.get(0);

//            DOWNLOAD_MEASURE_XML_COUNT += 1;
//            Log.v("DetailActivity", "DownloadMeasureXml: DOWNLOAD_MEASURE_XML_COUNT - " + DOWNLOAD_MEASURE_XML_COUNT);
            initView(true);
        }
    }

}
