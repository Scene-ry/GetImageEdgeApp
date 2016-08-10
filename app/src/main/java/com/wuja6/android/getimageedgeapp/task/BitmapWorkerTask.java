package com.wuja6.android.getimageedgeapp.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Scenery on 2016/8/9.
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<Context> contextReference;
    private WeakReference<ImageView> imageViewReference;

    public BitmapWorkerTask(Context context, ImageView view) {
        contextReference = new WeakReference<Context>(context);
        imageViewReference = new WeakReference<ImageView>(view);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String data = params[0];
        int q = getPreQuality();
        return decodeSampledBitmapFromResource(data, q, q);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
//            if (width > height) {
//                inSampleSize = Math.round((float)height / (float)reqHeight);
//            } else {
//                inSampleSize = Math.round((float)width / (float)reqWidth);
//            }

            final int halfHeight = height >> 1;
            final int halfWidth = width >> 1;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize <<= 1;
            }
        }
        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int getPreQuality() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(contextReference.get());
        return preferences.getInt("image-quality", 100);
    }
}
