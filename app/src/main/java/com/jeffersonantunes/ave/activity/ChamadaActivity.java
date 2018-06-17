package com.jeffersonantunes.ave.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class ChamadaActivity extends AppCompatActivity {
    private String data;
    private String turma;
    TextView Listaalunos;
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ArrayList<Aluno> filhoArrayList;
    private ArrayAdapter<Aluno> filhoArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamada);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Bundle extra = getIntent().getExtras();

        Listaalunos = (TextView) findViewById(R.id.Listaalunos);


        databaseReference.child("turma").child("1A").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               Listaalunos.setText(String.valueOf(dataSnapshot.getValue()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        if (extra != null){
            data = extra.getString("data");
            turma = extra.getString("turma");
        }

        setTitle("Chamada - " + turma + " - " + data);


    }
}
