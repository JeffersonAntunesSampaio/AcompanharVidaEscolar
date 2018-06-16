package com.jeffersonantunes.ave.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeffersonantunes.ave.R;

public class ChamadaActivity extends AppCompatActivity {
    private String data;
    private String turma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamada);

        Bundle extra = getIntent().getExtras();

        if (extra != null){
            data = extra.getString("data");
            turma = extra.getString("turma");
        }

    }
}
