package com.builtin.photoeditor;

import android.app.Activity;
import android.content.Intent;

public class ImageSelectorUtil {

    private static final int PICK_IMAGE_REQUEST = 1;

    public static void openImageSelector(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
