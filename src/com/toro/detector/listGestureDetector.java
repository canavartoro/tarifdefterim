package com.toro.detector;

import com.toro.interfaces.ChangePageListener;
import com.toro.interfaces.DoubleClickListener;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public class listGestureDetector extends SimpleOnGestureListener {

    @Override
    public boolean onDoubleTap(MotionEvent e) {
	Log.d("Double_Tap", "Yes, Clicked" + e.toString());

	if (doubleclickListener != null)
	    doubleclickListener.onDoubleClick(this, e);

	// Toast.makeText(context, "Cift kilik", Toast.LENGTH_SHORT).show();
	return super.onDoubleTap(e);
    }

    private DoubleClickListener doubleclickListener = null;
    private ChangePageListener changePageListener = null;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 350;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private Context context;
    int pageCount = 0;
    int counter = 0;

    public listGestureDetector(Context activity, int max) {
	context = activity;
	pageCount = max;
    }

    public ChangePageListener getChangePageListener() {
	return changePageListener;
    }

    public void setChangePageListener(ChangePageListener uListener) {
	this.changePageListener = uListener;
    }

    public DoubleClickListener getDoubleClickListener() {
	return doubleclickListener;
    }

    public void setDoubleClickListener(DoubleClickListener uListener) {
	this.doubleclickListener = uListener;
    }

    public void setCurrentPage(int page) {
	counter = page;
    }

    public int getCurrentPage() {
	return counter;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
	    float velocityY) {
	try {
	    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
		return false;

	    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
		    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		counter++;
		if (counter > pageCount - 1)
		    counter = 0;
		if (changePageListener != null)
		    changePageListener.onChangePage(this, counter);
		// touchView.setImageResource(array[counter]);
		// touchView.setScaleType(ScaleType.FIT_XY);
	    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
		    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
		counter--;
		if (counter < 0)
		    counter = pageCount;
		if (changePageListener != null)
		    changePageListener.onChangePage(this, counter);
		// touchView.setImageResource(array[counter]);
		// touchView.setScaleType(ScaleType.FIT_XY);
	    }

	} catch (Exception e) {
	    Toast.makeText(context, "Hata : " + e.toString(),
		    Toast.LENGTH_SHORT).show();
	}
	return super.onFling(e1, e2, velocityX, velocityY);
    }

}
