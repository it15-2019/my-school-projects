package com.example.projekat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterProductAdmin extends RecyclerView.Adapter<AdapterProductAdmin.HolderProductAdmin> implements  Filterable {

    private Context context;
    public ArrayList<ModelProduct> productList, filterList;
    private FilterProduct filter;

    public AdapterProductAdmin(Context context, ArrayList<ModelProduct> productList) {
        this.context = context;
        this.productList = productList;
        this.filterList = productList;
    }

    private void detailsBottomSheet(ModelProduct modelProduct) {
        //bottom sheet
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        //inflate view for bottom sheet
        View view = LayoutInflater.from(context).inflate(R.layout.product_details_admin, null);
        // set view to bottom sheet
        bottomSheetDialog.setContentView(view);

        //init views of bottom sheet
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton deleteBtn = view.findViewById(R.id.deleteBtn);
        ImageButton editBtn = view.findViewById(R.id.editBtn);
        ImageView productIconIv = view.findViewById(R.id.productIconIv);
        TextView discountPercentageTv = view.findViewById(R.id.discountPercentageTv);
        TextView nameTv = view.findViewById(R.id.nameTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);
        TextView categoryTv = view.findViewById(R.id.categoryTv);
        TextView quantityTv = view.findViewById(R.id.quantityTv);
        TextView discountPriceTv = view.findViewById(R.id.discountPriceTv);
        TextView priceTv = view.findViewById(R.id.priceTv);

        //get data
        String productId = modelProduct.getProductId();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountPercentage = modelProduct.getProductDiscountPercentage();
        String discountPrice = modelProduct.getProductDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String productDescription = modelProduct.getProductDescription();
        String productImage = modelProduct.getProductImage();
        String productQuantity = modelProduct.getProductQuantity();
        String productName = modelProduct.getProductName();
        String productPrice = modelProduct.getProductPrice();
        String timestamp = modelProduct.getTimestamp();

        //set data
        nameTv.setText(productName);
        descriptionTv.setText(productDescription);
        categoryTv.setText(productCategory);
        quantityTv.setText(productQuantity);
        discountPercentageTv.setText(discountPercentage + "% OFF");
        discountPriceTv.setText("$" + discountPrice);
        priceTv.setText("$" + productPrice);

        if(discountAvailable.equals("true")) {
            discountPriceTv.setVisibility(View.VISIBLE);
            discountPercentageTv.setVisibility(View.VISIBLE);
            discountPriceTv.setTextSize(25);
            priceTv.setTextSize(25);

            // add strike trough on price
            priceTv.setPaintFlags(priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            discountPriceTv.setVisibility(View.GONE);
            discountPercentageTv.setVisibility(View.GONE);
            priceTv.setTextSize(25);

        }

        try {
            Picasso.get().load(productImage).placeholder(R.drawable.ic_add_to_cart_white)
                    .into(productIconIv);
        }
        catch(Exception e) {
            productIconIv.setImageResource(R.drawable.ic_add_to_cart_white);
        }

        //show dialog
        bottomSheetDialog.show();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit product activity, pass id of product
                bottomSheetDialog.dismiss();
                Intent intent= new Intent(context, EditProductActivity.class);
                intent.putExtra("productId", productId);
                context.startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show delete confirm dialog
                bottomSheetDialog.dismiss();
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete " + productName + " ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduct(productId);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancel dialog
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss bottom sheet
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void deleteProduct(String id) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Products");
        ref.child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Product deleted...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //class
    class HolderProductAdmin extends RecyclerView.ViewHolder {

        //holds views of recycleview
        private ImageView productIconIv, nextIv;
        private TextView discountPercentageTv, nameTv, quantityTv, priceTv, discountPriceTv;

        public HolderProductAdmin(@NonNull View itemView) {
            super(itemView);

            productIconIv = itemView.findViewById(R.id.productIconIv);
            nextIv = itemView.findViewById(R.id.nextIv);
            discountPercentageTv = itemView.findViewById(R.id.discountPercentageTv);
            nameTv = itemView.findViewById(R.id.nameTv);
            quantityTv = itemView.findViewById(R.id.quantityTv);
            discountPriceTv = itemView.findViewById(R.id.discountPriceTv);
            priceTv = itemView.findViewById(R.id.priceTv);
        }
    }

    //overrides
    @NonNull
    @Override
    public HolderProductAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_product_admin, parent, false);
        return new HolderProductAdmin(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderProductAdmin holder, int position) {
        //get data
        ModelProduct modelProduct = productList.get(position);
        String productId = modelProduct.getProductId();
        String discountAvailable = modelProduct.getDiscountAvailable();
        String discountPercentage = modelProduct.getProductDiscountPercentage();
        String discountPrice = modelProduct.getProductDiscountPrice();
        String productCategory = modelProduct.getProductCategory();
        String productDescription = modelProduct.getProductDescription();
        String productImage = modelProduct.getProductImage();
        String productQuantity = modelProduct.getProductQuantity();
        String productName = modelProduct.getProductName();
        String productPrice = modelProduct.getProductPrice();
        String timestamp = modelProduct.getTimestamp();

        //set data
        holder.nameTv.setText(productName);
        holder.quantityTv.setText(productQuantity);
        holder.discountPercentageTv.setText(discountPercentage + "% OFF");
        holder.discountPriceTv.setText("$" + discountPrice);
        holder.priceTv.setText("$" + productPrice);


        if(discountAvailable.equals("true")) {
            //product is on discount
            holder.discountPriceTv.setVisibility(View.VISIBLE);
            holder.discountPercentageTv.setVisibility(View.VISIBLE);
            holder.discountPriceTv.setTextSize(20);
            holder.priceTv.setTextSize(20);

            //add strike through original price
            holder.priceTv.setPaintFlags(holder.priceTv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            //product is not on discount
            holder.discountPriceTv.setVisibility(View.GONE);
            holder.discountPercentageTv.setVisibility(View.GONE);
            holder.priceTv.setTextSize(20);
        }

        try {
            Picasso.get().load(productImage).placeholder(R.drawable.ic_add_to_cart_green).into(holder.productIconIv);
        }
        catch(Exception e) {
            holder.productIconIv.setImageResource(R.drawable.ic_add_to_cart_green);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle item clicks, show item details(in bottom sheet)
                detailsBottomSheet(modelProduct); //here modelProduct contains details of clicked product
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new FilterProduct(this, filterList);
        }
        return filter;
    }
}