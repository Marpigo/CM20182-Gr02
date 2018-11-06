package co.edu.edua.compumovil.gr02_20182.lab4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.DrinkFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.FoodFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Bebida;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Comida;
import co.edu.edua.compumovil.gr02_20182.lab4.Pattern.VolleySingleton;


public class FoodActivity extends AppCompatActivity {

    static List<Comida> recibirListFood;
    FoodFirebase foodFirebase = new  FoodFirebase();
    ArrayAdapter<Comida> arrayAdapterFood;
    ListView listV_food;
    Comida comidaSelected;

    private Uri filePath;
    public static int modo = 0; /*0.Nuevo, 1.Modificar*/


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

    TextView campoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        init();
        iniciarFirebaseList();
        setupActionBar();

        informationFood();

    }


    public void onClick(View view) {

        String name = campoName.getText().toString();
        String schedule = horariosPlato(campoMorning, campoAfternoon, campoEvening);
        String type = campoMain.isChecked() ? "TRUE":"FALSE";
        String time = campoTime.getText().toString();
        String preci = campoPrice.getText().toString();
        String ingredients = campoIngredients.getText().toString();

        final String idU = campoId.getText().toString();

        boolean requerimientos = false;

        switch (view.getId()) {
            case R.id.imaSaveF:
                requerimientos = validateCampo(name, time, preci, ingredients);
                if (requerimientos) {
                    if (modo == 0 ){ //Nuevo
                        foodFirebase.insertFood(name, schedule, type, time, preci, ingredients, filePath);
                        Toast.makeText(getApplicationContext(), getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                        limpiar();
                    }else if(modo == 1){ //Modificar
                        foodFirebase.updateFood(idU, name, schedule, type, time, preci, ingredients, filePath);
                        Toast.makeText(getApplicationContext(), getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                        limpiar();
                    }
                }
                break;
            case R.id.imaNewF:
                limpiar();
                modo=0;
                break;

            case R.id.imaDeleteF:

                if (TextUtils.isEmpty(name))
                {
                    break;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.s_Firebase_eliminar) + ": " + campoName.getText().toString())
                        .setTitle(getString(R.string.s_Firebase_eliminar_continua));

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliminarFood(idU);
                        limpiar();
                        modo =0;
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }

                });
                builder.show();
                break;

            case R.id.imgFood:
                imagenGallery();
                break;

            case R.id.butTime_food:
                abrirReloj();
                break;
        }
    }

    void eliminarFood(String id)
    {
        foodFirebase.deleteFood(id);
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
        campoId = (TextView)findViewById(R.id.texIdF);

        butTime = (Button) findViewById(R.id.butTime_food);

        listV_food = findViewById(R.id.lv_datosFood);
    }


    void iniciarFirebaseList()
    {
        FoodFirebase foodFirebase = new FoodFirebase();
        foodFirebase.limpiarLista();
        foodFirebase.cargarListFood();
        recibirListFood = FoodFirebase.foodList;
    }

    void informationFood() {
        /*Informacion de la captura*/
        arrayAdapterFood = new ArrayAdapter<Comida>(FoodActivity.this, android.R.layout.simple_list_item_1, recibirListFood);
        listV_food.setAdapter(arrayAdapterFood);

        listV_food.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modo = 1;
                comidaSelected = (Comida) parent.getItemAtPosition(position);
                campoName.setText(comidaSelected.getName());
                campoMorning.setChecked(tokenizer(comidaSelected.getSchedule(), 0));
                campoAfternoon.setChecked(tokenizer(comidaSelected.getSchedule(), 1));
                campoEvening.setChecked(tokenizer(comidaSelected.getSchedule(), 2));
                campoMain.setChecked(comidaSelected.getType().equals("TRUE"));
                campoEntry.setChecked(comidaSelected.getType().equals("TRUE")?false:true);
                 campoTime.setText(comidaSelected.getTime());
                 campoPrice.setText(comidaSelected.getPreci());
                 campoId.setText(comidaSelected.getId());
                campoIngredients.setText(comidaSelected.getIngredient());
                String imag = comidaSelected.getImagen();
                Glide.with(FoodActivity.this).load(imag).into(campoPhoto);
            }
        });
    }


    boolean tokenizer(String str, int posicion){
        String palabra="";
        String auxi ="";
        int i=0;
        StringTokenizer tokenizer = new StringTokenizer(str, ",");

        while (tokenizer.hasMoreElements() && palabra.equals("")) {
            auxi = tokenizer.nextToken();
            if(i == posicion)
            {palabra = auxi;}
            i++;
        }
        return palabra.equals("TRUE")?true:false;
    }

    /*
    Validar campos: Vacios o nulo
   */
    boolean validateCampo (String name, String time, String price, String ingredient){
        int vericar = 0;

        campoName.setError(null);
        campoTimep.setError(null);
        campoPrice.setError(null);
        campoIngredients.setError(null);

        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();

        if (TextUtils.isEmpty(name))
        {campoName.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(time))
        {campoTimep.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(price))
        {campoPrice.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(ingredient))
        {campoIngredients.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            filePath = data.getData();
            //campoPhoto.setImageURI(path);

            try {
                bitmaphoto=MediaStore.Images.Media.getBitmap(this.getContentResolver(),filePath);
                campoPhoto.setImageBitmap(bitmaphoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void limpiar() {
        campoName.setError(null);
        campoTimep.setError(null);
        campoPrice.setError(null);
        campoIngredients.setError(null);
        campoId.setText("");
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


    private String horariosPlato(CheckBox morning, CheckBox afternoon, CheckBox evening)
    {
        String schedule="";
        schedule = morning.isChecked()?"TRUE,":"FALSE,";
        schedule += afternoon.isChecked()?"TRUE,":"FALSE,";
        schedule += evening.isChecked()?"TRUE":"FALSE";
        return  schedule;
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
