package com.example.jordi.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// TODO: flipper, improve code organization

//import com.example.material.joanbarroso.flipper.CoolImageFlipper;

public class MemoryActivity extends BaseActivity implements  View.OnClickListener {

    private ImageButton imgButton0, imgButton1, imgButton2, imgButton3, imgButton4, imgButton5, imgButton6, imgButton7, imgButton8, imgButton9, imgButton10, imgButton11, imgButton12, imgButton13, imgButton14, imgButton15;

    // Global array to randomize
    private  Integer[] imagePaths = {R.drawable.banana,R.drawable.banana,R.drawable.cherry,R.drawable.cherry,R.drawable.grape,R.drawable.grape,R.drawable.lemon,R.drawable.lemon,R.drawable.melon,R.drawable.melon,R.drawable.orange,R.drawable.orange,R.drawable.strawberry,R.drawable.strawberry,R.drawable.watermelon,R.drawable.watermelon};
    private List<Integer> listImagePaths = Arrays.asList(imagePaths);

    Integer unknown = R.drawable.color_default;

    private int selectedCardCounter = 0;

    private ImageButton selectedCardImageButton1;
    private ImageButton selectedCardImageButton2;

    private int remainingCards;

    // Boolean to prevent the user from opening additional card just after a guess
    private boolean block = false;

    // Rank = number of attempts
    private int rank = 0;

    private int max_rank;

    TextView textView;

    SharedPreferences prefLoggedIn = null;

    // Flipper
    // CoolImageFlipper coolImageFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        // flipper
        //coolImageFlipper = new CoolImageFlipper(getApplicationContext());

        // db

        prefLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE);
        String user = prefLoggedIn.getString("user", null);


        LoginDataBaseAdapter loginDataBaseAdapter;
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        max_rank = loginDataBaseAdapter.getRank(user);
        loginDataBaseAdapter.close();


        // Attempts

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(String.valueOf(rank));

        // (there are 8 pairs)
        remainingCards = 8;

        // Random images
        Collections.shuffle(listImagePaths);
        imagePaths = listImagePaths.toArray(new Integer[listImagePaths.size()]);


        imgButton0 = (ImageButton) findViewById(R.id.imageButton0);
        imgButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imgButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imgButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imgButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imgButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imgButton6 = (ImageButton) findViewById(R.id.imageButton6);
        imgButton7 = (ImageButton) findViewById(R.id.imageButton7);
        imgButton8 = (ImageButton) findViewById(R.id.imageButton8);
        imgButton9 = (ImageButton) findViewById(R.id.imageButton9);
        imgButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imgButton11 = (ImageButton) findViewById(R.id.imageButton11);
        imgButton12 = (ImageButton) findViewById(R.id.imageButton12);
        imgButton13 = (ImageButton) findViewById(R.id.imageButton13);
        imgButton14 = (ImageButton) findViewById(R.id.imageButton14);
        imgButton15 = (ImageButton) findViewById(R.id.imageButton15);


        imgButton0.setImageResource(unknown);
        imgButton1.setImageResource(unknown);
        imgButton2.setImageResource(unknown);
        imgButton3.setImageResource(unknown);
        imgButton4.setImageResource(unknown);
        imgButton5.setImageResource(unknown);
        imgButton6.setImageResource(unknown);
        imgButton7.setImageResource(unknown);
        imgButton8.setImageResource(unknown);
        imgButton9.setImageResource(unknown);
        imgButton10.setImageResource(unknown);
        imgButton11.setImageResource(unknown);
        imgButton12.setImageResource(unknown);
        imgButton13.setImageResource(unknown);
        imgButton14.setImageResource(unknown);
        imgButton15.setImageResource(unknown);

        // Tags to identify each card

        imgButton0.setTag(imagePaths[0]);
        imgButton1.setTag(imagePaths[1]);
        imgButton2.setTag(imagePaths[2]);
        imgButton3.setTag(imagePaths[3]);
        imgButton4.setTag(imagePaths[4]);
        imgButton5.setTag(imagePaths[5]);
        imgButton6.setTag(imagePaths[6]);
        imgButton7.setTag(imagePaths[7]);
        imgButton8.setTag(imagePaths[8]);
        imgButton9.setTag(imagePaths[9]);
        imgButton10.setTag(imagePaths[10]);
        imgButton11.setTag(imagePaths[11]);
        imgButton12.setTag(imagePaths[12]);
        imgButton13.setTag(imagePaths[13]);
        imgButton14.setTag(imagePaths[14]);
        imgButton15.setTag(imagePaths[15]);


        imgButton0.setOnClickListener(this);
        imgButton1.setOnClickListener(this);
        imgButton2.setOnClickListener(this);
        imgButton3.setOnClickListener(this);
        imgButton4.setOnClickListener(this);
        imgButton5.setOnClickListener(this);
        imgButton6.setOnClickListener(this);
        imgButton7.setOnClickListener(this);
        imgButton8.setOnClickListener(this);
        imgButton9.setOnClickListener(this);
        imgButton10.setOnClickListener(this);
        imgButton11.setOnClickListener(this);
        imgButton12.setOnClickListener(this);
        imgButton13.setOnClickListener(this);
        imgButton14.setOnClickListener(this);
        imgButton15.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (!block) {
            switch (v.getId()) {
                case R.id.imageButton0:
                    if (selectedCardImageButton1 != imgButton0 && selectedCardImageButton2 != imgButton0) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton0;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton0;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton0;
                            ++selectedCardCounter;
                        }
                        imgButton0.setImageResource(imagePaths[0]);
                    }
                    break;
                case R.id.imageButton1:
                    if (selectedCardImageButton1 != imgButton1 && selectedCardImageButton2 != imgButton1) {
                        if (selectedCardCounter == 2) {
                            // tancar altres cartes
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton1;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton1;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton1;
                            ++selectedCardCounter;
                        }
                        imgButton1.setImageResource(imagePaths[1]);
                    }
                    break;
                case R.id.imageButton2:
                    if (selectedCardImageButton1 != imgButton2 && selectedCardImageButton2 != imgButton2) {
                        if (selectedCardCounter == 2) {
                            // tancar altres cartes
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton2;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton2;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton2;
                            ++selectedCardCounter;
                        }
                        imgButton2.setImageResource(imagePaths[2]);
                    }
                    break;
                case R.id.imageButton3:
                    if (selectedCardImageButton1 != imgButton3 && selectedCardImageButton2 != imgButton3) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton3;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton3;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton3;
                            ++selectedCardCounter;
                        }
                        imgButton3.setImageResource(imagePaths[3]);
                    }
                    break;
                case R.id.imageButton4:
                    if (selectedCardImageButton1 != imgButton4 && selectedCardImageButton2 != imgButton4) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton4;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton4;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton4;
                            ++selectedCardCounter;
                        }
                        imgButton4.setImageResource(imagePaths[4]);
                    }
                    break;
                case R.id.imageButton5:
                    if (selectedCardImageButton1 != imgButton5 && selectedCardImageButton2 != imgButton5) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton5;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton5;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton5;
                            ++selectedCardCounter;
                        }
                        imgButton5.setImageResource(imagePaths[5]);
                    }
                    break;
                case R.id.imageButton6:
                    if (selectedCardImageButton1 != imgButton6 && selectedCardImageButton2 != imgButton6) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton6;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton6;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton6;
                            ++selectedCardCounter;
                        }
                        imgButton6.setImageResource(imagePaths[6]);
                    }
                    break;
                case R.id.imageButton7:
                    if (selectedCardImageButton1 != imgButton7 && selectedCardImageButton2 != imgButton7) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton7;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton7;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton7;
                            ++selectedCardCounter;
                        }
                        imgButton7.setImageResource(imagePaths[7]);
                    }
                    break;
                case R.id.imageButton8:
                    if (selectedCardImageButton1 != imgButton8 && selectedCardImageButton2 != imgButton8) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton8;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton8;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton8;
                            ++selectedCardCounter;
                        }
                        imgButton8.setImageResource(imagePaths[8]);
                    }
                    break;
                case R.id.imageButton9:
                    if (selectedCardImageButton1 != imgButton9 && selectedCardImageButton2 != imgButton9) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton9;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton9;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton9;
                            ++selectedCardCounter;
                        }
                        imgButton9.setImageResource(imagePaths[9]);
                    }
                    break;
                case R.id.imageButton10:
                    if (selectedCardImageButton1 != imgButton10 && selectedCardImageButton2 != imgButton10) {
                        if (selectedCardCounter == 2) {
                            // tancar altres cartes
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton10;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton10;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton10;
                            ++selectedCardCounter;
                        }
                        imgButton10.setImageResource(imagePaths[10]);
                    }
                    break;
                case R.id.imageButton11:
                    if (selectedCardImageButton1 != imgButton11 && selectedCardImageButton2 != imgButton11) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton11;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton11;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton11;
                            ++selectedCardCounter;
                        }
                        imgButton11.setImageResource(imagePaths[11]);
                    }
                    break;
                case R.id.imageButton12:
                    if (selectedCardImageButton1 != imgButton12 && selectedCardImageButton2 != imgButton12) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton12;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton12;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton12;
                            ++selectedCardCounter;
                        }
                        imgButton12.setImageResource(imagePaths[12]);
                    }
                    break;
                case R.id.imageButton13:
                    if (selectedCardImageButton1 != imgButton13 && selectedCardImageButton2 != imgButton13) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton13;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton13;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton13;
                            ++selectedCardCounter;
                        }
                        imgButton13.setImageResource(imagePaths[13]);
                    }
                    break;
                case R.id.imageButton14:
                    if (selectedCardImageButton1 != imgButton14 && selectedCardImageButton2 != imgButton14) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton14;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton14;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton14;
                            ++selectedCardCounter;
                        }
                        imgButton14.setImageResource(imagePaths[14]);
                    }
                    break;
                case R.id.imageButton15:
                    if (selectedCardImageButton1 != imgButton15 && selectedCardImageButton2 != imgButton15) {
                        if (selectedCardCounter == 2) {
                            // Hide cards
                            selectedCardImageButton1.setImageResource(unknown);
                            selectedCardImageButton2.setImageResource(unknown);
                            selectedCardImageButton1 = imgButton15;
                            selectedCardImageButton2 = null;
                            selectedCardCounter = 1;
                        } else if (selectedCardCounter == 1) {
                            selectedCardImageButton2 = imgButton15;
                            ++selectedCardCounter;
                            rank++;
                            textView.setText(String.valueOf(rank));
                        } else {
                            selectedCardImageButton1 = imgButton15;
                            ++selectedCardCounter;
                        }
                        imgButton15.setImageResource(imagePaths[15]);
                    }
                    break;
            }
            if (selectedCardCounter == 2 && selectedCardImageButton1.getTag().toString().equals(selectedCardImageButton2.getTag().toString())) {


                // We need a new thread in order to block the app UI without blocking the system UI
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            block = true;
                            Thread.sleep(2000);
                            block = false;

                        } catch (InterruptedException e) {
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (selectedCardImageButton1 != null)
                                    selectedCardImageButton1.setVisibility(View.GONE);
                                if (selectedCardImageButton2 != null)
                                    selectedCardImageButton2.setVisibility(View.GONE);
                                if (remainingCards == 0)
                                    restart();

                            }
                        });
                    }
                };
                thread.start();

                remainingCards--;

                selectedCardCounter = 0;
            }
            if (remainingCards == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);
                AlertDialog dialog = builder.create();
                dialog.show();

                // Ranking

                prefLoggedIn = getSharedPreferences("com.example.jordi.myapplication", Context.MODE_PRIVATE);
                String user = prefLoggedIn.getString("user", null);
                LoginDataBaseAdapter loginDataBaseAdapter;
                loginDataBaseAdapter = new LoginDataBaseAdapter(this);
                loginDataBaseAdapter = loginDataBaseAdapter.open();
                if (rank > max_rank)
                    loginDataBaseAdapter.updateRank(user, rank);
                loginDataBaseAdapter.close();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.restart:
                restart();
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


    public void restart() {
        selectedCardCounter = 0;
        selectedCardImageButton1 = null;
        selectedCardImageButton2 = null;
        rank = 0;
        textView.setText(String.valueOf(rank));

        remainingCards = 8;

        Collections.shuffle(listImagePaths);
        imagePaths = listImagePaths.toArray(new Integer[listImagePaths.size()]);

        imgButton0.setVisibility(View.VISIBLE);
        imgButton1.setVisibility(View.VISIBLE);
        imgButton2.setVisibility(View.VISIBLE);
        imgButton3.setVisibility(View.VISIBLE);
        imgButton4.setVisibility(View.VISIBLE);
        imgButton5.setVisibility(View.VISIBLE);
        imgButton6.setVisibility(View.VISIBLE);
        imgButton7.setVisibility(View.VISIBLE);
        imgButton8.setVisibility(View.VISIBLE);
        imgButton9.setVisibility(View.VISIBLE);
        imgButton10.setVisibility(View.VISIBLE);
        imgButton11.setVisibility(View.VISIBLE);
        imgButton12.setVisibility(View.VISIBLE);
        imgButton13.setVisibility(View.VISIBLE);
        imgButton14.setVisibility(View.VISIBLE);
        imgButton15.setVisibility(View.VISIBLE);


        imgButton0 = (ImageButton) findViewById(R.id.imageButton0);
        imgButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imgButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imgButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imgButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imgButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imgButton6 = (ImageButton) findViewById(R.id.imageButton6);
        imgButton7 = (ImageButton) findViewById(R.id.imageButton7);
        imgButton8 = (ImageButton) findViewById(R.id.imageButton8);
        imgButton9 = (ImageButton) findViewById(R.id.imageButton9);
        imgButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imgButton11 = (ImageButton) findViewById(R.id.imageButton11);
        imgButton12 = (ImageButton) findViewById(R.id.imageButton12);
        imgButton13 = (ImageButton) findViewById(R.id.imageButton13);
        imgButton14 = (ImageButton) findViewById(R.id.imageButton14);
        imgButton15 = (ImageButton) findViewById(R.id.imageButton15);


        imgButton0.setImageResource(unknown);
        imgButton1.setImageResource(unknown);
        imgButton2.setImageResource(unknown);
        imgButton3.setImageResource(unknown);
        imgButton4.setImageResource(unknown);
        imgButton5.setImageResource(unknown);
        imgButton6.setImageResource(unknown);
        imgButton7.setImageResource(unknown);
        imgButton8.setImageResource(unknown);
        imgButton9.setImageResource(unknown);
        imgButton10.setImageResource(unknown);
        imgButton11.setImageResource(unknown);
        imgButton12.setImageResource(unknown);
        imgButton13.setImageResource(unknown);
        imgButton14.setImageResource(unknown);
        imgButton15.setImageResource(unknown);


        imgButton0.setTag(imagePaths[0]);
        imgButton1.setTag(imagePaths[1]);
        imgButton2.setTag(imagePaths[2]);
        imgButton3.setTag(imagePaths[3]);
        imgButton4.setTag(imagePaths[4]);
        imgButton5.setTag(imagePaths[5]);
        imgButton6.setTag(imagePaths[6]);
        imgButton7.setTag(imagePaths[7]);
        imgButton8.setTag(imagePaths[8]);
        imgButton9.setTag(imagePaths[9]);
        imgButton10.setTag(imagePaths[10]);
        imgButton11.setTag(imagePaths[11]);
        imgButton12.setTag(imagePaths[12]);
        imgButton13.setTag(imagePaths[13]);
        imgButton14.setTag(imagePaths[14]);
        imgButton15.setTag(imagePaths[15]);

    }

}