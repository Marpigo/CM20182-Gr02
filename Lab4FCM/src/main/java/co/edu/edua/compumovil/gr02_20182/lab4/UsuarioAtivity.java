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
public class UsuarioAtivity extends AppCompatActivity {


    DatabaseReference mDatabase; //Referencia a la base de datos
    StorageReference mStorageRef; // para referenciar la foto a guardar Storage
    FirebaseAuth mAuth;
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
            userQuery();
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
        mAuth = FirebaseAuth.getInstance();

        campoName = (EditText) findViewById(R.id.ediNameUser);
        campoEmail = (EditText) findViewById(R.id.ediEmailUser);
        campoPassword = (EditText) findViewById(R.id.ediPasswordUser);
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUser);
        butregistrarFirebase = (Button) findViewById(R.id.butRegistrarUser);
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUser);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.butRegistrarUser:
                String campos = "";
                //Validar si hay conexion de internet
                ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = con.getActiveNetworkInfo();

                campos = validateCampo(campoName.getText().toString(), campoEmail.getText().toString(), campoPassword.getText().toString());

                if (campos.length() == 0) {

                    if (modo == 0 ){ //Nuevo
                        if(networkinfo != null && networkinfo.isConnected())
                        {
                            cargarUserFirebase();


                        }else{
                            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
                        }
                    }else if(modo == 1){ //Modificar
                      //  updateWebServices();
                        openWebServiceLocalUser();//Actualizar user locales desde la nube
                        //PerfilFragment.user_login = campoName.getText().toString();
                        //PerfilFragment.user_pass = campoPassword.getText().toString();
                        modo =0;
                        limpiar();
                    }

                    }else{
                        Toast.makeText(getApplicationContext(), "Verificar Campos: " + campos, Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case R.id.imgPhotoUser:
                        imagenGallery();
                        break;
        }
    }

    public  void cargarUserFirebase() {
        progreso = new ProgressDialog(this);
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();


        // imagen = convertirImgString(bitmaphoto);


        if(filePath!=null){
            //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
            final StorageReference fotoRef = mStorageRef.child("Fotos").child(mAuth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
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

                        String name;
                        String email;
                        String password;
                        String imagen;
                        name = campoName.getText().toString().toUpperCase();
                        email = campoEmail.getText().toString().toLowerCase();
                        password = campoPassword.getText().toString().toUpperCase();


                        //utilizamos un Map, y por medio de un HasMap mandamos todos los datos
                        Map<String, Object> datosUsers = new HashMap<>();
                        datosUsers.put("name", name);
                        datosUsers.put("email", email);
                        datosUsers.put("password", password);
                        datosUsers.put("imagen", downloadLink.toString());

                        //.push es como un identificador para cada nod
                        mDatabase.child(Constantes.TABLA_USUARIO).child(mAuth.getCurrentUser().getUid()).child("productos").push().updateChildren(datosUsers).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                limpiar();
                                progreso.hide();
                                Toast.makeText(getApplicationContext(), "Se cargo el producto correctamente.", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progreso.hide();
                                Toast.makeText(getApplicationContext(), "Error al cargar el producto" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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



    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }

    public static byte[] imageViewToByte (ImageView image){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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

    public void  userQuery() {
        Boolean uservalido = false;
        List<Usuario> userList;
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String password;

      //  userList = databasesqliteduser.getUser(PerfilFragment.user_login, PerfilFragment.user_pass);
        //campoName.setText(userList.get(0).getName());
        //campoEmail.setText(userList.get(0).getEmail());
        //campoPassword.setText(userList.get(0).getPassword());
        //byte[] data = userList.get(0).getPhoto();
        //Bitmap image = toBitmap(data);
        //campoPhoto.setImageBitmap(image);
        databasesqlit.close();
    }

    public static Bitmap toBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    private void updateUser() {
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String email;
        String password;
        byte[] photo;
        int registro = 0;

        name = campoName.getText().toString();
        email = campoEmail.getText().toString();
        password = campoPassword.getText().toString();
        photo = imageViewToByte(campoPhoto);
        //registro = databasesqliteduser.updateUser(PerfilFragment.user_login, name, email, password, photo);
        Toast.makeText(getApplicationContext(), getString(R.string.s_user_update) + " " + registro, Toast.LENGTH_SHORT).show();
        databasesqlit.close();
    }


    public  void openWebServiceLocalUser() {

        String ipserver = getString(R.string.s_ip_000webhost);
        String url = ipserver+"/REST/wsJSONConsultarListaU.php";

        //Enviamos la informacion a volley. Realiza el llamado a la url, e intenta conectarse a nuestro servicio REST
        jsonobjectrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                deleteDatosLocalUser();
                UpdateDatosLocalUser(response);//Actualizar base local usuario
                campoPhoto.setImageResource(R.drawable.user);
                //   Toast.makeText(getApplicationContext(), "Update bd Local", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_query) + " " + error.toString(), Toast.LENGTH_SHORT).show();
                Log.i( getString(R.string.s_web_not_query), error.toString());
            }
        });
        //Instanciamos el patron singleton  - volleySingleton
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonobjectrequest);
    }


    private void deleteDatosLocalUser()
    {
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        databasesqlit.open();
        databasesqliteduser.deleteUsers();
        databasesqlit.close();
    }
    private void UpdateDatosLocalUser(JSONObject response) {
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        databasesqlit.open();

        int id;
        String name;
        String email;
        String password;
        String photourl;
        //byte[] photo;
        int registro = 0;

        JSONArray json = response.optJSONArray("usuarioArrJson");
        try {

            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                id = jsonObject.optInt("id");
                name = jsonObject.optString("name");
                email = jsonObject.optString("email");
                password = jsonObject.optString("password");
                photourl = jsonObject.optString("photo");

                openPhotoUrl(photourl);
                photo = imageViewToByte(campoPhoto);
                registro = databasesqliteduser.insertUser(id, name, email, password, photo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i( "Error 2: ", e.toString());
            Toast.makeText(this, "Error insertar bd: " + " "+response, Toast.LENGTH_LONG).show();
            // progreso.hide();
        }
        // Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
        databasesqlit.close();
    }


    private void openPhotoUrl(String url) {
        url=url.replace(" ","%20");
        //  url = url.replace("","%20");
        ImageRequest imagerequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                campoPhoto.setImageBitmap(response);
                // photo = imageViewToByte(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(imagerequest);
    }



}
