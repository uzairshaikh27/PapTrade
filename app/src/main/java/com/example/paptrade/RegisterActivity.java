package com.example.paptrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;

import android.widget.TextView;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {


    AppCompatButton register;
    TextView tv1, tv2;
    EditText et1, et2, et3, et4, et5;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register = findViewById(R.id.register);
        et1 = findViewById(R.id.email);
        et2 = findViewById(R.id.mobile);
        et3 = findViewById(R.id.password);
        et4 = findViewById(R.id.password2);
        et5 = findViewById(R.id.name);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        auth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);

                finish();
            }
        });
    }

    private void registerUser() {

        final String name_txt = et5.getText().toString().trim();
        final String email_txt = et1.getText().toString().trim();
        final String mobile_txt = et2.getText().toString().trim();
        final String pin_txt = et3.getText().toString().trim();
        final String pin2_txt = et4.getText().toString().trim();

        if (TextUtils.isEmpty(name_txt) || TextUtils.isEmpty(mobile_txt) || TextUtils.isEmpty(email_txt)
                || TextUtils.isEmpty(pin_txt) || TextUtils.isEmpty(pin2_txt)) {
            Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pin_txt.equals(pin2_txt)) {
            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email_txt, pin_txt).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null) {
                        String uid = user.getUid();

                        // databaseReference.child("user_details").addListenerForSingleValueEvent(new ValueEventListener() {


                        databaseReference.child("user_details").child(uid).child("name").setValue(name_txt);
                        databaseReference.child("user_details").child(uid).child("email").setValue(email_txt);
                        //databaseReference.child("user_details").child(uid).child("password").setValue(pin_txt);
                        databaseReference.child("user_details").child(uid).child("phone").setValue(mobile_txt);

                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_left);

                        finish();
                    }


            }



                 else {

                    Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
}


