package com.example.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

import com.google.android.gms.tasks.OnFailureListener;


public class EditProductActivity extends AppCompatActivity {

    //ui views
    private ImageButton backBtn;
    private ImageView productIv;
    private TextView categoryTv;
    private EditText productNameEt, descriptionEt, quantityEt, priceEt, discountPriceEt, discountPercentageEt;
    private SwitchCompat discountSwitch;
    private Button updateProductBtn;

    private String productId;

    // permission constants
    public static final int CAMERA_REQUEST_CODE = 200;
    public static final int STORAGE_REQUEST_CODE = 300;
    public static final int IMAGE_PICK_GALLERY_CODE = 400;
    public static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;

    //others
    private Uri imageUri;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        backBtn = findViewById(R.id.backBtn);
        productIv = findViewById(R.id.productIv);
        productNameEt = findViewById(R.id.productNameEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        quantityEt = findViewById(R.id.quantityEt);
        priceEt = findViewById(R.id.priceEt);
        discountPriceEt = findViewById(R.id.discountPriceEt);
        discountPercentageEt = findViewById(R.id.discountPercentageEt);
        categoryTv = findViewById(R.id.categoryTv);
        discountSwitch = findViewById(R.id.discountSwitch);
        updateProductBtn = findViewById(R.id.updateProductBtn);

        //get id of the product from intent
        productId = getIntent().getStringExtra("productId");

        //on start is unchecked: so hide discountPrice and discountPercentage
        discountPriceEt.setVisibility(View.GONE);
        discountPercentageEt.setVisibility(View.GONE);

        // init permission arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();

        loadProductDetails();

        //setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);

        discountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked: show discountPrice and discountPercentage
                    discountPriceEt.setVisibility(View.VISIBLE);
                    discountPercentageEt.setVisibility(View.VISIBLE);
                } else {
                    //not checked: hide discountPrice and discountPercentage
                    discountPriceEt.setVisibility(View.GONE);
                    discountPercentageEt.setVisibility(View.GONE);
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        productIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog to pick image
                showImagePickDialog();

            }
        });

        categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pick category
                categoryDialog();
            }
        });

        updateProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inputData();
                startActivity(new Intent(EditProductActivity.this, MainAdminActivity.class));
            }
        });

    }

    private void loadProductDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        ref.child(productId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //get data
                        String productId = "" + snapshot.child("productId").getValue();
                        String productName = "" + snapshot.child("productName").getValue();
                        String productDescription = "" + snapshot.child("productDescription").getValue();
                        String productCategory = "" + snapshot.child("productCategory").getValue();
                        String productQuantity = "" + snapshot.child("productQuantity").getValue();
                        String productImage = "" + snapshot.child("productImage").getValue();
                        String productPrice = "" + snapshot.child("productPrice").getValue();
                        String productDiscountPrice = "" + snapshot.child("productDiscountPrice").getValue();
                        String productDiscountPercentage = "" + snapshot.child("productDiscountPercentage").getValue();
                        String discountAvailable = "" + snapshot.child("discountAvailable").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();

                        //set data to views
                        if (discountAvailable.equals("true")) {
                            discountSwitch.setChecked(true);

                            discountPriceEt.setVisibility(View.VISIBLE);
                            discountPercentageEt.setVisibility(View.VISIBLE);
                        } else {
                            discountSwitch.setChecked(false);

                            discountPriceEt.setVisibility(View.GONE);
                            discountPercentageEt.setVisibility(View.GONE);
                        }

                        productNameEt.setText(productName);
                        descriptionEt.setText(productDescription);
                        categoryTv.setText(productCategory);
                        quantityEt.setText(productQuantity);
                        priceEt.setText(productPrice);
                        discountPercentageEt.setText(productDiscountPercentage);
                        discountPriceEt.setText(productDiscountPrice);

                        try {
                            Picasso.get().load(productImage).placeholder(R.drawable.ic_photo_grey)
                                    .into(productIv);
                        } catch (Exception e) {
                            productIv.setImageResource(R.drawable.ic_photo_grey);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String productName, productDescription, productCategory, productQuantity,
            productPrice, productDiscountPrice, productDiscountPercentage;
    private boolean discountAvailable = false;

    private void inputData() {

        //1) input data
        productName = productNameEt.getText().toString().trim();
        productDescription = descriptionEt.getText().toString().trim();
        productCategory = categoryTv.getText().toString().trim();
        productQuantity = quantityEt.getText().toString().trim();
        productPrice = priceEt.getText().toString().trim();
        discountAvailable = discountSwitch.isChecked();

        //2) validate data
        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Name of product is required!", Toast.LENGTH_SHORT).show();
            return; // do not proceed further
        }

        if (TextUtils.isEmpty(productDescription)) {
            Toast.makeText(this, "Description of product is required!", Toast.LENGTH_SHORT)
                    .show();
            return; // do not proceed further
        }

        if (TextUtils.isEmpty(productCategory)) {
            Toast.makeText(this, "Category of product is required!", Toast.LENGTH_SHORT).show();
            return; // do not proceed further
        }

        if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Price of product is required!", Toast.LENGTH_SHORT).show();
            return; // do not proceed further
        }

        if (discountAvailable) {
            //product is with discount
            productDiscountPrice = discountPriceEt.getText().toString().trim();
            productDiscountPercentage = discountPercentageEt.getText().toString().trim();

            if (TextUtils.isEmpty(productDiscountPrice)) {
                Toast.makeText(this, "Discount price of product is required!",
                        Toast.LENGTH_SHORT).show();
                return; // do not proceed further
            }
        } else {
            //product is without discount
            productDiscountPrice = "0";
            productDiscountPercentage = "";
        }

        updateProduct();
    }

    private void updateProduct() {

        progressDialog.setMessage("Updating Product...");
        progressDialog.show();

        if (imageUri == null) {
            //update without image
            //setup data in hashmap to update
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("productName", "" + productName);
            hashMap.put("productDescription", "" + productDescription);
            hashMap.put("productCategory", "" + productCategory);
            hashMap.put("productQuantity", "" + productQuantity);
            hashMap.put("productPrice", "" + productPrice);
            hashMap.put("productDiscountPrice", "" + productDiscountPrice);
            hashMap.put("productDiscountPercentage", "" + productDiscountPercentage);
            hashMap.put("discountAvailable", "" + discountAvailable);

            //update to database
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
            ref.child(productId)
                    .updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //update success
                            progressDialog.dismiss();
                            Toast.makeText(EditProductActivity.this, "Product updated...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //update failed
                            progressDialog.dismiss();
                            Toast.makeText(EditProductActivity.this, "" + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            //upload with image
            //first upload image to storage
            //name and path of image to be uploaded
            String filePathAndName = "product_images/" + "" + productId; //override previous image using same id

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //image uploaded
                            //get url of uploaded image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if(uriTask.isSuccessful()) {
                                //url of image received, upload to database
                                //setup data to upload
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("productName", "" + productName);
                                hashMap.put("productDescription", "" + productDescription);
                                hashMap.put("productCategory", "" + productCategory);
                                hashMap.put("productQuantity", "" + productQuantity);
                                hashMap.put("productImage", ""+downloadImageUri);
                                hashMap.put("productPrice", "" + productPrice);
                                hashMap.put("productDiscountPrice", "" + productDiscountPrice);
                                hashMap.put("productDiscountPercentage", "" + productDiscountPercentage);
                                hashMap.put("discountAvailable", "" + discountAvailable);

                                //update to database
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products");
                                reference.child(productId)
                                        .updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //update success
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProductActivity.this, "Product updated...",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //update failed
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProductActivity.this, "" + e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed uploading image
                            progressDialog.dismiss();
                            Toast.makeText(EditProductActivity.this, ""+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    //dialog
    private void categoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category")
                .setItems(Categories.productCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //get picked category
                        String productCategory = Categories.productCategories[which];

                        //set picked category
                        categoryTv.setText(productCategory);
                    }
                }).show();
    }

    private void showImagePickDialog() {
        // options to display in dialog
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick an image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle clicks
                        if (which == 0) {
                            //camera clicked
                            if (checkCameraPermission()) {
                                //camera permissions allowed
                                pickFromCamera();
                            } else {
                                //not allowed, request
                                requestCameraPermission();
                            }
                        } else {
                            //gallery clicked
                            if (checkStoragePermission()) {
                                //storage permissions allowed
                                pickFromGallery();
                            } else {
                                //not allowed, request
                                requestStoragePermission();
                            }
                        }
                    }
                }).show();
    }

    //picks
    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    //checks
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    //requests
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //overrides
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        // permission allowed
                        pickFromCamera();
                    } else {
                        // permission denied
                        Toast.makeText(this, "Camera permissions are necessary!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        // permission allowed
                        pickFromGallery();
                    } else {
                        // permission denied
                        Toast.makeText(this, "Storage permission is necessary!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK) {
            if(requestCode == IMAGE_PICK_GALLERY_CODE) {
                //get picked image
                imageUri = data.getData();
                //set to imageView
                productIv. setImageURI(imageUri);
            } else if(requestCode == IMAGE_PICK_CAMERA_CODE) {
                //set to imageView
                productIv. setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode,resultCode, data);
    }
}