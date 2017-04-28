package com.shubhobrata.roy.nasaspaceapps;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StatisticsVisualizerActivity extends AppCompatActivity
{
    private enum WorkingState   {
        BUSY, FREE
    }

    private RecyclerView recyclerView;
    private List<NamedImage> namedImages;
    private JSONObject imageNamesAndLocations;
    private WorkingState workingState;

    public synchronized void setImageNamesAndLocations(JSONObject imageNamesAndLocations)    {
        this.imageNamesAndLocations = imageNamesAndLocations;
    }

    public synchronized void setNamedImages(List<NamedImage> namedImages)    {
        this.namedImages = namedImages;
    }

    public synchronized void setWorkingState(WorkingState workingState) {
        this.workingState = workingState;
    }

    public synchronized List<NamedImage> getNamedImages()    {
        return namedImages;
    }

    public RecyclerView getRecyclerView()   {
        return recyclerView;
    }

    public synchronized JSONObject getImageNamesAndLocations()   {
        return imageNamesAndLocations;
    }

    public synchronized WorkingState getWorkingState()  {
        return workingState;
    }

    public synchronized void addNamedImage(NamedImage namedImage)    {
        namedImages.add(namedImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistical_view);
        imageNamesAndLocations = new JSONObject();
        workingState = WorkingState.FREE;
        namedImages = new ArrayList<>();
        retrieveImageNamesAndLocations("http://abdalimran.pythonanywhere.com/visualizations");
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new NamedImageAdapter(namedImages));
        // requestImageAsync("http://media.mnn.com/assets/images/2015/08/forest-waterfall-thailand.jpg.838x0_q80.jpg");
        // requestImageAsync("https://github.com/abdalimran/Bangladesh-Landslide-Data-Analysis/raw/master/03_city_vs_nlandslides.jpg");
        /*
        requestAsync("http://abdalimran.pythonanywhere.com/visualizations");
        requestImageAsync("http://media.mnn.com/assets/images/2015/08/forest-waterfall-thailand.jpg.838x0_q80.jpg");
        */
    }

    private void retrieveImageNamesAndLocations(String url)    {
        setWorkingState(WorkingState.BUSY);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET, url,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject)   {
                                setImageNamesAndLocations(jsonObject);
                                setWorkingState(WorkingState.FREE);
                                retrieveNamedImages(imageNamesAndLocations);
                            }
                        },
                        new Response.ErrorListener()    {
                            @Override
                            public void onErrorResponse(VolleyError error)    {
                                setImageNamesAndLocations(new JSONObject());
                            }
                        }
                );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void retrieveNamedImages(JSONObject jsonObject) {
        Iterator<String> keyIterator = jsonObject.keys();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                getRecyclerView().getAdapter().notifyDataSetChanged();
            }
        });
        while(keyIterator.hasNext())    {
            final String key = keyIterator.next();
            try {
                requestQueue.add(new ImageRequest(
                        jsonObject.getString(key),
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap image)    {
                                getNamedImages().add(new NamedImage(key, image));
                            }
                        }, 0, 0, null, null,
                        new Response.ErrorListener()    {
                            @Override
                            public void onErrorResponse(VolleyError error) { }
                        }
                ));
            }   catch(Exception exception) { }
        }
    }

    /*
    private void requestImageAsync(String imageAddress)   {
        final ImageView imageView = (ImageView)findViewById(R.id.downloadedImage);
        ImageRequest imageRequest = new ImageRequest(
                imageAddress,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, null, null);
        Volley.newRequestQueue(this).add(imageRequest);
    }
    */

    /*
    private void requestAsync(String url)    {
        final TextView responseView = (TextView)findViewById(R.id.responseView);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject jsonObject)    {
                                                                responseView.setText(jsonObject.toString());
                                                                Iterator<String> keys = jsonObject.keys();
                                                                responseView.setText("");
                                                                while(keys.hasNext())   {
                                                                    try {
                                                                        String key = keys.next();
                                                                        responseView.append("Key: " + key + "\nValue: " + jsonObject.getString(key) + "\n\n");
                                                                    }   catch(Exception ex) {

                                                                    }
                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener()    {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error)  {
                                                                responseView.setText(error.getMessage());
                                                            }
                                                        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        responseView.setText("Request sent to " + url);
    }
    */
}
