package com.kitaharaa.fakecalls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * Represents a main screen of an application
 */
public class MainActivity extends AppCompatActivity {
    TimePicker timePicker;
    CircleImageView spaceCowCircleImageView;
    CircleImageView primeCircleImageView;
    CircleImageView borisCircleImageView;
    private boolean alreadyExecuted;
    int imageId;
    String characterName;
    EditText enterNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enterNumber = findViewById(R.id.enter_number);

        timePicker = findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        spaceCowCircleImageView = findViewById(R.id.space_cow_image);
        primeCircleImageView = findViewById(R.id.prime_image);
        borisCircleImageView = findViewById(R.id.boris_image);

        isSavedInstanceStateNull(savedInstanceState);
    }

    //Check if activity was destroyed and rotated
    private void isSavedInstanceStateNull(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            enterNumber.setText(savedInstanceState.getString("enterNumber"));
            timePicker.setHour(savedInstanceState.getInt("hour"));
            timePicker.setMinute(savedInstanceState.getInt("minute"));
            characterName = savedInstanceState.getString("characterName");

            imageId = savedInstanceState.getInt("imageId");
            switch (imageId) {
                case R.drawable.character_space_cow:
                    changeBorderColorToPurple(spaceCowCircleImageView);
                    break;
                case R.drawable.character_boris:
                    changeBorderColorToPurple(borisCircleImageView);
                    break;
                case R.drawable.character_prime:
                    changeBorderColorToPurple(primeCircleImageView);
                    break;
            }
        }
    }

    //Save instance before destroy
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("enterNumber", enterNumber.getText().toString());
        savedInstanceState.putInt("imageId", imageId);
        savedInstanceState.putInt("hour",timePicker.getHour());
        savedInstanceState.putInt("minute", timePicker.getMinute());
        savedInstanceState.putString("characterName", characterName);
    }

    public void onSpaceCowIconClicked(View view) {
        setDataForIntent(R.drawable.character_space_cow, R.string.space_cow_name);
        changeBorderColorToPurple(spaceCowCircleImageView);
    }

    public void onPrimeIconClicked(View View) {
        setDataForIntent(R.drawable.character_prime, R.string.prime_name);
        changeBorderColorToPurple(primeCircleImageView);
    }

    public void onBorisIconClicked(View view) {
        setDataForIntent(R.drawable.character_boris, R.string.boris_name);
        changeBorderColorToPurple(borisCircleImageView);
    }

    //Change border color of selected image to purple
    private void changeBorderColorToPurple(CircleImageView image) {
        if (image != null) {
            changeBorderColorToDefault();
            image.setBorderColor(getResources().
                    getColor(R.color.selected_image,this.getTheme()));
        } else {
            changeBorderColorToDefault();
        }
    }

    //Change border color to default
    private void changeBorderColorToDefault() {
        spaceCowCircleImageView.setBorderColor(getResources().
                getColor(R.color.default_color,this.getTheme()));
        primeCircleImageView.setBorderColor(getResources().
                getColor(R.color.default_color,this.getTheme()));
        borisCircleImageView.setBorderColor(getResources().
                getColor(R.color.default_color,this.getTheme()));
    }

    //Output information about selected character
    private void setDataForIntent(int imageId,
                                         int characterNameId) {
        this.imageId = imageId;
        characterName = getString(characterNameId);

        Toast toast = Toast.makeText(this,
                getResources().getString(R.string.choosen_hero_info)
                + this.characterName, Toast.LENGTH_SHORT);
        toast.show();
    }

    //Show information about user choice
    public void onSubmitButtonClicked(View view) {
        if(!alreadyExecuted) {
            long delayMillis = countDelayMillis();

            String phoneNumber = enterNumber.getText().toString();

            if (isValidPhoneNumber(phoneNumber) && isValidImageId(imageId)) {
                Toast.makeText(this,
                            getResources().getString(R.string.hero_info)
                                + characterName + "\n"
                                + getResources().getString(R.string.number_info) + phoneNumber + "\n"
                                + getResources().getString(R.string.approximate_time_info)
                                + TimeUnit.MILLISECONDS.toSeconds(delayMillis) + getResources().getString(R.string.seconds_info),
                                Toast.LENGTH_SHORT).show();

                delayStartCallActivity(phoneNumber,characterName, delayMillis);

                isBackgroundMethodExecute(true);
            } else {
                Toast toast = Toast.makeText(this,
                        getResources().getString(R.string.enter_all_data_warning), Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast.makeText(this,
                    getResources().getString(R.string.already_execute_warning),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Change boolean value
    private void isBackgroundMethodExecute(boolean value) {
        alreadyExecuted = value;
    }

    //Validate entered number
    private boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^[+][3][8][0-9]{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    //Validate selected character
    private boolean isValidImageId(int imageId) {
        return imageId != 0;
    }

    //Start CallActivity with delay
    private void delayStartCallActivity(String phoneNumber, String characterName, long delayMillis) {
        Intent intent = new Intent(MainActivity.this, CallActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("imageId", imageId);
        intent.putExtra("characterName", characterName);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        clearEnterNumber(); //try to set here listener to sensor
                        isBackgroundMethodExecute(false);
                    }
                });
            }
        }, delayMillis);
    }

    //Clear phone number in EditText
    private void clearEnterNumber() {
        enterNumber.setText("");
    }

    //Counts time in millis to make a fake call
    private long countDelayMillis() {
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