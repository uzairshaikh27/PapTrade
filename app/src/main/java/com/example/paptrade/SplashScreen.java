package com.example.paptrade;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private static final int ANIMATION_DURATION = 1500; // Adjust the duration as needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView tvlogo=findViewById(R.id.logo);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvlogo, "scaleX", 0.5f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvlogo, "scaleY", 0.5f, 1.0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(tvlogo, "alpha", 0.0f, 1.0f);

        // Combine the animations using AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(ANIMATION_DURATION);

        // Start the animation
        animatorSet.start();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=auth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser!=null) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);

                    finish();
                }
                else{
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_right ,  R.anim.slide_left);

                    finish();
                }
            }
        }, ANIMATION_DURATION);  // Set the duration of the splash screen in milliseconds (e.g., 2000 = 2 seconds)

    }

}