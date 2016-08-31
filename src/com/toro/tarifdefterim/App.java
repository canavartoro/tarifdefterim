package com.toro.tarifdefterim;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseObject;
import com.toro.objects.tarifObje;

import android.app.Application;

public class App extends Application {
    
    public static final String APPLICATION_ID = "HXTwmhNjWNmC2Wi8M5tF8VMu0OrHHGLAsr6P9AYR";
    public static final String CLIENT_KEY = "Pw1Eh6UkWX2jjsUGFEXJnWaz7CWYrwbO0IQEMV5d";

    @Override
    public void onCreate() {
	super.onCreate();
	
	FacebookSdk.sdkInitialize(getApplicationContext());

	ParseObject.registerSubclass(tarifObje.class);
	//ParseObject.registerSubclass(kullaniciObje.class);
	Parse.initialize(this, App.APPLICATION_ID, App.CLIENT_KEY);
    }

    public App() {
    }

}
