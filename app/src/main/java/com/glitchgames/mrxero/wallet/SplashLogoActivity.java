package com.glitchgames.mrxero.wallet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashLogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlogoactivity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startHomeActivity();
    }

    private void startHomeActivity(){
        try{
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashLogoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
        }
        catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
        }
    }
}
