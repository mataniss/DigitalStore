package com.matan.digitalstore;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.matan.digitalstore.Utils.HttpUtil;
import com.squareup.picasso.BuildConfig;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.ResponseBody;

public class PostProductActivity extends AppCompatActivity {
    private EditText name, description, quantity, price;
    private ImageView imageView;
    private Uri imageUri;
    Button uploadButton;
    Button submitButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.post_product_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name = findViewById(R.id.productName);
        description = findViewById(R.id.productDescription);
        quantity = findViewById(R.id.productQuantity);
        price = findViewById(R.id.productPrice);
        imageView = findViewById(R.id.productImage);
        uploadButton = findViewById(R.id.uploadButton);
        submitButton = findViewById(R.id.submitButton);

        uploadButton.setOnClickListener(v -> selectImage());
        submitButton.setOnClickListener(v -> submitProduct());
    }

    /**
     * This function enables the user to take a picture for the products,
     * or to select from the local gallery. It opens a popup that that allows
     * him two chose one of these two options.
     */
    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PostProductActivity.this);
        builder.setTitle("Add Product Photo");
        builder.setItems(options, (dialog, item) -> {
            //todo: create two new functions: one for taking a picture and one for chosing a photo from the library
            if (options[item].equals("Take Photo")) {
            if( hasCamera()==true){
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    //we need to make sure that the app has access to camera before we can continue
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA }, REQUEST_IMAGE_CAPTURE);
                    } else {
                        // Continue with your code to open camera
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (photoFile != null) {
                            imageUri = FileProvider.getUriForFile(this, "com.matan.digitalstore.provider", photoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"No camera was found.",Toast.LENGTH_LONG).show();
                }
            }
            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , PICK_IMAGE_REQUEST);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imageUri = Uri.fromFile(image);
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(imageView);
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(this).load(imageUri).into(imageView);
        }
    }

    private void submitProduct() {
        //todo: implement submit action
        PostProduct postProduct = new PostProduct();
        postProduct.execute();
    }

    public class PostProduct extends AsyncTask< Integer, Integer, Long> {

        @Override
        protected Long doInBackground(Integer...integers) {
            Long productID = null;
            try {
                JSONObject json = new JSONObject();
                json.put("name",name.getText());
                json.put("price", price.getText());
                json.put("quantity", quantity.getText());
                json.put("description", description.getText());
                ResponseBody responseBody = HttpUtil.postRequest("products/",json);
                productID = Long.valueOf(responseBody.string());
                //todo: if the product was posted successfully, upload the image if necessary

            } catch (IOException | JSONException e) {
                System.err.println("An error occurred while trying to post a product.");
            }
            return productID;
        }

        @Override
        protected void onPostExecute(Long productID) {
            //todo: if posting was successful finish the activity
            //otherwise display an error

        }
    }
}