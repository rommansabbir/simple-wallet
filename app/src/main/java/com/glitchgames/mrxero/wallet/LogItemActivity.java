package com.glitchgames.mrxero.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LogItemActivity extends AppCompatActivity {
    private TextView showTransactionAmount, ShowTransactionTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_item);
        Intent intent = getIntent();

        this.showTransactionAmount = findViewById(R.id.showTransactionAmount);
        String tempBal = intent.getStringExtra("TempBalLog");
        this.showTransactionAmount.setText(tempBal);


    }
}
