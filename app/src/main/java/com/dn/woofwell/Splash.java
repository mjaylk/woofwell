package com.dn.woofwell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {
    FirebaseAuth auth;

    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Create a new Handler to delay the start of MainActivity or redirect based on user login status
        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null) {
                redirectUser(currentUser.getUid());
            } else {
                // Start Login activity if the user is not logged in
                Intent i = new Intent(Splash.this, Login.class);
                startActivity(i);
                // Close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void redirectUser(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    String accountType = user.getAccountType();
                    Intent intent;
                    if ("Seller".equals(accountType)) {
                        intent = new Intent(Splash.this, SellerDashboard.class);
                    } else {
                        intent = new Intent(Splash.this, MainActivity.class);
                    }
                    intent.putExtra("userid", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Splash.this, "Error: User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Splash.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
