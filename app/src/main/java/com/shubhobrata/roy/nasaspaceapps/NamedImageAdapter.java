package com.shubhobrata.roy.nasaspaceapps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NamedImageAdapter extends RecyclerView.Adapter<NamedImageAdapter.NamedImageViewHolder>   {
    private List<NamedImage> namedImages;

    public NamedImageAdapter(List<NamedImage> namedImages)  {
        this.namedImages = namedImages;
    }

    // Setters

    public void setNamedImages(List<NamedImage> namedImages)    {
        this.namedImages = namedImages;
    }

    @Override
    public NamedImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View namedImageView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_instance, parent, false);
        return new NamedImageViewHolder(namedImageView);
    }

    @Override
    public int getItemCount() {
        return namedImages.size();
    }

    @Override
    public void onBindViewHolder(NamedImageViewHolder holder, int position) {
        NamedImage namedImage = namedImages.get(position);
        holder.getImageNameView().setText(namedImage.getImageName());
        holder.getImageView().setImageBitmap(namedImage.getImage());
    }

    public class NamedImageViewHolder extends RecyclerView.ViewHolder   {
        private TextView imageNameView;
        private ImageView imageView;

        public NamedImageViewHolder(View view)  {
            super(view);
            imageNameView = (TextView)view.findViewById(R.id.imageNameView);
            imageView = (ImageView)view.findViewById(R.id.imageView);
        }

        //  Setters

        public void setImageNameView(TextView imageNameView)    {
            this.imageNameView = imageNameView;
        }

        public void setImageView(ImageView imageView)   {
            this.imageView = imageView;
        }

        //  Getters

        public TextView getImageNameView()  {
            return imageNameView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
