package co.edu.edua.compumovil.gr02_20182.lab4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

public class PerfilActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static String user_login, user_pass;

    TextView name_profil, email_profil;
    ImageView photo_profil;
    private Context contexto;

    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        init();
        setupActionBar();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loguinData();
    }

    public  void init()
    {
        name_profil = (TextView)findViewById(R.id.txtNameProfil);
        email_profil = (TextView)findViewById(R.id.txtEmailProfil);
        photo_profil = (ImageView)findViewById(R.id.imgPhotoProfile);
    }

    void loguinData()
    {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            userIn(result);
        }else{
            //Toast.makeText(getContext(), "NO SE HA INICIADO SESION", Toast.LENGTH_SHORT).show();
        }
    }

    //aca ponemos lo que queremos hacer con el resultado, validamos que la operacion fue exitosa
    private void userIn(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount user = result.getSignInAccount();
            name_profil.setText(user.getDisplayName());
            email_profil.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(photo_profil);

        } else {
            //Toast.makeText(this, "NO LOGUEADO", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.s_profile));
        }
    }
}
