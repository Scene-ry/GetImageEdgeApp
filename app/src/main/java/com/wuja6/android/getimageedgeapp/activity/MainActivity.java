package com.wuja6.android.getimageedgeapp.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

import com.wuja6.android.getimageedgeapp.R;
import com.wuja6.android.getimageedgeapp.param.CannyParam;
import com.wuja6.android.getimageedgeapp.service.*;
import com.wuja6.android.getimageedgeapp.task.BitmapWorkerTask;
import com.wuja6.android.getimageedgeapp.task.OpenCVProcessTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final String CAMERA_IMAGE_FILE = "capture.jpg";
    private static final String EDGE_IMAGE_FILE = "edge.jpg";

    private ImageView imageView;
    private SeekBar thresholdBar;

    private ImageFileService imageFileService;

    private ProgressDialog imageProcessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.main_img);
        thresholdBar = (SeekBar) findViewById(R.id.threshold_bar);

        imageFileService = ImageFileService.INSTANCE;
        imageFileService.setContext(MainActivity.this);

        imageProcessDialog = new ProgressDialog(this);
        imageProcessDialog.setTitle("Please wait");
        imageProcessDialog.setMessage("Processing image...");
        imageProcessDialog.setIndeterminate(true);
        imageProcessDialog.setCancelable(false);

        // set a initial picture for test
//		Bitmap init = BitmapFactory.decodeFile(getCaptureFile().getPath());
//		imageView.setImageBitmap(init);

        thresholdBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                generateImageEdge(seekBar.getProgress());
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            showCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFileService.getImageFile(CAMERA_IMAGE_FILE)));
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                if (data != null) {
//                    if (data.hasExtra("data")) {
//                        Bitmap thumbnail = data.getParcelableExtra("data");
//                        imageView.setImageBitmap(thumbnail);
//                    }
//                } else {
//                    Bitmap bitmap = BitmapFactory.decodeFile(imageFileService.getImagePath(CAMERA_IMAGE_FILE));
//                    imageView.setImageBitmap(bitmap);
//                }
                BitmapWorkerTask loadTask = new BitmapWorkerTask(imageView);
                loadTask.execute(imageFileService.getImagePath(CAMERA_IMAGE_FILE));
            }
        }
    }

    private void generateImageEdge(int value) {
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = imageFileService.getImageFile(CAMERA_IMAGE_FILE);
        File edgeFile = imageFileService.getImageFile(EDGE_IMAGE_FILE);

        if (!mediaFile.exists()) {
            if (imageView.getDrawable() != null) {
                imageView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                imageView.layout(0, 0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();

                if (!imageFileService.outputImage(CAMERA_IMAGE_FILE, bitmap)) {
                    return;
                }
            } else {
                return;
            }
        }

//        String output = openCVService.getEdgeByCanny(mediaFile.getPath(), edgeFile.getPath(), value, value * 3, 3);
//        Log.i(ACTIVITY_SERVICE, output);
        OpenCVProcessTask processTask = new OpenCVProcessTask(imageProcessDialog);
        processTask.execute(new CannyParam(mediaFile.getPath(), edgeFile.getPath(), value, value * 3, 3));

//        Bitmap edgeBitmap = BitmapFactory.decodeFile(edgeFile.getPath());
//        imageView.setImageBitmap(edgeBitmap);
        BitmapWorkerTask loadTask = new BitmapWorkerTask(imageView);
        loadTask.execute(edgeFile.getPath());
    }
}
