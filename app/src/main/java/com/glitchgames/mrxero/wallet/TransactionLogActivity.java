package com.glitchgames.mrxero.wallet;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.glitchgames.mrxero.wallet.database.DBHelper;

import java.util.ArrayList;

public class TransactionLogActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transactionlogactivity);

        dbHelper = new DBHelper(this);
        this.listView = (ListView) findViewById(R.id.transactionLogLV);

        try {
            final ArrayList<String> transactionLog = new ArrayList<>();
            final Cursor cursor = dbHelper.getTransactionLog();

            if (cursor.getCount() == 0){
                Toast.makeText(this, "Nothing in Database", Toast.LENGTH_SHORT).show();
            }
            else {
                while (cursor.moveToNext()){
                    if (cursor.getString(2).equals("0"))
                    {
                        transactionLog.add("Deposit: "+cursor.getString(3)+" TK\n\n"+"Transaction Time: "+cursor.getString(4)+"\n");
                        final ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.transactionlogview, transactionLog);
                        listView.setAdapter(listAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String TempBalLog = String.valueOf(parent.getItemAtPosition(position));
                                AlertDialog.Builder alertDialougeBuilder = new AlertDialog.Builder(TransactionLogActivity.this);
                                alertDialougeBuilder.setMessage(TempBalLog).setCancelable(true);
                                AlertDialog alertDialog = alertDialougeBuilder.create();
                                alertDialog.setTitle("Transaction Log");
                                alertDialog.show();
                            }
                        });
                    }
                    else if(cursor.getString(3).equals("0")){
                        transactionLog.add("Withdraw: "+cursor.getString(2)+" TK\n\n"+"Transaction Time: "+cursor.getString(4)+"\n");
                        ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.transactionlogview, transactionLog);
                        listView.setAdapter(listAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        });
                    }
                }
            }
        }
        catch (Exception e){
            Toast.makeText(this, "Error: "+e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
    }

    public void onBackPressed() {

        Intent intent = new Intent(TransactionLogActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
