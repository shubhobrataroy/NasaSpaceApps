package com.shubhobrata.roy.nasaspaceapps;

import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StatisticsVisualizerActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private JSONArray imageInfo;
    private List<ImageMetaData> namedImages;
    private int successfulImageCount;
    private int failedImageCount;
    private Toast userNotification;

    public synchronized void setRequestQueue(RequestQueue requestQueue)  {
        this.requestQueue = requestQueue;
    }

    public synchronized void setRecyclerView(RecyclerView recyclerView)  {
        this.recyclerView = recyclerView;
    }

    public synchronized void setImageInfo(JSONArray imageInfo)    {
        this.imageInfo = imageInfo;
    }

    public synchronized void setNamedImages(List<ImageMetaData> namedImages)    {
        this.namedImages = namedImages;
    }

    public synchronized void setSuccessfulImageCount(int successfulImageCount)  {
        if(successfulImageCount >= 0) {
            this.successfulImageCount = successfulImageCount;
        }
        else throw new IllegalArgumentException("Expected number of images cannot be negative.");
    }

    public synchronized void setFailedImageCount(int failedImageCount)  {
        if(failedImageCount >= 0)   {
            this.failedImageCount = failedImageCount;
        }
        else throw new IllegalArgumentException("Expected number of images cannot be negative.");
    }

    public synchronized void setUserNotification(Toast userNotification)    {
        this.userNotification = userNotification;
    }

    public synchronized RequestQueue getRequestQueue()  {
        return requestQueue;
    }

    public RecyclerView getRecyclerView()   {
        return recyclerView;
    }

    public synchronized JSONArray getInfo()   {
        return imageInfo;
    }

    public synchronized List<ImageMetaData> getNamedImages()    {
        return namedImages;
    }

    public synchronized int getSuccessfulImageCount() {
        return successfulImageCount;
    }

    public synchronized int getFailedImageCount()   {
        return failedImageCount;
    }

    public synchronized Toast getUserNotification() {
        return userNotification;
    }

    public synchronized void addNamedImage(ImageMetaData namedImage)    {
        namedImages.add(namedImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistical_view);
        setRecyclerView((RecyclerView)findViewById(R.id.recycler_view));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setNamedImages(new ArrayList<ImageMetaData>());
        recyclerView.setAdapter(new NamedImageAdapter(this, getNamedImages()));
        setRequestQueue(Volley.newRequestQueue(this));
        setImageInfo(new JSONArray());
        setSuccessfulImageCount(0);
        setFailedImageCount(0);
        setUserNotification(Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG));
        requestQueue.add(new JsonArrayRequest(
                Request.Method.GET,
                getString(R.string.url_imageNamesAndLocations),
                new Response.Listener<JSONArray>()   {
                    @Override
                    public void onResponse(JSONArray response)  {
                        setImageInfo(response);
                        notifyUser(response.toString());
                        notifyUser("Loading images.\nPlease wait for some time.");
                        downloadAndViewImages();
                    }
                },
                new Response.ErrorListener()    {
                    @Override
                    public void onErrorResponse(VolleyError error)  {
                        /// String message = "Sorry. Couldn't retrieve image info.\n";
                        // message += "Please check the internet connection.";
                        // notifyUser(message);
                        notifyUser("Error Message: " + error.toString());
                    }
                }
        ));
    }

    private void downloadAndViewImages()    {
        for(int i = 0; i < imageInfo.length(); i++) {
            try {
                JSONObject json = ((JSONObject) imageInfo.get(i));
                Iterator<String> keyIterator = json.keys();
                String imageURL = json.getString(keyIterator.next());
                final String imageDescription = json.getString(keyIterator.next());
                requestQueue.add(new ImageRequest(
                        imageURL,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap image) {
                                addNamedImage(new ImageMetaData(image, imageDescription));
                                recyclerView.getAdapter().notifyDataSetChanged();
                                if((++successfulImageCount) + failedImageCount == imageInfo.length())   {
                                    notifyUserOnImageProcessingFinished();
                                }
                            }
                        },
                        0, 0, null, null,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(successfulImageCount + (++failedImageCount) == imageInfo.length())   {
                                    notifyUserOnImageProcessingFinished();
                                }
                            }
                        }
                ));
            }
            catch(JSONException ex) {
                if((++failedImageCount) + successfulImageCount == imageInfo.length())   {
                    notifyUserOnImageProcessingFinished();
                }
            }
        }
    }

    private void notifyUserOnImageProcessingFinished()  {
        String message = "";
        if(successfulImageCount == 0)   {
            message += "Sorry. We couldn't retrieve any images from server.";
        }
        else    {
            message += "Your images are ready";
            if(failedImageCount > 0)   {
                message += " (" + failedImageCount + " failed)";
            }
            message += ".";
        }
        notifyUser(message);
    }
    
    public void notifyUser(String message)  {
        userNotification.setText(message);
        userNotification.show();
    }
}
