package com.builtin.photoeditor;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.ColorRes;


import java.util.HashMap;
import java.util.Map;

public class SVGColorUtil {
    private static Map<ImageView, Drawable> originalDrawables = new HashMap<>();

    public static void addImageView(ImageView imageView) {
        if (!originalDrawables.containsKey(imageView)) {
            originalDrawables.put(imageView, imageView.getDrawable());
        }
    }

    public static void changeColorAndResetOthers(ImageView clickedImageView, int colorResId) {
        // Change color of clicked ImageView
        changeColorOfDrawable(clickedImageView, colorResId);

        // Reset color of all other ImageViews
        for (ImageView imageView : originalDrawables.keySet()) {
            if (imageView != clickedImageView) {
                resetColor(imageView);
            }
        }
    }
    private static void changeColorOfDrawable(ImageView imageView, int colorResId) {
        Drawable drawable = originalDrawables.get(imageView).mutate(); // Make a copy of the original drawable
        int color = imageView.getContext().getResources().getColor(colorResId); // Get the color from resource ID
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)); // Apply color filter
        imageView.setImageDrawable(drawable); // Set the modified drawable to ImageView
    }
    private static void resetColor(ImageView imageView) {
        Drawable drawable = originalDrawables.get(imageView); // Get the original drawable
        drawable.setColorFilter(null); // Remove any color filter
        imageView.setImageDrawable(drawable); // Set the original drawable to ImageView
    }

}