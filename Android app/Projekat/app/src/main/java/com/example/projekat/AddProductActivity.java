package com.example.projekat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    //ui views
    private ImageButton backBtn;
    private ImageView productIv;
    private TextView categoryTv;
    private EditText productNameEt, descriptionEt, quantityEt, priceEt, discountPriceEt, discountPercentageEt;
    private SwitchCompat discountSwitch;
    private Button addProductBtn;

    //permission constants
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
        setContentView(R.layout.activity_add_product);

        //init ui views
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
        addProductBtn = findViewById(R.id.addProductBtn);

        //on start is unchecked: so hide discountPrice and discountPercentage
        discountPriceEt.setVisibility(View.GONE);
        discountPercentageEt.setVisibility(View.GONE);

        // init permission arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init others
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //if discountSwitch is checked: show discountPrice and discountPercentage
        //if discountSwitch is not checked: hide discountPrice and discountPercentage

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        discountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //checked: show discountPrice and discountPercentage
                    discountPriceEt.setVisibility(View.VISIBLE);
                    discountPercentageEt.setVisibility(View.VISIBLE);
                }
                else {
                    //not checked: hide discountPrice and discountPercentage
                    discountPriceEt.setVisibility(View.GONE);
                    discountPercentageEt.setVisibility(View.GONE);
                }
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

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

    }

    private String productName, description, category, quantity,
                   price, discountPrice, discountPercentage;
    private boolean discountAvailable = false;

    //data
    private void inputData() {

        //input data
        productName = productNameEt.getText().toString().trim();
        description = descriptionEt.getText().toString().trim();
        category = categoryTv.getText().toString().trim();
        quantity = quantityEt.getText().toString().trim();
        price = priceEt.getText().toString().trim();
        discountAvailable = discountSwitch.isChecked();

        //validate data
        if(TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Product name is required!", Toast.LENGTH_SHORT).show();
            return; // do not proceed further
        }

        if(TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Description is required!", Toast.LENGTH_SHORT)
                    .show();
            return; // do not proceed further
        }

        if(TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Category is required!", Toast.LENGTH_SHORT).show();
            return; // do not proceed further
        }

        if(TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Price is required!", Toast.LENGTH_SHORT).show();
            return; // do not proceed further
        }

        if(discountAvailable) {
            //product is with discount
            discountPrice = discountPriceEt.getText().toString().trim();
            discountPercentage = discountPercentageEt.getText().toString().trim();

            if(TextUtils.isEmpty(discountPrice)) {
                Toast.makeText(this, "Discount price is required!",
                        Toast.LENGTH_SHORT).show();
                return; // do not proceed further
            }
        }
        else {
            //product is without discount
            discountPrice = "0";
            discountPercentage = "";
        }

        addProduct();
    }

    //firebase
    private void addProduct() {
        //add data to database
        progressDialog.setMessage("Adding Product...");
        progressDialog.show();

        String timestamp = ""+System.currentTimeMillis();

        if(imageUri == null) {
            //upload without image
            //setup data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("productId", "" + timestamp);
            hashMap.put("productName", "" + productName);
            hashMap.put("productDescription", "" + description);
            hashMap.put("productCategory", "" + category);
            hashMap.put("productQuantity", "" + quantity);
            hashMap.put("productImage", ""); //no image
            hashMap.put("productPrice", "" + price);
            hashMap.put("productDiscountPrice", "" + discountPrice);
            hashMap.put("productDiscountPercentage", "" + discountPercentage);
            hashMap.put("discountAvailable", "" + discountAvailable);

            //add to database
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
            ref.child(timestamp).setValue(hashMap)
                     .addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void unused) {
                             //db updated
                             progressDialog.dismiss();
                             Toast.makeText(AddProductActivity.this, "Adding product...", Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(AddProductActivity.this, com.example.projekat.MainAdminActivity.class));
                             finish();

                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             //db failed to update
                             progressDialog.dismiss();
                             Toast.makeText(AddProductActivity.this, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(AddProductActivity.this, com.example.projekat.MainAdminActivity.class));
                             finish();
                         }
                     });
        }
        else {
            //upload with image
            //first upload image to storage
            //name and path of image to be uploaded
            String filePathName = "product_image/" + "" + firebaseAuth.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathName);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot snapshot) {
                            //image uploaded
                            //get url of uploaded image
                            Task<Uri> uriTask = snapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if(uriTask.isSuccessful()) {
                                //url of image received, upload to database
                                //setup data to upload
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("productId", "" + timestamp);
                                hashMap.put("productName", "" + productName);
                                hashMap.put("productDescription", "" + description);
                                hashMap.put("productCategory", "" + category);
                                hashMap.put("productQuantity", "" + quantity);
                                hashMap.put("productPrice", "" + price);
                                hashMap.put("productDiscountPrice", "" + discountPrice);
                                hashMap.put("productDiscountPercentage", "" + discountPercentage);
                                hashMap.put("discountAvailable", "" + discountAvailable);
                                hashMap.put("productImage", "" + downloadImageUri);

                                //add to database
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
                                ref.child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //db updated
                                                progressDialog.dismiss();
                                                Toast.makeText(AddProductActivity.this, "Adding product...", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AddProductActivity.this, com.example.projekat.MainAdminActivity.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failed adding to database
                                                progressDialog.dismiss();
                                                Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AddProductActivity.this, com.example.projekat.MainAdminActivity.class));
                                                finish();
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
                            Toast.makeText(AddProductActivity.this, ""+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    //dialog
    private void categoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category:")
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