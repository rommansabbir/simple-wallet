package com.glitchgames.mrxero.wallet;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.glitchgames.mrxero.wallet.database.DBHelper;


public class WithdrawBalanceActivity extends AppCompatActivity implements View.OnClickListener {

    public Button WithdrawBalanceButton;
    public EditText withdrawBalanceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdrawbalanceactivity);

        this.WithdrawBalanceButton = (Button) findViewById(R.id.btnWithdrawBalance);
        this.WithdrawBalanceButton.setOnClickListener(this);

        this.withdrawBalanceEditText = (EditText) findViewById(R.id.withdrawBalanceField);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(WithdrawBalanceActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnWithdrawBalance){
            withdrawBalance();
        }
    }

    public void withdrawBalance(){

        if (!withdrawBalanceEditText.getText().toString().isEmpty() && !withdrawBalanceEditText.getText().toString().equals("0")){
            try{
                Double finalBal;
                Double withdrawBal = Double.parseDouble(withdrawBalanceEditText.getText().toString());

                DBHelper dbHelper = new DBHelper(this);
                Cursor dbHelperData = dbHelper.getMainBalance();

                if (dbHelperData.getCount() == 0){
                    Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
                }

                StringBuffer stringBuffer = new StringBuffer();
                while (dbHelperData.moveToNext()){
                    stringBuffer.append(dbHelperData.getDouble(1));
                }

                String temp = stringBuffer.toString();

                if (temp.isEmpty()){
                    Toast.makeText(this, "Desposit some money first", Toast.LENGTH_SHORT).show();
                }
                else {
                    finalBal = Double.valueOf(temp) - withdrawBal;
                    if (finalBal!=0 && finalBal>0 || finalBal==0){
                        dbHelper.insertData(finalBal, withdrawBal, 0.0, finalBal);
                        Toast.makeText(this, "Withdraw: "+withdrawBal+" TK", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(WithdrawBalanceActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                        }
                        else{
                        Toast.makeText(this, "Not enough money", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                catch (Exception e){
                    Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
                    Log.d("Error", e.getLocalizedMessage());
                }
            }
            else {
                Toast.makeText(this, "Amount is required!", Toast.LENGTH_SHORT).show();
            }
        }
    }
