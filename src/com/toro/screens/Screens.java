package com.toro.screens;

import com.toro.interfaces.torolistener;
import com.toro.objects.globalObject;
import com.toro.tarifdefterim.R;
import com.toro.tarifdefterim.activity_tarifdetay;
import com.toro.util.DataBaseHandler;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.widget.EditText;
import android.widget.Toast;

public class Screens {

	private static ProgressDialog progressDialog;
	private static Integer SELECTED_COLOR = -1;
	private static Integer BACKGROUND_COLOR = -1;
	private static Integer RED_COLOR = -1;
	private static Integer BLUE_COLOR = -1;

	public static final int REQUEST_IMAGE_CAPTURE = 1;
	public static final int SELECT_FILE = 2;

	public Screens() {
	}

	public static Integer GetSelectedColor() {
		if (SELECTED_COLOR == -1) {
			SELECTED_COLOR = Color.parseColor("#C6361D");
		}
		return SELECTED_COLOR;
	}

	public static Integer GetBackgroundColor() {
		if (BACKGROUND_COLOR == -1) {
			BACKGROUND_COLOR = Color.parseColor("#FAF9FC");
		}
		return BACKGROUND_COLOR;
	}

	public static Integer GetRedColor() {
		if (RED_COLOR == -1) {
			RED_COLOR = Color.parseColor("#FB8989");
		}
		return RED_COLOR;
	}

	public static Integer GetBlueColor() {
		if (BLUE_COLOR == -1) {
			BLUE_COLOR = Color.parseColor("#04D8EB");
		}
		return BLUE_COLOR;
	}

	public static Integer GetWhiteColor() {
		if (BLUE_COLOR == -1) {
			BLUE_COLOR = Color.parseColor("#FFFEFE");
		}
		return BLUE_COLOR;
	}

	public static void showText(String text) {

		try {
			if (globalObject.getCurrentActivity() == null)
				return;

			Toast pieceToast = Toast.makeText(
					globalObject.getCurrentActivity(), text, Toast.LENGTH_LONG);

			pieceToast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void showAlert(String text, OnClickListener listener,
			Boolean iptalButton) {

		try {
			AlertDialog dialog = new AlertDialog.Builder(
					globalObject.getCurrentActivity()).create();
			dialog.setTitle("Dikkat");
			dialog.setIcon(R.drawable.ic_launcher);
			dialog.setMessage(text);
			if (iptalButton) {
				dialog.setButton2("Ýptal",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						});
			}
			dialog.setButton("Tamam", listener);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showAlert(String text) {

		try {
			AlertDialog dialog = new AlertDialog.Builder(
					globalObject.getCurrentActivity()).create();
			dialog.setTitle("Dikkat");
			dialog.setIcon(R.drawable.ic_launcher);
			dialog.setMessage(text);
			dialog.setButton2("Ýptal", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void kategoriEkle(String text, String title,
			torolistener listener) {

		try {
			final torolistener listening = listener;
			AlertDialog dialog = new AlertDialog.Builder(
					globalObject.getCurrentActivity()).create();
			dialog.setTitle(title);
			dialog.setIcon(R.drawable.ic_launcher);
			dialog.setMessage(text);
			final EditText input = new EditText(
					globalObject.getCurrentActivity());
			dialog.setView(input);
			dialog.setButton2("Ýptal", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			});
			dialog.setButton("Tamam", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String value = input.getText().toString();
					if (value != null && value.length() > 0) {
						globalObject.getDbHandler().addKategori(value);
						if (listening != null)
							listening.onResult(null, null);
					}
					dialog.dismiss();
				}
			});
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void selectImage(torolistener listener) {
		final torolistener flistener = listener;
		final CharSequence[] items = { "Resim Çek", "Galeriden Seç", "Ýptal" };
		AlertDialog.Builder builder = new AlertDialog.Builder(
				globalObject.getCurrentActivity());
		builder.setTitle("Resim Ekle!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Ýptal")) {
					dialog.dismiss();
				} else {
					if (flistener != null)
						flistener.onResult(dialog, items[item]);
				}
			}
		});
		builder.show();
	}

	@SuppressLint("NewApi")
	public static void sett() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				globalObject.getContext()).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Deneme").setContentText("Deneme mesaji");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(globalObject.getCurrentActivity(),
				globalObject.getCurrentActivity().getClass());

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(globalObject
				.getCurrentActivity());
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(globalObject.getCurrentActivity()
				.getClass());
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) globalObject
				.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		int mId = 0;
		mNotificationManager.notify(mId, mBuilder.build());
	}
}
