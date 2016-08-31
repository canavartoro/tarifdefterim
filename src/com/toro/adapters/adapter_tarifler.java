package com.toro.adapters;

import java.util.ArrayList;

import com.toro.objects.tarifBeans;
import com.toro.screens.Screens;
import com.toro.tarifdefterim.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class adapter_tarifler extends BaseAdapter {

    private LayoutInflater mInflater;
    private int selectedPos = -1;
    Context context;
    ArrayList<tarifBeans> searchArrayList = new ArrayList<tarifBeans>();

    public adapter_tarifler(Context cont, ArrayList<tarifBeans> data) {
	context = cont;
	searchArrayList = data;
	mInflater = LayoutInflater.from(context);
    }

    public void setSelectedPosition(int pos) {
	selectedPos = pos;
	// inform the view of this change
	notifyDataSetChanged();
    }

    public int getSelectedPosition() {
	return selectedPos;
    }

    public int getCount() {
	if (searchArrayList == null)
	    return 0;
	return searchArrayList.size();
    }

    public Object getItem(int position) {
	return searchArrayList.get(position);
    }

    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;
	if (convertView == null) {
	    LayoutInflater vi = (LayoutInflater) ((Activity) context)
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = vi.inflate(R.layout.row_tarifler, null);
	    // convertView = mInflater.inflate(R.layout.custom_row_view, null);
	    holder = new ViewHolder();
	    holder.img_tarifresim = (ImageView) convertView
		    .findViewById(R.id.img_tarifresim);
	    holder.text_tarifad = (TextView) convertView
		    .findViewById(R.id.text_tarifad);
	    holder.text_hazirlama = (TextView) convertView
		    .findViewById(R.id.text_hazirlama);
	    holder.text_pisirme = (TextView) convertView
		    .findViewById(R.id.text_pisirme);
	    holder.layout = (RelativeLayout) convertView
		    .findViewById(R.id.layout_tariflerrow);
	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}
	holder.text_tarifad.setText(searchArrayList.get(position).getAdShort());
	holder.text_hazirlama.setText(String.valueOf(searchArrayList.get(
		position).getHazirlama()));
	holder.text_pisirme.setText(String.valueOf(searchArrayList
		.get(position).getPisirme()));
	if (searchArrayList.get(position).getBitmap() != null)
	    holder.img_tarifresim.setImageBitmap(searchArrayList.get(position)
		    .getBitmap());

	if (selectedPos == position) {
	    holder.text_tarifad.setBackgroundColor(Screens.GetSelectedColor());
	    holder.text_tarifad.setTextColor(Screens.GetWhiteColor());
	    holder.layout.setBackgroundColor(Screens.GetSelectedColor());
	} else {
	    holder.text_tarifad
		    .setBackgroundColor(Screens.GetWhiteColor());
	    holder.text_tarifad.setTextColor(Color.parseColor("#504D4D"));
	    holder.layout.setBackgroundColor(Screens.GetWhiteColor());
	}

	return convertView;
    }

    static class ViewHolder {
	ImageView img_tarifresim;
	TextView text_tarifad;
	TextView text_hazirlama;
	TextView text_pisirme;
	RelativeLayout layout;
    }

}

/*
 * private static ArrayOfSecimObje searchArrayList;
 */
