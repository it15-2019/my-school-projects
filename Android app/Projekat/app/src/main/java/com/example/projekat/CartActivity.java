package com.example.projekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {

    //ui views
    TextView titleTv, totalTv1, totalPriceTv1, totalTv2, totalPriceTv2, deliveryTv, deliveryPriceTv;
    Button confirmOrderBtn;
    ImageButton backBtn;
    RecyclerView cartItemsRv;

    //others
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    public ArrayList<ModelCartItem> cartItemList;
    private AdapterCartItem adapterCartItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //init ui views
        titleTv = findViewById(R.id.titleTv);
        totalTv1 = findViewById(R.id.totalTv1);
        totalPriceTv1 = findViewById(R.id.totalPriceTv1);
        totalTv2 = findViewById(R.id.totalTv2);
        totalPriceTv2 = findViewById(R.id.totalPriceTv2);
        deliveryTv = findViewById(R.id.deliveryTv);
        deliveryPriceTv = findViewById(R.id.deliveryPriceTv);
        confirmOrderBtn = findViewById(R.id.confirmOrderBtn);
        backBtn = findViewById(R.id.backBtn);
        cartItemsRv = findViewById(R.id.cartItemsRv);

        //init others
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadCartItems();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }

    String orderTotal1;

    //data
    private void inputData() {

        //input data
        orderTotal1 = totalPriceTv1.getText().toString().trim();


        //validate data
        if(orderTotal1 == "$0" ) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return; // do not proceed further
        }

        submitOrder();
    }

    //firebase
    private void submitOrder() {

        progressDialog.setMessage("Placing order...");
        progressDialog.show();

        String timestamp = "" +System.currentTimeMillis();
        String total = totalPriceTv2.getText().toString().trim().replace("$", "");

        //set up order data
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderId", timestamp);
        hashMap.put("orderTime", timestamp);
        hashMap.put("orderStatus", "Progressing");
        hashMap.put("orderCost", total);
        hashMap.put("orderBy", firebaseAuth.getUid());

        //add to database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child("Orders");
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        for(int i = 0; i < cartItemList.size(); i++)
                        {
                            //order info add, add order items
                            String id = cartItemList.get(i).getId();
                            String name = cartItemList.get(i).getName();
                            String priceEach = cartItemList.get(i).getPriceEach();
                            String priceTotal = cartItemList.get(i).getPriceTotal();
                            String quantity = cartItemList.get(i).getQuantity();

                            HashMap<String, String> hashMap1= new HashMap<>();
                            hashMap1.put("id", id);
                            hashMap1.put("name", name);
                            hashMap1.put("priceEach", priceEach);
                            hashMap1.put("priceTotal", priceTotal);
                            hashMap1.put("quantity", quantity);

                            ref.child(timestamp).child("Items").child(id).setValue(hashMap);
                        }
                        progressDialog.dismiss();
                        deleteCartData();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CartActivity.this, "Order placed unsuccessfully!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteCartData() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid()).child("Cart");
        ref.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CartActivity.this, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private double total;
    private void loadCartItems() {
        cartItemList = new ArrayList<>();

        //get all cart item
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Cart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        cartItemList.clear();
                        total = 0.0;
                        for(DataSnapshot ds: snapshot.getChildren()) {
                            ModelCartItem modelCartItem = ds.getValue(ModelCartItem.class);
                            cartItemList.add(modelCartItem);
                            total = Double.parseDouble(modelCartItem.priceTotal) + total;
                        }

                        //setup adapter
                        adapterCartItem = new AdapterCartItem(CartActivity.this, cartItemList);

                        //set adapter
                        cartItemsRv.setAdapter(adapterCartItem);

                        if(cartItemList.size() == 0) {
                            totalPriceTv1.setText("$0");
                            totalPriceTv2.setText("$0");
                            deliveryPriceTv.setText("$0");
                        }
                        else {
                            totalPriceTv1.setText("$" + String.format("%.2f", total));
                            totalPriceTv2.setText("$" + String.format("%.2f", total + 10));
                            deliveryPriceTv.setText("$10");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });

    }
}