package com.dn.woofwell;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProductDetail extends AppCompatActivity {

    private ImageView image;
    private Button addBtn;
    private TextView title, description, price, youtube, age, brand, type;
    private ImageView backButton;
    private FloatingActionButton deleteButton, editButton;

    private String key = "";
    private String imageUrl = "";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        auth = FirebaseAuth.getInstance();

        image = findViewById(R.id.image);
        type = findViewById(R.id.type);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        youtube = findViewById(R.id.youtube);
        age = findViewById(R.id.age);
        brand = findViewById(R.id.brand);
        backButton = findViewById(R.id.backButton);
        deleteButton = findViewById(R.id.deleteButton);
        editButton= findViewById(R.id.editButton);

        // Retrieve data from Intent
        Intent intent = getIntent();
        String imageURL = intent.getStringExtra("Image");
        String titleText = intent.getStringExtra("Title");
        String descriptionText = intent.getStringExtra("Description");
        String youtubeLink = intent.getStringExtra("Youtube");
        String ageText = intent.getStringExtra("Age");
        String brandText = intent.getStringExtra("Brand");
        String typeText = intent.getStringExtra("Type");
        key = intent.getStringExtra("Key");
        String priceText = intent.getStringExtra("Price");

        // Get the current user
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();


            // Set the data to UI components
            title.setText(titleText);
            description.setText(descriptionText);
            price.setText(priceText);
            youtube.setText(youtubeLink);
            age.setText(ageText);
            brand.setText(brandText);
            type.setText(typeText);


            if (imageURL != null && !imageURL.isEmpty()) {
                imageUrl = imageURL;

                Glide.with(this).load(imageURL).into(image);
            }

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProductDetail.this, UpdateProduct.class);
                    intent.putExtra("Image", imageUrl);
                    intent.putExtra("Title", title.getText().toString());
                    intent.putExtra("Description", description.getText().toString());
                    intent.putExtra("Youtube", youtube.getText().toString());
                    intent.putExtra("Age", age.getText().toString());
                    intent.putExtra("Brand", brand.getText().toString());
                    intent.putExtra("Type", type.getText().toString());
                    intent.putExtra("Key", key);
                    intent.putExtra("Price", price.getText().toString());
                    intent.putExtra("uerId",currentUser.getUid().toString());
                    startActivity(intent);
                }
            });


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Products").child(userId).child(key);
                        FirebaseStorage storage = FirebaseStorage.getInstance();

                        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.removeValue();
                                Toast.makeText(ProductDetail.this, "Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Product.class));
                                finish();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(ProductDetail.this, "Failed to delete image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Toast.makeText(ProductDetail.this, "Image URL is not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            backButton.setOnClickListener(v -> finish());

        } else {

            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();

        }
    }
}
