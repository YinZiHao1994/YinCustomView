package com.source.yin.yincustomview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnWaveProgressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWaveProgressText = findViewById(R.id.btn_wave_progress_text);
        btnWaveProgressText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        Context context = getApplicationContext();
        switch (v.getId()) {
            case R.id.btn_wave_progress_text:
                intent = new Intent(context, WaveProgressTextActivity.class);
                startActivity(intent);

                break;
        }
    }
}
