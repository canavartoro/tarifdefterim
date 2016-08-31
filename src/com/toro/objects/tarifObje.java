package com.toro.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("tarifler")
public class tarifObje extends ParseObject {

    public tarifObje() {
	super();
    }

    public tarifObje(String strad, byte[] byteresim, Integer inthazirlama,
	    Integer intpisirme, Integer intkisi, String strmalzemeler,
	    String strhazirlanisi, String strkategori, Integer intpuan) {
	setAd(strad);
	// setResim(byteresim);
	setHazirlama(inthazirlama);
	setPisirme(intpisirme);
	setKisi(intkisi);
	setMalzemeler(strmalzemeler);
	setHazirlanisi(strhazirlanisi);
	setKategori(strkategori);
	setPuan(intpuan);
    }

    private Bitmap bitmap;

    public tarifBeans toBean() {
	tarifBeans obj = new tarifBeans();
	obj.setAd(getAd());
	obj.setHazirlama(getHazirlama());
	obj.setPisirme(getPisirme());
	obj.setKisi(getKisi());
	obj.setMalzemeler(getMalzemeler());
	obj.setHazirlanisi(getHazirlanisi());
	obj.setKategori(getKategori());
	obj.setPuan(getPuan());
	obj.setDefault(0);
	if (null != getResim()) {
	    try {
		obj.setResim(getResim().getData());
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	}

	return obj;
    }

    public String getAd() {
	if (getResim() != null) {
	    getResim().getDataInBackground(new GetDataCallback() {
		@Override
		public void done(byte[] data, ParseException e) {
		    if (e == null) {
			bitmap = BitmapFactory.decodeByteArray(data, 0,
				data.length);
			if (bitmap != null) {
			    Log.e("parse dosya ok!!!!!!!", " null");
			}
		    } else {
			Log.e("parse dosya ok deðil", " null");
		    }
		}

	    });
	}
	return this.getString("ad");
    }

    public void setAd(String str) {
	this.put("ad", str);
    }

    public String getUser() {
	return this.getString("user");
    }

    public void setUser(String str) {
	this.put("user", str);
    }

    /*
     * public Bitmap getBitmap() { if (resim != null) return
     * BitmapFactory.decodeByteArray(resim, 0, resim.length); else return null;
     * }
     */
    public Bitmap getImage() {
	return bitmap;
    }

    public ParseFile getResim() {
	return this.getParseFile("resim");
    }

    public void setResim(ParseFile resim) {
	this.put("resim", resim);
    }

    public Integer getPisirme() {
	return this.getInt("pisirme");
    }

    public void setPisirme(Integer i) {
	this.put("pisirme", i);

    }

    public Integer getHazirlama() {
	return this.getInt("hazirlama");
    }

    public void setHazirlama(Integer i) {
	this.put("hazirlama", i);
    }

    public Integer getKisi() {
	return this.getInt("kisi");
    }

    public void setKisi(Integer i) {
	this.put("kisi", i);
    }

    public String getMalzemeler() {
	return this.getString("malzemeler");
    }

    public void setMalzemeler(String str) {
	this.put("malzemeler", str);
    }

    public String getHazirlanisi() {
	return this.getString("hazirlanisi");
    }

    public void setHazirlanisi(String str) {
	this.put("hazirlanisi", str);
    }

    public String getKategori() {
	return this.getString("kategori");
    }

    public void setKategori(String str) {
	this.put("kategori", str);
    }

    public Integer getPuan() {
	return this.getInt("puan");
    }

    public void setPuan(Integer i) {
	this.put("puan", i);
    }

    public static ParseQuery<tarifObje> getQuery() {
	return ParseQuery.getQuery(tarifObje.class);
    }
}
