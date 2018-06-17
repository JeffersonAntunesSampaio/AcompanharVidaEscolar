package com.jeffersonantunes.ave.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.model.Aluno;
import com.jeffersonantunes.ave.model.Nota;
import com.jeffersonantunes.ave.model.Professor;
import com.jeffersonantunes.ave.model.Turma;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlunoActivity extends AppCompatActivity {

    private Aluno aluno;
    private Professor professor;
    private Bundle extra;
    private DatabaseReference dbAveReference;

    private ValueEventListener valueEventListenerAulaHoje;
    private ValueEventListener valueEventListenerTurma;
    private ValueEventListener valueEventListenerProfessor;

    private ArrayList<Nota> notaArrayList;
    private ArrayAdapter<Nota> notaArrayAdapter;
    private ValueEventListener valueEventListenerNota;

    private ArrayList<Turma> turmaArrayList;
    private ArrayAdapter<Turma> turmaArrayAdapter;
    private ValueEventListener valueEventListenerTurmaList;

    private TextView txtvwInfo;
    private TextView txtvwNomeAluno;
    private TextView txtvwMatricula;
    private TextView txtvwTurma;
    private TextView txtvwProfessor;
    private TextView txtvwDisciplina;
    private TextView txtvwPresente;
    private TextView txtvwSala;
    private TextView txtvwPeriodo;
    private ListView lstvwNota;
    private ListView lstvwAula;

    private String presente;
    private String diaSemana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        aluno = new Aluno();
        professor = new Professor();

        blocoAluno();
        blocoAulaHoje();
        blocoNota();
        //blocoTurma();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
        getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
        getSupportActionBar().setTitle(aluno.getNome());     //Titulo para ser exibido na sua Action Bar em frente à seta

    }

    /*private void blocoTurma(){

        //Config ListView
        lstvwAula = (ListView) findViewById(R.id.lstvwAula);
        turmaArrayList = new ArrayList<>();
        turmaArrayAdapter = new ArrayAdapter<Turma>(
                AlunoActivity.this
                ,android.R.layout.simple_list_item_1
                , turmaArrayList
        );
        lstvwNota.setAdapter(turmaArrayAdapter);

        dbAveReference = ConfigFirebase.getDbAveReference()
                .child("turma")
                .child(aluno.getTurma());

        valueEventListenerTurmaList = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar array list
                turmaArrayList.clear();
                //Pegando lista dos dados
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Turma turma = dados.getValue(Turma.class);
                    turmaArrayList.add(turma);
                }
                turmaArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbAveReference.addValueEventListener(valueEventListenerTurmaList);

    }*/

    private void blocoNota(){

        //Config ListView
        lstvwNota = (ListView) findViewById(R.id.lstvwNota);
        notaArrayList = new ArrayList<>();
        notaArrayAdapter = new ArrayAdapter<Nota>(
                AlunoActivity.this
                ,android.R.layout.simple_list_item_1
                , notaArrayList
        );
        lstvwNota.setAdapter(notaArrayAdapter);

        dbAveReference = ConfigFirebase.getDbAveReference()
                .child("nota")
                .child(String.valueOf(aluno.getMatricula()));

        valueEventListenerNota = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar array list
                notaArrayList.clear();
                //Pegando lista dos dados
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Nota nota = dados.getValue(Nota.class);
                    notaArrayList.add(nota);
                }
                notaArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbAveReference.addValueEventListener(valueEventListenerNota);

    }


    private void blocoAulaHoje() {

        txtvwInfo = (TextView) findViewById(R.id.txtvwInfo);
        txtvwProfessor = (TextView) findViewById(R.id.txtvwProfessor);
        txtvwDisciplina = (TextView) findViewById(R.id.txtvwDisciplina);
        txtvwPresente = (TextView) findViewById(R.id.txtvwPresente);
        txtvwSala = (TextView) findViewById(R.id.txtvwSala);
        txtvwPeriodo = (TextView) findViewById(R.id.txtvwPeriodo);


        //Obtendo data de hoje e colocando no formato
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        diaSemana = getWeek(dateString);

        if (diaSemana.equals("DOM") || diaSemana.equals("SAB")) {
            txtvwInfo.setText("Hoje não tem Aula");
            txtvwInfo.setTextColor(Color.WHITE);
            txtvwInfo.setBackgroundColor(Color.RED);
        } else {
            txtvwInfo.setText("Aula de Hoje (" + dateString + ")");
            txtvwInfo.setBackgroundColor(Color.TRANSPARENT);
        }

        dbAveReference = ConfigFirebase.getDbAveReference()
                .child("aula")
                .child(dateString)
                .child(String.valueOf(aluno.getMatricula()))
                .child("presente");

        valueEventListenerAulaHoje = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    presente = dataSnapshot.getValue().toString();
                    switch (presente) {
                        case "V":
                            presente = "Presença Confirmada";
                            txtvwPresente.setText(presente);
                            txtvwPresente.setTextColor(Color.BLACK);
                            txtvwPresente.setBackgroundColor(Color.GREEN);
                            break;
                        case "F":
                            presente = "Falta";
                            txtvwPresente.setText(presente);
                            txtvwPresente.setTextColor(Color.WHITE);
                            txtvwPresente.setBackgroundColor(Color.RED);
                            break;
                        default:
                            presente = "-";
                            txtvwPresente.setText(presente);
                            txtvwPresente.setTextColor(Color.BLACK);
                            txtvwPresente.setBackgroundColor(Color.TRANSPARENT);
                            break;
                    }
                } else {
                    presente = "-";
                    txtvwPresente.setTextColor(Color.BLACK);
                    txtvwPresente.setBackgroundColor(Color.TRANSPARENT);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbAveReference.addValueEventListener(valueEventListenerAulaHoje);

        dbAveReference = ConfigFirebase.getDbAveReference()
                .child("turma")
                .child(aluno.getTurma());

        valueEventListenerTurma = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        if (dados.getKey().equals("sala")) txtvwSala.setText(dados.getValue().toString());
                        if (dados.getKey().equals("turno")) txtvwPeriodo.setText(dados.getValue().toString());

                        if (dados.getKey().equals(diaSemana.toLowerCase())) {
                            professor.setMatricula(Integer.parseInt(dados.getValue().toString()));
                            if (professor.equals(0) || professor == null) {

                            }else {
                                atualizarDadosAulaHoje();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbAveReference.addValueEventListener(valueEventListenerTurma);

    }

    private void atualizarDadosAulaHoje(){

        dbAveReference = ConfigFirebase.getDbAveReference()
                .child("professor")
                .child(String.valueOf(professor.getMatricula()));

        valueEventListenerProfessor = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Professor professorRecuperado = dataSnapshot.getValue(Professor.class);
                    professor.setDisciplina(professorRecuperado.getDisciplina());
                    professor.setNome(professorRecuperado.getNome());
                    txtvwProfessor.setText(professor.getNome());
                    txtvwDisciplina.setText(professor.getDisciplina());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbAveReference.addValueEventListener(valueEventListenerProfessor);
    }

    private void blocoAluno(){

        extra = getIntent().getExtras();

        aluno.setNome(extra.getString("nome"));
        aluno.setMatricula(Integer.parseInt(extra.getString("matricula")));
        aluno.setTurma(extra.getString("turma"));

        txtvwNomeAluno          = (TextView) findViewById(R.id.txtvwNomeAluno);
        txtvwMatricula          = (TextView) findViewById(R.id.txtvwMatricula);
        txtvwTurma              = (TextView) findViewById(R.id.txtvwTurma);

        txtvwNomeAluno.setText(aluno.getNome());
        txtvwMatricula.setText(String.valueOf(aluno.getMatricula()));
        txtvwTurma.setText(aluno.getTurma());

    }

    public static String getWeek(String date){ //ex 07/03/2017
        String dayWeek = "---";
        GregorianCalendar gc = new GregorianCalendar();
        try {
            gc.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(date));
            switch (gc.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    dayWeek = "DOM";
                    break;
                case Calendar.MONDAY:
                    dayWeek = "SEG";
                    break;
                case Calendar.TUESDAY:
                    dayWeek = "TER";
                    break;
                case Calendar.WEDNESDAY:
                    dayWeek = "QUA";
                    break;
                case Calendar.THURSDAY:
                    dayWeek = "QUI";
                    break;
                case Calendar.FRIDAY:
                    dayWeek = "SEX";
                    break;
                case Calendar.SATURDAY:
                    dayWeek = "SAB";

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayWeek;
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
    public void onStop() {
        super.onStop();
        dbAveReference.removeEventListener(valueEventListenerAulaHoje);
        dbAveReference.removeEventListener(valueEventListenerTurma);
        dbAveReference.removeEventListener(valueEventListenerProfessor);
        dbAveReference.removeEventListener(valueEventListenerNota);
        //dbAveReference.removeEventListener(valueEventListenerTurmaList);
    }

}
