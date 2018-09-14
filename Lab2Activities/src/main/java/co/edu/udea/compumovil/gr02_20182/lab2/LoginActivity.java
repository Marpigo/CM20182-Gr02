package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;


public class LoginActivity extends AppCompatActivity {


    EditText campoName_profil;/*Usaurio a buscar, perfil*/
    Button butenter_services;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        butenter_services = (Button)findViewById(R.id.butLoguin);

        //campo a buscar
        campoName_profil = (EditText) findViewById(R.id.ediName_loguin);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.butLoguin:
                PerfilFragment.user_login= campoName_profil.getText().toString();
                openNavigationDrawer();
        }
    }

    public void onCreateClick(View view) {
        openCreateUser();
    }

    private void openNavigationDrawer() {
        Intent miIntent = new Intent(LoginActivity.this, ServiciosActivityNavigationDrawer.class);
        startActivity(miIntent);
        finish();
    }

    private void openCreateUser() {
        Intent miIntent = new Intent(LoginActivity.this, UsuarioAtivity.class);
        startActivity(miIntent);
        finish();
    }



}
