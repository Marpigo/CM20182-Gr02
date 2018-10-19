package co.edu.udea.compumovil.gr02_20182.lab3;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.Usuario;
import co.edu.udea.compumovil.gr02_20182.lab3.Pattern.VolleySingleton;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteDrink;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteFood;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteUser;

public class LoginActivity extends AppCompatActivity{


    EditText campoName;/*Usaurio a buscar, perfil*/
    EditText campoPassword;
    Button butenter_services;
    ImageView campoPhoto;

    public static byte[] photo;

    ArrayList<Usuario> userList;
    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services
    JsonObjectRequest jsonobjectrequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        butenter_services = (Button) findViewById(R.id.butLoguin);

        //campo a buscar
        campoName = (EditText) findViewById(R.id.ediName_loguin);
        campoPassword = (EditText) findViewById(R.id.ediPass_loguin);
        campoPhoto = (ImageView) findViewById(R.id.imageView);

        //Validar si hay conexion de internet
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            openWebServiceLocalUser();
            openWebServiceLocaDrink();
            openWebServiceLocaFood();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(getApplicationContext(), "Update bd Local", Toast.LENGTH_SHORT).show();
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

    public  void openWebServiceLocaDrink() {

        String ipserver = getString(R.string.s_ip_000webhost);
        String url = ipserver+"/REST/wsJSONConsultarListaB.php";

        //Enviamos la informacion a volley. Realiza el llamado a la url, e intenta conectarse a nuestro servicio REST
        jsonobjectrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                deleteDatosLocalDrink();
                UpdateDatosLocaDrink(response);//Actualizar base local usuario
                campoPhoto.setImageResource(R.drawable.user);
               // Toast.makeText(getApplicationContext(), "Update bd Local", Toast.LENGTH_SHORT).show();
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

    private void deleteDatosLocalDrink()
    {
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        DatabaseSQLiteDrink databaseSQLitedrink = new DatabaseSQLiteDrink();
        databasesqlit.open();
        databaseSQLitedrink.deleteDrink();
        databasesqlit.close();
    }


    private void UpdateDatosLocaDrink(JSONObject response) {
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        DatabaseSQLiteDrink databaseSQLitedrink = new DatabaseSQLiteDrink();
        databasesqlit.open();

        int id;
        String name;
        int preci;
        String ingredient;
        String photourl;
        //byte[] photo;
        int registro = 0;

        JSONArray json = response.optJSONArray("bebidaArrJson");
        try {

            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                id = jsonObject.optInt("id");
                name = jsonObject.optString("name");
                preci = jsonObject.optInt("preci");
                ingredient = jsonObject.optString("ingredient");
                photourl = jsonObject.optString("photo");

                openPhotoUrl(photourl);
                photo = imageViewToByte(campoPhoto);
                registro = databaseSQLitedrink.insertDrink(id, name, preci, ingredient, photo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i( "Error 2: ", e.toString());
            Toast.makeText(this, "Error insertar bd: " + " "+response, Toast.LENGTH_LONG).show();
            // progreso.hide();
        }
         Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
        databasesqlit.close();
        //progreso.hide();

    }

    public  void openWebServiceLocaFood() {
        String ipserver = getString(R.string.s_ip_000webhost);
        String url = ipserver+"/REST/wsJSONConsultarListaC.php";

        //Enviamos la informacion a volley. Realiza el llamado a la url, e intenta conectarse a nuestro servicio REST
        jsonobjectrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                deleteDatosLocalFood();
                UpdateDatosLocaFood(response);//Actualizar base local usuario
                campoPhoto.setImageResource(R.drawable.user);
                // Toast.makeText(getApplicationContext(), "Update bd Local", Toast.LENGTH_SHORT).show();
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

    private void deleteDatosLocalFood()
    {
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        DatabaseSQLiteFood databaseSQLitedfood = new DatabaseSQLiteFood();
        databasesqlit.open();
        databaseSQLitedfood.deleteFood();
        databasesqlit.close();
    }


    private void UpdateDatosLocaFood(JSONObject response) {
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        DatabaseSQLiteFood databaseSQLitedfood = new DatabaseSQLiteFood();
        databasesqlit.open();

        int id;
        String name;
        String schedule;
        String type;
        String time;
        double preci;
        String ingredient;
        String photourl;
        //byte[] photo;
        int registro = 0;

        JSONArray json = response.optJSONArray("comidaArrJson");
        try {

            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                id = jsonObject.optInt("id");
                name = jsonObject.optString("name");
                schedule = jsonObject.optString("schedule");
                type = jsonObject.optString("type");
                time = jsonObject.optString("time");
                preci = jsonObject.optDouble("preci");
                ingredient = jsonObject.optString("ingredient");
                photourl = jsonObject.optString("photo");

                openPhotoUrl(photourl);
                photo = imageViewToByte(campoPhoto);
                registro = databaseSQLitedfood.insertFood(id, name, schedule, type, time, preci, ingredient, photo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i( "Error 2: ", e.toString());
            Toast.makeText(this, "Error insertar bd: " + " "+response, Toast.LENGTH_LONG).show();
            // progreso.hide();
        }
        Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
        databasesqlit.close();
        //progreso.hide();

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



    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

        public void onClick(View view) {
        switch (view.getId()){
            case R.id.butLoguin:
                String campos="";
                campos = validateCampo(campoName.getText().toString(), campoPassword.getText().toString());
                if(campos.length() == 0)
                {
                    PerfilFragment.user_login= campoName.getText().toString(); //User logueado
                    PerfilFragment.user_pass= campoPassword.getText().toString();
                    openNavigationDrawer();
            }else{
                    Toast.makeText(this, campos, Toast.LENGTH_SHORT).show();
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
    String validateCampo (String name, String password){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += password !=null && password.trim().length()>0?"" : campoPassword.getHint() + "\n";
        campos += userValidate()?"" : "Usuario o contraseña no valido"+ "\n";
        return campos;
    }


    Boolean userValidate()
    {
        Boolean uservalido = false;
        List<Usuario> userList;
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String password;
        int consulta = 0;

        name = campoName.getText().toString();
        password = campoPassword.getText().toString();

        userList = databasesqliteduser.getUser(name, password);

        uservalido = userList.size()>0?true:false;

        databasesqlit.close();
        return  uservalido;
    }



}
