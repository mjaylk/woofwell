package com.dn.woofwell;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class ProfileFragment extends Fragment {

    TextView profileName, profileEmail, profileUsername, profileNumber, address;
    TextView titleName, titleUsername;
    Button editProfile, signOut;
    DatabaseReference userRef;
    ImageView profileImg;
    AlertDialog dialog;

    private static final int PICK_IMAGE_FROM_GALLERY = 1;
    private static final int CAPTURE_IMAGE_FROM_CAMERA = 2;

    private Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileUsername = view.findViewById(R.id.profileUsername);
        profileNumber = view.findViewById(R.id.profileNumber);
        titleName = view.findViewById(R.id.titleName);
        titleUsername = view.findViewById(R.id.titleUsername);
        editProfile = view.findViewById(R.id.editButton);
        profileImg = view.findViewById(R.id.profileImg);
        signOut = view.findViewById(R.id.signOut);
        address = view.findViewById(R.id.address);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        dialog = builder.create();



        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isAdded() && dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    String profileImageUrl = user.getProfileImageUrl();

                    titleName.setText(user.getName());
                    titleUsername.setText(user.getFullName());
                    profileName.setText(user.getFullName());
                    profileEmail.setText(user.getEmail());
                    profileNumber.setText(user.getPhoneNumber());
                    profileUsername.setText(user.getName());
                    address.setText(user.getAddress());
                    String key = user.getUserId();





                    editProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(),EditProfile.class);
                            intent.putExtra("UserName",user.getName());
                            intent.putExtra("Email",user.getEmail());
                            intent.putExtra("Phone",user.getPhoneNumber());
                            intent.putExtra("FullName",user.getFullName());
                            intent.putExtra("Image",user.getProfileImageUrl());
                            intent.putExtra("Address",user.getAddress());

                            startActivity(intent);

                        }
                    });

                    if (profileImageUrl != null) {
                        if (isAdded()) { // Check if the fragment is attached
                            Glide.with(ProfileFragment.this)
                                    .load(profileImageUrl)
                                    .placeholder(R.drawable.proi)
                                    .error(R.drawable.proi)
                                    .into(profileImg);
                        }
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), Login.class));
                getActivity().finish();


            }
        });
        profileImg = view.findViewById(R.id.profileImg);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder imagePickerDialog = new AlertDialog.Builder(getContext());
                imagePickerDialog.setTitle("Select Image");
                imagePickerDialog.setItems(new CharSequence[]{"From Gallery", "From Camera"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY);
                            break;
                        case 1:
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAPTURE_IMAGE_FROM_CAMERA);
                            break;
                    }
                });
                imagePickerDialog.show();
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_FROM_GALLERY) {
                selectedImageUri = data.getData();
            } else if (requestCode == CAPTURE_IMAGE_FROM_CAMERA) {
                selectedImageUri = createImageUri();
            }

            if (selectedImageUri != null) {

                profileImg.setImageURI(selectedImageUri);

                final StorageReference profileImageRef = FirebaseStorage.getInstance()
                        .getReference("profileImages/" + FirebaseAuth.getInstance().getCurrentUser().getUid());

                UploadTask uploadTask = profileImageRef.putFile(selectedImageUri);
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        String profileImageUrl = uri1.toString();


                        userRef.child("profileImageUrl").setValue(profileImageUrl);

                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).addOnFailureListener(e -> {

                    Toast.makeText(getContext(), "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        } else {
            Toast.makeText(getContext(), "Image selection cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    private Uri createImageUri() {
        File imageFile = new File(getContext().getExternalCacheDir(), "temp_image.jpg");
        return FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", imageFile);
    }

}
