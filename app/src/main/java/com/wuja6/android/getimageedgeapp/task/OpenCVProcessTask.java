package com.wuja6.android.getimageedgeapp.task;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.wuja6.android.getimageedgeapp.param.CannyParam;

import java.lang.ref.WeakReference;

/**
 * Created by Scenery on 2016/8/9.
 */
public class OpenCVProcessTask extends AsyncTask<CannyParam, Void, String> {
    private final WeakReference<ProgressDialog> dialogReference;

    private native String getEdgeByCanny(String input, String output, double low_threshold, double high_threshold, int kernel_size);

    static {
        System.loadLibrary("OpenCVCannyJNI");
    }

    public OpenCVProcessTask(ProgressDialog dialog) {
        dialogReference = new WeakReference<ProgressDialog>(dialog);
    }

    @Override
    protected void onPreExecute() {
        dialogReference.get().show();
    }

    @Override
    protected String doInBackground(CannyParam... params) {
        CannyParam data = params[0];
        return getEdgeByCanny(data.getInput(), data.getOutput(), data.getLow_threshold(), data.getHigh_threshold(), data.getKernel_size());
    }

    @Override
    protected void onPostExecute(String s) {
        dialogReference.get().hide();
        Log.i("output-path", s);
    }
}
