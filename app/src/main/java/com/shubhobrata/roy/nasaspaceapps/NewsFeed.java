package com.shubhobrata.roy.nasaspaceapps;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public static NewsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public  static Context context;

    public List<NewsHolder> getSh() {
        return sh;
    }

    public List<NewsHolder> sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        context=this;
        List<NewsHolder> sh= new ArrayList<>();
        NewsHolder nh = new NewsHolder();
        nh.setTitle("Please Wait");
        nh.setUrl("Connecting to server");
        sh.add(nh);
        mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter( NewsFeed.this, sh);
        mRecyclerView.setAdapter(mAdapter);
        new SetNews().execute();

    }


    private class SetNews extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(NewsFeed.this, "Contacting to server", Toast.LENGTH_LONG).show();


        }



        @Override
        protected Void doInBackground(Void... voids) {
            final HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("http://abdalimran.pythonanywhere.com/news-feeds");
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    final JSONArray news = jsonObj.getJSONArray("news");
                     final List<NewsHolder> nh = new ArrayList<>();
                    for (int i = 0; i < news.length(); i++) {
                        String link= news.getJSONObject(i).getString("link");
                        String title=news.getJSONObject(i).getString("title");
                        NewsHolder newsHolder = new NewsHolder();
                        newsHolder.setTitle(title);
                        newsHolder.setUrl(link);
                        nh.add(newsHolder);

                    }
                    final List<NewsHolder> sh2 = nh;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.updateList(sh2);
                        }
                    });


                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {



            super.onPostExecute(result);

        }
    }
}
