package com.jeffersonantunes.ave.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.helper.Base64Custom;
import com.jeffersonantunes.ave.helper.Preferencias;
import com.jeffersonantunes.ave.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnLogar;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private SignInButton mGoogleBtn;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;

    private Usuario usuario;
    private DatabaseReference dbAve;
    private ValueEventListener valueEventListener;

    private String idUsuarioLogado;
    private String childUsuario;

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        getSupportActionBar().hide(); //<< this
        setContentView(R.layout.activity_login);

        mAuth = ConfigFirebase.getAuth();
        mUser = mAuth.getCurrentUser();

        usuario = new Usuario();

        childUsuario = "usuario";

        mAuth.signOut();
        LoginManager.getInstance().logOut();

        //Obtendo os inputs
        txtEmail                = (EditText) findViewById(R.id.txtEmail);
        txtSenha                = (EditText) findViewById(R.id.txtSenha);
        btnLogar                = (Button) findViewById(R.id.btnLogar);

        //Botão Logar
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().length() < 1 || txtSenha.getText().length() < 1){

                    if (txtEmail.getText().length() < 1) txtEmail.setError("Por favor digite seu Email");
                    if (txtSenha.getText().length() < 1) txtSenha.setError("Por favor digite sua Senha");

                }else {

                    usuario.setEmail(txtEmail.getText().toString());
                    usuario.setSenha(txtSenha.getText().toString());

                    validaLogin();


                }
            }
        });

        //Facebook
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions("email","public_profile");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginActivity.this,"processo cancelado",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginActivity.this,"Error Por favor contate o admistrador",Toast.LENGTH_LONG).show();
            }

        });

        mGoogleBtn = (SignInButton) findViewById(R.id.signInButton);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,"Error ao logar com conta google",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //mGoogleSignInClient = new GoogleSignInClient(getApplicationContext());

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient); //mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void validaNoUsuario(){

        mUser = mAuth.getCurrentUser();
        usuario.setEmail(mUser.getEmail());

        idUsuarioLogado = Base64Custom.codificaBase64(usuario.getEmail());

        dbAve = ConfigFirebase.getDbAveReference().child(childUsuario).child(idUsuarioLogado);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //valida se usuario existe no nosso db
                if (dataSnapshot.exists()) {
                    Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    preferencias.salvarDados(idUsuarioLogado, usuarioRecuperado.getNome(), usuarioRecuperado.getAcesso());
                }else{
                    usuario.setUid(mUser.getUid());
                    usuario.setNome(mUser.getDisplayName());
                    usuario.setId(idUsuarioLogado);
                    usuario.setAcesso("usuario");
                    usuario.salvar();
                    Preferencias preferencias = new Preferencias(LoginActivity.this);
                    preferencias.salvarDados(usuario.getId(), usuario.getNome(), usuario.getAcesso());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        dbAve.addListenerForSingleValueEvent(valueEventListener);

        Toast.makeText(LoginActivity.this,"Sucesso ao fazer login", Toast.LENGTH_SHORT).show();

        abrirTelaPrincipal();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            validaNoUsuario();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    //login com Email e Senha
    private void validaLogin(){

        mAuth.signInWithEmailAndPassword(
                usuario.getEmail()
                ,usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    idUsuarioLogado = Base64Custom.codificaBase64(usuario.getEmail());

                    dbAve = ConfigFirebase.getDbAveReference().child(childUsuario).child(idUsuarioLogado);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);
                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(idUsuarioLogado,usuarioRecuperado.getNome(),usuarioRecuperado.getAcesso());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    dbAve.addListenerForSingleValueEvent(valueEventListener);


                    Toast.makeText(LoginActivity.this,"Sucesso ao fazer login", Toast.LENGTH_SHORT).show();
                    abrirTelaPrincipal();

                }else {
                    String error = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        error = "Email não cadastrado, por favor cadastre-se antes.";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        error = "Senha incorreta, verifique sua senha.";
                    } catch (Exception e) {
                        error = "ao logar usuário";
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,"Erro: " + error, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    //Cadastrar no firebase face login
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("handle", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("handle", "signInWithCredential:success");
                            validaNoUsuario();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("handle", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Falha ao logar com Facebook",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void abrirCadastroUsuario(View v){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        verificaUsuariologado();
    }

    private void verificaUsuariologado(){
        if (mUser != null ){
            //mAuth.signOut();
            //LoginManager.getInstance().logOut();
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
