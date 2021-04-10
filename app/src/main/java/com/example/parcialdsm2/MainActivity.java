package com.example.parcialdsm2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void bebidas(View view)
    {
        Intent i = new Intent(this,Bebidas.class);
        startActivity(i);
    }
    public void platillos(View view)
    {
        Intent i = new Intent(this,Comidas.class);
        startActivity(i);
    }
}