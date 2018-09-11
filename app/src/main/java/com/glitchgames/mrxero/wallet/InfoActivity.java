package com.glitchgames.mrxero.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moreactivity);
    }
    public void onBackPressed() {

        Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
