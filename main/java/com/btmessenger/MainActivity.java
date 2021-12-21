package com.btmessenger;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * MainActivity : Application starts here
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Activity Life Cycle
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));

        // Object Initialization
        ConstraintLayout clServerContainer = findViewById(R.id.cl_server_button);
        ConstraintLayout clClientContainer = findViewById(R.id.cl_client_button);

        // Responds
        clServerContainer.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, com.btmessenger.ServerActivity.class);
            startActivity(intent);
            finish();
        });

        clClientContainer.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, com.btmessenger.ClientActivity.class);
            startActivity(intent);
            finish();
        });
    }
}