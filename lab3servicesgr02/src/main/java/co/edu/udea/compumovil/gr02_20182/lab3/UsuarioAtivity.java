package co.edu.udea.compumovil.gr02_20182.lab3;

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
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.Usuario;
import co.edu.udea.compumovil.gr02_20182.lab3.Pattern.VolleySingleton;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteUser;

/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */
public class UsuarioAtivity extends AppCompatActivity {


    ImageView campoPhoto;
    EditText campoName, campoEmail, campoPassword;

    Button butregistrar;
    public static int modo = 0; /*0.Nuevo, 1.Modificar*/

    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services

    StringRequest stringrequest;
    Bitmap bitmaphoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ativity);

        init();
        setupActionBar();

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
        campoName = (EditText) findViewById(R.id.ediNameUser);
        campoEmail = (EditText) findViewById(R.id.ediEmailUser);
        campoPassword = (EditText) findViewById(R.id.ediPasswordUser);
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUser);
        butregistrar = (Button) findViewById(R.id.butRegistrarUser);
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
                            campos = validateCampo(campoName.getText().toString(), campoEmail.getText().toString(), campoPassword.getText().toString());

                            if(campos.length()>0){
                                Toast.makeText(getApplicationContext(), "Verificar Campos: " + campos, Toast.LENGTH_SHORT).show();
                            }else
                            {
                                openWebServices();
                                //limpiar(); limpio los campos si todo sale bien en, webservice en onResponse()
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
                        }



                    }else if(modo == 1){ //Modificar
                        updateWebServices();
                        PerfilFragment.user_login = campoName.getText().toString();
                        PerfilFragment.user_pass = campoPassword.getText().toString();
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

    public  void openWebServices() {
        progreso = new ProgressDialog(this);
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();
        String ipserver = getString(R.string.s_ip_000webhost);
        //String server ="192.168.1.6";
        String url = ipserver+"/REST/wsJSONRegistroU.php?";
        //Conexion mediante el metodo POST
        stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("registraJson")){
                    limpiar();
                    progreso.hide();
                    Toast.makeText(getApplicationContext(), getString(R.string.s_web_insert_full), Toast.LENGTH_SHORT).show();
                }else{
                    progreso.hide();
                    Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_register) + " " + response +"", Toast.LENGTH_SHORT).show();
                    Log.i( getString(R.string.s_web_not_register), response +"");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_register), Toast.LENGTH_SHORT).show();
                Log.i( getString(R.string.s_web_not_register),"No conexion");
                progreso.hide();
            }
        })

        {//Implementar GETpara para enviar los datos
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String name;
                String email;
                String password;
                String imagen;

                name = campoName.getText().toString().toUpperCase();
                email = campoEmail.getText().toString().toLowerCase();
                password = campoPassword.getText().toString().toUpperCase();

                imagen = convertirImgString(bitmaphoto);

                //Llenamos la structura de datos getParams, para enviar webservices
                Map<String,String> parametros=new HashMap<>();
                parametros.put("id","");
                parametros.put("name",name);
                parametros.put("email",email);
                parametros.put("password",password);

                parametros.put("imagen",imagen);
                return parametros;
            }
        };
        //Instanciamos el patron singleton - VolleySingleton
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringrequest);
    }


    public  void updateWebServices() {
        progreso = new ProgressDialog(this);
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();
        String ipserver = getString(R.string.s_ip_000webhost);
        //String server ="192.168.1.6";
        String url = ipserver+"/REST/wsJSONUpdateU.php?";
        //Conexion mediante el metodo POST
        stringrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if (response.trim().equalsIgnoreCase("updateJson")){
                    limpiar();
                    progreso.hide();
                    Toast.makeText(getApplicationContext(), getString(R.string.s_web_insert_full), Toast.LENGTH_SHORT).show();
                }else{
                    progreso.hide();
                    Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_register) + " " + response +"", Toast.LENGTH_SHORT).show();
                    Log.i( getString(R.string.s_web_not_register), response +"");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_register), Toast.LENGTH_SHORT).show();
                Log.i( getString(R.string.s_web_not_register),"No conexion");
                progreso.hide();
            }
        })

        {//Implementar GETpara para enviar los datos
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String name;
                String email;
                String password;
                String imagen;

                name = campoName.getText().toString().toUpperCase();
                email = campoEmail.getText().toString().toLowerCase();
                password = campoPassword.getText().toString().toUpperCase();

                imagen = convertirImgString(bitmaphoto);

                //Llenamos la structura de datos getParams, para enviar webservices
                Map<String,String> parametros=new HashMap<>();
                parametros.put("id","");
                parametros.put("name",name);
                parametros.put("email",email);
                parametros.put("password",password);

                parametros.put("imagen",imagen);
                return parametros;
            }
        };
        //Instanciamos el patron singleton - VolleySingleton
        stringrequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getIntanciaVolley(this).addToRequestQueue(stringrequest);
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
        campos += bitmaphoto !=null?"":"Imagen";
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
            Uri path = data.getData();
            //campoPhoto.setImageURI(path);

            try {
                bitmaphoto=MediaStore.Images.Media.getBitmap(this.getContentResolver(),path);
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

        userList = databasesqliteduser.getUser(PerfilFragment.user_login, PerfilFragment.user_pass);
        campoName.setText(userList.get(0).getName());
        campoEmail.setText(userList.get(0).getEmail());
        campoPassword.setText(userList.get(0).getPassword());
        byte[] data = userList.get(0).getPhoto();
        Bitmap image = toBitmap(data);
        campoPhoto.setImageBitmap(image);
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
        registro = databasesqliteduser.updateUser(PerfilFragment.user_login, name, email, password, photo);
        Toast.makeText(getApplicationContext(), getString(R.string.s_user_update) + " " + registro, Toast.LENGTH_SHORT).show();
        databasesqlit.close();
    }



}
