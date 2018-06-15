package com.jeffersonantunes.ave.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.activity.CadastroUsuarioActivity;
import com.jeffersonantunes.ave.activity.ChamadaActivity;
import com.jeffersonantunes.ave.activity.LoginActivity;
import com.jeffersonantunes.ave.activity.MainActivity;
import com.jeffersonantunes.ave.model.Professor;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfessorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfessorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfessorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText txtMatriculaProfessor;
    private EditText txtNomeProfessor;
    private EditText txtDisciplina;
    private EditText txtTurmaChamada;
    private EditText txtData;
    private Button btnCadastrarProfessor;
    private Button btnChamada;

    private OnFragmentInteractionListener mListener;

    public ProfessorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfessorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfessorFragment newInstance(String param1, String param2) {
        ProfessorFragment fragment = new ProfessorFragment();
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
        View view = inflater.inflate(R.layout.fragment_professor, container, false);

        txtMatriculaProfessor                      = (EditText) view.findViewById(R.id.txtMatriculaProfessor);
        txtNomeProfessor                           = (EditText) view.findViewById(R.id.txtNomeProfessor);
        txtDisciplina                              = (EditText) view.findViewById(R.id.txtDisciplina);
        txtTurmaChamada                            = (EditText) view.findViewById(R.id.txtTurmaChamada);
        txtData                                    = (EditText) view.findViewById(R.id.txtDataChamada);

        btnCadastrarProfessor                      = (Button) view.findViewById(R.id.btnCadastrarProfessor);
        btnCadastrarProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaEditText("professor")){

                    Professor professor = new Professor();

                    professor.setDisciplina(txtDisciplina.getText().toString());
                    professor.setNome(txtNomeProfessor.getText().toString());
                    professor.setMatricula(Integer.parseInt(txtMatriculaProfessor.getText().toString()));

                    professor.salvar();

                    Toast.makeText(getActivity(), "Novo professor adicionado.", Toast.LENGTH_LONG).show();

                    txtDisciplina.setText("");
                    txtNomeProfessor.setText("");
                    txtMatriculaProfessor.setText("");

                }
            }
        });


        btnChamada                     = (Button) view.findViewById(R.id.btnFazerChamada);
        btnChamada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaEditText("chamada")){
                    abrirLoginUsuario();
                }
            }
        });

        return view;
    }
    private void abrirLoginUsuario(){
        Intent intent = new Intent(getActivity(), ChamadaActivity.class);
        intent.putExtra("data",txtData.toString());
        intent.putExtra("turma",txtTurmaChamada.toString());
        startActivity(intent);
    }
    private boolean validaEditText(String bloco){

        switch (bloco) {
            case "professor":
                if (txtMatriculaProfessor.getText().length() <= 0
                        || txtNomeProfessor.getText().length() <= 0
                        || txtDisciplina.getText().length() <= 0) {

                    Toast.makeText(getActivity(), "Por favor preencha todos os campos para continuar.", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            case "chamada":
                if (txtTurmaChamada.getText().length() <= 0
                        || txtData.getText().length() <= 0) {

                    Toast.makeText(getActivity(), "Por favor preencha todos os campos para continuar.", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            default:
                return false;
        }
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
