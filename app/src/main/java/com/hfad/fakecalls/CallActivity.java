package com.hfad.fakecalls;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        mediaPlayer = MediaPlayer.create(this, R.raw.den_den_mushi);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        TextView characterName = (TextView) findViewById(R.id.character_name);
        TextView phoneNumber = (TextView) findViewById(R.id.phone_number);
        CircleImageView characterImage = (CircleImageView) findViewById(R.id.character_image);

        Intent intent = getIntent();

        String name = intent.getStringExtra("characterName");
        String number = intent.getStringExtra("phoneNumber");
        int imageId = intent.getIntExtra("imageId", 0);

        characterName.setText(name);
        phoneNumber.setText(number);
        characterImage.setImageResource(imageId);

        MotionLayout acceptMotion = (MotionLayout) findViewById(R.id.accept_motionLayout);
        ImageView acceptButton = (ImageView)  findViewById(R.id.accept_button);
        acceptMotion.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout,
                                            int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId,
                                           int endId, float progress) {
                changingButtonDuringMovement(progress, acceptButton);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {   //when touch button - exit
                changingButtonDuringTouching(acceptButton, currentId);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId,
                                            boolean positive, float progress) {

            }
        });

        MotionLayout rejectMotion = (MotionLayout) findViewById(R.id.reject_motionLayout);
        ImageView rejectButton = (ImageView) findViewById(R.id.reject_button);
        rejectMotion.setTransitionListener(
                new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout,
                                            int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId,
                                           int endId, float progress) {
                changingButtonDuringMovement(progress, rejectButton);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                changingButtonDuringTouching(rejectButton, currentId);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId,
                                            boolean positive, float progress) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    private void changingButtonDuringMovement(float progress,
                                              ImageView button) {
        if (progress >=0 & progress <= 1) {
            button.setImageResource(R.drawable.call);
        }
    }

    private void changingButtonDuringTouching(ImageView button, int currentId) {
        button.setImageResource(R.drawable.accept_call);
        if (currentId > 0.9) {
            System.exit(0);
        }
    }
}