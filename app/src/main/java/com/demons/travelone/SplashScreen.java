package com.demons.travelone;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kinjal on 23/7/16.
 */
public class SplashScreen extends AppCompatActivity {

    FirebaseAuth auth;

    private static final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

            // already signed in
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {

            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
            finish();
        }

    }
}
