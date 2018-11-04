package co.edu.edua.compumovil.gr02_20182.lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.edua.compumovil.gr02_20182.lab4.Constants.Constantes;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Usuario;
import co.edu.edua.compumovil.gr02_20182.lab4.Pattern.VolleySingleton;
import co.edu.edua.compumovil.gr02_20182.lab4.SQLiteconexion.DatabaseSQLite;
import co.edu.edua.compumovil.gr02_20182.lab4.SQLiteconexion.DatabaseSQLiteUser;


/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */
public class UsuarioAtivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;

    DatabaseReference mDatabase; //Referencia a la base de datos
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


    public  void init()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();


        campoName = (EditText) findViewById(R.id.ediNameUserR);
        campoEmail = (EditText) findViewById(R.id.ediEmailUserR);
        campoPassword = (EditText) findViewById(R.id.ediPasswordUserR);
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUserR);
        butregistrarFirebase = (Button) findViewById(R.id.butRegistrarUser);
    }


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.butRegistrarUser:

                final String name = campoName.getText().toString();
                final String email = campoEmail.getText().toString();
                final String password = campoPassword.getText().toString();

                String campos = "";
                //Validar si hay conexion de internet
                ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = con.getActiveNetworkInfo();

                campos = validateCampo(campoName.getText().toString(), campoEmail.getText().toString(), campoPassword.getText().toString());

                if (campos.length() == 0) {

                    if (modo == 0 ){ //Nuevo
                        if(networkinfo != null && networkinfo.isConnected())
                        {
                            cargarUserFirebase(name, email, password);
                        }else{
                            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
                        }
                    }else if(modo == 1){ //Modificar
                        cargarUserFirebase(name, email, password);

                        limpiar();

                        modo =0;
                    }
                    }else{
                        Toast.makeText(getApplicationContext(), "Verificar Campos: " + campos, Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case R.id.imgPhotoUserR:
                        imagenGallery();
                        break;
        }
    }



    public  void cargarUserFirebase(final String name, final String email, final String password) {

        if(filePath!=null){
            //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
            final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_USUARIO).child(filePath.getLastPathSegment());
            //final StorageReference fotoRef = mStorageRef.child("Fotos").child(firebaseAuth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
             fotoRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception { //subimos la foto al Storage con fotoRef.putFile
                    if(!task.isSuccessful()){
                        throw new Exception();
                    }

                    return fotoRef.getDownloadUrl(); //una vez que suba todo, devuelve el link de descarga
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){ //si se pudo devolver el link, gurdo el resultado en dowloadLink
                        Uri downloadLink = task.getResult();

                        //utilizamos un Map, y por medio de un HasMap mandamos todos los datos
                        Map<String, Object> datosUsers = new HashMap<>();
                        datosUsers.put("name", name);
                        datosUsers.put("email", email);
                        datosUsers.put("password", password);
                        datosUsers.put("imagen", downloadLink.toString());
                        mDatabase.child(Constantes.TABLA_USUARIO).push().setValue(datosUsers);

                    }
                }
            });
        }

    }


    public  void recibirFirebase() {
        progreso = new ProgressDialog(this);
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();

        //estamos dentro del nodo usuario
        mDatabase.child(Constantes.TABLA_USUARIO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()) //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                {
                    //Itero dentro de cada uno de los push o key subido de usuarios
                    mDatabase.child(Constantes.TABLA_USUARIO).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Usuario user = snapshot.getValue(Usuario.class); //Obtenemos los valores que solo estan declarado en Usuario models
                            Log.e("Datos ", "" + snapshot.getValue());
                            //String nombre = user.getName();

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        progreso.hide();

    }




    /*
         * Validar campos: Vacios o nulo
         * */
        String validateCampo (String name, String email, String password){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += email !=null && email.trim().length()>0? "" :campoEmail.getHint() + "\n";
        campos += password !=null && password.trim().length()>0?"" : campoPassword.getHint() + "\n";
       //campos += bitmaphoto !=null?"":"Imagen";
        return campos;
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
        bitmaphoto=redimensionarImagen(bitmaphoto,256,123);

    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();
        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }
    }

    private void limpiar() {
        campoName.setText("");
        campoEmail.setText("");
        campoPassword.setText("");
        campoPhoto.setImageResource(R.drawable.ic_person_red_24dp);
        bitmaphoto = null;
    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
