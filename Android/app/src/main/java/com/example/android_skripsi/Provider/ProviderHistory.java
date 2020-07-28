package com.example.android_skripsi.Provider;

import android.graphics.Bitmap;

public class ProviderHistory {
    Integer idAspirasi, idBiroUnit, anonym;
    String teksAspirasi, fotoAspirasi, detailFotoAspirasi, tglAspirasi, namaBiroUnit, namaUserCivitas;

    Integer idStatusAspirasi;
    String namaStatusAspirasi, tglStatusAspirasi;

    byte[] fotoAspirasiByteArray;

    //data resolusi aspirasi
    String teksResolusiAspirasi, tglResolusiAspirasi, fotoResolusiAspirasi, detailFotoResolusiAspirasi;

    String fotoBiroUnit;

    byte[] fotoBiroUnitbytearray;

    Bitmap imageBitmap;

    //constructor
    public ProviderHistory(){
        this.idAspirasi = 0;
        this.idBiroUnit = 0;
        this.idStatusAspirasi = 0;
        this.teksAspirasi = "";
        this.fotoAspirasi = "";
        this.detailFotoAspirasi = "";
        this.tglAspirasi = "";
        this.tglStatusAspirasi = "";
        this.namaBiroUnit = "";
        this.namaStatusAspirasi = "";
        this.namaUserCivitas = "";
        this.anonym = 0;
        this.teksResolusiAspirasi="";
        this.tglResolusiAspirasi="";
        this.fotoResolusiAspirasi="";
        this.detailFotoResolusiAspirasi="";
        this.fotoBiroUnit="";
    }

    public byte[] getFotoBiroUnitbytearray() {
        return fotoBiroUnitbytearray;
    }

    public void setFotoBiroUnitbytearray(byte[] fotoBiroUnitbytearray) {
        this.fotoBiroUnitbytearray = fotoBiroUnitbytearray;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getFotoBiroUnit() {
        return fotoBiroUnit;
    }

    public void setFotoBiroUnit(String fotoBiroUnit) {
        this.fotoBiroUnit = fotoBiroUnit;
    }

    public String getTeksResolusiAspirasi() {
        return teksResolusiAspirasi;
    }

    public void setTeksResolusiAspirasi(String teksResolusiAspirasi) {
        this.teksResolusiAspirasi = teksResolusiAspirasi;
    }

    public String getTglResolusiAspirasi() {
        return tglResolusiAspirasi;
    }

    public void setTglResolusiAspirasi(String tglResolusiAspirasi) {
        this.tglResolusiAspirasi = tglResolusiAspirasi;
    }

    public String getFotoResolusiAspirasi() {
        return fotoResolusiAspirasi;
    }

    public void setFotoResolusiAspirasi(String fotoResolusiAspirasi) {
        this.fotoResolusiAspirasi = fotoResolusiAspirasi;
    }

    public String getDetailFotoResolusiAspirasi() {
        return detailFotoResolusiAspirasi;
    }

    public void setDetailFotoResolusiAspirasi(String detailFotoResolusiAspirasi) {
        this.detailFotoResolusiAspirasi = detailFotoResolusiAspirasi;
    }

    public Integer getAnonym() {
        return anonym;
    }

    public void setAnonym(Integer anonym) {
        this.anonym = anonym;
    }

    public String getNamaUserCivitas() {
        return namaUserCivitas;
    }

    public void setNamaUserCivitas(String namaUserCivitas) {
        this.namaUserCivitas = namaUserCivitas;
    }

    public String getNamaStatusAspirasi() {
        return namaStatusAspirasi;
    }

    public void setNamaStatusAspirasi(String namaStatusAspirasi) {
        this.namaStatusAspirasi = namaStatusAspirasi;
    }

    public String getNamaBiroUnit() {
        return namaBiroUnit;
    }

    public void setNamaBiroUnit(String namaBiroUnit) {
        this.namaBiroUnit = namaBiroUnit;
    }

    public byte[] getFotoAspirasiByteArray() { return fotoAspirasiByteArray; }

    public void setFotoAspirasiByteArray(byte[] fotoAspirasiByteArray) { this.fotoAspirasiByteArray = fotoAspirasiByteArray; }

    public Integer getIdBiroUnit() {
        return idBiroUnit;
    }

    public void setIdBiroUnit(Integer idBiroUnit) {
        this.idBiroUnit = idBiroUnit;
    }

    public String getTglStatusAspirasi() {
        return tglStatusAspirasi;
    }

    public void setTglStatusAspirasi(String tglStatusAspirasi) {
        this.tglStatusAspirasi = tglStatusAspirasi;
    }

    public Integer getIdAspirasi() {
        return idAspirasi;
    }

    public void setIdAspirasi(Integer idAspirasi) {
        this.idAspirasi = idAspirasi;
    }

    public String getTeksAspirasi() {
        return teksAspirasi;
    }

    public void setTeksAspirasi(String teksAspirasi) {
        this.teksAspirasi = teksAspirasi;
    }

    public String getFotoAspirasi() {
        return fotoAspirasi;
    }

    public void setFotoAspirasi(String fotoAspirasi) {
        this.fotoAspirasi = fotoAspirasi;
    }

    public String getDetailFotoAspirasi() {
        return detailFotoAspirasi;
    }

    public void setDetailFotoAspirasi(String detailFotoAspirasi) {
        this.detailFotoAspirasi = detailFotoAspirasi;
    }

    public String getTglAspirasi() {
        return tglAspirasi;
    }

    public void setTglAspirasi(String tglAspirasi) {
        this.tglAspirasi = tglAspirasi;
    }

    public Integer getIdStatusAspirasi() {
        return idStatusAspirasi;
    }

    public void setIdStatusAspirasi(Integer idStatusAspirasi) {
        this.idStatusAspirasi = idStatusAspirasi;
    }
}
