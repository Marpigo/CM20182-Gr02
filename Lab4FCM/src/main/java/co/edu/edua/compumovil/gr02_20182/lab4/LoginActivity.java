package co.edu.edua.compumovil.gr02_20182.lab4;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import co.edu.edua.compumovil.gr02_20182.lab4.Models.Usuario;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    EditText campoName;/*Usaurio a buscar, perfil*/
    EditText campoPassword;
    Button mbutLoguin;
    ImageView campoPhoto;

    public static byte[] photo;

    ArrayList<Usuario> userList;
    ProgressDialog progreso;


    private FirebaseAuth mAuth; //Para conectarnos con los datos de la base de datos
    private FirebaseAuth.AuthStateListener mAuthListener; //Escucha si los datos ingresados, usuario y contraseña son correctos
    private static final String TAG = "mobil";

    private SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    public static final int SIGN_IN_CODE = 777; //codigo unico
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent que abre el selector o el inicio de sesion para una cuenta google
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });


        //Validar si hay conexion de internet
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
        }

    }


    //Cada vez que cargue la aplicacion
    // Verifica si el usuario esta logueado, e ingresa a la aplicacion
   // @Override
   // protected void onStart() {
    //    super.onStart();
     //   mAuth.addAuthStateListener(mAuthListener);
   // }


    private void init() {
        //segundo parametro de la autenticacion un objeto de opciones que dira como autenticarnos
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //inicializamos el GoogleApiCliente
        //gestionamos el ciclo de vida googlecliente con el activity utilizando enableAutoManage
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.signInButton); //button de google
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        mAuth = FirebaseAuth.getInstance();//referencia con firebase
        mbutLoguin = (Button) findViewById(R.id.butLoguin);
        //campo a buscar
        campoName = (EditText) findViewById(R.id.ediName_loguin);
        campoPassword = (EditText) findViewById(R.id.ediPass_loguin);
        campoPhoto = (ImageView) findViewById(R.id.imageView);
    }

    //este metodo se ejecuta cuando la conexion falla
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Authentication failed.Google service", Toast.LENGTH_SHORT).show();
    }

    //Este metodo llegan los resultado utilizando el requesCode, con el codigo a comprobar
    // y mediante Intent data obtenemos un resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    //aca ponemos lo que queremos hacer con el resultado, validamos que la operacion fue exitosa
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            firebaseAuthWithGoogle(result.getSignInAccount());
        } else {
            Toast.makeText(this, "NO LOGUEADO", Toast.LENGTH_SHORT).show();
        }
    }

    //Abro el navigator
    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {

        //Intent que abre el selector o el inicio de sesion para una cuenta google
        //Intent intent = new Intent(this, ServiciosActivityNavigationDrawer.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intent);
        openNavigationDrawer();

    }


    private void openNavigationDrawer() {
        Intent miIntent = new Intent(LoginActivity.this, ServiciosActivityNavigationDrawer.class);
        startActivity(miIntent);
        finish();
    }


    /*
     * Validar campos: Vacios o nulo
     * */
    String validateCampo (String name, String password){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += password !=null && password.trim().length()>0?"" : campoPassword.getHint() + "\n";
        //campos += userValidate()?"" : "Usuario o contraseña no valido"+ "\n";
        return campos;
    }




}
