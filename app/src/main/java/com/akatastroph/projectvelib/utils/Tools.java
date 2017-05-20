package com.akatastroph.projectvelib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.akatastroph.projectvelib.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Bitmap.Config.ARGB_8888;

/**
 * Created by genfinternet on 14/04/2017.
 */

public class Tools {
    private static Map<String, BitmapDescriptor> mBitmapDescriptorMap = new HashMap<>();

    public static BitmapDescriptor getBitmapDescriptor(Context context, int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, id);
        int h = vectorDrawable.getIntrinsicHeight();
        int w = vectorDrawable.getIntrinsicWidth();
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bm);
    }

    public static BitmapDescriptor makeBitmap(Context context, String text)
    {
        if (mBitmapDescriptorMap.containsKey(text)) {
            return mBitmapDescriptorMap.get(text);
        }
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_pin_map_bike);
        int h = vectorDrawable.getIntrinsicHeight();
        int w = vectorDrawable.getIntrinsicWidth();
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.draw(canvas);
        float scale = context.getResources().getDisplayMetrics().density;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(ContextCompat.getColor(context, R.color.white));
        paint.setTextSize(14 * scale);
        Rect boundsText = new Rect();
        paint.getTextBounds(text, 0, text.length(), boundsText);
        int x = (bm.getWidth() - boundsText.width()) / 2;
        int y = (bm.getHeight()) / 2;
        canvas.drawText(text, x, y, paint);
        mBitmapDescriptorMap.put(text, BitmapDescriptorFactory.fromBitmap(bm));
        return mBitmapDescriptorMap.get(text);
    }

}
