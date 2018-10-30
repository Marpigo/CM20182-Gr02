package co.edu.edua.compumovil.gr02_20182.lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
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
import java.util.Map;

import co.edu.udea.compumovil.gr02_20182.lab4.Pattern.VolleySingleton;

public class DrinkActivity extends AppCompatActivity{

    EditText campoName;
    EditText campoPrice;
    EditText campoIngredients;
    ImageView campoPhoto;
    Button butregister;

    TextView campoNameInfo, campoPriceInfo, campoIngredientsInfo;

    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services

    StringRequest stringrequest;
    Bitmap bitmaphoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        init();

        butregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Validar si hay conexion de internet
                ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = con.getActiveNetworkInfo();

                if(networkinfo != null && networkinfo.isConnected())
                {
                    String campos="";
                    campos = validateCampo(campoName.getText().toString(), campoPrice.getText().toString(), campoIngredients.getText().toString());

                    if(campos.length()>0){
                        Toast.makeText(getApplicationContext(), "Verificar Campos: " + campos, Toast.LENGTH_SHORT).show();
                    }else
                    {
                        openWebServices();
                        informationFood();
                        //limpiar(); limpio los campos si todo sale bien en, webservice en onResponse()
                    }
                }else{
                    Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
                }
         }
        });
        campoPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                imagenGallery();
            }
        });
        setupActionBar();
    }


    public  void openWebServices() {
        progreso = new ProgressDialog(this);
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();

        String ipserver = getString(R.string.s_ip_000webhost);
        //String server ="192.168.1.6";
        String url = ipserver+"/REST/wsJSONRegistroB.php?";
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
                String price;
                String ingredients;
                byte[] photo;
                String imagen;

                name = campoName.getText().toString().toUpperCase();
                price = campoPrice.getText().toString();
                ingredients = campoIngredients.getText().toString().toUpperCase();
                //photo = imageViewToByte(campoPhoto);

                imagen = convertirImgString(bitmaphoto);

                //Llenamos la structura de datos getParams, para enviar webservices
                Map<String,String> parametros=new HashMap<>();
                parametros.put("id","");
                parametros.put("name",name);
                parametros.put("preci",price);
                parametros.put("ingredient",ingredients);

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


    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.s_register) +" " + getString(R.string.s_drink));
        }
    }

    public  void init()
    {
        campoName = (EditText) findViewById(R.id.ediName_Drink);
        campoPrice= (EditText) findViewById(R.id.ediPreci_Drink);
        campoIngredients = (EditText) findViewById(R.id.ediIngredents_Drink);
        campoPhoto= (ImageView) findViewById(R.id.imgDrik_register);

        butregister = (Button) findViewById(R.id.butRegister_Drink);

        campoNameInfo = (TextView) findViewById(R.id.txtName_drink_information);
        campoPriceInfo = (TextView) findViewById(R.id.txtPrice_drink_information);
        campoIngredientsInfo = (TextView) findViewById(R.id.txtIngredents_drink_information);

    }

    /*
     * Validar campos: Vacios o nulo
     * */
    String validateCampo (String name, String price, String ingredients){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += price !=null && price.trim().length()>0? "" :campoPrice.getHint() + "\n";
        campos += ingredients !=null && ingredients.trim().length()>0?"" : campoIngredients.getHint() + "\n";
        campos += bitmaphoto !=null?"":"Imagen";
        return campos;
    }


    private void informationFood() {
        /*Informacion de la captura*/
        campoNameInfo.setText(campoName.getText().toString());
        campoPriceInfo.setText(campoPrice.getText().toString());
        campoIngredientsInfo.setText(campoIngredients.getText().toString());
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void imagenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccionar la aplicación"),10);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        campoPrice.setText("0");
        campoIngredients.setText("");
        campoPhoto.setImageResource(R.drawable.drink2);
        bitmaphoto = null;
    }


}
