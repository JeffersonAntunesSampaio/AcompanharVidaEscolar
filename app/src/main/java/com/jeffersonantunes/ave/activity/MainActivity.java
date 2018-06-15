package com.jeffersonantunes.ave.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.jeffersonantunes.ave.R;
import com.jeffersonantunes.ave.config.ConfigFirebase;
import com.jeffersonantunes.ave.fragment.FilhoFragment;
import com.jeffersonantunes.ave.fragment.ProfessorFragment;
import com.jeffersonantunes.ave.fragment.TurmaFragment;
import com.jeffersonantunes.ave.helper.Preferencias;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private MenuItem menuConfiguracoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = ConfigFirebase.getAuth();

        //toolbar = (Toolbar) findViewById(R.id.toolbar_principal);
        //toolbar.setTitle("AvE");
        //setSupportActionBar(toolbar);
        setTitle("AvE - Feed");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle  = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Preferencias preferencias = new Preferencias(MainActivity.this);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nv_menu_itens);

        //Validando permissoes do usuario
        permissoesUsuario( nvDrawer.getMenu(), preferencias.getAcessoUsuario());

        setupDrawerContent(nvDrawer);

    }

    private void permissoesUsuario(Menu menu, String tipoAcesso){

        switch (tipoAcesso){

            case "administrador":
                menu.findItem(R.id.menu_item_filhos).setVisible(true);
                menu.findItem(R.id.menu_item_professor).setVisible(true);
                menu.findItem(R.id.menu_item_turmas).setVisible(true);
                menu.findItem(R.id.menu_item_configuracoes).setVisible(true);
                break;

            case "usuario":
                menu.findItem(R.id.menu_item_filhos).setVisible(true);
                break;

            case "professor":
                //menu.findItem(R.id.menu_item_filhos).setVisible(true);
                break;

            default:
                break;
        }


    }


    public void selectItemDrawer(MenuItem menuItem){
        Fragment myFragment = null;
        Class fragmentClass = null;

        menuItem.setCheckable(true);
        switch (menuItem.getItemId()){
            case R.id.menu_item_filhos:
                fragmentClass = FilhoFragment.class;
                break;
            case R.id.menu_item_professor:
                fragmentClass = ProfessorFragment.class;
                break;
            case R.id.menu_item_turmas:
                fragmentClass = TurmaFragment.class;
                break;

            case R.id.menu_item_sair:
                deslogarUsuario();

            default:
                fragmentClass = FilhoFragment.class;
                break;

        }

        try {
            myFragment = (Fragment) fragmentClass.newInstance();

        }catch (Exception e){
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.myFragment,myFragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawerLayout.closeDrawers();
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //selectItemDrawer(item);
                selectItemDrawer(item);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        /*switch (item.getItemId()){

            case R.id.menu_item_sair:
                deslogarUsuario();
                return true;

            case R.id.menu_item_configuracoes:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
            mAuth.signOut();
            LoginManager.getInstance().logOut();
            abrirTelaLogin();
        }

    private void abrirTelaLogin(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
