package com.shubhobrata.roy.nasaspaceapps;

import android.graphics.Bitmap;

public class NamedImage {
    private String imageName;
    private Bitmap image;

    public NamedImage(String imageName, Bitmap image)   {
        this.imageName = imageName;
        this.image = image;
    }

    // Setters

    public void setImageName(String imageName)  {
        this.imageName = imageName;
    }

    public void setImage(Bitmap image)  {
        this.image = image;
    }

    // Getters

    public String getImageName()    {
        return imageName;
    }

    public Bitmap getImage()    {
        return image;
    }
}
