package com.dn.woofwell;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.util.Calendar;

public class AddProduct extends AppCompatActivity {

    private ImageView image;
    private Button addBtn;
    private EditText title, description, price, youtube, age, brand, type;
    private String imageURL,userId,sellerName;
    private Uri uri;
    private ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        image = findViewById(R.id.image);
        addBtn = findViewById(R.id.addBtn);
        type = findViewById(R.id.type);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        youtube = findViewById(R.id.youtube);
        age = findViewById(R.id.age);
        brand = findViewById(R.id.brand);
        backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        userId= intent.getStringExtra("userid");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            image.setImageURI(uri);
                        } else {
                            Toast.makeText(AddProduct.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {
                    saveData();
                } else {
                    Toast.makeText(AddProduct.this, "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void saveData() {
        if (uri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Product")
                .child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(AddProduct.this);
        builder.setCancelable(false);
        builder.setView(R.layout.upload_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                uriTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri urlImage = task.getResult();
                            imageURL = urlImage.toString();
                            fetchUserDetailsAndAddProduct(dialog);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(AddProduct.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(AddProduct.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void fetchUserDetailsAndAddProduct(AlertDialog dialog) {
        DatabaseReference userDetail = FirebaseDatabase.getInstance().getReference("users").child(userId);

        userDetail.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sellerName = snapshot.child("name").getValue(String.class);
                    addProduct(dialog);
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

    private void addProduct(AlertDialog dialog) {
        String productTitle = title.getText().toString().trim();
        String productType = type.getText().toString().trim();
        String productDescription = description.getText().toString().trim();
        String productPrice = price.getText().toString().trim();
        String youtubeLink = youtube.getText().toString().trim();
        String productAge = age.getText().toString().trim();
        String productBrand = brand.getText().toString().trim();

        if (TextUtils.isEmpty(productTitle) || TextUtils.isEmpty(productDescription) || TextUtils.isEmpty(productType) ||
                TextUtils.isEmpty(productPrice) || TextUtils.isEmpty(youtubeLink) ||
                TextUtils.isEmpty(productAge) || TextUtils.isEmpty(productBrand)) {
            dialog.dismiss();
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int ageValue = Integer.parseInt(productAge);
            if (ageValue < 0 || ageValue > 120) {
                dialog.dismiss();
                Toast.makeText(this, "Please enter a valid age between 0 and 120", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            dialog.dismiss();
            Toast.makeText(this, "Invalid age format", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products").child(userId);
        String key = productsRef.push().getKey();

        DataClass dataClass = new DataClass(productTitle, productDescription, productType, productBrand, productPrice + " LKR", youtubeLink, imageURL, productAge, key, sellerName, userId);

        productsRef.child(key).setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(AddProduct.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(AddProduct.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(AddProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        title.setText("");
        type.setText("");
        description.setText("");
        price.setText("");
        youtube.setText("");
        age.setText("");
        brand.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
