package com.example.jordi.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


public class RankingActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;

    SharedPreferences.Editor editorLoggedIn;

    Intent myIntent;

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RankingActivity.this);

        builder
                .setMessage("Are you sure you want to remove all users?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        LoginDataBaseAdapter loginDataBaseAdapter;
                        loginDataBaseAdapter = new LoginDataBaseAdapter(getApplicationContext());
                        loginDataBaseAdapter = loginDataBaseAdapter.open();
                        String users[] = loginDataBaseAdapter.getAllUsers();
                        int nUsers = loginDataBaseAdapter.getNumberOfUsers();
                        for (int i = 0; i < nUsers - 1; ++i) {  // ¯\_(ツ)_/¯
                            loginDataBaseAdapter.deleteEntry(users[i]);
                        }
                        loginDataBaseAdapter.close();
                        editorLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE).edit();
                        editorLoggedIn.putBoolean("loggedIn", false).apply();
                        editorLoggedIn.putString("user", null).apply();
                        myIntent = new Intent(RankingActivity.this, Login.class);
                        RankingActivity.this.startActivity(myIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);

        //LayoutManager manages items inside recyclerview

        mLinearLayout = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLinearLayout);

        // Adapter: object -> XML view
        mRecyclerView.setAdapter(new MyCustomAdapter(this.getApplicationContext()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ranking_menu, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                confirmDialog();
                return true;
            case R.id.logout:
                editorLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE).edit();
                editorLoggedIn.putBoolean("loggedIn", false).apply();
                editorLoggedIn.putString("user", null).apply();
                myIntent = new Intent(this, Login.class);
                this.startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}