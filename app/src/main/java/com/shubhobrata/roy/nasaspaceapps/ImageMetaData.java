package com.shubhobrata.roy.nasaspaceapps;

import android.graphics.Bitmap;

public class ImageMetaData {
    private String description;
    private Bitmap image;

    public ImageMetaData(Bitmap image, String description)  {
        this.description = description;
        this.image = image;
    }

    // Setters

    public void setDescription(String description)  {
        this.description = description;
    }

    public void setImage(Bitmap image)  {
        this.image = image;
    }

    // Getters

    public String getDescription()    {
        return description;
    }

    public Bitmap getImage()    {
        return image;
    }
}
