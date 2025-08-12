package com.dn.woofwell;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    RecyclerView productRv;
    List<DataClass> dataList;
    DisplayAdapter displayAdapter;
    String userId;
    SearchView searchView;
    private String imageUrl;
    CircleImageView profileImageHome;
    TextView txtHello;
    DatabaseReference db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        productRv = view.findViewById(R.id.productRv);
        searchView = view.findViewById(R.id.searchView);
        txtHello =view.findViewById(R.id.txtHello);
        profileImageHome = view.findViewById(R.id.profileImageHome);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        productRv.setLayoutManager(gridLayoutManager);


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            db = FirebaseDatabase.getInstance().getReference("users").child(userId);

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (isAdded() && dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        imageUrl = user.getProfileImageUrl();

                        String textWithHello = "Hello " + user.getName();
                        txtHello.setText(textWithHello);

                        if (imageUrl != null) {
                            Glide.with(getContext()).load(imageUrl)
                                    .placeholder(R.drawable.proi)
                                    .error(R.drawable.proi)
                                    .into(profileImageHome);
                        }
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

        dataList = new ArrayList<>();
        displayAdapter = new DisplayAdapter(getContext(), dataList);
        productRv.setAdapter(displayAdapter);

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot productSnapshot : userSnapshot.getChildren()) {
                        DataClass productData = productSnapshot.getValue(DataClass.class);
                        productData.setKey(productSnapshot.getKey());
                        dataList.add(productData);
                    }
                }
                displayAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });



        return view;


    }

    public void searchList(String text) {
        ArrayList<DataClass> searchList = new ArrayList<>();
        for (DataClass dataClass : dataList) {
            if (dataClass.getTitle() != null && dataClass.getTitle().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
           else if (dataClass.getBrand() != null && dataClass.getBrand().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            } else if (dataClass.getType() != null && dataClass.getType().toLowerCase().contains(text.toLowerCase())) {
                searchList.add(dataClass);
            }
        }
        displayAdapter.searchDataList(searchList);
    }
}
