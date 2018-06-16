package com.jeffersonantunes.ave.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.model.Aluno;
import com.jeffersonantunes.ave.model.Turma;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TurmaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TurmaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TurmaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText txtTurma;
    private EditText txtTurno;
    private EditText txtSala;
    private EditText txtSeg;
    private EditText txtTer;
    private EditText txtQua;
    private EditText txtQui;
    private EditText txtSex;
    private EditText txtMatriculaAluno;
    private EditText txtTurmaAluno;
    private EditText txtNomeAluno;
    private Button btnCadastrarTurma;
    private Button btnCadastrarAluno;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TurmaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TurmaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TurmaFragment newInstance(String param1, String param2) {
        TurmaFragment fragment = new TurmaFragment();
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

        View view =  inflater.inflate(R.layout.fragment_turma, container, false);

        txtTurma                                = (EditText) view.findViewById(R.id.txtTurma);
        txtTurno                                = (EditText) view.findViewById(R.id.txtTurno);
        txtSala                                 = (EditText) view.findViewById(R.id.txtSala);
        txtSeg                                  = (EditText) view.findViewById(R.id.txtSeg);
        txtTer                                  = (EditText) view.findViewById(R.id.txtTer);
        txtQua                                  = (EditText) view.findViewById(R.id.txtQua);
        txtQui                                  = (EditText) view.findViewById(R.id.txtQui);
        txtSex                                  = (EditText) view.findViewById(R.id.txtSex);
        txtMatriculaAluno                       = (EditText) view.findViewById(R.id.txtMatriculaAluno);
        txtTurmaAluno                           = (EditText) view.findViewById(R.id.txtTurmaAluno);
        txtNomeAluno                            = (EditText) view.findViewById(R.id.txtNomeAluno);

        btnCadastrarTurma                       = (Button) view.findViewById(R.id.btnCadastrarTurma);
        btnCadastrarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaEditText("turma")){
                    Turma turma = new Turma();

                    turma.setTurma(txtTurma.getText().toString());
                    turma.setTurno(txtTurno.getText().toString());
                    turma.setSala(Integer.parseInt(txtSala.getText().toString()));
                    turma.setSeg(Integer.parseInt(txtSeg.getText().toString()));
                    turma.setTer(Integer.parseInt(txtTer.getText().toString()));
                    turma.setQua(Integer.parseInt(txtQua.getText().toString()));
                    turma.setQui(Integer.parseInt(txtQui.getText().toString()));
                    turma.setSex(Integer.parseInt(txtSex.getText().toString()));

                    turma.salvar();

                    Toast.makeText(getActivity(), "Nova turma adicionada.", Toast.LENGTH_LONG).show();

                    txtTurma.setText("");
                    txtTurno.setText("");
                    txtSala.setText("");
                    txtSeg.setText("");
                    txtTer.setText("");
                    txtQua.setText("");
                    txtQui.setText("");
                    txtSex.setText("");


                }
            }
        });


        btnCadastrarAluno                       = (Button) view.findViewById(R.id.btnCadastrarAluno);
        btnCadastrarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaEditText("aluno")){

                    Aluno aluno = new Aluno();

                    aluno.setTurma(txtTurmaAluno.getText().toString());
                    aluno.setNome(txtNomeAluno.getText().toString());
                    aluno.setMatricula(Integer.parseInt(txtMatriculaAluno.getText().toString()));

                    aluno.salvar();

                    Toast.makeText(getActivity(), "Novo usuario adicionado.", Toast.LENGTH_LONG).show();

                    txtTurmaAluno.setText("");
                    txtMatriculaAluno.setText("");
                    txtNomeAluno.setText("");

                }
            }
        });




        // Inflate the layout for this fragment
        return view;

    }

    private boolean validaEditText(String bloco){

        switch (bloco) {
            case "turma":
                if (txtTurma.getText().length() <= 0
                        || txtTurno.getText().length() <= 0
                        || txtSala.getText().length() <= 0
                        || txtSeg.getText().length() <= 0
                        || txtTer.getText().length() <= 0
                        || txtQua.getText().length() <= 0
                        || txtQui.getText().length() <= 0
                        || txtSex.getText().length() <= 0) {

                    Toast.makeText(getActivity(), "Por favor preencha todos os campos para continuar.", Toast.LENGTH_LONG).show();
                    return false;
                } else {
                    return true;
                }
            case "aluno":
                if (txtMatriculaAluno.getText().length() <= 0
                        || txtTurmaAluno.getText().length() <= 0
                        || txtNomeAluno.getText().length() <= 0) {

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
