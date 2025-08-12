package com.dn.woofwell;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detail extends AppCompatActivity {
    TextView itemDesc, itemTitle, itemPrice, itemHead, total,age,type,brand,proRates,sellerName;
    ImageView itemlImage, backButton, cartButton;
    String key = "";
    String imageUrl = "";
    String allIt = "";
    TextView numberTxt;
    BottomSheetDialog bottomSheetDialog,bottomSheetDialog2;
    CardView webViewCard;
    String videoUrl,sellerId,userId,name;

    RecyclerView reviewRv;
    Button reviewBtn;

    ReviewAdapter reviewAdapter;
    List<DataClass> dataList;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        itemDesc = findViewById(R.id.itemDesc);
        itemlImage = findViewById(R.id.itemImage);
        itemHead = findViewById(R.id.itemHead);
        itemPrice = findViewById(R.id.itemPrice);
        itemTitle = findViewById(R.id.itemTitle);
        age = findViewById(R.id.age);
        type = findViewById(R.id.type);
        brand = findViewById(R.id.brand);
        sellerName = findViewById(R.id.sellerName);
        reviewRv = findViewById(R.id.reviewRv);
        reviewBtn = findViewById(R.id.reviewBtn);
        backButton = findViewById(R.id.backButton);
        cartButton= findViewById(R.id.cartButton);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        userId = currentUser.getUid().toString();


        WebView webView = findViewById(R.id.webView);
        webViewCard = findViewById(R.id.webViewCard);

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog2 =new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet);
        bottomSheetDialog2.setContentView(R.layout.bottom_review_sheet);

        numberTxt = bottomSheetDialog.findViewById(R.id.numberTxt);
        ImageView addItem = bottomSheetDialog.findViewById(R.id.addItem);
        ImageView removeItem = bottomSheetDialog.findViewById(R.id.removeItem);
        TextView buyAll = bottomSheetDialog.findViewById(R.id.buyAll);
        TextView addAll = bottomSheetDialog.findViewById(R.id.addAll);
        total = bottomSheetDialog.findViewById(R.id.total);


        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Detail.this, MainActivity.class);
                intent.putExtra("fragment", "cart");
                startActivity(intent);
                finish();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemDesc.setText(bundle.getString("Description"));
            itemHead.setText(bundle.getString("Title"));
            itemTitle.setText(bundle.getString("Title"));
            String price = bundle.getString("Price");
            itemPrice.setText(formatPrice(price));
            videoUrl = bundle.getString("Youtube");
            total.setText(bundle.getString("Price"));
            key = bundle.getString("Key");
            allIt = bundle.getString("cartItem");
            imageUrl = bundle.getString("Image");
            age.setText(bundle.getString("Age"));
            type.setText(bundle.getString("Type"));
            brand.setText(bundle.getString("Brand"));
            sellerName.setText(bundle.getString("SellerName"));
            sellerId = (bundle.getString("SellerId")) ;



            Glide.with(this).load(imageUrl).into(itemlImage);

            if (allIt != null) {
                numberTxt.setText(allIt);
                total.setText(bundle.getString("total"));
            } else {
                numberTxt.setText("1");
                total.setText(bundle.getString("Price"));
            }

            addItem.setOnClickListener(v -> {
                try {
                    int count = Integer.parseInt(numberTxt.getText().toString());
                    if (count < 50000) {
                        count++;
                        numberTxt.setText(String.valueOf(count));
                        String finalPrice = itemPrice.getText().toString().replace("LKR", "").trim();
                        updateItemPrice(finalPrice, count);
                    } else {
                        Toast.makeText(Detail.this, "You can only add 50000 items", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(Detail.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            });

            removeItem.setOnClickListener(v -> {
                try {
                    int count = Integer.parseInt(numberTxt.getText().toString());
                    count--;
                    if (count >= 1) {
                        numberTxt.setText(String.valueOf(count));
                        String finalPrice = itemPrice.getText().toString().replace("LKR", "").trim();
                        updateItemPrice(finalPrice, count);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(Detail.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            });

            buyAll.setOnClickListener(v -> {
                Intent intent = new Intent(Detail.this, Checkout.class);
                String sendItem = numberTxt.getText().toString();
                String sendTotal = total.getText().toString();

                intent.putExtra("itemCount", sendItem);
                intent.putExtra("itemPrice", sendTotal);
                intent.putExtra("price", sendItem);
                intent.putExtra("desc", itemDesc.getText().toString());
                intent.putExtra("title", itemTitle.getText().toString());
                intent.putExtra("image", imageUrl);
                intent.putExtra("SellerId", sellerId);
                intent.putExtra("ProductKey", key);
                String FROM = "DetailActivity";
                intent.putExtra("FROM", FROM);
                String RESULT = "OK";
                intent.putExtra("RESULT", RESULT);

                bottomSheetDialog.dismiss();
                startActivity(intent);
            });

            addAll.setOnClickListener(v -> {
                String addItemsStr = numberTxt.getText().toString();
                int addItems = Integer.parseInt(addItemsStr);

                String addTotStr = total.getText().toString().replaceAll("[^\\d.]+", "");
                if (addTotStr.isEmpty()) {
                    addTotStr = "1";
                }
                int addTot = (int) Double.parseDouble(addTotStr);

                int getSinglePrice = addTot / addItems;
                String singlePrice = String.valueOf(getSinglePrice);

                String addTotals = addTotStr ;
                String itemsName = itemTitle.getText().toString();
                String itemsDesc = itemDesc.getText().toString();

                Toast.makeText(this, addItemsStr, Toast.LENGTH_SHORT).show();
                CartDB cartDB = new CartDB(Detail.this);
                String brandName = brand.getText().toString();
                String typeName = type.getText().toString();
                String ageSize = age.getText().toString();
                String seller = sellerName.getText().toString();
                long result = cartDB.addCarts(itemsName,addItemsStr, addTotals, itemsDesc, imageUrl, singlePrice,videoUrl,brandName,typeName,ageSize,seller,sellerId,key);

                if (result > -1) {
                    Toast.makeText(Detail.this, "Added to the Cart", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                } else {
                    Toast.makeText(Detail.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

        findViewById(R.id.buyNow).setOnClickListener(v -> bottomSheetDialog.show());
        findViewById(R.id.addCart).setOnClickListener(v -> bottomSheetDialog.show());

        if (videoUrl != null && !videoUrl.isEmpty()) {
            String videoEmbedUrl = "https://www.youtube.com/embed/" + extractVideoId(videoUrl);
            webView.loadUrl(videoEmbedUrl);
            webViewCard.setVisibility(View.VISIBLE);
        } else {
            webViewCard.setVisibility(View.GONE);
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog2.show();
                Button btnPost = bottomSheetDialog2.findViewById(R.id.btnPost);
                EditText reviewComment = bottomSheetDialog2.findViewById(R.id.reviewComment);

                btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = reviewComment.getText().toString();

                        if (TextUtils.isEmpty(comment)) {
                            Toast.makeText(Detail.this, "Write a comment to post", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Products").child(sellerId).child(key).child("Reviews");
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                            userRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    name = snapshot.child("name").getValue(String.class);
                                    String reviewId = reviewRef.push().getKey();


                                    Map<String, Object> reviewData = new HashMap<>();
                                    reviewData.put("review", comment);
                                    reviewData.put("name", name);
                                    reviewRef.child(reviewId).setValue(reviewData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Detail.this, "Review posted successfully", Toast.LENGTH_SHORT).show();
                                                reviewComment.setText("");
                                                bottomSheetDialog2.dismiss();
                                            } else {
                                                Toast.makeText(Detail.this, "Failed to post review", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }
                    }
                });
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(Detail.this, 1);
        reviewRv.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(Detail.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(Detail.this, dataList);
        reviewRv.setAdapter(reviewAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(sellerId).child(key).child("Reviews");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    DataClass data = reviewSnapshot.getValue(DataClass.class);

                    if (data != null) {
                        dataList.add(data);
                    }
                }
                reviewAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Detail.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        };

        databaseReference.addValueEventListener(eventListener);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private String extractVideoId(String url) {
        if (url.contains("youtu.be/")) {
            return url.substring(url.lastIndexOf("/") + 1).split("\\?")[0];
        } else if (url.contains("youtube.com/watch?v=")) {
            return url.substring(url.indexOf("v=") + 2).split("&")[0];
        }
        return "";
    }

    private String formatPrice(String price) {
        String numericPart = price.replaceAll("[^\\d.]", "");
        return numericPart.isEmpty() ? "0" : numericPart + " LKR";
    }

    private void updateItemPrice(String price, int count) {
        double itemPrice = Double.parseDouble(price);
        double totalPrice = itemPrice * count;
        String formattedPrice;
        if (count <= 50000) {
            formattedPrice = String.format(" %.0f LKR", totalPrice);
        } else {
            formattedPrice = "LKR -";
            Toast.makeText(Detail.this, "50000 is the Limit", Toast.LENGTH_SHORT).show();
        }
        total.setText(formattedPrice);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

