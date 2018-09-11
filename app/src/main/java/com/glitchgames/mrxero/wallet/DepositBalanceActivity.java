package com.glitchgames.mrxero.wallet;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchgames.mrxero.wallet.database.DBHelper;

public class DepositBalanceActivity extends AppCompatActivity implements View.OnClickListener {
    private Button depositBalanceButton;
    private EditText despositBalanceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.depositbalanceactivity);

        this.depositBalanceButton = (Button) findViewById(R.id.btnDepositBalance);
        this.depositBalanceButton.setOnClickListener(this);

        this.despositBalanceEditText = (EditText) findViewById(R.id.depositBalanceField);

    }

    public void onBackPressed() {
        Intent intent = new Intent(DepositBalanceActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnDepositBalance){
            despositBalance();
            }
        }

        public void despositBalance(){
            Double finalBal;
            Double depoBal = 0.0;

            if(!despositBalanceEditText.getText().toString().isEmpty() && !despositBalanceEditText.getText().toString().equals("0")){
                try{
                    depoBal= Double.parseDouble(despositBalanceEditText.getText().toString());

                    DBHelper dbHelper = new DBHelper(this);

                    Cursor dbHelperData = dbHelper.getData();
                    if (dbHelperData.getCount() == 0) {
                        dbHelper.insertData(depoBal,0.0,depoBal,depoBal);
                    }
                    else{
                        Cursor cursor = dbHelper.getFinalBalance();
                        if (cursor.getCount() ==0){
                            Toast.makeText(this, "Transaction empty", Toast.LENGTH_SHORT).show();
                        }

                        StringBuffer stringBuffer = new StringBuffer();
                        while (cursor.moveToNext()){
                            stringBuffer.append(cursor.getDouble(5));
                        }

                        String temp = stringBuffer.toString();
                        finalBal = Double.parseDouble(temp);
                        finalBal += depoBal;

                        dbHelper.insertData(finalBal, 0.0, depoBal ,finalBal);
                    }
                    Toast.makeText(this, "Deposit:  "+ String.valueOf(depoBal)+" TK added", Toast.LENGTH_SHORT).show();
                }

                catch (Exception e){
                    Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(DepositBalanceActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, " *Amount is required!", Toast.LENGTH_SHORT).show();
            }
        }
    }

