package com.shubhobrata.roy.nasaspaceapps;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StatisticsVisualizerActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private RecyclerView recyclerView;
    private JSONObject imageNamesAndLocations;
    private List<NamedImage> namedImages;
    private int successfulImageCount;
    private int failedImageCount;
    private Toast userNotification;

    public synchronized void setRequestQueue(RequestQueue requestQueue)  {
        this.requestQueue = requestQueue;
    }

    public synchronized void setRecyclerView(RecyclerView recyclerView)  {
        this.recyclerView = recyclerView;
    }

    public synchronized void setImageNamesAndLocations(JSONObject imageNamesAndLocations)    {
        this.imageNamesAndLocations = imageNamesAndLocations;
    }

    public synchronized void setNamedImages(List<NamedImage> namedImages)    {
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

    public synchronized JSONObject getImageNamesAndLocations()   {
        return imageNamesAndLocations;
    }

    public synchronized List<NamedImage> getNamedImages()    {
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

    public synchronized void addNamedImage(NamedImage namedImage)    {
        namedImages.add(namedImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistical_view);
        setRecyclerView((RecyclerView)findViewById(R.id.recycler_view));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setNamedImages(new ArrayList<NamedImage>());
        recyclerView.setAdapter(new NamedImageAdapter(this, getNamedImages()));
        setRequestQueue(Volley.newRequestQueue(this));
        setImageNamesAndLocations(new JSONObject());
        setSuccessfulImageCount(0);
        setFailedImageCount(0);
        setUserNotification(Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG));
        requestQueue.add(new JsonObjectRequest(
                Request.Method.GET,
                getString(R.string.url_imageNamesAndLocations),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setImageNamesAndLocations(response);
                        notifyUser("Loading images.\nPlease wait for some time.");
                        downloadAndViewImages();
                    }
                },
                new Response.ErrorListener()    {
                    @Override
                    public void onErrorResponse(VolleyError error)  {
                        String message = "Sorry. Couldn't retrieve image info.\n";
                        message += "Please check the internet connection.";
                        notifyUser(message);
                    }
                }
        ));
    }

    private void downloadAndViewImages()    {
        Iterator<String> keyIterator = imageNamesAndLocations.keys();
        final List<String> imageNames = new ArrayList<>();
        while(keyIterator.hasNext())    {
            imageNames.add(keyIterator.next());
        }
        for(int i = 0; i < imageNames.size(); i++)  {
            final String imageName = imageNames.get(i);
            String imageURL = "";
            try {
                imageURL = imageNamesAndLocations.getString(imageName);
            }   catch(JSONException ex) {
                if((++failedImageCount) + successfulImageCount == imageNames.size())  {
                    notifyUserOnImageProcessingFinished();
                }
                continue;
            }
            requestQueue.add(new ImageRequest(
                    imageURL,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap image)    {
                            addNamedImage(new NamedImage(imageName, image));
                            recyclerView.getAdapter().notifyDataSetChanged();
                            if((++successfulImageCount) + failedImageCount == imageNames.size())   {
                                notifyUserOnImageProcessingFinished();
                            }
                        }
                    },
                    0, 0, null, null,
                    new Response.ErrorListener()    {
                        @Override
                        public void onErrorResponse(VolleyError error)  {
                            if(successfulImageCount + (++failedImageCount) == imageNames.size())  {
                                notifyUserOnImageProcessingFinished();
                            }
                        }
                    }
            ));
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
