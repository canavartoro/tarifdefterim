package com.toro.tarifdefterim;

import java.util.ArrayList;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;
import com.toro.objects.globalObject;
import com.toro.objects.tarifBeans;
import com.toro.screens.Screens;
import com.toro.task.getTask;
import com.toro.task.sendTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;

/*
 Facebook - "com.facebook.katana"
 Twitter - "com.twitter.android"
 Instagram - "com.instagram.android"
 * */

//6OU1iyM9wAtiaV7vhjQpx0P7a5c=
public class activity_hakkinda extends Activity {

    /* http://www.mobilhanem.com/android-facebook-login-ve-facebook-share/ */
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ShareDialog shareDialog;
    LoginManager manager;
    public static ArrayList<tarifBeans> data;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_hakkinda);

	WebView wv;
	wv = (WebView) findViewById(R.id.webView1);
	wv.loadUrl("file:///android_asset/help.html");

	callbackManager = CallbackManager.Factory.create();
	shareDialog = new ShareDialog(this);
	shareDialog.registerCallback(callbackManager,
		new FacebookCallback<Sharer.Result>() {
		    @Override
		    public void onSuccess(Result result) {
		    }

		    @Override
		    public void onCancel() {
		    }

		    @Override
		    public void onError(FacebookException error) {
			error.printStackTrace();
		    }
		});
	LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
	loginButton.registerCallback(callbackManager,
		new FacebookCallback<LoginResult>() {
		    @Override
		    public void onSuccess(LoginResult result) {
			/*
			 * if (result != null && result.getAccessToken() !=
			 * null) Screens.showAlert(result.getAccessToken()
			 * .getUserId());
			 */
		    }

		    @Override
		    public void onError(FacebookException error) {
			error.printStackTrace();
		    }

		    @Override
		    public void onCancel() {
			Screens.showText("Ýptal edildi.");
		    }
		});

	accessTokenTracker = new AccessTokenTracker() {
	    @Override
	    protected void onCurrentAccessTokenChanged(
		    AccessToken oldAccessToken, AccessToken currentAccessToken) {
		accessToken = currentAccessToken;
	    }
	};
	accessToken = AccessToken.getCurrentAccessToken();

	// face sonu
	((ImageButton) findViewById(R.id.btn_upload))
		.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			if (accessToken == null) {
			    Screens.showAlert("Facebook ile login olunmadý.");
			    return;
			}

			activity_hakkinda.data = globalObject.getDbHandler()
				.getAllTarifler("", true);

			if (activity_hakkinda.data != null
				&& activity_hakkinda.data.size() > 0) {
			    new sendTask().execute(new String[] { accessToken
				    .getUserId() });
			} else {
			    Screens.showAlert("Sadece kendi tarifleriniz sunucuya gönderilir. Hiç tarif girmediniz.");
			    return;
			}

		    }
		});

	((ImageButton) findViewById(R.id.btn_download))
		.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			if (accessToken == null) {
			    Screens.showAlert("Facebook ile login olunmadý.");
			    return;
			}

			new getTask().execute(new String[] { accessToken
				.getUserId() });

		    }
		});

	((ImageButton) findViewById(R.id.btn_face))
		.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	
		    	Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

			if (ShareDialog.canShow(ShareLinkContent.class)) {

			    ShareLinkContent linkContent = new ShareLinkContent.Builder()
				    .setContentTitle("Tarif Defterim")
				    .setContentDescription(
					    "Android için kendi tariflerinizi ekleyip çýkarabileceðiniz. Girdiðiniz kayýtlarý sunucuda yedekleyebileceðiniz bir uygulama.")
					    .setImageUrl(Uri.parse("http://svn2.barset.com.tr:6060/park/ic_launcher.png"))
				    .setContentUrl(
					    Uri.parse("https://developers.facebook.com/apps/133869903637188/dashboard/"))
				    .build();

			    shareDialog.show(linkContent);
			}

		    }
		});

	globalObject.setCurrentActivity(this);

    }

    public activity_hakkinda() {
    }

    /*
     * public void RequestData(){ GraphRequest request =
     * GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new
     * GraphRequest.GraphJSONObjectCallback() {
     * 
     * @Override public void onCompleted(JSONObject object, GraphResponse
     * response) { JSONObject json = response.getJSONObject(); try { if(json !=
     * null){ String text =
     * "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "
     * +json.getString
     * ("email")+"<br><br><b>Profile link :</b> "+json.getString("link") + "+" +
     * json.getString("id"); }
     * 
     * } catch (JSONException e) { e.printStackTrace(); }
     * 
     * } }); Bundle parameters = new Bundle(); parameters.putString("fields",
     * "id,name,link,email,picture"); request.setParameters(parameters);
     * request.executeAsync(); }
     */

}
