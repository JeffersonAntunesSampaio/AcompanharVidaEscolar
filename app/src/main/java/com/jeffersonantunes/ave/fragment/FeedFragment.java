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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.helper.Preferencias;
import com.jeffersonantunes.ave.model.Aluno;
import com.jeffersonantunes.ave.model.Feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Preferencias preferencias;
    private FloatingActionButton fb;
    private DatabaseReference dbAveReference;
    private View view;

    private ListView lstvwFeed;
    private ArrayList<Feed> feedArrayList;
    private ArrayAdapter<Feed> feedArrayAdapter;
    private ValueEventListener valueEventListenerFeed;

    private Feed feed;
    private String dataMensagem;
    private String mensagem;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        preferencias = new Preferencias(getActivity());

        String acesso = preferencias.getAcessoUsuario();
        if (acesso.equals("professor") || acesso.equals("administrador")){

            fb = (FloatingActionButton) view.findViewById(R.id.fbAddFeed);
            fb.setVisibility(View.VISIBLE);
            fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddFeed();
                    //if (proximoDialog) dialogAddFeed();
                }
            });

        }

        //Config ListView
        lstvwFeed = (ListView) view.findViewById(R.id.lstFeed);
        feedArrayList = new ArrayList<>();
        feedArrayAdapter = new ArrayAdapter<Feed>(
                getActivity()
                ,android.R.layout.simple_list_item_1
                , feedArrayList
        );
        lstvwFeed.setAdapter(feedArrayAdapter);

        dbAveReference = ConfigFirebase.getDbAveReference().child("feed");
        dbAveReference.orderByChild("data");

        valueEventListenerFeed = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //limpar array list
                feedArrayList.clear();
                //Pegando lista dos dados
                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Feed feed = dados.getValue(Feed.class);
                    feedArrayList.add(feed);
                }
                feedArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        dbAveReference.addValueEventListener(valueEventListenerFeed);

        return view;
    }


    private void dialogAddFeed(){

        feed = new Feed();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        //COnfiguração da alert
        alertDialog.setTitle("Comunicado");
        alertDialog.setMessage("Digite a mensagem e a data do comunidado que deseja envia a todos");
        alertDialog.setCancelable(false);

        //Personalizando dialog com layout
        Context context = this.getContext();
        final LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Add a TextView here for the "Title" label, as noted in the comments
        final EditText mensagemBox = new EditText(context);
        mensagemBox.setHint("digite sua mensagem");
        layout.addView(mensagemBox); // Notice this is an add method

        // Add another TextView here for the "Description" label
        final EditText dataBox = new EditText(context);
        dataBox.setHint("data do comunicado (dd/mm/aaaa)");
        layout.addView(dataBox); // Another add method

        alertDialog.setView(layout);

        //Configuração dos botões
        alertDialog.setPositiveButton("Mandar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mensagem = mensagemBox.getText().toString();
                dataMensagem = dataBox.getText().toString();

                //Valida se user digitou e-mail
                if (mensagem.isEmpty() || dataMensagem.isEmpty()){
                    Toast.makeText(getContext(), "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }else {

                    feed.setIdUsuario(preferencias.getIdentificador());
                    feed.setMensagem(mensagem);
                    feed.setData(dataMensagem);
                    //Recuperar Instancia Fire Base
                    dbAveReference = ConfigFirebase.getDbAveReference().child("feed");
                    dbAveReference.push().setValue(feed);
                    Toast.makeText(getContext(), "Comunicado Enviado", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStop() {
        super.onStop();
        dbAveReference.removeEventListener(valueEventListenerFeed);
    }
}
