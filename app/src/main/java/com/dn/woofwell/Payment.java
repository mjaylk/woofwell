package com.dn.woofwell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Payment extends AppCompatActivity {

    TextView Main,paymentPrice;
    ImageView backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentPrice = findViewById(R.id.paymentPrice);
        Main = findViewById(R.id.seeOrders);
        backToMain = findViewById(R.id.backToMain);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            String price = bundle.getString("Price");
            paymentPrice.setText(price + " LKR");
        }


        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }}