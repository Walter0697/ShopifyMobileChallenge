package com.testing.shopifychallenge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by walter on 2017-12-25.
 */

//process all the image here
public class ImageProcess {

    static Context context;

    //get the image bitmap from the url
    public static void getImageBitmap(String url, final float ratio, final ImageCallback callback)
    {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap result = scaleImage(bitmap, ratio);
                callback.onSuccess(result);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context).load(url).into(target);
    }

    //to scale the bitmap image according to the screen height
    public static Bitmap scaleImage(Bitmap pic, float ratio)
    {
        int width = pic.getWidth();
        int height = pic.getHeight();
        float ratioBitmap = (float) height / (float) width;

        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int maxHeight = (int)(screenHeight * ratio);
        int maxWidth = (int)((float) maxHeight / ratioBitmap);
        Bitmap image = Bitmap.createScaledBitmap(pic, maxWidth, maxHeight, true);

        return image;
    }
}
