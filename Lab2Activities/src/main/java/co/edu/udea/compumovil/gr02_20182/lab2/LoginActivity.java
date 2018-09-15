package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;


public class LoginActivity extends AppCompatActivity {


    EditText campoName_profil;/*Usaurio a buscar, perfil*/
    EditText campoPassword;
    Button butenter_services;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        butenter_services = (Button)findViewById(R.id.butLoguin);

        //campo a buscar
        campoName_profil = (EditText) findViewById(R.id.ediName_loguin);
        campoPassword = (EditText) findViewById(R.id.ediPass_loguin);


    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.butLoguin:
                if(validateString(campoName_profil.getText().toString()) && validateString(campoPassword.getText().toString())  )
            {
                PerfilFragment.user_login= campoName_profil.getText().toString();
                openNavigationDrawer();
            }else{
                    Toast.makeText(getApplicationContext(), "Login ó Password ", Toast.LENGTH_SHORT).show();
            }
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

    public void openCreateUser() {
        Intent miIntent = new Intent(LoginActivity.this, UsuarioAtivity.class);
        startActivity(miIntent);
    }


    /*
     * Validar campos: Vacios o nulo
     * */
    Boolean validateString (String text){
        return text!=null && text.trim().length()>0; //Valido si el texto es diferente null y texto quitado los espacios es > 0 sera valido

    }



}
