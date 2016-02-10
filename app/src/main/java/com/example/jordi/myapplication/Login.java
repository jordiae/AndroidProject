package com.example.jordi.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class Login extends AppCompatActivity {
    Button buttonSignIn, buttonSignUp;
    LoginDataBaseAdapter loginDataBaseAdapter;

    SharedPreferences prefLoggedIn = null;

    EditText editTextUserName, editTextPassword;

    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        prefLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE);
        Boolean restoredLogin = prefLoggedIn.getBoolean("loggedIn", false);
        if (restoredLogin) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this)
                .load("http://s.hswstatic.com/gif/landscape-photography-1.jpg")
                .into(imageView);

        // Create an instance of SQLite DB
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        buttonSignIn = (Button)findViewById(R.id.login);
        buttonSignUp = (Button)findViewById(R.id.sign_up);

        // Get instance of db adapter
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        // Get references of views
        editTextUserName = (EditText) findViewById(R.id.username);
        editTextPassword = (EditText) findViewById(R.id.password);

    }

    // Sign in
    public void signIn(View V) {

        username = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();

        // Fetch password
        String storedPassword = loginDataBaseAdapter.getPassword(username);

        // Check if the Stored password matches with the password entered by the user
        if (password.equals(storedPassword)) {
            Toast.makeText(Login.this, "Congrats: Login Successful", Toast.LENGTH_SHORT).show();

            // SharedPreferences to store the login
            SharedPreferences.Editor editorLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE).edit();
            editorLoggedIn.putBoolean("loggedIn", true).apply();
            editorLoggedIn.putString("user", username).apply();
            Intent myIntent = new Intent(this, MainActivity.class);
            this.startActivity(myIntent);
        }
        else {
            Toast.makeText(Login.this, "Username and password do not match", Toast.LENGTH_SHORT).show();
        }
    }

    public void signUp(View V) {

        username = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();

        // Check if any of the fields are vacant
        if (username.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            // Save data
            Boolean alreadyExists=loginDataBaseAdapter.checkIfUserAlreadyExists(username);
            // Check if the user already exists
            if (alreadyExists) {
                Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
            }
            else {
                loginDataBaseAdapter.insertEntry(username, password, 0, null);
                Toast.makeText(getApplicationContext(), "Account successfully created", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the db
        loginDataBaseAdapter.close();
    }
}