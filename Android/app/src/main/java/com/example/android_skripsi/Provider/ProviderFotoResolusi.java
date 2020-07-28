package com.example.android_skripsi.Provider;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProviderFotoResolusi {

    String imageRemarksResolusi;
    Bitmap imageBitmapResolusi;
    String imagePathResolusi;
    String idImageRemarksResolusi;

    public ProviderFotoResolusi() {
    }

    public ProviderFotoResolusi(String imageRemarksResolusi, Bitmap imageBitmapResolusi, String imagePathResolusi) {
        this.imageRemarksResolusi = imageRemarksResolusi;
        this.imageBitmapResolusi = imageBitmapResolusi;
        this.imagePathResolusi = imagePathResolusi;
    }

    public String getImageRemarksResolusi() {
        return imageRemarksResolusi;
    }

    public void setImageRemarksResolusi(String imageRemarksResolusi) {
        this.imageRemarksResolusi = imageRemarksResolusi;
    }

    public Bitmap getImageBitmapResolusi() {
        return imageBitmapResolusi;
    }

    public void setImageBitmapResolusi(Bitmap imageBitmapResolusi) {
        this.imageBitmapResolusi = imageBitmapResolusi;
    }

    public String getImagePathResolusi() {
        return imagePathResolusi;
    }

    public void setImagePathResolusi(String imagePathResolusi) {
        this.imagePathResolusi = imagePathResolusi;
    }

    public String getIdImageRemarksResolusi() {
        return idImageRemarksResolusi;
    }

    public void setIdImageRemarksResolusi(String idImageRemarksResolusi) {
        this.idImageRemarksResolusi = idImageRemarksResolusi;
    }
}
