package com.example.android_skripsi.Provider;

import android.graphics.Bitmap;

public class ProviderFotoBiroUnit {

    Bitmap imageBitmap;
    String imagePath;

    public Bitmap getImageBitmap() { return imageBitmap; }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public ProviderFotoBiroUnit(Bitmap imageBitmap, String imagePath) {
        this.imageBitmap = imageBitmap;
        this.imagePath = imagePath;
    }

    public ProviderFotoBiroUnit() {
    }
}
