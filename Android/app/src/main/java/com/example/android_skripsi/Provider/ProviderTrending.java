package com.example.android_skripsi.Provider;

public class ProviderTrending {
    String idTrending, teksTrending, biroUnitTrending;

    //constructor
    public ProviderTrending(){
        this.idTrending = "";
        this.teksTrending = "";
        this.biroUnitTrending = "";
    }

    public String getIdTrending() {
        return idTrending;
    }

    public void setIdTrending(String idTrending) {
        this.idTrending = idTrending;
    }

    public String getTeksTrending() {
        return teksTrending;
    }

    public void setTeksTrending(String teksTrending) {
        this.teksTrending = teksTrending;
    }

    public String getBiroUnitTrending() {
        return biroUnitTrending;
    }

    public void setBiroUnitTrending(String biroUnitTrending) {
        this.biroUnitTrending = biroUnitTrending;
    }

}
