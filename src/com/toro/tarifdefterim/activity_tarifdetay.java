package com.toro.tarifdefterim;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.toro.interfaces.torolistener;
import com.toro.objects.globalObject;
import com.toro.screens.Screens;
import com.toro.util.DbBitmapUtility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class activity_tarifdetay extends Activity implements android.view.View.OnClickListener {

	TextView text_tarifad;
	ImageView mImageView;
	int puan = 1;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		globalObject.setCurrentActivity(this);
		if (resultCode == RESULT_OK) {
			if (requestCode == Screens.REQUEST_IMAGE_CAPTURE) {
				Bundle extras = data.getExtras();
				Bitmap imageBitmap = (Bitmap) extras.get("data");
				mImageView.setImageBitmap(imageBitmap);
			} else if (requestCode == Screens.SELECT_FILE) {
				try {
					Uri selectedImageUri = data.getData();
					String[] projection = { MediaColumns.DATA };
					Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
					int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
					cursor.moveToFirst();
					String selectedImagePath = cursor.getString(column_index);
					Bitmap bm;
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(selectedImagePath, options);
					final int REQUIRED_SIZE = 200;
					int scale = 1;
					while (options.outWidth / scale / 2 >= REQUIRED_SIZE && options.outHeight / scale / 2 >= REQUIRED_SIZE)
						scale *= 2;
					options.inSampleSize = scale;
					options.inJustDecodeBounds = false;
					bm = BitmapFactory.decodeFile(selectedImagePath, options);
					mImageView.setImageBitmap(bm);
				} catch (Exception e) {
					Screens.showAlert(e.getMessage());
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tarifdetay);

		((ImageButton) findViewById(R.id.btn_share)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					Bitmap b = null;

					if (globalObject.getTarif().getBitmap() != null)
						b = globalObject.getTarif().getBitmap();
					else
						b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

					File file = new File(globalObject.getContext().getCacheDir(), "yemek.png");
					FileOutputStream fOut;
					fOut = new FileOutputStream(file);
					b.compress(CompressFormat.PNG, 100, fOut);
					fOut.flush();
					fOut.close();
					file.setReadable(true, false);
					final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					intent.putExtra(Intent.EXTRA_SUBJECT, text_tarifad.getText().toString());
					intent.putExtra(Intent.EXTRA_TEXT, Uri.parse("https://play.google.com/store/apps/details?id=com.toro.tarifdefterim"));//Html.fromHtml("<p>Hazirlanisi.</p>"));
					intent.setType("image/jpeg");
					startActivity(intent);

				} catch (Exception e) {
					e.printStackTrace();
				}

				/*
				 * Intent shareIntent = new Intent();
				 * shareIntent.setAction(Intent.ACTION_SEND);
				 * shareIntent.putExtra(Intent.EXTRA_TEXT,
				 * text_tarifad.getText().toString());
				 * shareIntent.putExtra
				 * (Intent.EXTRA_STREAM,Uri.fromFile(new
				 * File(filePath))); //optional//use this when you want
				 * to send an image shareIntent.setType("image/jpeg");
				 * shareIntent.addFlags
				 * (Intent.FLAG_GRANT_READ_URI_PERMISSION);
				 * startActivity(Intent.createChooser(shareIntent,
				 * "send"));
				 */

				/*
				 * SharePhoto photo = new SharePhoto.Builder()
				 * .setBitmap(image) .setCaption(
				 * "Give me my codez or I will ... you know, do that thing you don't like!"
				 * ) .build();
				 * 
				 * SharePhotoContent content = new
				 * SharePhotoContent.Builder() .addPhoto(photo).build();
				 * 
				 * ShareApi.share(content, null);
				 */
				// ShareDialog.show(activity_tarifdetay.this, content);

				/*
				 * ShareOpenGraphObject object = new
				 * ShareOpenGraphObject.Builder() .putString("og:type",
				 * "books.book") .putString("og:title",
				 * "A Game of Thrones") .putString( "og:description",
				 * "In the frozen wastes to the north of Winterfell, sinister and supernatural forces are mustering."
				 * ) .putString("books:isbn", "0-553-57340-3") .build();
				 * 
				 * SharePhoto photo = new SharePhoto.Builder()
				 * .setBitmap(image).setUserGenerated(true)
				 * .setUserGenerated(true).build();
				 * 
				 * ShareOpenGraphAction action = new
				 * ShareOpenGraphAction.Builder()
				 * .setActionType("books.reads") .putObject("book",
				 * object) .putPhoto("image", photo).build();
				 * ShareOpenGraphContent content = new
				 * ShareOpenGraphContent.Builder()
				 * .setPreviewPropertyName("book")
				 * .setAction(action).build();
				 * ShareDialog.show(activity_tarifdetay.this, content);
				 */

			}
		});

		mImageView = (ImageView) findViewById(R.id.img_tarifresim);
		((TextView) findViewById(R.id.text_kategori)).setText(globalObject.getTarif().getKategori());
		text_tarifad = ((TextView) findViewById(R.id.text_tarifad));
		text_tarifad.setText(globalObject.getTarif().getAd());
		((TextView) findViewById(R.id.text_hazirlama)).setText(String.valueOf(globalObject.getTarif().getHazirlama()));
		((TextView) findViewById(R.id.text_pisirme)).setText(String.valueOf(globalObject.getTarif().getPisirme()));
		((TextView) findViewById(R.id.text_malzemeler)).setText(globalObject.getTarif().getMalzemeler());
		((TextView) findViewById(R.id.text_hazirlanisi)).setText(globalObject.getTarif().getHazirlanisi());
		((TextView) findViewById(R.id.text_kisi)).setText(String.valueOf(globalObject.getTarif().getKisi()));
		if (globalObject.getTarif().getBitmap() != null)
			mImageView.setImageBitmap(globalObject.getTarif().getBitmap());
		puan = globalObject.getTarif().getPuan();
		puanView();

		((ImageButton) findViewById(R.id.btn_sil)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Screens.showAlert(globalObject.getTarif().getAd() + " silinsin mi?", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						globalObject.getDbHandler().deleteTarif(globalObject.getTarif());
						globalObject.setTarif(null);
						finish();
					}
				}, true);
			}
		});

		((ImageButton) findViewById(R.id.btn_kaydet)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mImageView.getDrawable() != null) {
					Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
					if (bitmap != null)
						globalObject.getTarif().setResim(DbBitmapUtility.getBytes(bitmap));

				}
				globalObject.getTarif().setPuan(puan);
				globalObject.getDbHandler().updateTarif(globalObject.getTarif());
				finish();
			}
		});

		((ImageButton) findViewById(R.id.btn_kamera)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Screens.selectImage(new torolistener() {
					@Override
					public void onResult(Object sender, Object o) {
						String item = (String) o;
						if (item.equals("Resim Çek")) {
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, Screens.REQUEST_IMAGE_CAPTURE);
						} else if (item.equals("Galeriden Seç")) {
							Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							intent.setType("image/*");
							startActivityForResult(Intent.createChooser(intent, "Resim Seç"), Screens.SELECT_FILE);
						}
						((AlertDialog) sender).dismiss();
					}
				});
			}
		});

		globalObject.setCurrentActivity(this);

	}

	public void onCikis(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getTag() != null) {
			puan = Integer.parseInt(v.getTag().toString());
		}
		puanView();
	}

	private void puanView() {
		if (puan > 0)
			((ImageButton) findViewById(R.id.img_1puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_1puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 1)
			((ImageButton) findViewById(R.id.img_2puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_2puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 2)
			((ImageButton) findViewById(R.id.img_3puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_3puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 3)
			((ImageButton) findViewById(R.id.img_4puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_4puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 4)
			((ImageButton) findViewById(R.id.img_5puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_5puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 5)
			((ImageButton) findViewById(R.id.img_6puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_6puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 6)
			((ImageButton) findViewById(R.id.img_7puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_7puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 7)
			((ImageButton) findViewById(R.id.img_8puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_8puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 7)
			((ImageButton) findViewById(R.id.img_8puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_8puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 8)
			((ImageButton) findViewById(R.id.img_9puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_9puan)).setImageResource(R.drawable.yildiz_bos);
		if (puan > 9)
			((ImageButton) findViewById(R.id.img_10puan)).setImageResource(R.drawable.yildiz);
		else
			((ImageButton) findViewById(R.id.img_10puan)).setImageResource(R.drawable.yildiz_bos);
	}

	public activity_tarifdetay() {
	}

}
