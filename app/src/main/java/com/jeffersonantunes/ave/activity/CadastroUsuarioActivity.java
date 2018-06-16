package com.jeffersonantunes.ave.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.helper.Base64Custom;
import com.jeffersonantunes.ave.helper.Preferencias;
import com.jeffersonantunes.ave.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText txtCadNome;
    private EditText txtCadEmail;
    private EditText txtCadSenha;

    private Button btnCadastrar;

    private Usuario usuario;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_cadastro_usuario);

        txtCadNome  = (EditText) findViewById(R.id.txtCadNome);
        txtCadEmail  = (EditText) findViewById(R.id.txtCadEmail);
        txtCadSenha  = (EditText) findViewById(R.id.txtCadSenha);

        btnCadastrar  = (Button) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Valida se todos campos estão preenchidos
                if (txtCadNome.getText().length() < 1 || txtCadEmail.getText().length() < 1 ||txtCadSenha.getText().length() < 1 ){

                    if (txtCadNome.getText().length() < 1) txtCadNome.setError("Por favor digite um Nome");
                    if (txtCadEmail.getText().length() < 1)txtCadEmail.setError("Por favor digite um Email");
                    if (txtCadSenha.getText().length() < 1)txtCadSenha.setError("Por favor digite uma Senha");

                }else {
                    usuario = new Usuario();

                    usuario.setNome(txtCadNome.getText().toString());
                    usuario.setEmail(txtCadEmail.getText().toString());
                    usuario.setSenha(txtCadSenha.getText().toString());
                    usuario.setAcesso("usuario");

                    cadastrarUsuario();
                }

            }
        });

    }

    private void cadastrarUsuario(){

        auth = ConfigFirebase.getAuth();
        auth.createUserWithEmailAndPassword(
                usuario.getEmail()
                ,usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroUsuarioActivity.this,"Sucesso ao cadastrar usuario", Toast.LENGTH_SHORT).show();

                    String idUsuarioCodificado = Base64Custom.codificaBase64(usuario.getEmail());
                    usuario.setId(idUsuarioCodificado);

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    usuario.setUid(firebaseUser.getUid());

                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados(idUsuarioCodificado, usuario.getNome(), usuario.getAcesso());

                    abrirLoginUsuario();

                    //firebaseUser.sendEmailVerification();
                    //auth.signOut();
                    //finish();
                }else{

                    String error = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = "Digite uma senha mais forte, contendo mais caracteres e com letras e numeros";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "O Email digitado é inválido, digite um novo e-mail";
                    }catch (FirebaseAuthUserCollisionException e) {
                        error = "Esse e-mail ja está em uso no App";
                    } catch (Exception e) {
                        error = "ao cadastrar usuário";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this,"Erro: " + error, Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
