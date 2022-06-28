package com.kitaharaa.fakecalls;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.widget.ImageView;

import androidx.constraintlayout.motion.widget.MotionLayout;

/*
*Represent actions while user touch and move reject/accept call buttons
*/
public class ImageViewTransitionListener implements
                                        MotionLayout.TransitionListener {
    private final ImageView answerCallButton;
    private final MediaPlayer mediaPlayer;
    private final ImageView acceptButton;
    private final ImageView rejectButton;

    public ImageViewTransitionListener(ImageView answerCallButton,
                                       MediaPlayer mediaPlayer,
                                       ImageView acceptButton,
                                       ImageView rejectButton) {
        this.answerCallButton = answerCallButton;
        this.mediaPlayer = mediaPlayer;
        this.acceptButton = acceptButton;
        this.rejectButton = rejectButton;
    }
    @Override
    public void onTransitionStarted(MotionLayout motionLayout,
                                    int startId, int endId) {
    }

    @Override
    public void onTransitionChange(MotionLayout motionLayout, int startId,
                                   int endId, float progress) {
        changeImageOfMovingButton(progress,answerCallButton);
        doOnEndPoint(motionLayout);
    }

    @Override
    public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {   //when touch button - exit
        changeImageToDefault();
        doOnEndPoint(motionLayout);
    }

    @Override
    public void onTransitionTrigger(MotionLayout motionLayout, int triggerId,
                                    boolean positive, float progress) {

    }

    //Do actions on achieving end point
    private void doOnEndPoint(MotionLayout motionLayout) {
        if(motionLayout.getProgress() == 1) {
            endMediaPlayer();
            deleteButtonsImages();
        }
    }

    //Change button view on achieving different position
    private void changeImageOfMovingButton(float progress,
                                              ImageView button) {
        if(progress == 0) {
            switch (button.getId()) {
                case R.id.accept_button:
                    changeImageToDefault();
                    break;
                case R.id.reject_button:
                    changeImageToDefault();
                    break;
            }
        }
        if (progress >=0 & progress <= 1) {
            button.setImageResource(R.drawable.call_button);
        }
    }

    private void changeImageToDefault() {
        if (answerCallButton.getId() == R.id.accept_button) {
            answerCallButton.setImageResource(R.drawable.accept_call_buton);
        } else {
            answerCallButton.setImageResource(R.drawable.reject_call_button);
        }
    }

    //End ringtone
    private void endMediaPlayer() {
        mediaPlayer.stop();
    }

    //Delete button images
    private void deleteButtonsImages() {
        acceptButton.setImageResource(0);
        rejectButton.setImageResource(0);
    }
}