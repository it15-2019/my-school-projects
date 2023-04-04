package com.example.projekat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterCartItem extends RecyclerView.Adapter<AdapterCartItem.HolderCartItem> {

    private Context context;
    private ArrayList<ModelCartItem> cartItems;
    private HolderCartItem holder;

    public AdapterCartItem(Context context, ArrayList<ModelCartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public HolderCartItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_cart, parent, false);
        return new HolderCartItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCartItem holder, @SuppressLint("RecyclerView") int position) {

        //get data
        ModelCartItem modelCartItem = cartItems.get(position);
        String id = modelCartItem.getId();
        String uid = modelCartItem.getUid();
        String name = modelCartItem.getName();
        String priceEach = modelCartItem.getPriceEach();
        String priceTotal = modelCartItem.getPriceTotal();
        String quantity = modelCartItem.getQuantity();

        //set data
        holder.itemName.setText("" + name);
        holder.itemPriceEach.setText("$" + priceEach);
        holder.itemQuantity.setText("[" + quantity + "]");
        holder.itemPrice.setText("$" + priceTotal);

        holder.itemRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(id);
                //refresh list
                cartItems.remove(position);
                notifyItemChanged(position);
                notifyDataSetChanged();

                double oldTotal = Double.parseDouble((((CartActivity)context).totalPriceTv1.getText().toString().trim().replace("$", "")));
                double newTotal = oldTotal - Double.parseDouble(priceTotal);
                ((CartActivity)context).totalPriceTv1.setText("$" + String.format("%.2f", newTotal));
                ((CartActivity)context).totalPriceTv2.setText("$" + String.format("%.2f", newTotal + 10));
            }
        });

    }

    private void removeItem(String id) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).child("Cart").child(id).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Product removed...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class HolderCartItem extends RecyclerView.ViewHolder {

        //ui views of row_cart.xml
        private TextView itemName, itemPrice, itemPriceEach, itemQuantity, itemRemove;

        public HolderCartItem(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemNameTv);
            itemPrice = itemView.findViewById(R.id.itemPriceEachTv);
            itemPriceEach = itemView.findViewById(R.id.itemPriceTotalTv);
            itemQuantity = itemView.findViewById(R.id.itemQuantityTv);
            itemRemove = itemView.findViewById(R.id.itemRemoveTv);
        }
    }
}