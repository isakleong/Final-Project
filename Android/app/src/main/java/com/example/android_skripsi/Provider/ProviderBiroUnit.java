package com.example.android_skripsi.Provider;

import android.graphics.Bitmap;

public class ProviderBiroUnit {
    String idBiroUnit, namaBiroUnit, emailBiroUnit, ratingBiroUnit, jobDescBiroUnit;

    byte[] bytearray;

    Bitmap imageBitmap;
    String imagePath;

    //constructor
    public ProviderBiroUnit(){
        //set dummy data
        idBiroUnit="";
        namaBiroUnit="";
        emailBiroUnit="";
        ratingBiroUnit="";
        jobDescBiroUnit="";
    }

    public String getIdBiroUnit() {
        return idBiroUnit;
    }

    public void setIdBiroUnit(String idBiroUnit) {
        this.idBiroUnit = idBiroUnit;
    }

    public String getNamaBiroUnit() {
        return namaBiroUnit;
    }

    public void setNamaBiroUnit(String namaBiroUnit) {
        this.namaBiroUnit = namaBiroUnit;
    }

    public String getEmailBiroUnit() {
        return emailBiroUnit;
    }

    public void setEmailBiroUnit(String emailBiroUnit) {
        this.emailBiroUnit = emailBiroUnit;
    }

    public String getJobDescBiroUnit() {
        return jobDescBiroUnit;
    }

    public void setJobDescBiroUnit(String jobDescBiroUnit) {
        this.jobDescBiroUnit = jobDescBiroUnit;
    }

    public String getRatingBiroUnit() {
        return ratingBiroUnit;
    }

    public void setRatingBiroUnit(String ratingBiroUnit) {
        this.ratingBiroUnit = ratingBiroUnit;
    }

    public byte[] getBytearray() {
        return bytearray;
    }

    public void setBytearray(byte[] bytearray) {
        this.bytearray = bytearray;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
