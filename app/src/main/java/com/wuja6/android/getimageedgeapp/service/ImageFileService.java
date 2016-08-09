package com.wuja6.android.getimageedgeapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wuja6.android.getimageedgeapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public enum ImageFileService {

    INSTANCE;

    private String imageFolderPath = null;
    private Context context;

    private ImageFileService() {

    }

    private String getImageFolderPath() {
        File mediaStorageDir = null;
        try {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    context.getString(R.string.app_name));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

//		captureFilePath = mediaStorageDir.getPath() + File.separator + CAMERA_IMAGE_FILE;
//		edgeFilePath = mediaStorageDir.getPath() + File.separator + EDGE_IMAGE_FILE;

        return mediaStorageDir.getPath();
    }

    public void setContext(Context context) {
        this.context = context;
        imageFolderPath = getImageFolderPath();
    }

    public File getImageFile(String filename) {
        if (filename != null && imageFolderPath != null) {
            return new File(imageFolderPath + File.separator + filename);
        }
        return null;
    }

    public String getImagePath(String filename) {
        if (filename != null && imageFolderPath != null) {
            return imageFolderPath + File.separator + filename;
        }
        return null;
    }

    public boolean outputImage(String filename, Bitmap bitmap) {
        File imageFile = null;
        FileOutputStream fos = null;

        try {
            imageFile = new File(imageFolderPath + File.separator + filename);
            if (imageFile.exists()) {
                return false;
            }

            imageFile.createNewFile();

            fos = new FileOutputStream(imageFile);
            bitmap.compress(CompressFormat.JPEG, 95, fos);

            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return false;
    }
}
