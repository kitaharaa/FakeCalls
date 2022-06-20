package com.hfad.fakecalls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TimePicker timePicker;
    int imageId;
    String characterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
    }

    public void onSpaceCowIconClicked(View view) {
        onSelectedIconCLicked(R.drawable.space_cow, R.string.space_cow_name);
    }

    public void onPrimeIconClicked(View View) {
        onSelectedIconCLicked(R.drawable.prime, R.string.prime_name);
    }

    public void onBorisIconClicked(View view) {
        onSelectedIconCLicked(R.drawable.boris, R.string.boris_name);
    }

    public void onSelectedIconCLicked(int imageId, int characterNameId) {
        this.imageId = imageId;
        characterName = getString(characterNameId);

        Toast toast = Toast.makeText(this, "Обраний рятівник: "
                + this.characterName, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onSubmitButtonClicked(View view) {
        long delayTime = countingDelayTime();

        EditText enterNumber = (EditText) findViewById(R.id.enter_number);
        String phoneNumber = enterNumber.getText().toString();

        if (phoneNumber.matches("") || imageId == 0) {
            Toast toast = Toast.makeText(this,
                    "Введи всі данні!", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Intent intent = new Intent(MainActivity.this, CallActivity.class);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("imageId", imageId);
            intent.putExtra("characterName", characterName);

            Toast toast = Toast.makeText(this,"Герой: "
                            + characterName + "\n"
                            + "Номер: " + phoneNumber + "\n"
                            + "Приблизний час до дзвінка: "
                            + TimeUnit.MILLISECONDS.toSeconds(delayTime) + "сек.",
                    Toast.LENGTH_SHORT);
            toast.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    enterNumber.setText("");
                }
            },delayTime);
        }
    }

    private long countingDelayTime() {
        int hour = timePicker.getHour();
        int minutes = timePicker.getMinute();
        long selectedTimeInMillis = TimeUnit.HOURS.toMillis(hour)
                + TimeUnit.MINUTES.toMillis(minutes);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentSecond = calendar.get(Calendar.SECOND);
        long currentTimeInMillis = TimeUnit.HOURS.toMillis(currentHour)
                + TimeUnit.MINUTES.toMillis(currentMinute)
                + TimeUnit.SECONDS.toMillis(currentSecond);

        if (selectedTimeInMillis > currentTimeInMillis) {
            return (selectedTimeInMillis - currentTimeInMillis);
        } else {
            return (currentTimeInMillis - selectedTimeInMillis) + selectedTimeInMillis;
        }
    }
}