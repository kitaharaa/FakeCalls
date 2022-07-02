package com.kitaharaa.fakecalls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/*
* Make a fake call based on information inputted in MainActivity
 */
public class CallActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private PowerManager manager;

    //Initialize variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        TextView characterName = findViewById(R.id.character_name);
        TextView phoneNumber = findViewById(R.id.phone_number);
        CircleImageView characterImage = findViewById(R.id.character_image);

        manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        setMediaPlayer();
        setIntentData(characterName, phoneNumber, characterImage);
        setListenerToMotionLayout();
    }

    //Initialize media player
    private void setMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.den_den_mushi_ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    //Set data from intent
    private void setIntentData(TextView characterName, TextView phoneNumber,
                               CircleImageView characterImage) {
        Intent intent = getIntent();
        String name = intent.getStringExtra("characterName");
        String number = intent.getStringExtra("phoneNumber");
        int imageId = intent.getIntExtra("imageId", 0);

        characterName.setText(name);
        phoneNumber.setText(number);
        characterImage.setImageResource(imageId);
    }

    //Start ringtone when app is visible
    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();

        registerSensorListener();
    }

    //Pause ringtone when app is invisible
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();

        unregisterSensorListener();
    }

    //Register sensor listener
    private void registerSensorListener() {
        sensorManager.registerListener(new TypeLightListener(manager),
                lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Unregister sensor listener
    private void unregisterSensorListener() {
        sensorManager.unregisterListener(new TypeLightListener(manager));
    }

    //Set listeners to accept/reject buttons
    private void setListenerToMotionLayout() {
        MotionLayout acceptMotion = findViewById(R.id.accept_motionLayout);
        MotionLayout rejectMotion = findViewById(R.id.reject_motionLayout);
        ImageView acceptButton = findViewById(R.id.accept_button);
        ImageView rejectButton = findViewById(R.id.reject_button);

        acceptMotion.setTransitionListener(new ImageViewTransitionListener
                (acceptButton, mediaPlayer, acceptButton, rejectButton));
        rejectMotion.setTransitionListener(new ImageViewTransitionListener
                (rejectButton, mediaPlayer, acceptButton, rejectButton));
    }
}