package com.example.tattooshopapp;

public class GalleryImage {

    private String galleryImageDescription;
    private String galleryImageUrl;

    public GalleryImage(String galleryImageDescription, String galleryImageUrl) {
        this.galleryImageDescription = galleryImageDescription;
        this.galleryImageUrl = galleryImageUrl;
    }

    public GalleryImage() {
    }

    public String getGalleryImageDescription() {
        return galleryImageDescription;
    }

    public void setGalleryImageDescription(String galleryImageDescription) {
        this.galleryImageDescription = galleryImageDescription;
    }

    public String getGalleryImageUrl() {
        return galleryImageUrl;
    }

    public void setGalleryImageUrl(String galleryImageUrl) {
        this.galleryImageUrl = galleryImageUrl;
    }
}
