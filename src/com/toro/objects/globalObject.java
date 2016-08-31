package com.toro.objects;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.toro.tarifdefterim.MainActivity;
import com.toro.tarifdefterim.MainActivity.PlaceholderFragment;
import com.toro.util.DataBaseHandler;

public class globalObject {

    public globalObject() {
    }
    
    static MainActivity mainactivity;
    static Activity activity;
    static Context context;
    static DataBaseHandler dbhelper;
    static tarifBeans tarif;
    static ListView navigationView;
    static String kategori;
    static PlaceholderFragment fragment;
    public static String APP_ID = "133869903637188";
    
    public static void setFragment(PlaceholderFragment frg) {
	fragment = frg;
    } 
    
    public static PlaceholderFragment getFragment() {
	return fragment;
    } 
    
    public static void setMainActivity(MainActivity act) {
	mainactivity = act;
    } 
    
    public static MainActivity getMainActivity() {
	return mainactivity;
    } 
    
    public static void setCurrentActivity(Activity act) {
	activity = act;
    } 
    
    public static Activity getCurrentActivity() {
	return activity;
    } 
    
    public static void setContext(Context cnt) {
	context = cnt;
    } 
    
    public static Context getContext() {
	return context;
    }
    
    public static void setDbHandler(DataBaseHandler db) {
	dbhelper = db;
    } 
    
    public static DataBaseHandler getDbHandler() {
	return dbhelper;
    }
    
    public static void setTarif(tarifBeans trf) {
	tarif = trf;
    } 
    
    public static tarifBeans getTarif() {
	return tarif;
    }
    
    public static void setNavigationView(ListView list) {
	navigationView = list;
    } 
    
    public static ListView getNavigationView() {
	return navigationView;
    }
    
    public static void setKategori(String ktg) {
	kategori = ktg;
    } 
    
    public static String getKategori() {
	return kategori;
    }
    

}
