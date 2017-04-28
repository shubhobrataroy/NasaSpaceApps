package com.shubhobrata.roy.nasaspaceapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize
        view_landslide_on_map= (Button) findViewById(R.id.view_data_on_map);
        view_langslide_statistics=(Button) findViewById(R.id.view_landslide_statistics);
        view_news_feeds=(Button) findViewById(R.id.view_news_feed);

        //Listeners
        view_landslide_on_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LandslideViewerActivity.class);
                startActivity(intent);
            }
        });

        view_langslide_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,StatisticsVisualizerActivity.class);
                startActivity(intent);
            }
        });
        view_news_feeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,NewsFeed.class);
                startActivity(intent);
            }
        });
    }


    //Button
    Button view_landslide_on_map ;
    Button view_langslide_statistics;
    Button view_news_feeds;
}
