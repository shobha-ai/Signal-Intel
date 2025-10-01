package com.yourname.signalintel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This line connects the Java code to the layout file.
        setContentView(R.layout.activity_main);
    }
}
