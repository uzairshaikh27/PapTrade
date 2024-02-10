package com.example.paptrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    AppCompatButton login;
    TextView tv1, tv2;
      FirebaseAuth firebaseAuth;
      FirebaseUser currentUser;
    EditText et1 , et2;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
         currentUser = firebaseAuth.getCurrentUser();
//        if(currentUser != null){
//            startActivity(new Intent(LoginActivity.this,MainActivity.class));
//            overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
//
//            finish();
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);
         firebaseAuth=FirebaseAuth.getInstance();

        login=findViewById(R.id.login);
        et1=findViewById(R.id.email);
        et2=findViewById(R.id.password);
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
//        if(currentUser!=null){
//            startActivity(new Intent(this,MainActivity.class));
//            finish();
//        }
//
login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        loginUser();
    }
});





        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

                finish();
            }
        });


    }

    private void loginUser() {
        String email=et1.getText().toString();
        String pin=et2.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pin)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, pin)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                           // overridePendingTransition(R.anim.slide_left, R.anim.slide_right);

                            finish();
                           // FirebaseUser user = firebaseAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}