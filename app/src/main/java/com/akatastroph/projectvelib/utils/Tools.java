package com.akatastroph.projectvelib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

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
    private static SparseArray<Map<String, BitmapDescriptor>> mBitmapDescriptorsByColor = new SparseArray<>();

    public static BitmapDescriptor makeBitmap(Context context, @ColorRes int idColor, @DrawableRes int idDrawable, String text)
    {
        Map<String, BitmapDescriptor> mBitmapDescriptorMap = mBitmapDescriptorsByColor.get(idColor);
        if (mBitmapDescriptorMap == null) {
            mBitmapDescriptorMap = new HashMap<>();
            mBitmapDescriptorsByColor.put(idColor, mBitmapDescriptorMap);
        }
        if (mBitmapDescriptorMap.containsKey(text)) {
            return mBitmapDescriptorMap.get(text);
        }
        Drawable vectorDrawable = ContextCompat.getDrawable(context, idDrawable);
        int h = vectorDrawable.getIntrinsicHeight();
        int w = vectorDrawable.getIntrinsicWidth();
        vectorDrawable.setBounds(0, 0, w, h);
        Bitmap bm = Bitmap.createBitmap(w, h, ARGB_8888);
        Canvas canvas = new Canvas(bm);
        vectorDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, idColor), PorterDuff.Mode.SRC_ATOP));
        vectorDrawable.draw(canvas);
        float scale = context.getResources().getDisplayMetrics().density;
        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(ContextCompat.getColor(context, R.color.white));
        paintText.setTextSize(14 * scale);
        Rect boundsText = new Rect();
        paintText.getTextBounds(text, 0, text.length(), boundsText);
        int x = (bm.getWidth() - boundsText.width()) / 2;
        int y = (bm.getHeight()) / 2;
        canvas.drawText(text, x, y, paintText);
        mBitmapDescriptorMap.put(text, BitmapDescriptorFactory.fromBitmap(bm));
        return mBitmapDescriptorMap.get(text);
    }

}
