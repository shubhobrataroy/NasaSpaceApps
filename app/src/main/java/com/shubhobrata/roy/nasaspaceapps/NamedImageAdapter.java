package com.shubhobrata.roy.nasaspaceapps;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NamedImageAdapter extends RecyclerView.Adapter<NamedImageAdapter.NamedImageViewHolder>   {
    private AppCompatActivity activity;
    private List<NamedImage> namedImages;

    public NamedImageAdapter(AppCompatActivity activity, List<NamedImage> namedImages)  {
        this.namedImages = namedImages;
        this.activity = activity;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setNamedImages(List<NamedImage> namedImages)    {
        this.namedImages = namedImages;
    }

    public AppCompatActivity getActivity()  {
        return activity;
    }

    public List<NamedImage> getNamedImages()    {
        return namedImages;
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
    public void onBindViewHolder(final NamedImageViewHolder holder, int position)   {
        NamedImage namedImage = namedImages.get(position);
        holder.getImageNameView().setText(namedImage.getImageName());
        holder.getImageView().setImageBitmap(namedImage.getImage());
        final View view = holder.getView();
        view.setId(position);
        view.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("" + view.getId())
                        .setPositiveButton("OK", null).show();
            }
        });
    }

    public class NamedImageViewHolder extends RecyclerView.ViewHolder   {
        private View view;
        private TextView imageNameView;
        private ImageView imageView;

        public NamedImageViewHolder(View view)  {
            super(view);
            setView(view);
            setImageNameView((TextView)view.findViewById(R.id.imageNameView));
            setImageView((ImageView)view.findViewById(R.id.imageView));
        }

        public void setView(View view)  {
            this.view = view;
        }

        public void setImageNameView(TextView imageNameView)    {
            this.imageNameView = imageNameView;
        }

        public void setImageView(ImageView imageView)   {
            this.imageView = imageView;
        }

        public View getView()   {
            return view;
        }

        public TextView getImageNameView()  {
            return imageNameView;
        }

        public ImageView getImageView() {
            return imageView;
        }
    }
}
