package com.dn.woofwell;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerDashboard extends AppCompatActivity {
    CardView card1,card2,card3;
    ImageView backButton;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        userId= intent.getStringExtra("userid");

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerDashboard.this, Product.class);
                intent.putExtra("userid",userId);
                startActivity(intent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerDashboard.this, Orders.class);
                intent.putExtra("userid",userId);
                startActivity(intent);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerDashboard.this, EditProfile.class);
                intent.putExtra("userid",userId);
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(userId);
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String userName = snapshot.child("name").getValue(String.class);
                            String email = snapshot.child("email").getValue(String.class);
                            String fullName = snapshot.child("fullName").getValue(String.class);

                            String phone = snapshot.child("phoneNumber").getValue(String.class);
                            String address = snapshot.child("address").getValue(String.class);

                           intent.putExtra("Phone",phone);
                            intent.putExtra("Address",address);
                            intent.putExtra("Email",email);
                            intent.putExtra("UserName",userName);
                            intent.putExtra("FullName",fullName);
                            intent.putExtra("Seller","seller");
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SellerDashboard.this, Login.class));
                finish();
            }
        });
    }
}