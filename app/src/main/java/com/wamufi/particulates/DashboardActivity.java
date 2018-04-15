package com.wamufi.particulates;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wamufi.particulates.databases.DbManager;
import com.wamufi.particulates.models.DbModel;
import com.wamufi.particulates.models.MeasureModel;
import com.wamufi.particulates.models.StationModel;
import com.wamufi.particulates.networks.UrlUtil;
import com.wamufi.particulates.networks.XmlParserUtil;
import com.wamufi.particulates.utils.ColorResources;
import com.wamufi.particulates.utils.GeoPoint;
import com.wamufi.particulates.utils.GeoTrans;
import com.wamufi.particulates.utils.LocationUtil;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    public static String TAG = "DashboardActivity";

    private Context mContext;

    private LinearLayout mLayout;
    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    private DbManager mDbManager;
    private ArrayList<DbModel> dbModels = null;

    static List stationList = null;
    int DOWNLOAD_MEASURE_XML_COUNT = 1;

    private LocationUtil mLocationUtil;
    private Location mLocation;
    private String mHereName;
//    private String mSearchName = "";
    private String mHereAddr;

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
        setContentView(R.layout.activity_dashboard);

        mContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

        mLayout = (LinearLayout) findViewById(R.id.dashboard_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.dashboard_recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        checkForPermissions();

        mDbManager = new DbManager(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbModels = mDbManager.selectAllStations();
        Log.v(TAG, "onResume: dbModels - " + dbModels);

        stationList = new ArrayList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocation != null)
            mLocationUtil.stopLocationUtil();

        DOWNLOAD_MEASURE_XML_COUNT = 1;
//        stationList.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);

        // Search View
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("읍/면/동 이름을 입력하세요.");
        searchView.setOnQueryTextListener(new SearchQueryListener());
        searchView.setIconifiedByDefault(false);

        return true;
    }

    /**
     * Permissions
     */
    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            getUserLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, "Permission was granted.", Snackbar.LENGTH_SHORT).show();
                getUserLocation();
            } else {
                Snackbar.make(mLayout, "Permission was NOT granted.", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Location
     */
    private void getUserLocation() {
        mLocationUtil = new LocationUtil(mContext);
        mLocation = mLocationUtil.getLocation();
        Log.v(TAG, "onCreate: mLocation - " + mLocation);
        Log.v(TAG, "onCreate: getProvider() - " + mLocation.getProvider());

        getNearbyStation();
    }

    private void getNearbyStation() {
        GeoPoint locationPoint = new GeoPoint(mLocation.getLongitude(), mLocation.getLatitude());
        double longitude = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, locationPoint).getX();
        double latitude = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, locationPoint).getY();

        String urlString = UrlUtil.STATION_URL + UrlUtil.STATION_TM_X + longitude + UrlUtil.STATION_TM_Y + latitude + getResources().getString(R.string.api_key);
        loadStationPage(urlString);
    }

    /**
     * Search Listener
     */
    private class SearchQueryListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            String urlString = UrlUtil.STATION_URL + UrlUtil.STATION_SEARCH + query + getResources().getString(R.string.api_key);

            Intent intent = new Intent(mContext, SearchActivity.class);
            intent.putExtra("urlString", urlString);
            startActivity(intent);
//            startActivityForResult(intent, 100);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 100 && resultCode == 50) {
//            String urlString = data.getStringExtra("stationUrl");
//            mSearchName = data.getStringExtra("searchName");
//            Log.v(TAG, "onActivityResult: mSearchName - " + mSearchName);
//            Log.v(TAG, "onActivityResult: urlString - " + urlString);
//            loadStationPage(urlString);
//        }
//    }

    /**
     * Connect network
     */
    private void loadStationPage(String urlString) { // 측정소정보 조회
        Log.v(TAG, "loadStationPage: URL - " + urlString);
        new DownloadStationXml().execute(urlString);
    }

    private void loadMeasurePage(String urlString) { // 측정정보 조회
        Log.v(TAG, "loadMeasurePage: URL - " + urlString);
        new DownloadMeasureXml().execute(urlString);
    }

    private class DownloadStationXml extends AsyncTask<String, Void, ArrayList<StationModel>> { // 측정소정보 조회
        @Override
        protected ArrayList<StationModel> doInBackground(String... strings) {
            return new XmlParserUtil().StationInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<StationModel> stationModels) {
            mHereName = String.valueOf(stationModels.get(0).getStationName());
            mHereAddr = String.valueOf(stationModels.get(0).getAddr());
//            Log.v(TAG, "DownloadStationXml: mSearchName - " + mSearchName);
            Log.v(TAG, "DownloadStationXml: searchStationName - " + mHereName);
//            if (!mSearchName.equals("")) {
//                mDbManager.insertStation(mSearchName, String.valueOf(stationModels.get(0).getStationName()));
//            }

            dbModels = mDbManager.selectAllStations();
            Log.v(TAG, "DownloadStationXml: dbModels - " + dbModels);

            String measureUrlString = UrlUtil.AIR_POLLUTION_URL + UrlUtil.AP_REALTIME_MEASURE + mHereName + getResources().getString(R.string.api_key);
            loadMeasurePage(measureUrlString);

            int i = 0;
            while (i < dbModels.size()) {
                Log.v(TAG, "DownloadStationXml: dbModels - " + dbModels.get(i).getSearchName());
                measureUrlString = UrlUtil.AIR_POLLUTION_URL + UrlUtil.AP_REALTIME_MEASURE + dbModels.get(i).getStationName() + getResources().getString(R.string.api_key);
                loadMeasurePage(measureUrlString);
                DOWNLOAD_MEASURE_XML_COUNT += 1;
                i++;
            }
            Log.v(TAG, "DownloadStationXml: DOWNLOAD_MEASURE_XML_COUNT - " + DOWNLOAD_MEASURE_XML_COUNT);
        }
    }

    private class DownloadMeasureXml extends AsyncTask<String, Void, ArrayList<MeasureModel>> { // 측정정보 조회
        @Override
        protected ArrayList<MeasureModel> doInBackground(String... strings) {
            return new XmlParserUtil().RealtimeMeasure(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<MeasureModel> measureModels) {
            stationList.add(measureModels);
            Log.v(TAG, "DownloadMeasureXml: stationList - " + stationList);
            Log.v(TAG, "DownloadMeasureXml: stationList.size() - " + stationList.size());

//            DOWNLOAD_MEASURE_XML_COUNT += 1;
            Log.v(TAG, "DownloadMeasureXml: DOWNLOAD_MEASURE_XML_COUNT - " + DOWNLOAD_MEASURE_XML_COUNT);

            if (DOWNLOAD_MEASURE_XML_COUNT == stationList.size()) {
//            mAdapter = new DashboardAdapter(measureModels);
                RecyclerView.Adapter adapter = new DashboardAdapter(stationList);
                mRecyclerView.setAdapter(adapter);
            }
        }
    }

//    private class DownloadSearchXml extends AsyncTask<String, Void, ArrayList<SearchModel>> {
//        @Override
//        protected ArrayList<SearchModel> doInBackground(String... strings) {
//            return new XmlParserUtil().SearchCoordinate(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(ArrayList<SearchModel> searchModels) {
//            String urlString = UrlUtil.STATION_URL + UrlUtil.STATION_TM_X + String.valueOf(searchModels.get(0).getTmX()) + UrlUtil.STATION_TM_Y + String.valueOf(searchModels.get(0).getTmY()) + getResources().getString(R.string.api_key);
//            loadStationPage(urlString);
//        }
//    }

    /**
     * Card Adapter
     */
    private class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

        private ArrayList<MeasureModel> mMeasureModels;
        private List mStationList;

//        DashboardAdapter(ArrayList<MeasureModel> measureModels) {
//            mMeasureModels = measureModels;
//            Log.v(TAG, "DashboardAdapter: mMeasureModels.size() - " + mMeasureModels.size());
//        }

        DashboardAdapter(List stationList) {
            mStationList = stationList;
            Log.v(TAG, "DashboardAdapter: mStationList.size() - " + mStationList.size());
        }

        @Override
        public DashboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DashboardActivity.this).inflate(R.layout.card_dashboard, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DashboardAdapter.ViewHolder holder, int position) {
            ArrayList<MeasureModel> measureModels = (ArrayList<MeasureModel>) mStationList.get(position);
            Log.v(TAG, "DashboardAdapter: measureModels.get(0).getDateTime() - " + measureModels.get(0).getPm10Value());

            final MeasureModel item = measureModels.get(0);
            final String intentStationName;
            final String intentStationAddr;
            final String intentUpdateTime;
            final String intentMeasureStationName;

            if (position == 0) { // 현위치
                holder.stationText.setText(mHereName);
                holder.updateText.setText(mHereAddr);
                intentStationName = mHereName;
                intentMeasureStationName = mHereName;

//                mColorResources.setColor(Integer.parseInt(item.getPm10Grade1h()));
                mColorResources.setColor(item.getPm10Grade1h());
//                holder.pm10Text.setTextColor(mColorResources.getColor(R.color.gradeColor));
                holder.pm10Layout.setBackgroundColor(mColorResources.getColor(R.color.gradeColor));
                holder.pm10Text.setText(item.getPm10Value());
                holder.pm10Grade.setText(item.getPm10Grade1h());
//                mColorResources.setColor(Integer.parseInt(item.getKhaiGrade()));
                mColorResources.setColor(item.getKhaiGrade());
                holder.khaiText.setTextColor(mColorResources.getColor(R.color.gradeColor));
                holder.khaiText.setText(item.getKhaiValue());
                if (!item.getPm25Value().equals("-")) {
                    holder.pm25Text.setText(item.getPm25Value());
                    holder.pm25Text.setTextColor(mColorResources.getColor(R.color.gradeColor));
//                    mColorResources.setColor(Integer.parseInt(item.getPm25Grade()));
                    mColorResources.setColor(item.getPm25Grade());
                }
            } else { // 검색된 위치
                intentStationName = dbModels.get(position - 1).getSearchName();
                intentStationAddr = dbModels.get(position - 1).getSearchAddr();
                intentUpdateTime = dbModels.get(position - 1).getUpdateTime();
                intentMeasureStationName = dbModels.get(position - 1).getStationName();

                holder.stationText.setText(intentStationName);
                holder.updateText.setText(intentUpdateTime);
//                holder.stationText.setText(dbModels.get(position).getSearchName());

//                mColorResources.setColor(Integer.parseInt(item.getPm10Grade1h()));
                mColorResources.setColor(item.getPm10Grade1h());
//                holder.pm10Text.setTextColor(mColorResources.getColor(R.color.gradeColor));
                holder.pm10Layout.setBackgroundColor(mColorResources.getColor(R.color.gradeColor));
                holder.pm10Text.setText(item.getPm10Value());
                holder.pm10Grade.setText(item.getPm10Grade1h());
//                mColorResources.setColor(Integer.parseInt(item.getKhaiGrade()));
                mColorResources.setColor(item.getKhaiGrade());
                holder.khaiText.setTextColor(mColorResources.getColor(R.color.gradeColor));
                holder.khaiText.setText(item.getKhaiValue());
                if (!item.getPm25Value().equals("-")) {
                    holder.pm25Text.setText(item.getPm25Value());
                    holder.pm25Text.setTextColor(mColorResources.getColor(R.color.gradeColor));
//                    mColorResources.setColor(Integer.parseInt(item.getPm25Grade()));
                    mColorResources.setColor(item.getPm25Grade());
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("measure_model", item);
//                    intent.putExtra("station_name", mHereName);
                    intent.putExtra("station_name", intentStationName);
//                    intent.putExtra("station_addr", intentStationAddr);
                    intent.putExtra("measure_name", intentMeasureStationName);
                    intent.putExtra("search", false);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dbModels.size() + 1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            TextView stationText;
            TextView updateText;
            RelativeLayout pm10Layout;
            TextView pm10Text;
            TextView pm10Grade;
            TextView khaiText;
            TextView pm25Text;

            ViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card_dashboard);
                stationText = (TextView) itemView.findViewById(R.id.card_station);
                updateText = (TextView) itemView.findViewById(R.id.card_update);
                pm10Layout = (RelativeLayout) itemView.findViewById(R.id.card_pm10_layout);
                pm10Text = (TextView) itemView.findViewById(R.id.card_pm10_value);
                pm10Grade = (TextView) itemView.findViewById(R.id.card_pm10_grade);
                khaiText = (TextView) itemView.findViewById(R.id.card_khai_value);
                pm25Text = (TextView) itemView.findViewById(R.id.card_pm25_value);
            }
        }
    }
}
