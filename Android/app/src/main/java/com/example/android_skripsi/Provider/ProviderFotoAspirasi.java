package com.example.android_skripsi.Provider;

import android.graphics.Bitmap;

public class ProviderFotoAspirasi {

    String imageRemarks;
    Bitmap imageBitmap;
    String imagePath;
    String idImageRemarks;

    public String getIdImageRemarks() {
        return idImageRemarks;
    }

    public void setIdImageRemarks(String idImageRemarks) {
        this.idImageRemarks = idImageRemarks;
    }

    public Bitmap getImageBitmap() { return imageBitmap; }
    public String getImageRemarks() {
        return imageRemarks;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImageRemarks(String imageRemarks) {
        this.imageRemarks = imageRemarks;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public ProviderFotoAspirasi(String imageRemarks, Bitmap imageBitmap, String imagePath) {
        this.imageRemarks = imageRemarks;
        this.imageBitmap = imageBitmap;
        this.imagePath = imagePath;
    }

    public ProviderFotoAspirasi() {
    }
}
