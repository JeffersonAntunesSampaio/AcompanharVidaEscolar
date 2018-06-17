package com.jeffersonantunes.ave.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.adapter.ChamadaAdapter;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.helper.Preferencias;
import com.jeffersonantunes.ave.model.Aluno;
import com.jeffersonantunes.ave.model.Aula;
import com.jeffersonantunes.ave.model.DataModel;
import com.jeffersonantunes.ave.model.Nota;
import com.jeffersonantunes.ave.model.Turma;

import java.util.ArrayList;

public class ChamadaActivity extends AppCompatActivity {

    private DatabaseReference dbAveReference;

    private ArrayList<Aluno> filhoArrayList;
    private ArrayAdapter<Aluno> filhoArrayAdapter;
    private ValueEventListener valueEventListenerFilho;

    private ListView lstChamada;
    private FloatingActionButton fb;
    private CheckBox checkBox;

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

        Preferencias preferencias = new Preferencias(ChamadaActivity.this);
        preferencias.salvarData(data);

        carregandoAlunos();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle("Turma: " + turma + " - " + data);     //Titulo para ser exibido na sua Action Bar em frente à seta


        fb = (FloatingActionButton) findViewById(R.id.fbLancaPresenca);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChamadaActivity.this, "Presença Lançada.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    private void carregandoAlunos(){

        //Config ListView
        lstChamada = (ListView) findViewById(R.id.lstChamada);
        filhoArrayList = new ArrayList<>();
        filhoArrayAdapter = new ChamadaAdapter(ChamadaActivity.this, filhoArrayList);
        /*filhoArrayAdapter = new ArrayAdapter<Aluno>(
                ChamadaActivity.this
                ,android.R.layout.simple_list_item_1
                , filhoArrayList
        );*/
        lstChamada.setAdapter(filhoArrayAdapter);

        dbAveReference = ConfigFirebase.getDbAveReference()
                .child("aluno");

        valueEventListenerFilho = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar array list
                filhoArrayList.clear();
                //Pegando lista dos dados
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Aluno aluno = dados.getValue(Aluno.class);
                    if (aluno.getTurma().equals(turma)) filhoArrayList.add(aluno);
                }
                filhoArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbAveReference.addValueEventListener(valueEventListenerFilho);

        checkBox = (CheckBox) findViewById(R.id.checkBox);
        lstChamada.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Aluno aluno = filhoArrayList.get(position);

            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        dbAveReference.removeEventListener(valueEventListenerFilho);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                finish();
                break;
            default:break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu,menu);
        return true;

    }
}
