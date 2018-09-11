package com.glitchgames.mrxero.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.glitchgames.mrxero.wallet.database.DBHelper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private CardView depositBalanceCardView, withdrawBalanceCardView, infoCardView, transactionLogCardView;
    private TextView mainBalanceTextView, lastTransactionTextView;
    private DBHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onCreateCasting();
        setMainBalanceView();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_linkedin_item) {
            try {
                Intent linkedinAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/glitchgames/"));
                startActivity(linkedinAppIntent);
            }
            catch (Exception e){
                Log.d("onFacebookIntent", "");
            }
            // Handle the camera action
        } else if (id == R.id.nav_facebook_item) {
            try {
                Intent facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/GlitchGames.HQ/"));
                startActivity(facebookAppIntent);
            }
            catch (Exception e){
                Log.d("onFacebookIntent", "");
            }

        }  else if (id == R.id.nav_web_item) {
            try {
                Intent webAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.glitchbd.com/"));
                startActivity(webAppIntent);
            }
            catch (Exception e){
                Log.d("onFacebookIntent", "");
            }

        } else if (id == R.id.nav_revert_last_transaction_item) {
            Cursor cursor = myDBHelper.getIsVisibleTrue();

            try {
                if (cursor.getCount()!=0){

                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()){
                        stringBuffer.append(cursor.getInt(6));
                    }
                    String temp = "1";
                    if ( temp.equals(stringBuffer.toString()))
                    {
                        myDBHelper.undoLastTransaction();
                        Toast.makeText(this, "Last transaction deletion successful", Toast.LENGTH_SHORT).show();
                        setMainBalanceView();
                    }
                    else {
                        Toast.makeText(this, "No transaction available right now", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e){
                Log.d("revertLastTransaction", e.getLocalizedMessage());
            }



        }
        else if(id==R.id.nav_clear_transaction_log_item){

            try {
                clearTransactionLog();
            }
            catch (Exception e){
                Log.d("clear_transaction_log", e.getLocalizedMessage());
            }


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearTransactionLog(){
        Cursor cursor = myDBHelper.getIsVisibleTrue();

        try {
            if (cursor.getCount()!=0){
                StringBuffer stringBuffer = new StringBuffer();
                while (cursor.moveToNext()){
                    stringBuffer.append(cursor.getInt(6));
                }
                String temp = "1";
                if ( temp.equals(stringBuffer.toString()))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Are you sure?");
                    alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            myDBHelper.clearTransactionLog();
                            setMainBalanceView();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(this, "No transaction available", Toast.LENGTH_SHORT).show();
                }

            }
            else {
                Toast.makeText(this, "No transaction available", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Log.d("clearTransactionLog()", e.getLocalizedMessage());
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.depositBalanceCardView){
            Intent intent = new Intent(HomeActivity.this, DepositBalanceActivity.class);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.withdrawBalanceCardView){
            Intent intent = new Intent(HomeActivity.this, WithdrawBalanceActivity.class);
            startActivity(intent);
            finish();
        }

        if (v.getId() == R.id.transactionLogCardView){
            Cursor cursor = myDBHelper.getIsVisibleTrue();

            try {
                if (cursor.getCount()!=0){

                    StringBuffer stringBuffer = new StringBuffer();
                    while (cursor.moveToNext()){
                        stringBuffer.append(cursor.getInt(6));
                    }
                    String temp = "1";
                    if ( temp.equals(stringBuffer.toString()))
                    {
                        Intent intent = new Intent(HomeActivity.this, TransactionLogActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this, "No transaction history available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (Exception e){
                Log.d("transactionLogCardView", e.getLocalizedMessage());
            }

        }

        if (v.getId() == R.id.moreCardView){
            Intent intent = new Intent(HomeActivity.this, InfoActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void  onCreateCasting(){
        myDBHelper = new DBHelper(this);

        SQLiteDatabase helperReadableDatabase = myDBHelper.getReadableDatabase();

        this.depositBalanceCardView = (CardView) findViewById(R.id.depositBalanceCardView);
        this.depositBalanceCardView.setOnClickListener(this);

        this.withdrawBalanceCardView = (CardView) findViewById(R.id.withdrawBalanceCardView);
        this.withdrawBalanceCardView.setOnClickListener(this);

        this.infoCardView = (CardView) findViewById(R.id.moreCardView);
        this.infoCardView.setOnClickListener(this);

        this.transactionLogCardView = (CardView) findViewById(R.id.transactionLogCardView);
        this.transactionLogCardView.setOnClickListener(this);
    }

    public void setMainBalanceView(){
        mainBalanceTextView = (TextView) findViewById(R.id.mainBalanceView);
        lastTransactionTextView = (TextView) findViewById(R.id.lastTransaction);

            try {
                //setting up main balance
                Cursor cursor1 = myDBHelper.getFinalBalance();
                if (cursor1.getCount() ==0){
                    Toast.makeText(this, "Current balance is empty", Toast.LENGTH_SHORT).show();
                }
                StringBuffer stringBuffer1 = new StringBuffer();
                while (cursor1.moveToNext()){
                    stringBuffer1.append(cursor1.getDouble(1));
                }

                String temp1 = stringBuffer1.toString();
                if (temp1.isEmpty()){
                    mainBalanceTextView.setText("Main Balance: 0 TK");
                }
                else {
                    mainBalanceTextView.setText("Main Balance: "+temp1+ " TK");
                }


                //setting up last transaction
                Cursor cursor2 = myDBHelper.getLastTransaction();
                if (cursor2.getCount() ==0){
                    Toast.makeText(this, "First, deposit some money", Toast.LENGTH_SHORT).show();
                }

                StringBuffer stringBuffer2 = new StringBuffer();
                while (cursor2.moveToNext()){
                    stringBuffer2.append(cursor2.getString(4));
                }
                String temp2 = stringBuffer2.toString();
                if (temp2.isEmpty()){
                    lastTransactionTextView.setText("Last transaction: 00:00 AM | 00-00-0000");
                }
                else {
                    lastTransactionTextView.setText("Last transaction: "+temp2);
                }

            }
            catch (Exception e){
                Log.d("setMainBalanceView", e.getLocalizedMessage());
            }

    }
}
