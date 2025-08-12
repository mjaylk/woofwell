package com.dn.woofwell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    TextView checkUserName;
    TextView checkUserAddress;
    TextView finalItemName;
    TextView finalItemPrice;
    TextView finalItemQun;
    TextView checkBuy;
    TextView totalQun, checkUserNumber, checkUserEmail;

    ImageView finalCheckImg, checkBackButton, editDetails;
    private CheckAdapter adapter;
    private List<Cart> LastList = new ArrayList<>();
    private CartDB cartDB;
    RecyclerView checkRv;
    LinearLayout rvLayout, singleLayout;
    String sendTotal;
    String titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkUserName = findViewById(R.id.checkUserName);
        checkUserAddress = findViewById(R.id.checkUserAddress);
        finalItemName = findViewById(R.id.finalItemName);
        finalItemPrice = findViewById(R.id.finalItemPrice);
        finalItemQun = findViewById(R.id.finalItemQun);
        checkUserNumber = findViewById(R.id.checkUserNumber);
        checkUserEmail = findViewById(R.id.checkUserEmail);
        finalCheckImg = findViewById(R.id.finalCheckImg);
        checkBuy = findViewById(R.id.checkBuy);
        checkRv = findViewById(R.id.checkRv);
        rvLayout = findViewById(R.id.rvLayout);
        singleLayout = findViewById(R.id.singleLayout);
        checkBackButton = findViewById(R.id.checkBackButton);
        totalQun = findViewById(R.id.totalQun);
        editDetails = findViewById(R.id.editDetails);

        cartDB = new CartDB(this);
        LastList = cartDB.getCarts();
        adapter = new CheckAdapter(LastList, this);
        singleLayout.setVisibility(View.GONE);
        rvLayout.setVisibility(View.VISIBLE);
        checkRv.setLayoutManager(new LinearLayoutManager(this));
        checkRv.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();

        int itemsCount = 0;
        double totalPriceValue = 0;

        for (Cart cart : LastList) {
            titles = cart.getItemName();
            int conv1 = Integer.parseInt(cart.getItemCount());
            itemsCount += conv1;
            String itemC = String.valueOf(itemsCount);
            String itemTotal = cart.getSinglePrice();
            sendTotal = itemTotal;
            String image = cart.getItemImage();

            double numericValue = Double.parseDouble(itemTotal.replaceAll("[^\\d.]", ""));
            totalPriceValue += conv1 * numericValue;
            totalQun.setText(itemC);
        }

        checkBuy.setText(String.format("PAY %.2f LKR", totalPriceValue));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("users").child(userId);

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User userHome = dataSnapshot.getValue(User.class);

                        String textWithHello = userHome.getFullName();
                        checkUserName.setText(textWithHello);
                        checkUserAddress.setText(userHome.getAddress());
                        checkUserNumber.setText(userHome.getPhoneNumber());
                        checkUserEmail.setText(userHome.getEmail());

                        if (!checkUserName.getText().toString().isEmpty() &&
                                !checkUserAddress.getText().toString().isEmpty() &&
                                !checkUserNumber.getText().toString().isEmpty() &&
                                !checkUserEmail.getText().toString().isEmpty()) {
                            checkBuy.setEnabled(true);
                        } else {
                            Intent intent = new Intent(Checkout.this, EditProfile.class);
                            intent.putExtra("UserName", userHome.getName());
                            intent.putExtra("Email", userHome.getEmail());
                            intent.putExtra("Phone", userHome.getPhoneNumber());
                            intent.putExtra("FullName", userHome.getFullName());
                            intent.putExtra("Address", userHome.getAddress());

                            startActivity(intent);
                        }

                        editDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Checkout.this, EditProfile.class);
                                intent.putExtra("UserName", userHome.getName());
                                intent.putExtra("Email", userHome.getEmail());
                                intent.putExtra("Phone", userHome.getPhoneNumber());
                                intent.putExtra("FullName", userHome.getFullName());
                                intent.putExtra("Address", userHome.getAddress());

                                startActivity(intent);
                            }
                        });

                        checkBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Checkout.this, CardActivity.class);
                                intent.putExtra("UserName", userHome.getName());
                                intent.putExtra("Email", userHome.getEmail());
                                intent.putExtra("Phone", userHome.getPhoneNumber());
                                intent.putExtra("FullName", userHome.getFullName());
                                intent.putExtra("Address", userHome.getAddress());

                                String qun = totalQun.getText().toString().trim();
                                intent.putExtra("itemCount", qun);

                                String singlename = bundle.getString("title");
                                String singlePrice = bundle.getString("itemPrice");
                                String singleQun = bundle.getString("itemCount");
                                String image = bundle.getString("image");
                                String sellerId = bundle.getString("SellerId");
                                String productKey = bundle.getString("ProductKey");


                                if (singlename != null && singlePrice != null && singleQun != null && bundle.getString("image") != null && sellerId !=null  && productKey != null) {
                                    intent.putExtra("SinglePrice", singlePrice);
                                    intent.putExtra("SingleQun", singleQun);
                                    intent.putExtra("SingleName", singlename);
                                    intent.putExtra("SellerId", sellerId);
                                    intent.putExtra("ProductKey", productKey);
                                    intent.putExtra("isEmpty", "FALSE");
                                    intent.putExtra("Image", image);
                                } else {
                                    for (Cart cart : LastList) {
                                        String images = cart.getItemImage();
                                        intent.putExtra("Images", images);
                                        intent.putExtra("SellerId", cart.getSellerId());
                                        intent.putExtra("ProductKey", cart.getKey());
                                        intent.putExtra("Titles", cart.getItemName());
                                    }
                                }

                                String pri = checkBuy.getText().toString().replaceAll("[^\\d.]", "");
                                intent.putExtra("price", pri);
                                startActivity(intent);
                            }
                        });
                    } else {
                        Log.d("TAG", "User not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("TAG", "Database error: " + error.getMessage());
                }
            });
        } else {
            Log.d("TAG", "User not logged in");
        }

        checkBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (bundle != null) {
            String RESULT = bundle.getString("RESULT");
            if (RESULT == null) {
                singleLayout.setVisibility(View.GONE);
                rvLayout.setVisibility(View.VISIBLE);
            } else {
                singleLayout.setVisibility(View.VISIBLE);
                rvLayout.setVisibility(View.GONE);
                finalItemName.setText(bundle.getString("title"));

                String buttonPay = "PAY " + bundle.getString("itemPrice")+" LKR";
                checkBuy.setText(buttonPay);
                totalQun.setText(bundle.getString("itemCount"));
                finalItemPrice.setText(bundle.getString("itemPrice")+" LKR");
                finalItemQun.setText(bundle.getString("itemCount"));

                Glide.with(this).load(bundle.getString("image")).into(finalCheckImg);
            }
        }
    }
}
