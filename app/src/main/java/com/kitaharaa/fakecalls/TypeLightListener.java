package com.kitaharaa.fakecalls;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.PowerManager;
import android.util.Log;

public class TypeLightListener implements SensorEventListener {
    private final PowerManager.WakeLock wakeLock;

    public TypeLightListener(PowerManager manager) {
        int field = 0x00000020;
        try {
            field = PowerManager.class.getClass().getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
        } catch (Throwable ignored) {
            Log.e("field", "value is not assigned");
        }

        wakeLock = manager.newWakeLock(field,"com.kitaharaa.fakecalls:turnOffScreen");
    }

    //Turn on/of screen when accuracy of the registered sensor has changed.
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float value = sensorEvent.values[0];
        if (value < 4 && !wakeLock.isHeld()) {
            wakeLock.acquire();
        } else if(value >= 10 && wakeLock.isHeld()) {
            wakeLock.release();
            exitActivity();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //Exit Activity
    private void exitActivity() {
        System.exit(0);
    }
}
