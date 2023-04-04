package com.example.projekat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterProductUser extends RecyclerView.Adapter<AdapterProductUser.HolderProductUser> implements Filterable {

    private Context context;
    public ArrayList<ModelProduct> productList, filterList;
    private FilterProductUser filter;

    public AdapterProductUser(Context context, ArrayList<ModelProduct> productsList) {
        this.context = context;
        this.productList = productsList;
        this.filterList = productsList;
    }

    @NonNull
    @Override
    public HolderProductUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_user, parent, false);
        return new HolderProductUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductUser holder, int position) {
        //get data
        ModelProduct modelProduct = productList.get(position);
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountPercentage = modelProduct.getProductDiscountPercentage();
        String discountPrice = modelProduct.getProductDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String productDescription = modelProduct.getProductDescription();
        String productImage = modelProduct.getProductImage();
        String productQuantity = modelProduct.getProductQuantity();
        String productName = modelProduct.getProductName();
        String productPrice = modelProduct.getProductPrice();
        String productId = modelProduct.getProductId();
        String timestamp = modelProduct.getTimestamp();

        //set data
        holder.nameTv.setText(productName);
        holder.descriptionTv.setText(productDescription);
        holder.discountPercentageTv.setText(discountPercentage + "% OFF");
        holder.discountPriceTv.setText("$" + discountPrice);
        holder.priceTv.setText("$" + productPrice);

        if(discountAvailable.equals("true")) {
            holder.discountPriceTv.setVisibility(View.VISIBLE);
            holder.discountPercentageTv.setVisibility(View.VISIBLE);

            // add strike trough on price
            holder.priceTv.setPaintFlags(holder.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.discountPriceTv.setVisibility(View.GONE);
            holder.discountPercentageTv.setVisibility(View.GONE);
            holder.priceTv.setPaintFlags(0);
        }

        try {
            Picasso.get().load(productImage).placeholder(R.drawable.ic_add_to_cart_green)
                    .into(holder.productIconIv);
        }
        catch(Exception e) {
            holder.productIconIv.setImageResource(R.drawable.ic_add_to_cart_green);
        }

        holder.addToCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add product to cart
                showQuantityDialog(modelProduct);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show product details

            }
        });
    }

    private double cost = 0;
    private double finalCost = 0;
    private int productQuantity = 0;
    private int itemId = 1;

    private FirebaseAuth firebaseAuth ;
    private ProgressDialog progressDialog;

    private void showQuantityDialog(ModelProduct modelProduct) {
        //inflate layout for dialog
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_product_to_cart, null);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //init layout views
        ImageView productIv = view.findViewById(R.id.productIv);
        TextView nameTv = view.findViewById(R.id.nameTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView discountTv = view.findViewById(R.id.discountTv);
        TextView priceTv = view.findViewById(R.id.priceTv);
        TextView discountPriceTv = view.findViewById(R.id.discountPriceTv);
        TextView totalTv = view.findViewById(R.id.totalTv);
        TextView quantityTv= view.findViewById(R.id.quantityTv);
        ImageButton incrementBtn= view.findViewById(R.id.incrementBtn);
        ImageButton decrementBtn= view.findViewById(R.id.decrementBtn);
        Button addBtn = view.findViewById(R.id.addBtn);

        //get data from model
        String productId = modelProduct.getProductId();
        String productName = modelProduct.getProductName();
        String productDescription = modelProduct.getProductDescription();
        String discountPercentage = modelProduct.getProductDiscountPercentage();
        String productImage = modelProduct.getProductImage();

        final String productPrice;
        if(modelProduct.getDiscountAvailable().equals("true"))
        {
            productPrice = modelProduct.getProductDiscountPrice();
            discountTv.setVisibility(View.VISIBLE);
            priceTv.setPaintFlags(priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            discountTv.setVisibility(View.GONE);
            discountPriceTv.setVisibility(View.GONE);
            productPrice = modelProduct.getProductPrice();
        }

        cost = Double.parseDouble(productPrice.replaceAll("$", ""));
        finalCost = Double.parseDouble(productPrice.replaceAll("$", ""));
        productQuantity = 1;

        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        //set data
        try
        {
            Picasso.get().load(productImage).placeholder(R.drawable.ic_cart_white).into(productIv);
        }
        catch (Exception e)
        {
            productIv.setImageResource(R.drawable.ic_cart_white);
        }

        nameTv.setText(""+ productName);
        descriptionTv.setText("" + productDescription);
        discountTv.setText(discountPercentage + "% OFF");
        quantityTv.setText("" + productQuantity);
        priceTv.setText("$" + modelProduct.getProductPrice());
        discountPriceTv.setText("$" + modelProduct.getProductDiscountPrice());
        totalTv.setText("$" + finalCost);

        AlertDialog dialog = builder.create();
        dialog.show();

        //increase quantity of the product
        incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productQuantity < Integer.parseInt(modelProduct.getProductQuantity())) {
                    finalCost = finalCost + cost;
                    productQuantity++;

                    totalTv.setText("$" + finalCost);
                    quantityTv.setText("" + productQuantity);
                }
            }
        });

        decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productQuantity > 1)
                {
                    finalCost = finalCost - cost;
                    productQuantity--;

                    totalTv.setText("$" + finalCost);
                    quantityTv.setText("" + productQuantity);
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = nameTv.getText().toString().trim();
                String priceEach = productPrice;
                String totalPrice = totalTv.getText().toString().trim().replace("$", "");
                String productQuantity= quantityTv.getText().toString().trim();

                addToCart(productName, priceEach, totalPrice, productQuantity);
            }
        });
    }

    private void addToCart(String productName, String priceEach, String totalPrice, String productQuantity) {
        //add data to database
        progressDialog.setMessage("Adding To Cart...");
        progressDialog.show();

        String timestamp = "" + System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("name", "" + productName);
        hashMap.put("priceEach", "" + priceEach);
        hashMap.put("priceTotal", "" + totalPrice);
        hashMap.put("quantity", "" + productQuantity);
        hashMap.put("timestamp", ""+timestamp);
        hashMap.put("uid", ""+firebaseAuth.getUid());

        //add to database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Cart").child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //db updated
                        progressDialog.dismiss();
                        Toast.makeText(context, "Adding product...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //db failed to update
                        progressDialog.dismiss();
                        Toast.makeText(context, "" +e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    class HolderProductUser extends RecyclerView.ViewHolder {

        private ImageView productIconIv;
        private TextView nameTv, descriptionTv, priceTv, addToCartTv, discountPercentageTv, discountPriceTv;

        public HolderProductUser(@NonNull View itemView) {
            super(itemView);

            productIconIv = itemView.findViewById(R.id.productIconIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            descriptionTv = itemView.findViewById(R.id.descriptionTv);
            priceTv = itemView.findViewById(R.id.priceTv);
            addToCartTv = itemView.findViewById(R.id.addToCartTv);
            discountPercentageTv = itemView.findViewById(R.id.discountPercentageTv);
            discountPriceTv = itemView.findViewById(R.id.discountPriceTv);
        }
    }

    //overrides
    @Override
    public int getItemCount() {

        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new FilterProductUser(this, filterList);
        }
        return filter;
    }
}
