package co.edu.edua.compumovil.gr02_20182.lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.edua.compumovil.gr02_20182.lab4.Constants.Constantes;
import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.UserFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Usuario;


/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */
public class UsuarioAtivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    private final List<Usuario> usuarioList = new ArrayList<Usuario>();
    ArrayAdapter<Usuario> usuarioArrayAdapter;
    Usuario usuarioSelected;

    public static DatabaseReference mDatabase; //Referencia a la base de datos
    StorageReference mStorageRef; // para referenciar la foto a guardar Storage
    private Uri filePath;


    ImageView campoPhoto;
    EditText campoName, campoEmail, campoPassword;

    Button butregistrarFirebase;
    public static int modo = 0; /*0.Nuevo, 1.Modificar*/

    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services

    StringRequest stringrequest;
    Bitmap bitmaphoto;

    public static byte[] photo;

    //Van a permitir establecer la conexion con nuestro servicio web services
    JsonObjectRequest jsonobjectrequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ativity);


        init();
        inicilizarFirebase();
        setupActionBar();

        //cargarFirebase();

        if (modo == 1)
        {
           // userQuery();
        }

    }



    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    void inicilizarFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
       // mDatabase.setPersistenceEnable(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public  void init()
    {
        campoName = (EditText) findViewById(R.id.ediNameUserR);
        campoEmail = (EditText) findViewById(R.id.ediEmailUserR);
        campoPassword = (EditText) findViewById(R.id.ediPasswordUserR);
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUserR);
    }


    public void onClick(View view) {
        final String name = campoName.getText().toString();
        final String email = campoEmail.getText().toString();
        final String password = campoPassword.getText().toString();
        boolean requerimientos = false;

        UserFirebase userFirebase = new  UserFirebase();


        switch (view.getId()) {
            case R.id.imaSave:
                requerimientos = validateCampo(campoName.getText().toString(), campoEmail.getText().toString(), campoPassword.getText().toString());
                if (requerimientos) {
                    if (modo == 0 ){ //Nuevo
                        userFirebase.insertUser(name, email, password, filePath);
                        limpiar();
                    }else if(modo == 1){ //Modificar
                        userFirebase.insertUser(name, email, password, filePath);
                        limpiar();
                        modo =0;
                    }
                }
                break;
            case R.id.imaNew:
                limpiar();
                break;

            case R.id.imaDelete:

                break;
            case R.id.imgPhotoUserR:
                imagenGallery();
                break;
    }

    }


    /*
      Validar campos: Vacios o nulo
     */
    boolean validateCampo (String name, String email, String password){
        int vericar = 0;

        campoName.setError(null);
        campoEmail.setError(null);
        campoPassword.setError(null);
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();

        if (TextUtils.isEmpty(name))
        {campoName.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(email))
        {campoEmail.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(password))
        {campoPassword.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if(filePath ==null)
            { Toast.makeText(this, "Imagen", Toast.LENGTH_SHORT).show();vericar += 1;}

        if(networkinfo == null && !networkinfo.isConnected()){
            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar>0?false:true;
    }


    private void imagenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccionar la aplicación"),10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            filePath = data.getData();

            try {
                bitmaphoto=MediaStore.Images.Media.getBitmap(this.getContentResolver(),filePath);
                campoPhoto.setImageBitmap(bitmaphoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void limpiar() {
        campoName.setText("");
        campoEmail.setText("");
        campoPassword.setText("");
        campoPhoto.setImageResource(R.drawable.ic_person_red_24dp);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public List<Usuario> getListUsuario()
    {
        return usuarioList;
    }



}
