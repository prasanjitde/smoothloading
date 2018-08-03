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

        SmoothLoadingView btnLoad = findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Smooth loading view", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
