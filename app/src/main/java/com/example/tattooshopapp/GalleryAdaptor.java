package com.example.tattooshopapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdaptor extends RecyclerView.Adapter<GalleryAdaptor.ViewHolder> {

    private ArrayList<GalleryImage> galleryImages;

    public GalleryAdaptor(ArrayList<GalleryImage> galleryImages) {
        this.galleryImages = galleryImages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View galleryImageView = layoutInflater.inflate(R.layout.gallery_image,viewGroup,false);
        return new ViewHolder(galleryImageView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdaptor.ViewHolder viewHolder, int position) {
        GalleryImage currentGalleryImage = galleryImages.get(position);
        viewHolder.imageDescriptionView.setText(currentGalleryImage.getGalleryImageDescription());
        viewHolder.imageView.setVisibility(View.VISIBLE);
        Glide.with(viewHolder.imageView.getContext())
                .load(currentGalleryImage.getGalleryImageUrl())
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return galleryImages.size();
    }

    public class ViewHolder  extends  RecyclerView.ViewHolder{

        TextView imageDescriptionView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDescriptionView = itemView.findViewById(R.id.galleryImageDescription);
            imageView = itemView.findViewById(R.id.galleryImage);
        }
    }
}
