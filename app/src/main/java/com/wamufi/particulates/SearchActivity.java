package com.wamufi.particulates;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wamufi.particulates.models.SearchModel;
import com.wamufi.particulates.networks.UrlUtil;
import com.wamufi.particulates.networks.XmlParserUtil;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public static String TAG = "SearchActivity";

    private Context mContext;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = getApplicationContext();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.search_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), 1); // 1 == vertical
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String urlString = getIntent().getStringExtra("urlString");
        loadSearchPage(urlString);
    }

    /**
     * Connect network
     */
    private void loadSearchPage(String urlString) {
        Log.v(TAG, "loadSearchPage: URL - " + urlString);
        new DownloadSearchXml().execute(urlString);
    }

    private class DownloadSearchXml extends AsyncTask<String, Void, ArrayList<SearchModel>> {

        @Override
        protected ArrayList<SearchModel> doInBackground(String... strings) {
            return new XmlParserUtil().SearchCoordinate(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<SearchModel> searchModels) {
            mRecyclerView.setAdapter(new SearchAdapter(searchModels));
        }
    }

    /**
     * Recycler Adapter
     */

    private class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        private ArrayList<SearchModel> mSearchModels;

        public SearchAdapter(ArrayList<SearchModel> searchModels) {
            mSearchModels = searchModels;
        }

        @Override
        public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.adapter_search, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
            final SearchModel item = mSearchModels.get(position);

            holder.umdText.setText(item.getUmdName());
            holder.sidoText.setText(item.getSidoName());
            holder.sggText.setText(item.getSggName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String urlString = UrlUtil.STATION_URL + UrlUtil.STATION_TM_X + String.valueOf(item.getTmX()) + UrlUtil.STATION_TM_Y + String.valueOf(item.getTmY()) + getResources().getString(R.string.api_key);

                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("search", true);
                    intent.putExtra("searchName", item.getUmdName());
                    intent.putExtra("stationUrl", urlString);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mSearchModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView umdText;
            TextView sidoText;
            TextView sggText;

            public ViewHolder(View itemView) {
                super(itemView);
                umdText = (TextView) itemView.findViewById(R.id.search_umd_text);
                sidoText = (TextView) itemView.findViewById(R.id.search_sido_text);
                sggText = (TextView) itemView.findViewById(R.id.search_sgg_text);
            }
        }
    }
}
