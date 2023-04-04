package com.example.projekat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AdapterOrderUser extends RecyclerView.Adapter<AdapterOrderUser.HolderOrderUser> {

    private Context context;
    public ArrayList<ModelOrderUser> orderUserList;


    public AdapterOrderUser(Context context, ArrayList<ModelOrderUser> orderUserList) {
        this.context = context;
        this.orderUserList = orderUserList;
    }

    //class
    class HolderOrderUser extends RecyclerView.ViewHolder {

        private TextView orderId, orderDate, amount, status;

        public HolderOrderUser(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
        }
    }

    //overrides
    @NonNull
    @Override
    public HolderOrderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_order_user, parent, false);
        return new HolderOrderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderOrderUser holder, @SuppressLint("RecyclerView") int position) {

        //get data
        ModelOrderUser modelOrderUser = orderUserList.get(position);
        String orderId = modelOrderUser.getOrderId();
        String orderDate = modelOrderUser.getOrderDate();
        String orderBy = modelOrderUser.getOrderBy();
        String orderCost = modelOrderUser.getOrderCost();
        String orderStatus = modelOrderUser.getOrderStatus();

        //set data
        holder.amount.setText("Amount: $" + orderCost);
        holder.status.setText(orderStatus);
        holder.orderId.setText("OrderID: " + orderId);

        if(orderStatus.equals("Progressing")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.blue));
        }
        else if(orderStatus.equals("Completed")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else if(orderStatus.equals("Cancelled")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.colorAccent));
        }

        //convert timestamp to proper format
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formatDate = df.format(c);

        holder.orderDate.setText(formatDate);

        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Status Of Order")
                        .setItems(Categories2.orderCategories, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //get picked category
                                String orderCategory = Categories2.orderCategories[which];
                                //set picked category
                                holder.status.setText(orderCategory);

                                if(orderCategory.equals("Progressing")) {
                                    holder.status.setTextColor(context.getResources().getColor(R.color.blue));
                                }
                                else if(orderCategory.equals("Completed")) {
                                    holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                                }
                                else if(orderCategory.equals("Cancelled")) {
                                    holder.status.setTextColor(context.getResources().getColor(R.color.colorAccent));
                                }
                                updateOrder(orderId, orderDate, orderCategory, orderBy, orderCost);
                            }
                        }).show();
            }
        });

    }

    private void updateOrder(String id, String date, String oStatus, String by, String oCost) {

        //init others
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.setMessage("Updating order status...");
        progressDialog.show();


        //set up order data
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("orderId", id);
        hashMap.put("orderTime", date);
        hashMap.put("orderStatus", oStatus);
        hashMap.put("orderCost", oCost);
        hashMap.put("orderBy", by);

        //update to database
        DatabaseReference  ref = FirebaseDatabase.getInstance().getReference("Users").child("Orders");
        ref.child(id).updateChildren(hashMap)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //updated
                    progressDialog.dismiss();
                    Toast.makeText(context,"Status updated succesfully!", Toast.LENGTH_SHORT).show();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // failed updating profile
                    progressDialog.dismiss();
                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }



    @Override
    public int getItemCount() {
        return orderUserList.size();
    }
}
