package com.jeffersonantunes.ave.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.helper.Preferencias;
import com.jeffersonantunes.ave.model.Aluno;
import com.jeffersonantunes.ave.model.Aula;

import java.util.ArrayList;

public class ChamadaAdapter extends ArrayAdapter<Aluno> {

    private ArrayList<Aluno> alunos;
    private Context context;
    private Aula aula;
    private Preferencias preferencias;

    public ChamadaAdapter(@NonNull Context c, @NonNull ArrayList objects) {
        super(c, 0, objects);
        this.context = c;
        this.alunos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        if (alunos!=null){

            //Inicializa objeto para montagem da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montar view apartir do xml
            view = inflater.inflate(R.layout.ly_list_chamada, parent, false);

            //Recupera Elemento para exibição
            TextView txtvwChamadaNome = (TextView) view.findViewById(R.id.txtvwChamadaNome);
            //TextView txtvwMensagemConversa = (TextView) view.findViewById(R.id.txtvwMensagemConversa);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

            Aluno aluno = alunos.get(position);

            preferencias = new Preferencias(getContext());

            aula = new Aula();
            aula.setData(preferencias.getData());
            aula.setMatriculaAluno(aluno.getMatricula());
            aula.setPresente("F");

            aula.salvar();

            txtvwChamadaNome.setText(aluno.getMatricula() + " - " + aluno.getNome());
            checkBox.setChecked(aluno.getMarcado());
            checkBox.setTag(aluno);

            checkBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    CheckBox check = (CheckBox) v;

                    Aluno a = (Aluno) v.getTag();
                    a.setMarcado(((CheckBox) v).isChecked());


                    if (check.isChecked()) {
                        //Toast.makeText(context, "Marcado Nº " + a.getMatricula(), Toast.LENGTH_SHORT).show();
                        //Faz uma checagem se existe o mesmo valor na lista de inteiros
                        aula.setData(preferencias.getData());
                        aula.setMatriculaAluno(a.getMatricula());
                        aula.setPresente("V");

                        aula.salvar();


                    } else {
                        //Toast.makeText(context, "Desmarcado Nº " + a.getMatricula(), Toast.LENGTH_SHORT).show();
                        //Faz uma checagem se existe o mesmo valor na lista de inteiros
                        aula.setData(preferencias.getData());
                        aula.setMatriculaAluno(a.getMatricula());
                        aula.setPresente("F");

                        aula.salvar();


                    }

                }
            });



        }

        return view;
    }
}
