package com.builtin.photoeditor;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SeekBar intensity;

    private SeekBar brightness;
    private TextView textView, textView2;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageUri;

    private Button apply;
    private int intensityValue, brightnessValue;

    // Declare a variable to store the edited image bitmap
    private Bitmap editedImageBitmap;
    // Declare a variable to keep track of the previous intensity
    private int previousIntensity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // "context" must be an Activity, Service or Application object from your app.
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        intensity = findViewById(R.id.seekBar);
        brightness = findViewById(R.id.seekBar2);
        textView = findViewById(R.id.valueTxt);
        textView2 = findViewById(R.id.valueTxt2);

        imageView = findViewById(R.id.preview);
        apply = findViewById(R.id.apply);

        ImageView magic = findViewById(R.id.magic);
        ImageView intensity = findViewById(R.id.intensity);
        ImageView brightness = findViewById(R.id.brightness);
        ImageView eye = findViewById(R.id.eye);
        ImageView lips = findViewById(R.id.lips);

        // Add ImageViews to SVGColorUtil
        SVGColorUtil.addImageView(magic);
        SVGColorUtil.addImageView(intensity);
        SVGColorUtil.addImageView(brightness);
        SVGColorUtil.addImageView(eye);
        SVGColorUtil.addImageView(lips);

        this.intensity.setVisibility(View.GONE);

        textView.setVisibility(View.GONE);
        this.brightness.setVisibility(View.GONE);
        textView2.setVisibility(View.GONE);


        magic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SVGColorUtil.changeColorAndResetOthers((ImageView) v, R.color.clicked);
                MainActivity.this.intensity.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                MainActivity.this.brightness.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                applyBrightnessAndUpdate();
                applyIntensityAndUpdate();


            }
        });
        intensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SVGColorUtil.changeColorAndResetOthers((ImageView) v, R.color.clicked);
                MainActivity.this.intensity.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                MainActivity.this.brightness.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);


            }
        });
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SVGColorUtil.changeColorAndResetOthers((ImageView) v, R.color.clicked);
                MainActivity.this.brightness.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                MainActivity.this.intensity.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);

            }
        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SVGColorUtil.changeColorAndResetOthers((ImageView) v, R.color.clicked);
                MainActivity.this.intensity.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                MainActivity.this.brightness.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                ///readEye();
               // eye.setClickable(false);
                removeRedEyesAndUpdate();


            }
        });
        lips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SVGColorUtil.changeColorAndResetOthers((ImageView) v, R.color.clicked);
                MainActivity.this.intensity.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                MainActivity.this.brightness.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
                //lipstic();
                applyLipstickAndUpdate();

            }
        });

        // Set the maximum progress of the SeekBar
        this.intensity.setMax(90); // Since the range is from 10 to 100
        this.brightness.setMax(90);

        // Set a listener to track changes in SeekBar progress
        this.intensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the selected value
                intensityValue = progress + 10; // Adjust for the range starting from 10
                textView.setText(String.valueOf(intensityValue));
                // Call applyIntensityAndUpdate with the current SeekBar progress

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Check if the intensity has increased or decreased
                boolean increasing = intensityValue >= previousIntensity;
                // Get the maximum intensity value expected by the Python function
                int maxIntensity = 255;
                if(increasing == true){
                    applyIntensityAndUpdate1(intensityValue);
                }else{

                    applyIntensityAndUpdate1(intensityValue);
                }

            }


        });

        // Set a listener to track changes in SeekBar progress
        this.brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the selected value
                brightnessValue = progress + 10; // Adjust for the range starting from 10
                textView2.setText(String.valueOf(brightnessValue));
                // Convert the selected image to byte array


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Get the current image bitmap
                Bitmap currentBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                // Convert the current image bitmap to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                currentBitmap.compress(Bitmap.CompressFormat.PNG, brightnessValue, stream);
                byte[] currentImageBytes = stream.toByteArray();

                // Get Python instance
                Python py = Python.getInstance();

                // Get access to the python files
                PyObject pyObject1 = py.getModule("brightness");

                // Call the Python function to get the modified image data
                PyObject obj1 = pyObject1.callAttr("increase_brightness", currentImageBytes, brightnessValue);
                if (obj1 != null) {
                    // Convert the modified image data to Bitmap
                    byte[] modifiedImageBytes = obj1.toJava(byte[].class);
                    if (modifiedImageBytes != null) {
                        Bitmap resultBitmap = BitmapFactory.decodeByteArray(modifiedImageBytes, 0, modifiedImageBytes.length);

                        // Set the Bitmap to the ImageView
                        imageView.setImageBitmap(resultBitmap);
                    } else {
                        // Handle the case where the Python function returns null image data
                        Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle the case where the Python function returns null
                    Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
                }

            }

        });





        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // intensity();
                //readEye();

            }
        });


    }


    public void onImageViewClick(View view) {
        // Open image selector when the ImageView is clicked
        openImageSelector();
    }

    private void openImageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            Log.d("Image URI", "URI: " + imageUri.toString()); // Add logging

            try {
                // Load the image from the URI
                loadImageFromUri(imageUri);

                // Update the selected image URI
                updateSelectedImageUri(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to pick image", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageFromUri(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            Log.e("Image Loading", "Bitmap is null");
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
    }
    // Function to apply intensity based on SeekBar value and update the edited image
    private void applyIntensityAndUpdate1(int intensity) {
        // Check if the intensity has increased or decreased
        boolean increasing = intensity >= previousIntensity;

        // Get the current image bitmap
        Bitmap currentBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Convert the current image bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] currentImageBytes = stream.toByteArray();

        // Get Python instance
        Python py = Python.getInstance();

        // Get access to the python files
        PyObject pyObject1 = py.getModule("intensity");

        // Call the Python function to get the modified image data
        PyObject obj1 = pyObject1.callAttr("increase_intensity", currentImageBytes, intensity);
        if (obj1 != null) {
            // Convert the modified image data to Bitmap
            byte[] modifiedImageBytes = obj1.toJava(byte[].class);
            if (modifiedImageBytes != null) {
                Bitmap resultBitmap = BitmapFactory.decodeByteArray(modifiedImageBytes, 0, modifiedImageBytes.length);

                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(resultBitmap);
            } else {
                // Handle the case where the Python function returns null image data
                Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the Python function returns null
            Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
        }
        // Update the previous intensity
        previousIntensity = intensity;
    }



    // Function to apply intensity and update the edited image
    private void applyIntensityAndUpdate() {
        // Get the current image bitmap
        Bitmap currentBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Convert the current image bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] currentImageBytes = stream.toByteArray();

        // Get Python instance
        Python py = Python.getInstance();

        // Get access to the python files
        PyObject pyObject1 = py.getModule("intensity");

        // Call the Python function to get the modified image data
        PyObject obj1 = pyObject1.callAttr("increase_intensity", currentImageBytes, 100);

        if (obj1 != null) {
            // Convert the modified image data to Bitmap
            byte[] modifiedImageBytes = obj1.toJava(byte[].class);
            if (modifiedImageBytes != null) {
                // Update the edited image bitmap
                editedImageBitmap = BitmapFactory.decodeByteArray(modifiedImageBytes, 0, modifiedImageBytes.length);

                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(editedImageBitmap);
            } else {
                // Handle the case where the Python function returns null image data
                Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the Python function returns null
            Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to apply brightness and update the edited image
    private void applyBrightnessAndUpdate() {
        // Get the current image bitmap
        Bitmap currentBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Convert the current image bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] currentImageBytes = stream.toByteArray();

        // Get Python instance
        Python py = Python.getInstance();

        // Get access to the python files
        PyObject pyObject1 = py.getModule("brightness");

        // Call the Python function to get the modified image data
        PyObject obj1 = pyObject1.callAttr("increase_brightness", currentImageBytes, 50); // Adjust brightness by 50%

        if (obj1 != null) {
            // Convert the modified image data to Bitmap
            byte[] modifiedImageBytes = obj1.toJava(byte[].class);
            if (modifiedImageBytes != null) {
                // Update the edited image bitmap
                editedImageBitmap = BitmapFactory.decodeByteArray(modifiedImageBytes, 0, modifiedImageBytes.length);

                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(editedImageBitmap);
            } else {
                // Handle the case where the Python function returns null image data
                Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the Python function returns null
            Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
        }
    }


    // Function to apply lipstick and update the edited image
    private void applyLipstickAndUpdate() {
        // Get the current image bitmap
        Bitmap currentBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Convert the current image bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] currentImageBytes = stream.toByteArray();

        // Get Python instance
        Python py = Python.getInstance();
        // Get access to the python files
        PyObject pyObject1 = py.getModule("lipstic");

        // Call the Python function to apply lipstick
        PyObject obj1 = pyObject1.callAttr("apply_lipstick", currentImageBytes);
        if (obj1 != null) {
            // Convert the modified image data to Bitmap
            byte[] modifiedImageBytes = obj1.toJava(byte[].class);
            if (modifiedImageBytes != null) {
                // Update the edited image bitmap
                editedImageBitmap = BitmapFactory.decodeByteArray(modifiedImageBytes, 0, modifiedImageBytes.length);

                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(editedImageBitmap);
            } else {
                // Handle the case where the Python function returns null image data
                Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the Python function returns null
            Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
        }
    }


    // Function to remove red eyes and update the edited image
    private void removeRedEyesAndUpdate() {
        // Get the current image bitmap
        Bitmap currentBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        // Call Python function to remove red eyes
        Python py = Python.getInstance();
        PyObject pyObject1 = py.getModule("readeye");

        // Convert the current image bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        currentBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] currentImageBytes = stream.toByteArray();

        // Call the Python function to get the processed image data
        PyObject obj1 = pyObject1.callAttr("removeRedEyes", currentImageBytes);
        if (obj1 != null) {
            // Convert the modified image data to byte array
            byte[] modifiedImageBytes = obj1.toJava(byte[].class);
            if (modifiedImageBytes != null) {
                // Update the edited image bitmap
                editedImageBitmap = BitmapFactory.decodeByteArray(modifiedImageBytes, 0, modifiedImageBytes.length);

                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(editedImageBitmap);
            } else {
                // Handle the case where the Python function returns null image data
                Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the Python function returns null
            Toast.makeText(MainActivity.this, "Failed to process image", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to update the currently selected image URI
    private void updateSelectedImageUri(Uri uri) {
        this.imageUri = uri;
    }

    // Function to get the currently selected image URI
    public Uri getSelectedImageUri() {
        return imageUri;
    }

    // Function to convert image Uri to byte array
    private byte[] convertImageToByteArray(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}