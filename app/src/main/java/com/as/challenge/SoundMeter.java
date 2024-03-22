package com.as.challenge;

import static java.lang.Thread.sleep;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.core.app.ActivityCompat;

public class SoundMeter {

    private static final int sampleRate = 8000;
    private AudioRecord ar = null;
    private int minSize;

    public void start(Context context) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO handle
            return;
        }
        minSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
        ar.startRecording();


        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("TEST " + this.getAmplitude());
                    sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void stop() {
        if (ar != null) {
            ar.stop();
        }
    }

    public double getAmplitude() {
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer) {
            if (Math.abs(s) > max) {
                max = Math.abs(s);
            }
        }
        return max;
    }

    public boolean isBlowingOnMicrophone() {
        double amplitude = getAmplitude();
        return amplitude > 5000;
    }

}
