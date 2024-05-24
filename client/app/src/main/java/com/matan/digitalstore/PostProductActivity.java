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
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.matan.digitalstore.Utils.HttpUtil;
import com.matan.digitalstore.Utils.Utils;
import com.matan.digitalstore.model.Product;
import com.squareup.picasso.Picasso;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * An Activity for purchasing or updating a product.
 */
public class PostProductActivity extends AppCompatActivity {
    private TextView activityTitle;
    private EditText name, description, quantity, price;
    private ImageView imageView;
    private Uri imageUri = null;
    private Product product;
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
        activityTitle = findViewById(R.id.post_product_title);
        description = findViewById(R.id.productDescription);
        quantity = findViewById(R.id.productQuantity);
        price = findViewById(R.id.productPrice);
        imageView = findViewById(R.id.productImage);
        uploadButton = findViewById(R.id.uploadButton);
        submitButton = findViewById(R.id.submitButton);
        //if the user wants to edit an existing product, we'll get it from the product property.
        product = getIntent().getParcelableExtra("product");
        if(product!= null){
            activityTitle.setText("Edit an existing item");
            name.setText(product.getName());
            description.setText(product.getDescription());
            quantity.setText(product.getQuantity()+"");
            price.setText(product.getPrice()+"");
            String image = product.getImage();
            if(image!=null){
                String imageURL = HttpUtil.getImageURL(image);
                Picasso.get().load(imageURL).into(imageView);
            }
        }
        uploadButton.setOnClickListener(v -> showSelectImageDialog());
        submitButton.setOnClickListener(v -> submitProduct());
    }

    /**
     * This function enables the user to take a picture for the products,
     * or to select from the local gallery. It opens a popup that that allows
     * him two chose one of these two options.
     */
    private void showSelectImageDialog() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(PostProductActivity.this);
        builder.setTitle("Add Product Photo");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                takePicture();
            } else if (options[item].equals("Choose from Gallery")) {
                chosePictureFromCamera();
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    /*
    The function open a new activity for choosing an image for the phone gallery
     */
    private void chosePictureFromCamera(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , PICK_IMAGE_REQUEST);
    }

    private void takePicture(){
        if(deviceHasCamera()== true) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePicture.resolveActivity(getPackageManager()) != null) {
                //we need to make sure that the app has access to camera before we can continue
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    // Continue with the code to open camera
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
            } else {
                Toast.makeText(getApplicationContext(), "No camera was found.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    The function returns is the device has a camera, otherwise false.
     */
    private boolean deviceHasCamera() {
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
        imageUri = Uri.fromFile(image);
        return image;
    }

    /*
    In the function we handle the image that the user took or chose from the gallery,
    and load it onto the ImageView of the product in the activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK ){
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                imageUri = data.getData();
                Glide.with(this).load(imageUri).into(imageView);
            } else if (requestCode == REQUEST_IMAGE_CAPTURE ) {
                Glide.with(this).load(imageUri).into(imageView);
            }
        }

    }

    /**
     * The function handles the click on the sumbit button - it start a new therad
     * for uploading the product
     */
    private void submitProduct() {
        PostProduct postProduct = new PostProduct();
        postProduct.execute();
    }
    /*
    This thread send a post request if this is a new item.
    If this is an existing item (The user want to edit its details),
    we'll send a put request.
    If the user chose an image for the product,
    it will be sent in another post request. After a successful post, the catalog activity
    will be loaded.
     */
    public class PostProduct extends AsyncTask< Integer, Integer, Long> {
        private String error;
        @Override
        protected Long doInBackground(Integer...integers) {
            Long productID = null;
            try {
                JSONObject json = new JSONObject();
                json.put("name",name.getText());
                json.put("price", price.getText());
                json.put("quantity", quantity.getText());
                json.put("description", description.getText());
                if(product==null){
                    //user want to post a new product
                    ResponseBody responseBody = HttpUtil.postRequest("products/",json);
                    productID = Long.valueOf(responseBody.string());
                }
                else {
                    //User want to update a product, send a put request
                    json.put("image",product.getImage());
                    ResponseBody responseBody = HttpUtil.putRequest("products/"+product.getId(),json);
                    productID = product.getId();
                }
                // if the product was posted successfully, upload the image if necessary
                if(imageUri != null){
                    File file = Utils.uriToFile(getApplicationContext(), imageUri);
                    if (file != null) {
                        RequestBody fileBody = RequestBody.create(file, MediaType.parse(getApplicationContext().getContentResolver().getType(imageUri)));

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", file.getName(), fileBody)
                                .build();
                        String imageUrl = "products/uploadImage/"+productID;
                        ResponseBody imageResponse = HttpUtil.postRequest(imageUrl,requestBody,true);
                    }
                }

                } catch (IOException | JSONException e) {
                error =  "An error occurred while trying to post product: " + e.toString();
                System.err.println(error);
            }
            return productID;
        }

        @Override
        protected void onPostExecute(Long productID) {
            if(productID == null){
                Toast.makeText(getApplicationContext(),error, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Product was posted successfully.",Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }
}