package com.dn.woofwell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {
    Spinner accountTypeSpinner;
    TextView LogText;
    EditText usernameSign, passwordSign, phoneNumber, email;
    Button signinButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        accountTypeSpinner = findViewById(R.id.accountTypeSpinner);
        LogText = findViewById(R.id.LogText);
        usernameSign = findViewById(R.id.usernameSign);
        passwordSign = findViewById(R.id.passwordSign);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        signinButton = findViewById(R.id.signinButton);
        auth = FirebaseAuth.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter);

        LogText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String pass = passwordSign.getText().toString().trim();
                String userNm = usernameSign.getText().toString().trim();
                String pNumber = phoneNumber.getText().toString().trim();
                String accountType = accountTypeSpinner.getSelectedItem().toString();

                if (userEmail.isEmpty()) {
                    email.setError("Email Cannot be Empty");
                } else if (pass.isEmpty()) {
                    passwordSign.setError("Password Cannot be Empty");
                } else if (userNm.isEmpty()) {
                    usernameSign.setError("Username Cannot be Empty");
                } else if (pNumber.isEmpty()) {
                    phoneNumber.setError("Phone Number Cannot be Empty");
                } else {
                    auth.createUserWithEmailAndPassword(userEmail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = auth.getCurrentUser().getUid();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

                                User user = new User(userNm, pNumber, userEmail, userId, accountType);
                                userRef.setValue(user);

                                Toast.makeText(Signup.this, "Register Successful. User ID: " + userId, Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();

                                startActivity(new Intent(Signup.this, Login.class));
                                finish();
                            } else {
                                Toast.makeText(Signup.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
