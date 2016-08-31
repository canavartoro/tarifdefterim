package com.toro.task;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.toro.objects.globalObject;
import com.toro.objects.tarifBeans;
import com.toro.objects.tarifObje;
import com.toro.tarifdefterim.activity_hakkinda;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.WindowManager.BadTokenException;

public class getTask extends AsyncTask<String, Void, Void> {

    private static ProgressDialog dialog;

    public getTask() {
	super();
	dialog = new ProgressDialog(globalObject.getCurrentActivity());
	dialog.setTitle("Bekleyin...");
	dialog.setMessage("Bilgiler sunucudan alýnýyor...");
    }

    @Override
    protected void onPostExecute(Void result) {
	if (dialog.isShowing()) {
	    dialog.dismiss();
	}
	super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
	try {
	    dialog.show();
	} catch (BadTokenException be) {
	    be.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
	super.onProgressUpdate(values);
    }

    @Override
    protected Void doInBackground(String... params) {
	final String userId = params[0];
	try {
	    ParseQuery<tarifObje> query = tarifObje.getQuery();
	    query.whereEqualTo("user", userId);
	    query.findInBackground(new FindCallback<tarifObje>() {
		@Override
		public void done(List<tarifObje> list,
			ParseException parseexception) {
		    if (null == parseexception) {

			globalObject.getDbHandler().deleteTarifNotDefault();

			if (list != null && list.size() > 0) {
			    for (tarifObje obj : list) {
				tarifBeans tarif = obj.toBean();
				globalObject.getDbHandler().addTarif(tarif);
			    }
			}
		    } else {
			parseexception.printStackTrace();
		    }
		}
	    });

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return null;
    }

}
