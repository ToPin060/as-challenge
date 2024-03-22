package com.as.challenge;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import androidx.core.app.ActivityCompat;

public class SoundMeter {

    private static final int sampleRate = 8000;
    private final static int MIN_BLOWING_AMPLITUDE = 5000;
    private AudioRecord audioRecord = null;
    private int minSize;

    public void start(Context context) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO handle
            return;
        }
        minSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize);
        audioRecord.startRecording();
    }

    public void stop() {
        if (audioRecord != null) audioRecord.stop();
    }

    public double getAmplitude() {
        short[] buffer = new short[minSize];
        audioRecord.read(buffer, 0, minSize);
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
        System.out.println("AMPLITUDE " + amplitude);
        return amplitude > MIN_BLOWING_AMPLITUDE;
    }

}
