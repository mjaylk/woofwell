package com.dn.woofwell;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateProduct extends AppCompatActivity {
    private ImageView image;
    private Button updateBtn;
    private EditText title, description, price, youtube, age, brand, type;
    private String imageURL, oldImageURL, key,userId,sellerName;
    private Uri uri;
    private ImageView backButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        // Initialize UI components
        image = findViewById(R.id.image);
        updateBtn = findViewById(R.id.updateBtn);
        type = findViewById(R.id.type);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        youtube = findViewById(R.id.youtube);
        age = findViewById(R.id.age);
        brand = findViewById(R.id.brand);
        backButton = findViewById(R.id.backButton);

        // Get data from intent
        Intent intent = getIntent();
        imageURL = intent.getStringExtra("Image");
        oldImageURL = imageURL; // Assign oldImageURL here
        String titleText = intent.getStringExtra("Title");
        String descriptionText = intent.getStringExtra("Description");
        String youtubeLink = intent.getStringExtra("Youtube");
        String ageText = intent.getStringExtra("Age");
        String brandText = intent.getStringExtra("Brand");
        String typeText = intent.getStringExtra("Type");
        key = intent.getStringExtra("Key");
        String priceText = intent.getStringExtra("Price");
        userId = intent.getStringExtra("uerId");

        // Set data to UI components
        title.setText(titleText);
        description.setText(descriptionText);
        price.setText(priceText);
        youtube.setText(youtubeLink);
        age.setText(ageText);
        brand.setText(brandText);
        type.setText(typeText);

        if (imageURL != null && !imageURL.isEmpty()) {
            Glide.with(this).load(imageURL).into(image);
        }

        // Activity result launcher for image picking
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                uri = data.getData();
                                image.setImageURI(uri);
                            }
                        } else {
                            Toast.makeText(UpdateProduct.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Products").child(userId).child(key);

        // Image click listener to pick image
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        // Back button click listener to finish activity
        backButton.setOnClickListener(v -> finish());

        // Update button click listener to save data
        updateBtn.setOnClickListener(v -> {
            saveData();
        });
    }

    public void saveData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProduct.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        if (uri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("Product" ).child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    fetchUserDetailsAndUpdateProduct(dialog);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(UpdateProduct.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            fetchUserDetailsAndUpdateProduct(dialog);
        }
    }
    private void fetchUserDetailsAndUpdateProduct(AlertDialog dialog) {
        DatabaseReference userDetail = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userDetail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sellerName = snapshot.child("name").getValue(String.class);
                    updateData(dialog);
                } else {
                    dialog.dismiss();
                    Log.d("UserDetail", "User data not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Log.e("UserDetail", "Failed to read user data: " + error.getMessage());
            }
        });
    }

    public void updateData(AlertDialog dialog) {
        String productTitle = title.getText().toString().trim();
        String productDescription = description.getText().toString().trim();
        String productBrand = brand.getText().toString().trim();
        String productType = type.getText().toString().trim();
        String productPrice = price.getText().toString().trim();
        String youtubeLink = youtube.getText().toString().trim();
        String productAge = age.getText().toString().trim();

        DataClass dataClass = new DataClass(productTitle, productDescription, productType, productBrand, productPrice + " LKR", youtubeLink, imageURL, productAge, key, sellerName, userId);

        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    if (oldImageURL != null && !oldImageURL.equals(imageURL)) {
                        StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                        reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(UpdateProduct.this, "Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateProduct.this, Product.class);
                                intent.putExtra("userid", userId);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateProduct.this, "Failed to delete old image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(UpdateProduct.this, "Updated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateProduct.this, Product.class);
                        intent.putExtra("userid", userId);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Toast.makeText(UpdateProduct.this, "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(UpdateProduct.this, "Failed to update product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
