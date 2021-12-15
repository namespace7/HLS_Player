package com.namespace.hlsplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;


public class SplashScreenActivity extends AppCompatActivity {

    private LottieAnimationView animationView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        animationView = (LottieAnimationView) findViewById(R.id.animationViewActivity);
        animationView.playAnimation();


        /////////////////////////////////
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
             //   animationView.cancelAnimation();
                startActivity(i);
                // close this activity
                finish();
            }
        }, 3000);


    }
}
