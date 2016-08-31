package com.toro.objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.parse.ParseFile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class tarifBeans {

    public tarifBeans() {
    }

    public tarifBeans(String strad, byte[] byteresim, String strresimurl,
	    Integer inthazirlama, Integer intpisirme, Integer intkisi,
	    String strmalzemeler, String strhazirlanisi, String strkategori,
	    Integer intpuan) {
	setAd(strad);
	setResim(byteresim);
	setResimUrl(strresimurl);
	setHazirlama(inthazirlama);
	setPisirme(intpisirme);
	setKisi(intkisi);
	setMalzemeler(strmalzemeler);
	setHazirlanisi(strhazirlanisi);
	setKategori(strkategori);
	setPuan(intpuan);
    }

    private String ad = "";
    private byte[] resim = null;
    private String resim_url = "";

    private int hazirlama = 0;
    private int pisirme = 0;
    private int kisi = 0;

    private String malzemeler = "";
    private String hazirlanisi = "";
    private String kategori = "";
    private int puan = 0;
    private int def = 0;

    public tarifObje toObje() {
	tarifObje obj = new tarifObje();
	obj.setAd(getAd());
	if (null != getResim())
	    obj.setResim(getParseFile());
	obj.setHazirlama(getHazirlama());
	obj.setPisirme(getPisirme());
	obj.setKisi(getKisi());
	obj.setMalzemeler(getMalzemeler());
	obj.setHazirlanisi(getHazirlanisi());
	obj.setKategori(getKategori());
	obj.setPuan(getPuan());
	return obj;
    }

    public String getAd() {
	if (ad != null)
	    return ad;
	else
	    return "";
    }

    public String getAdShort() {
	if (ad != null) {
	    if (ad.length() > 18) {
		char[] ads = ad.toCharArray();
		for (int i = 18; i > 0; i--) {
		    if (ads[i] == ' ') {
			return ad.substring(0, i) + " ...";
		    }
		}
		return ad.substring(0, 18) + " ...";
	    } else
		return ad;
	} else
	    return "";
    }

    public void setAd(String str) {
	ad = str;
    }

    public ParseFile getParseFile() {
	if (resim != null) {
	    Bitmap bmp = BitmapFactory.decodeByteArray(resim, 0, resim.length);
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    byte[] image = stream.toByteArray();
	    ParseFile imagefile = new ParseFile(getAd() + ".jpg", image);
	    return imagefile;
	} else
	    return null;
    }

    public Bitmap getBitmap() {
	if (resim != null)
	    return BitmapFactory.decodeByteArray(resim, 0, resim.length);
	else {
	    if (getResimUrl().length() > 0) {
		try {
		    InputStream bitmap = globalObject.getContext().getAssets()
			    .open("images/" + getResimUrl());
		    return BitmapFactory.decodeStream(bitmap);
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
	    }
	    return null;
	}
    }

    public byte[] getResim() {
	return resim;
    }

    public void setResim(byte[] byt) {
	resim = byt;
    }

    public String getResimUrl() {
	if (resim_url != null)
	    return resim_url;
	else
	    return "";
    }

    public void setResimUrl(String str) {
	resim_url = str;
    }

    public Integer getPisirme() {
	return pisirme;
    }

    public void setPisirme(Integer i) {
	pisirme = i;
    }

    public Integer getHazirlama() {
	return hazirlama;
    }

    public void setHazirlama(Integer i) {
	hazirlama = i;
    }

    public Integer getKisi() {
	return kisi;
    }

    public void setKisi(Integer i) {
	kisi = i;
    }

    public String getMalzemeler() {
	if (malzemeler != null)
	    return malzemeler;
	else
	    return "";
    }

    public void setMalzemeler(String str) {
	malzemeler = str;
    }

    public String getHazirlanisi() {
	if (hazirlanisi != null)
	    return hazirlanisi;
	else
	    return "";
    }

    public void setHazirlanisi(String str) {
	hazirlanisi = str;
    }

    public String getKategori() {
	if (kategori != null)
	    return kategori;
	else
	    return "";
    }

    public void setKategori(String str) {
	kategori = str;
    }

    public Integer getPuan() {
	return puan;
    }

    public void setPuan(Integer i) {
	puan = i;
    }

    public Integer getDefault() {
	return def;
    }

    public void setDefault(Integer i) {
	def = i;
    }

}
