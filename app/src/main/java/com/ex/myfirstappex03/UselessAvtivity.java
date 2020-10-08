package com.ex.myfirstappex03;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class UselessAvtivity extends AppCompatActivity {

    String height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useless_avtivity);

        Intent intent = getIntent();

        height = String.valueOf(intent.getExtras().getInt("height"));
        Intent resultIntent = new Intent();
        resultIntent.putExtra("height", height);

        setResult(RESULT_OK,resultIntent);
        finish();
    }
}