package com.source.yin.yincustomview;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.source.yin.yincustomview.waveprogresstext.WaveProgressText;

public class WaveProgressTextActivity extends AppCompatActivity {

    private WaveProgressText waveProgressText;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave_progress_text);
        waveProgressText = findViewById(R.id.wave_progress_text);

        seekBar = findViewById(R.id.seek_bar);
        seekBar.setMax(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double rate = 1.0 * progress / seekBar.getMax();
                waveProgressText.setProgress((int) (rate * waveProgressText.getMaxProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        waveProgressText.setWaveLengthTag(2);
        waveProgressText.setDuration(1000);
        waveProgressText.setMaxProgress(500);
        waveProgressText.setSwingTag(10);
        waveProgressText.setWaveColor(Color.WHITE);


        ValueAnimator mAnimator = ValueAnimator.ofInt(0, 500);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
//                waveProgressText.setProgress(animatedValue);
            }
        });
        mAnimator.setDuration(10000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();

    }

}
