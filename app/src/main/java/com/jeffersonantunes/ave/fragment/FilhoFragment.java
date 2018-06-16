package com.jeffersonantunes.ave.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.helper.Preferencias;
import com.jeffersonantunes.ave.model.Aluno;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilhoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilhoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilhoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton fb;

    private DatabaseReference dbAveReference;
    private Preferencias preferencias;

    private OnFragmentInteractionListener mListener;

    public FilhoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilhoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilhoFragment newInstance(String param1, String param2) {
        FilhoFragment fragment = new FilhoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_filho, container, false);

        preferencias = new Preferencias(getActivity());

        fb = (FloatingActionButton) view.findViewById(R.id.fbAddFilho);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastroContato();
            }
        });

        return view;

    }

    private void abrirCadastroContato(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        //COnfiguração da alert
        alertDialog.setTitle("Novo Filho");
        alertDialog.setMessage("Matricula");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(getActivity());
        alertDialog.setView(editText);

        //Configuração dos botões
        alertDialog.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String matriculaFilho = editText.getText().toString();

                //Valida se user digitou e-mail
                if (matriculaFilho.isEmpty()){
                    Toast.makeText(getContext(), "Por favor insira a matricula do seu filho (a)", Toast.LENGTH_SHORT).show();
                }else {

                    final String idUsuarioLogado = preferencias.getIdentificador();

                    //Recuperar Instancia Fire Base
                    dbAveReference = ConfigFirebase.getDbAveReference().child("aluno").child(matriculaFilho);

                    dbAveReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){

                                //Recuperar Dados do user Contato
                                Aluno alunoRecuperado = dataSnapshot.getValue(Aluno.class);

                                dbAveReference = ConfigFirebase.getDbAveReference().child("responsavel")
                                        .child(idUsuarioLogado)
                                        .child(String.valueOf(alunoRecuperado.getMatricula()))
                                        .child("nome_filho");

                                dbAveReference.setValue(alunoRecuperado.getNome());

                                Toast.makeText(getActivity(),"Filho adicionado",Toast.LENGTH_LONG).show();


                            }else {
                                Toast.makeText(getActivity(),"Não foi possivél adicionar essa matricula aos seus filhos, aluno não é cadastrado",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
