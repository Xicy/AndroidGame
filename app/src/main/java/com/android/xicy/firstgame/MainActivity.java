package com.android.xicy.firstgame;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.Rectangle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static HashMap<String, Integer> color = new HashMap();
    Random rnd = new Random();
    LinearLayout.LayoutParams prm;
    ConstraintLayout backgrounLayout;
    LinearLayout buttonContainer;
    TextView pointTextView;
    TextView mathQuestionTextView;
    android.support.v4.app.NotificationCompat.Builder builder;
    NotificationManager nManager;
    ProgressBar timeProgressBar;
    CountDownTimer countDownTimer;
    int notificationID = 0;

    SharedPreferences preferences;
    final String settingsKey = "xicy.android.com.FirstGame";
    boolean isNotifed = false;
    int point = 0;

    public static void init() {
        if (color.size() != 0) return;
        color.put("Black", 0xFF000000);
        color.put("Dark Gray", 0xFF444444);
        color.put("Gray", 0xFF888888);
        color.put("Light Gray", 0xFFCCCCCC);
        color.put("White", 0xFFFFFFFF);
        color.put("Light Gray", 0xFFCCCCCC);
        color.put("Red", 0xFFFF0000);
        color.put("Green", 0xFF00FF00);
        color.put("Blue", 0xFF0000FF);
        color.put("Yellow", 0xFFFFFF00);
        color.put("Cyan", 0xFF00FFFF);
        color.put("Magenta", 0xFFFF00FF);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        prm = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        prm.setMargins(5, 5, 5, 5);

        backgrounLayout = (ConstraintLayout) findViewById(R.id.backgrounLayout);
        buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        pointTextView = (TextView) findViewById(R.id.pointTextView);
        mathQuestionTextView = (TextView) findViewById(R.id.mathQuestionTextView);
        timeProgressBar = (ProgressBar) findViewById(R.id.timeProgressBar);

        preferences = getSharedPreferences(settingsKey, Context.MODE_PRIVATE);
        point = preferences.getInt("Point", 0);

        builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Game");
        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        countDownTimer = new CountDownTimer(10000, 30) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeProgressBar.setProgress(10000 - (int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                finish();
            }
        };

        getRandomQuestion();
    }

    private void getRandomQuestion() {

        if (preferences.getInt("Point", 0) < point)
            preferences.edit().putInt("Point", point);

        if (point < 0)
            finish();

        buttonContainer.removeAllViews();
        pointTextView.setText("Point : " + point);
        mathQuestionTextView.setText("");

        if (point > 10 && !isNotifed) {
            isNotifed = true;
            nManager.notify(notificationID, builder.setContentText(point + " a ulaştınız").build());
        }

        if (rnd.nextFloat() > 0.5)
            getRandomColorQuestion();
        else
            getMathQuestion();

        countDownTimer.start();
    }

    private void getRandomColorQuestion() {
        Object[] arr = color.keySet().toArray();

        String value = (String) arr[rnd.nextInt(color.size())];

        Rectangle rect = new Rectangle();

        Button btnCorrect = new Button(this);
        btnCorrect.setLayoutParams(prm);
        GradientDrawable rdC = new GradientDrawable();
        rdC.setCornerRadius(8);
        rdC.setColor(color.get(value));
        btnCorrect.setBackground(rdC);
        btnCorrect.setTextColor(~color.get(value) | 0xFF000000);
        btnCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point += 2;
                getRandomQuestion();
            }
        });
        btnCorrect.setText(value);

        String wrongValue;
        do {
            wrongValue = (String) arr[rnd.nextInt(color.size())];
        } while (value.equals(wrongValue));

        String wrongValue2;
        do {
            wrongValue2 = (String) arr[rnd.nextInt(color.size())];
        } while (value.equals(wrongValue2) || wrongValue.equals(wrongValue2));

        String wrongValue3;
        do {
            wrongValue3 = (String) arr[rnd.nextInt(color.size())];
        }
        while (value.equals(wrongValue3) || wrongValue.equals(wrongValue3) || wrongValue2.equals(wrongValue3));


        Button btnWrong = new Button(this);
        btnWrong.setLayoutParams(prm);
        GradientDrawable rdW = new GradientDrawable();
        rdW.setCornerRadius(8);
        rdW.setColor(color.get(wrongValue));
        btnWrong.setBackground(rdW);
        btnWrong.setTextColor(~color.get(wrongValue3) | 0xFF000000);
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point -= 1;
                getRandomQuestion();
            }
        });
        btnWrong.setText(wrongValue);

        Button btnWrong2 = new Button(this);
        btnWrong2.setLayoutParams(prm);
        GradientDrawable rdW2 = new GradientDrawable();
        rdW2.setCornerRadius(8);
        rdW2.setColor(color.get(wrongValue2));
        btnWrong2.setBackground(rdW2);
        btnWrong2.setTextColor(~color.get(wrongValue) | 0xFF000000);
        btnWrong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point -= 1;
                getRandomQuestion();
            }
        });
        btnWrong2.setText(wrongValue2);

        Button btnWrong3 = new Button(this);
        btnWrong3.setLayoutParams(prm);
        GradientDrawable rdW3 = new GradientDrawable();
        rdW3.setCornerRadius(8);
        rdW3.setColor(color.get(wrongValue3));
        btnWrong.setBackground(rdW3);
        btnWrong3.setTextColor(~color.get(wrongValue2) | 0xFF000000);
        btnWrong3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point -= 1;
                getRandomQuestion();
            }
        });
        btnWrong3.setText(wrongValue3);

        for (int c = 0; c != 0x1111; ) {
            if (Math.floor(Math.log10(Math.max(1, point))) == 0)
                c |= 0x1100;
            else if (Math.floor(Math.log10(Math.max(1, point))) == 1)
                c |= 0x1000;
            else c |= 0x0000;

            if (rnd.nextInt(3) == 0 && (c & 0x0001) == 0) {
                buttonContainer.addView(btnCorrect);
                c |= 0x0001;
            } else if (rnd.nextInt(3) == 0 && (c & 0x0010) == 0) {
                buttonContainer.addView(btnWrong);
                c |= 0x0010;
            } else if (rnd.nextInt(3) == 0 && (c & 0x0100) == 0) {
                buttonContainer.addView(btnWrong2);
                c |= 0x0100;
            } else if (rnd.nextInt(3) == 0 && (c & 0x1000) == 0) {
                buttonContainer.addView(btnWrong3);
                c |= 0x1000;
            }
        }
    }

    private void getMathQuestion() {
        Object[] arr = color.keySet().toArray();
        String value = (String) arr[rnd.nextInt(color.size())];

        backgrounLayout.setBackgroundColor(color.get(value));
        timeProgressBar.getProgressDrawable().setColorFilter(~color.get(value) | 0xFF000000, android.graphics.PorterDuff.Mode.SRC_IN);
        pointTextView.setTextColor(~color.get(value) | 0xFF000000);
        mathQuestionTextView.setTextColor(~color.get(value) | 0xFF000000);

        int first = rnd.nextInt(20);
        int sec = rnd.nextInt(20);
        int operator = rnd.nextInt(3);
        String operatorStr = "";
        int result = 0;
        int resW = 0;
        int resW2 = 0;
        int resW3 = 0;
        switch (operator) {
            case 0:
                result = first + sec;
                operatorStr = " + ";
                resW = first + sec + 1;
                resW2 = first + sec - 1;
                resW3 = first + sec - 2;
                break;
            case 1:
                result = first - sec;
                operatorStr = " - ";
                resW = first - sec - 1;
                resW2 = first - sec + 2;
                resW3 = first - sec - 2;
                break;
            case 2:
                result = first * sec;
                operatorStr = " x ";
                resW = first * sec + 1;
                resW2 = first * sec + 3;
                resW3 = first * sec - 3;
                break;
        }

        mathQuestionTextView.setText(first + operatorStr + sec + " = ?");

        GradientDrawable rdC = new GradientDrawable();
        rdC.setCornerRadius(8);
        rdC.setColor(Color.GRAY);

        Button btnCorrect = new Button(this);
        btnCorrect.setLayoutParams(prm);
        btnCorrect.setBackground(rdC);
        btnCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point += 2;
                getRandomQuestion();
            }
        });
        btnCorrect.setText("" + result);

        Button btnWrong = new Button(this);
        btnWrong.setLayoutParams(prm);
        btnWrong.setBackground(rdC);
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point -= 1;
                getRandomQuestion();
            }
        });
        btnWrong.setText("" + resW);

        Button btnWrong2 = new Button(this);
        btnWrong2.setLayoutParams(prm);
        btnWrong2.setBackground(rdC);
        btnWrong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point -= 1;
                getRandomQuestion();
            }
        });
        btnWrong2.setText("" + resW2);

        Button btnWrong3 = new Button(this);
        btnWrong3.setLayoutParams(prm);
        btnWrong3.setBackground(rdC);
        btnWrong3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                point -= 1;
                getRandomQuestion();
            }
        });
        btnWrong3.setText("" + resW3);

        for (int c = 0; c != 0x1111; ) {
            if (Math.floor(Math.log10(Math.max(1, point))) == 0)
                c |= 0x1100;
            else if (Math.floor(Math.log10(Math.max(1, point))) == 1)
                c |= 0x1000;
            else c |= 0x0000;

            if (rnd.nextInt(3) == 0 && (c & 0x0001) == 0) {
                buttonContainer.addView(btnCorrect);
                c |= 0x0001;
            } else if (rnd.nextInt(3) == 0 && (c & 0x0010) == 0) {
                buttonContainer.addView(btnWrong);
                c |= 0x0010;
            } else if (rnd.nextInt(3) == 0 && (c & 0x0100) == 0) {
                buttonContainer.addView(btnWrong2);
                c |= 0x0100;
            } else if (rnd.nextInt(3) == 0 && (c & 0x1000) == 0) {
                buttonContainer.addView(btnWrong3);
                c |= 0x1000;
            }
        }
    }
}
