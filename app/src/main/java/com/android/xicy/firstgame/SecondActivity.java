package com.android.xicy.firstgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    Button startButton;
    TextView maxPointTextView;
    final String settingsKey = "xicy.android.com.FirstGame";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        preferences = getSharedPreferences(settingsKey, Context.MODE_PRIVATE);

        maxPointTextView = (TextView) findViewById(R.id.maxPointTextView);
        maxPointTextView.setText("Max Point : " + preferences.getInt("Point", 0));
        startButton = (Button) findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
