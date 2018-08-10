package com.prasanjit.smoothloading;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.prasanjit.smoothloadinglib.SmoothLoadingView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SmoothLoadingView btnLoad = findViewById(R.id.btn_load);
        // SmoothLoadingView btnLoad = new SmoothLoadingView(this);

        btnLoad.setOnSmoothLoadingClickEventListener(new SmoothLoadingView.OnSmoothLoadingClickEventListener() {
            @Override
            public void onSmoothButtonClicked(SmoothLoadingView view) {
                Toast.makeText(getApplicationContext(), "Smooth loading started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSmoothAnimateCompleted(SmoothLoadingView view) {
                Toast.makeText(getApplicationContext(), "Smooth loading ended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSmoothAnimationStopped(SmoothLoadingView view) {
                Toast.makeText(getApplicationContext(), "Smooth loading ended", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
