package com.dn.woofwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    LinearLayout homeLayout,cartLayout,profileLayout;
    ImageView homeImage,cartImage,profileImage;
    TextView homeTxt,cartTxt,profileTxt;
    private int selectedTab = 1;
    String userId;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        userId = currentUser.getUid().toString();

        homeLayout = findViewById(R.id.homeLayout);
        cartLayout = findViewById(R.id.cartLayout);
        profileLayout = findViewById(R.id.profileLayout);

        homeImage = findViewById(R.id.homeImg);
        cartImage = findViewById(R.id.cartImg);
        profileImage = findViewById(R.id.proImg);

        homeTxt = findViewById(R.id.homeTxt);
        cartTxt = findViewById(R.id.cartTxt);
        profileTxt =findViewById(R.id.proTxt);



        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer,HomeFragment.class,null)
                .commit();
        homeImage.setImageResource(R.drawable.ic_home_after);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String fragment = bundle.getString("fragment");
            if (fragment != null && fragment.equals("cart")) {
                selectedTab = 2;

                if (selectedTab == 2) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, CartFragment.class, null);
                    transaction.commit();

                    homeImage.setImageResource(R.drawable.ic_home);
                    profileImage.setImageResource(R.drawable.ic_person);
                    cartImage.setImageResource(R.drawable.ic_round_shopping_cart_24_after);

                    selectedTab = 2;
                }
            }
        }


        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab !=1){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, HomeFragment.class, null);
                    transaction.commit();

                    homeImage.setImageResource(R.drawable.ic_home_after);
                    profileImage.setImageResource(R.drawable.ic_person);
                    cartImage.setImageResource(R.drawable.ic_round_shopping_cart_24);

                    selectedTab = 1;
                }

            }
        });


        cartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedTab != 2){
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, CartFragment.class, null);
                    transaction.commit();

                    homeImage.setImageResource(R.drawable.ic_home);
                    profileImage.setImageResource(R.drawable.ic_person);
                    cartImage.setImageResource(R.drawable.ic_round_shopping_cart_24_after);

                    selectedTab = 2;
                }
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab !=3) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, ProfileFragment.class, null);
                    transaction.commit();

                    homeImage.setImageResource(R.drawable.ic_home);
                    profileImage.setImageResource(R.drawable.ic_person_after);
                    cartImage.setImageResource(R.drawable.ic_round_shopping_cart_24);

                    selectedTab = 3;
                }
            }
        });


    }
}