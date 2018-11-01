package co.edu.edua.compumovil.gr02_20182.lab4;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import co.edu.edua.compumovil.gr02_20182.lab4.Pattern.VolleySingleton;


public class FoodActivity extends AppCompatActivity {

    EditText campoName;
    CheckBox campoMorning, campoAfternoon, campoEvening;
    RadioButton campoMain, campoEntry;
    TextView campoTime;
    TextView campoTimep;
    EditText campoPrice;
    EditText campoIngredients;
    ImageView campoPhoto;
    Button butregister;
    Button butTime;
    Bitmap bitmaphoto;



    TextView campoNameInfo, campoHourInfo, campoTypeInfo, getCampoTimeInfo, getCampoPriceInfo, getCampoIngredientsInfo;
    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services

    StringRequest stringrequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

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
                    campos = validateCampo(campoName.getText().toString(), campoTime.getText().toString(), campoPrice.getText().toString(), campoIngredients.getText().toString());

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
        butTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                abrirReloj();
            }
        });

        setupActionBar();

    }

    /*
     * Validar campos: Vacios o nulo
     * */
    String validateCampo (String name, String time, String price, String ingredients){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += time !=null && time.trim().length()>0? "" :campoTimep.getText().toString() + "\n";
        campos += price !=null && price.trim().length()>0? "" :campoPrice.getHint() + "\n";
        campos += ingredients !=null && ingredients.trim().length()>0?"" : campoIngredients.getHint() + "\n";
        campos += bitmaphoto !=null?"":"Imagen";
        return campos;
    }



    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
           actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.s_register_food_frame) +" " + getString(R.string.s_food));
         }
    }

    public  void init()
    {
        campoName = (EditText) findViewById(R.id.ediName_food);
        campoMorning = (CheckBox) findViewById(R.id.cheMorning_food);
        campoAfternoon = (CheckBox) findViewById(R.id.cheAfternoon_food);
        campoEvening = (CheckBox) findViewById(R.id.cheEvening_food);
        campoMain = (RadioButton) findViewById(R.id.radMain_food);
        campoEntry = (RadioButton) findViewById(R.id.radEntry_food);
        campoTimep = (TextView) findViewById(R.id.txtTime_preparation_food);
        campoTime = (TextView) findViewById(R.id.txttime_look_food);
        campoPrice= (EditText) findViewById(R.id.ediPreci_food);
        campoIngredients = (EditText) findViewById(R.id.ediIngredents_food);
        campoPhoto= (ImageView) findViewById(R.id.imgFood);

        butregister = (Button) findViewById(R.id.butRegister_food);
        butTime = (Button) findViewById(R.id.butTime_food);

        /*Informacion de la captura*/
        campoNameInfo = (TextView) findViewById(R.id.txtName_food_information);
        campoHourInfo = (TextView) findViewById(R.id.txtSchule_food_information);
        campoTypeInfo = (TextView) findViewById(R.id.txtType_food_information);
        getCampoTimeInfo = (TextView) findViewById(R.id.txtTime_food_information);
        getCampoPriceInfo = (TextView) findViewById(R.id.txtPrice_food_information);
        getCampoIngredientsInfo = (TextView) findViewById(R.id.txtIngredents_food_information);

      }

    public  void openWebServices() {

        progreso = new ProgressDialog(this);
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();


        String ipserver = getString(R.string.s_ip_000webhost);
        //String server ="192.168.1.6";
        String url = ipserver+"/REST/wsJSONRegistroC.php?";

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
               // Log.i( getString(R.string.s_web_not_register),"No conexion");
                progreso.hide();
            }
        })

        {//Implementar GETpara para enviar los datos
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String name;
                String schedule;
                String type;
                String time;
                String preci;
                String ingredients;
                byte[] photo;
                String imagen;
                int registro =0;

                name = campoName.getText().toString().toUpperCase();
                schedule = horariosPlato(campoMorning, campoAfternoon, campoEvening);
                type = campoMain.isChecked() ? "TRUE":"FALSE";
                time = campoTime.getText().toString();
                preci = campoPrice.getText().toString();
                ingredients = campoIngredients.getText().toString().toUpperCase();
                imagen = convertirImgString(bitmaphoto);
                //name=name.replace(" ","%20");

                //Llenamos la structura de datos getParams, para enviar webservices
                Map<String,String> parametros=new HashMap<>();
                parametros.put("id","");
                parametros.put("name",name);
                parametros.put("schedule",schedule);
                parametros.put("type",type);
                parametros.put("time",time);
                parametros.put("preci",preci);
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



    private void informationFood() {
        /*Informacion de la captura*/
        campoNameInfo.setText(campoName.getText().toString());
        campoHourInfo.setText(horariosPlato(campoMorning, campoAfternoon, campoEvening));
        campoTypeInfo.setText( campoMain.isChecked()?"Main food":"Entry");
        getCampoTimeInfo.setText(campoTime.getText().toString());
        getCampoPriceInfo.setText(campoPrice.getText().toString());
        getCampoIngredientsInfo.setText(campoIngredients.getText().toString());
    }


    private String horariosPlato(CheckBox morning, CheckBox afternoon, CheckBox evening)
    {
        String schedule="";

        schedule = morning.isChecked()?"TRUE,":"FALSE,";
        schedule += afternoon.isChecked()?"TRUE,":"FALSE,";
        schedule += evening.isChecked()?"TRUE":"FALSE";

        return  schedule;
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
        campoMorning.setChecked(false);
        campoAfternoon.setChecked(false);
        campoEvening.setChecked(false);
        campoTime.setText("");
        campoPrice.setText("0");
        campoIngredients.setText("");
        campoPhoto.setImageResource(R.drawable.food);
        bitmaphoto = null;

    }
    public void abrirReloj() {
        final Calendar calendario = Calendar.getInstance();
        int  hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                campoTime.setText(hourOfDay+":"+minute);
            }
        }, minuto, hora,false);
        timePickerDialog.show();
    }
}
