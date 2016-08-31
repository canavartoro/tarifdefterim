package com.toro.tarifdefterim;

import com.toro.interfaces.torolistener;
import com.toro.objects.globalObject;
import com.toro.objects.tarifBeans;
import com.toro.screens.Screens;
import com.toro.util.DbBitmapUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class activity_yenitarif extends Activity implements OnClickListener {

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
		    Cursor cursor = managedQuery(selectedImageUri, projection,
			    null, null, null);
		    int column_index = cursor
			    .getColumnIndexOrThrow(MediaColumns.DATA);
		    cursor.moveToFirst();
		    String selectedImagePath = cursor.getString(column_index);
		    Bitmap bm;
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeFile(selectedImagePath, options);
		    final int REQUIRED_SIZE = 200;
		    int scale = 1;
		    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
			    && options.outHeight / scale / 2 >= REQUIRED_SIZE)
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

    ImageView mImageView;
    Spinner spinKategori;
    String kategori = "Ana Yemekler"; // "Kategorilenmemiþ";
    Integer puan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_yenitarif);

	mImageView = (ImageView) findViewById(R.id.img_tarifresim);

	spinKategori = (Spinner) findViewById(R.id.spinner1);
	String[] items = globalObject.getDbHandler().getAllKategori();

	spinKategori.setAdapter(new ArrayAdapter<String>(
		getApplicationContext(), R.layout.row_view_spinner,
		R.id.text_alan_row_view_spinner, items));
	spinKategori.setFocusable(true);
	spinKategori.setFocusableInTouchMode(true);
	spinKategori
		.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> arg0, View arg1,
			    int pos, long arg3) {
			kategori = (String) spinKategori.getAdapter().getItem(
				pos);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> arg0) {
		    }
		});
	if (null != globalObject.getKategori()) {
	    for (int l = 0; l < items.length; l++) {
		if (globalObject.getKategori().equalsIgnoreCase(items[l])) {
		    spinKategori.setSelection(l);
		    kategori = globalObject.getKategori();
		    break;
		}
	    }
	}

	((ImageButton) findViewById(R.id.img_kaydet))
		.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
			kaydet();
		    }
		});

	((ImageButton) findViewById(R.id.btn_kamera))
		.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {

			Screens.selectImage(new torolistener() {
			    @Override
			    public void onResult(Object sender, Object o) {
				String item = (String) o;
				if (item.equals("Resim Çek")) {
				    Intent intent = new Intent(
					    MediaStore.ACTION_IMAGE_CAPTURE);
				    startActivityForResult(intent,
					    Screens.REQUEST_IMAGE_CAPTURE);
				} else if (item.equals("Galeriden Seç")) {
				    Intent intent = new Intent(
					    Intent.ACTION_PICK,
					    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				    intent.setType("image/*");
				    startActivityForResult(
					    Intent.createChooser(intent,
						    "Resim Seç"),
					    Screens.SELECT_FILE);
				}
				((AlertDialog) sender).dismiss();
			    }
			});
		    }
		});
	globalObject.setCurrentActivity(this);
    }

    private void kaydet() {
	globalObject.setCurrentActivity(this);

	tarifBeans trf = new tarifBeans();

	EditText editAd = (EditText) findViewById(R.id.edit_tarifad);
	EditText editHazirlama = (EditText) findViewById(R.id.edit_hazirlama);
	EditText editPisirme = (EditText) findViewById(R.id.edit_pisirme);
	EditText editKisi = (EditText) findViewById(R.id.edit_kisi);
	EditText editMalzemeler = (EditText) findViewById(R.id.edit_malzemeler);
	EditText editHazirlanisi = (EditText) findViewById(R.id.edit_hazirlanisi);

	trf.setDefault(0);
	trf.setAd(editAd.getText().toString());
	trf.setKategori(kategori);
	trf.setHazirlanisi(editHazirlanisi.getText().toString());
	trf.setMalzemeler(editMalzemeler.getText().toString());
	if (editHazirlama.getText().length() > 0)
	    trf.setHazirlama(Integer.parseInt(editHazirlama.getText()
		    .toString()));
	if (editPisirme.getText().length() > 0)
	    trf.setPisirme(Integer.parseInt(editPisirme.getText().toString()));
	if (editKisi.getText().length() > 0)
	    trf.setKisi(Integer.parseInt(editKisi.getText().toString()));
	if (mImageView.getDrawable() != null) {
	    Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable())
		    .getBitmap();
	    if (bitmap != null)
		trf.setResim(DbBitmapUtility.getBytes(bitmap));

	}
	trf.setPuan(puan);
	globalObject.getDbHandler().addTarif(trf);
	finish();
    }

    @Override
    public void onClick(View v) {
	puan = 1;
	if (v.getTag() != null) {
	    puan = Integer.parseInt(v.getTag().toString());
	}
	if (puan > 0)
	    ((ImageView) findViewById(R.id.img_1puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_1puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 1)
	    ((ImageView) findViewById(R.id.img_2puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_2puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 2)
	    ((ImageView) findViewById(R.id.img_3puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_3puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 3)
	    ((ImageView) findViewById(R.id.img_4puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_4puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 4)
	    ((ImageView) findViewById(R.id.img_5puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_5puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 5)
	    ((ImageView) findViewById(R.id.img_6puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_6puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 6)
	    ((ImageView) findViewById(R.id.img_7puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_7puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 7)
	    ((ImageView) findViewById(R.id.img_8puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_8puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 7)
	    ((ImageView) findViewById(R.id.img_8puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_8puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 8)
	    ((ImageView) findViewById(R.id.img_9puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_9puan))
		    .setImageResource(R.drawable.yildiz_bos);
	if (puan > 9)
	    ((ImageView) findViewById(R.id.img_10puan))
		    .setImageResource(R.drawable.yildiz);
	else
	    ((ImageView) findViewById(R.id.img_10puan))
		    .setImageResource(R.drawable.yildiz_bos);
    }

    public activity_yenitarif() {
    }

}
