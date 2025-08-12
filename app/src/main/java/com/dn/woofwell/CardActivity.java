package com.dn.woofwell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;


public class CardActivity extends AppCompatActivity {

    private int itemCount;
    private String itemPrice,itemTitle,price,image,desc;
    private EditText etHolderName, etCardNumber, etYear;
    private TextView btnPayNow;
    private CartDB cartDB;
    private CardDB dbHelper;
    ImageView backButton;
    private List<Cart> list = new ArrayList<>();
    DatabaseReference db;
    String itemTotalPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        cartDB = new CartDB(this);
        list = cartDB.getCarts();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemCount = extras.getInt("itemCount");
            itemPrice = extras.getString("itemPrice");
            price = extras.getString("price");
            desc = extras.getString("desc");
            itemTitle = extras.getString("Titles");
            image = extras.getString("image");
            extras.getString("UserName");
            extras.getString("Email");
            extras.getString("Phone");
            extras.getString("FullName");
            extras.getString("Address");
            extras.getString("SellerId");
            extras.getString("ProductKey");
            extras.getString("Titles");
            extras.getString("Images");
            extras.getString("Image");
            itemTotalPrice = extras.getString("price");

        }


        etHolderName = findViewById(R.id.holderName);
        etCardNumber = findViewById(R.id.cardNumber);
        etYear = findViewById(R.id.year);
        btnPayNow = findViewById(R.id.addCard);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        dbHelper = new CardDB(this);

        Card card = dbHelper.getCard();

        if (card != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to use your existing card?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Set the values of the EditText fields
                            etHolderName.setText(card.getHolderName());
                            etCardNumber.setText(card.getCardNumber());
                            etYear.setText(card.getYear());
                        }
                    })
                    .setNegativeButton("No", null)
                    .create()
                    .show();
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        db = FirebaseDatabase.getInstance().getReference("Orders");

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String holderName = etHolderName.getText().toString().trim();
                String cardNumber = etCardNumber.getText().toString().trim();
                String year = etYear.getText().toString().trim();

                if (holderName.isEmpty() || cardNumber.isEmpty() || year.isEmpty()) {
                    Toast.makeText(CardActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    long result;

                    result = dbHelper.insertCard(holderName, cardNumber, year);


                    if (extras != null) {

                        String RESULT = extras.getString("RESULT");

                        if(RESULT != null) {

                            //Toast.makeText(BuyNow.this," Result is Null ",Toast.LENGTH_SHORT).show();

                        } else {
                            itemCount = extras.getInt("itemCount");
                            itemPrice = extras.getString("itemPrice");
                            price = extras.getString("price");
                            desc = extras.getString("desc");
                            itemTitle = extras.getString("Titles");
                            image = extras.getString("image");
                            extras.getString("UserName");
                            extras.getString("Email");
                            extras.getString("Phone");
                            extras.getString("FullName");
                            extras.getString("Address");
                            extras.getString("SinglePrice");
                            extras.getString("SingleQun");
                            extras.getString("SingleName");
                            extras.getString("isEmpty");
                            extras.getString("price");
                            extras.getString("Image");

                        }

                    }

                    for (Cart cart : list) {
                        String ItemTitles =   cart.getItemName();

                    }


                    {

                        if(extras.getString("isEmpty") != null) {

                            String singleName = extras.getString("SingleName");
                            String singleQun = extras.getString("SingleQun");
                            String singlePrice = extras.getString("SinglePrice");
                            String sellerId = extras.getString("SellerId");
                            String productKey = extras.getString("ProductKey");
                            String key = db.push().getKey();

                            DatabaseReference itemRef = db.child(sellerId).child(key);
                            itemRef.child("ItemName").setValue(singleName);
                            itemRef.child("ItemCount").setValue(singleQun);
                            itemRef.child("ItemPrice").setValue(singlePrice);
                            itemRef.child("ProductKey").setValue(productKey);
                            itemRef.child("SellerId").setValue(sellerId);
                            itemRef.child("Key").setValue(key);
                            itemRef.child("Address").setValue( extras.getString("Address"));
                            itemRef.child("Phone").setValue( extras.getString("Phone"));
                            itemRef.child("Email").setValue( extras.getString("Email"));
                            itemRef.child("Image").setValue( extras.getString("Image"));

                        }
                        else {
                            for (Cart cart : list) {
                                String itemName = cart.getItemName();
                                String itemCount = cart.getItemCount();
                                String itemPrice = cart.getItemTotal();
                                String img = cart.getItemImage();
                                String sellerIds = cart.getSellerId();
                                String productKey = cart.getKey();

                                String key = db.push().getKey();
                                DatabaseReference itemRef =  db.child(sellerIds).child(key);
                                itemRef.child("ItemName").setValue(itemName);
                                itemRef.child("ItemCount").setValue(itemCount);
                                itemRef.child("ItemPrice").setValue(itemPrice);
                                itemRef.child("ProductKey").setValue(productKey);
                                itemRef.child("SellerId").setValue(sellerIds);
                                itemRef.child("Key").setValue(key);
                                itemRef.child("ItemPrice").setValue(itemPrice);
                                itemRef.child("Address").setValue( extras.getString("Address"));
                                itemRef.child("FullName").setValue( extras.getString("FullName"));
                                itemRef.child("Phone").setValue( extras.getString("Phone"));
                                itemRef.child("Email").setValue( extras.getString("Email"));
                                itemRef.child("Image").setValue(img);

                            }

                        }
                        Intent intent = new Intent(CardActivity.this, Payment.class);

                        for(Cart cart : list) {


                            intent.putExtra("SingleName",extras.getString("SingleName"));
                            intent.putExtra("SingleQun", extras.getString("SingleQun"));
                            intent.putExtra("SingleImg", extras.getString("Image"));

                        }
                        intent.putExtra("Price",itemTotalPrice);
                        startActivity(intent);
                        cartDB.deleteAllItems();
                        list.clear();
                        finish();

                    }


                }
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
