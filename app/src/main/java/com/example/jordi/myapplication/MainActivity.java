package com.example.jordi.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
// import android.app.PendingIntent;

// TODO: brackets and exponentiation on the UI side, improve code organization

public class MainActivity extends BaseActivity implements  View.OnClickListener{

    private TextView display;
    private Button zero, one, two, three, four, five, six, seven, eight, nine, dot, equal, plus, minus, mult, div;

    private String result ="";

    private boolean equalPressed = false;
    private boolean multDivDotPressed = false;
    private int plusMinusCounter = 0;

    int notificationID = 1;

    SharedPreferences prefIsFirstTime = null;
    SharedPreferences prefNotify = null;
    SharedPreferences prefLoggedIn = null;

    // Function to parse and calculate mathematical expressions extracted and adapted from Stackoverflow
    public static double eval(final String str) {
        class Parser {
            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) eatChar();
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) throw new RuntimeException("Unexpected: " + (char)c);
                return v;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `x` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`

            double parseExpression() {
                double v = parseTerm();
                for (;;) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (;;) {
                    eatSpace();
                    if (c == '/') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == 'x' || c == '(') { // multiplication
                        if (c == 'x') eatChar();
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();
                if (c == '+' || c == '-') { // unary plus & minus
                    negate = c == '-';
                    eatChar();
                    eatSpace();
                }
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') eatChar();
                } else { // numbers
                    StringBuilder sb = new StringBuilder();
                    while ((c >= '0' && c <= '9') || c == '.') {
                        sb.append((char)c);
                        eatChar();
                    }
                    if (sb.length() == 0) throw new RuntimeException("Unexpected: " + (char)c);
                    v = Double.parseDouble(sb.toString());
                }
                eatSpace();
                if (c == '^') { // exponentiation
                    eatChar();
                    v = Math.pow(v, parseFactor());
                }
                if (negate) v = -v; // unary minus is applied after exponentiation; e.g. -3^2=-9
                return v;
            }
        }
        return new Parser().parse();
    }

    protected void displayNotification(){
        /*Intent i = new Intent(this, NotificationView.class);
        i.putExtra("notificationID", notificationID);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);*/
        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        CharSequence ticker = "";
        CharSequence contentTitle = "Expressió invàlida";
        CharSequence contentText = "Expressió invàlida, no es pot calcular";
        Notification noti = new NotificationCompat.Builder(this)
                //.setContentIntent(pendingIntent)
                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.warning)
                //.addAction(R.drawable.warning, ticker, pendingIntent)
                .setVibrate(new long[] {100, 250, 100, 500})
                .build();
        nm.notify(notificationID, noti);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefIsFirstTime = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE);
        prefNotify = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE);

        display = (TextView) findViewById(R.id.textView);
        display.setText("");

        zero = (Button) findViewById(R.id.zero);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        six = (Button) findViewById(R.id.six);
        seven = (Button) findViewById(R.id.seven);
        eight = (Button) findViewById(R.id.eight);
        nine = (Button) findViewById(R.id.nine);
        dot = (Button) findViewById(R.id.dot);
        equal = (Button) findViewById(R.id.equal);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        mult = (Button) findViewById(R.id.mult);
        div = (Button) findViewById(R.id.div);


        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        dot.setOnClickListener(this);
        equal.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        mult.setOnClickListener(this);
        div.setOnClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("screen", result);
        savedInstanceState.putBoolean("equal", equalPressed);
        savedInstanceState.putBoolean("multDivDotPressed", multDivDotPressed);
        savedInstanceState.putInt("plusMinusCounter", plusMinusCounter);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String screen = savedInstanceState.getString("screen");
        result = screen;
        display.setText(result);

        boolean equal = savedInstanceState.getBoolean("equal");
        equalPressed = equal;

        boolean mDVP = savedInstanceState.getBoolean("multDivDotPressed");
        multDivDotPressed = mDVP;

        int pMC = savedInstanceState.getInt("plusMinusCounter");
        plusMinusCounter = pMC;
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences.Editor editorIsFirstTime = prefIsFirstTime.edit();
        SharedPreferences.Editor editorNotify = prefNotify.edit();

        if (prefIsFirstTime.getBoolean("firstrun", true)) {
            editorNotify.putBoolean("notify", true).apply();
            openDrawer();
            editorIsFirstTime.putBoolean("firstrun", false).apply();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zero:
                if (!equalPressed) {
                    result += (this.getString(R.string.zero));
                    display.append(this.getString(R.string.zero));
                }
                else {
                    result = (this.getString(R.string.zero));
                    display.setText(this.getString(R.string.zero));
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.one:
                if (!equalPressed) {
                    result += (this.getString(R.string.one));
                    display.append(this.getString(R.string.one));
                }
                else {
                    result = (this.getString(R.string.one));
                    display.setText(this.getString(R.string.one));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.two:
                if (!equalPressed) {
                    result += (this.getString(R.string.two));
                    display.append(this.getString(R.string.two));
                }
                else {
                    result = (this.getString(R.string.two));
                    display.setText(this.getString(R.string.two));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.three:
                if (!equalPressed) {
                    result += (this.getString(R.string.three));
                    display.append(this.getString(R.string.three));
                }
                else {
                    result = (this.getString(R.string.three));
                    display.setText(this.getString(R.string.three));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.four:
                if (!equalPressed) {
                    result += (this.getString(R.string.four));
                    display.append(this.getString(R.string.four));
                }
                else {
                    result = (this.getString(R.string.four));
                    display.setText(this.getString(R.string.four));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.five:
                if (!equalPressed) {
                    result += (this.getString(R.string.five));
                    display.append(this.getString(R.string.five));
                }
                else {
                    result = (this.getString(R.string.five));
                    display.setText(this.getString(R.string.five));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.six:
                if (!equalPressed) {
                    result += (this.getString(R.string.six));
                    display.append(this.getString(R.string.six));
                    equalPressed = false;
                }
                else {
                    result = (this.getString(R.string.six));
                    display.setText(this.getString(R.string.six));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.seven:
                if (!equalPressed) {
                    result += (this.getString(R.string.seven));
                    display.append(this.getString(R.string.seven));
                    equalPressed = false;
                }
                else {
                    result = (this.getString(R.string.seven));
                    display.setText(this.getString(R.string.seven));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.eight:
                if (!equalPressed) {
                    result += (this.getString(R.string.eight));
                    display.append(this.getString(R.string.eight));
                    equalPressed = false;
                }
                else {
                    result = (this.getString(R.string.eight));
                    display.setText(this.getString(R.string.eight));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.nine:
                if (!equalPressed) {
                    result += (this.getString(R.string.nine));
                    display.append(this.getString(R.string.nine));
                }
                else {
                    result = (this.getString(R.string.nine));
                    display.setText(this.getString(R.string.nine));
                    equalPressed = false;
                }
                multDivDotPressed = false;
                plusMinusCounter = 0;
                break;
            case R.id.dot:
                if (result!= "" && plusMinusCounter == 0 && !equalPressed && !multDivDotPressed) {
                    result += (this.getString(R.string.dot));
                    display.append(this.getString(R.string.dot));
                    multDivDotPressed = true;
                    plusMinusCounter = 2;
                }
                break;
            case R.id.equal:
                if (!equalPressed) {
                    while ((result != null) && (result.length() > 0) && (result.substring(result.length() - 1).equals("+") || result.substring(result.length() - 1).equals("-") || result.substring(result.length() - 1).equals("x") || result.substring(result.length() - 1).equals("/"))) {
                        result = result.substring(0, result.length()-1);
                    }
                    if (result.equals("")) result = "0";
                    double result_operations = eval(result);

                    Context context = getApplicationContext();
                    CharSequence text = "Invalid expression";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);

                    if (String.valueOf(result_operations).equals("Infinity") || String.valueOf(result_operations).equals("-Infinity") || Double.isNaN(result_operations)) {
                        boolean userWantsNotification = prefNotify.getBoolean("notify", true);
                        if (userWantsNotification) displayNotification();
                        else toast.show();
                        display.setText("");
                        result = "";
                    } else {
                        display.setText(String.valueOf(result_operations));
                        result = "" + result_operations;
                        equalPressed = true;
                        multDivDotPressed = false;
                        plusMinusCounter = 0;
                    }
                }
                break;
            case R.id.plus:
                if (result!= "" && plusMinusCounter < 2) {
                    result += (this.getString(R.string.plus));
                    display.append(this.getString(R.string.plus));
                    equalPressed = false;
                    multDivDotPressed = false;
                    plusMinusCounter++;
                }
                break;
            case R.id.minus:
                if (result!= "" && plusMinusCounter < 2) {
                    result += (this.getString(R.string.minus));
                    display.append(this.getString(R.string.minus));
                    equalPressed = false;
                    multDivDotPressed = false;
                    plusMinusCounter++;
                }
                break;
            case R.id.mult:
                if (result!= "" && !multDivDotPressed) {
                    result += (this.getString(R.string.mult));
                    display.append(this.getString(R.string.mult));
                    equalPressed = false;
                    multDivDotPressed = true;
                    plusMinusCounter++;
                }
                break;
            case R.id.div:
                if (result!= "" && !multDivDotPressed) {
                    result += (this.getString(R.string.div));
                    display.append(this.getString(R.string.div));
                    equalPressed = false;
                    multDivDotPressed = true;
                    plusMinusCounter++;
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        prefNotify = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE);
        MenuItem item = menu.findItem(R.id.notify);
        if (prefNotify.getBoolean("notify", true)) {
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
        }
        else {
            item = menu.findItem(R.id.toast);
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editorNotify = prefNotify.edit();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"));
                startActivity(intent);
                return true;
            case R.id.browser:
                String url = "http://google.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
            case R.id.notify:
                editorNotify.putBoolean("notify", true).apply();
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
            case R.id.toast:
                editorNotify.putBoolean("notify", false).apply();
                if(item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                return true;
            case R.id.logout:
                SharedPreferences.Editor editorLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE).edit();
                editorLoggedIn.putBoolean("loggedIn", false).apply();
                editorLoggedIn.putString("user",null).apply();
                Intent myIntent = new Intent(this, Login.class);
                this.startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}