package com.dn.woofwell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OrderDetail extends AppCompatActivity {

    TextView detailEmail, detailTitle, detailAdd,detailPhone,detailItems,detailItemsPrice,FullName,detailItemKey;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String sellerId = "";
    String imageUrl = "";
    String tt ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        detailEmail = findViewById(R.id.detailEmail);
        detailImage = findViewById(R.id.detailImage);
        detailTitle = findViewById(R.id.detailTitle);
        deleteButton = findViewById(R.id.deleteButton);
        detailPhone = findViewById(R.id.detailPhone);
        detailItems = findViewById(R.id.detailItems);
        detailItemsPrice = findViewById(R.id.detailItemsPrice);
        detailAdd = findViewById(R.id.detailAdd);
        FullName = findViewById(R.id.orderfullname);
        detailItemKey = findViewById(R.id.detailItemKey);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailEmail.setText(bundle.getString("Email"));
            detailTitle.setText(bundle.getString("Title"));
            detailAdd.setText(bundle.getString("Address"));
            detailPhone.setText(bundle.getString("Phone"));
            key = bundle.getString("Key");
            sellerId =bundle.getString("SellerId");

            detailItemKey.setText(key);

            detailItems.setText(bundle.getString("ItemCount"));
            detailItemsPrice.setText(bundle.getString("ItemPrice"));
            FullName.setText(bundle.getString("FullName"));
            tt =bundle.getString("Title");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(imageUrl).into(detailImage);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(sellerId).child(key);
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(OrderDetail.this, "Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Orders.class));
                        finish();
                    }
                });
            }
        });


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}