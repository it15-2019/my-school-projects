package com.example.projekat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MainAdminActivity extends AppCompatActivity {

    //ui views
    private EditText searchProductEt;
    private TextView nameTv, accountTypeTv, emailTv, tabProductsTv, tabOrdersTv, filteredProductTv;
    private ImageButton logoutBtn, editProfileBtn, addProductBtn, filterBtn;
    private ImageView profileIv;
    private RecyclerView productsRv, ordersRv;

    //others
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private ArrayList<ModelProduct> productList;
    private AdapterProductAdmin adapterProductAdmin;
    private ArrayList<ModelOrderUser> ordersList;
    private AdapterOrderUser adapterOrderUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        //init ui vies
        searchProductEt = findViewById(R.id.searchProductEt);
        productsRv = findViewById(R.id.productsRv);
        ordersRv = findViewById(R.id.ordersRv);
        nameTv = findViewById(R.id.nameTv);
        accountTypeTv = findViewById(R.id.accountTypeTv);
        emailTv = findViewById(R.id.emailTv);
        tabProductsTv = findViewById(R.id.tabProductsTv);
        tabOrdersTv = findViewById(R.id.tabOrdersTv);
        filteredProductTv = findViewById(R.id.filteredProductTv);
        logoutBtn = findViewById(R.id.logoutBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        addProductBtn = findViewById(R.id.addProductBtn);
        filterBtn = findViewById(R.id.filterBtn);
        profileIv = findViewById(R.id.profileIv);

        //others
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        checkUser();
        loadOrders();
        loadAllProducts();
        showProducts();

        searchProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterProductAdmin.getFilter().filter(s);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make offline, sign out and go to login activity
                makeMeOffline();
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit profile activity
                startActivity(new Intent(MainAdminActivity.this, EditAdminProfileActivity.class));
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open add product activity
                startActivity(new Intent(MainAdminActivity.this, AddProductActivity.class));
            }
        });

        tabProductsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load products
                showProducts();
            }
        });

        tabOrdersTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load orders
                showOrders();
            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainAdminActivity.this);
                builder.setTitle("Choose Category:")
                        .setItems(Categories.productCategories2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get selected item
                                String selected = Categories.productCategories2[which];
                                filteredProductTv.setText(selected + ":");
                                if(selected.equals("All products")) {
                                    //load all
                                    loadAllProducts();
                                }
                                else {
                                    //load filtered
                                    loadFilteredProducts(selected);
                                }
                            }
                        }).show();
            }
        });
    }

    private void loadFilteredProducts(String selected) {
        productList = new ArrayList<>();

        //get all products
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        ref.orderByChild("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        productList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()) {

                            String category = "" + ds.child("productCategory").getValue();

                            //if selected category matches product category then add in list
                            if(selected.equals(category)) {
                                ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                                productList.add(modelProduct);
                            }
                        }

                        //setup adapter
                        adapterProductAdmin = new AdapterProductAdmin(MainAdminActivity.this, productList);

                        //set adapter
                        productsRv.setAdapter(adapterProductAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

    }

    private void loadAllProducts() {
        productList = new ArrayList<>();

        //get all products
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        ref.orderByChild("Products")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        productList.clear();

                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            ModelProduct modelProduct = ds.getValue(ModelProduct.class);
                            productList.add(modelProduct);
                        }

                        //setup adapter
                        adapterProductAdmin = new AdapterProductAdmin(MainAdminActivity.this, productList);

                        //set adapter
                        productsRv.setAdapter(adapterProductAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }

    private void loadOrders() {
        //init order list
        ordersList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Orders");
        ref.orderByChild("Orders")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        ordersList.clear();

                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            ModelOrderUser modelOrderUser = ds.getValue(ModelOrderUser.class);
                            ordersList.add(modelOrderUser);
                        }

                        //setup adapter
                        adapterOrderUser= new AdapterOrderUser(MainAdminActivity.this, ordersList);
                        ordersRv.setAdapter(adapterOrderUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void showProducts() {
        //show products and hide orders
        ordersRv.setVisibility(View.GONE);
        productsRv.setVisibility(View.VISIBLE);

        searchProductEt.setVisibility(View.VISIBLE);
        filterBtn.setVisibility(View.VISIBLE);
        filteredProductTv.setText("All products:");

        tabProductsTv.setTextColor(getResources().getColor(R.color.colorBlack));
        tabProductsTv.setBackgroundResource(R.drawable.shape_r4);

        tabOrdersTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabOrdersTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void showOrders() {
        //show orders and hide products
        productsRv.setVisibility(View.GONE);
        ordersRv.setVisibility(View.VISIBLE);

        searchProductEt.setVisibility(View.GONE);
        filterBtn.setVisibility(View.GONE);
        filteredProductTv.setText("All orders:");

        tabProductsTv.setTextColor(getResources().getColor(R.color.colorWhite));
        tabProductsTv.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabOrdersTv.setTextColor(getResources().getColor(R.color.colorBlack));
        tabOrdersTv.setBackgroundResource(R.drawable.shape_r4);
    }

    private void makeMeOffline() {
        //after logging in, make user online
        progressDialog.setMessage("Logging out...");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("online", "false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //updated succesfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed updating
                        progressDialog.dismiss();
                        Toast.makeText(MainAdminActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null) {
            startActivity(new Intent(MainAdminActivity.this, LoginActivity.class));
            finish();
        }
        else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()) {
                            //get data from db
                            String name = "" + ds.child("name").getValue();
                            String accountType = "" + ds.child("accountType").getValue();
                            String email = "" + ds.child("email").getValue();
                            String profileImage = "" + ds.child("profileImage").getValue();

                            //set data
                            nameTv.setText(name);
                            accountTypeTv.setText("(" + accountType + ")");
                            emailTv.setText(email);
                            try {
                                Picasso.get().load(profileImage).placeholder
                                        (R.drawable.ic_person_white).into(profileIv);
                            }
                            catch(Exception e) {
                                profileIv.setImageResource(R.drawable.ic_person_white);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }
}