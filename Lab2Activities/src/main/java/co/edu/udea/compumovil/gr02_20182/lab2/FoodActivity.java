package co.edu.udea.compumovil.gr02_20182.lab2;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.AlphabeticIndex;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLiteFood;

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

    TextView campoNameInfo, campoHourInfo, campoTypeInfo, getCampoTimeInfo, getCampoPriceInfo, getCampoIngredientsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        init();

        butregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                insertFood();
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

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
           actionBar.setDisplayHomeAsUpEnabled(true);
           // actionBar.setTitle("");
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

    private void insertFood()
    {
        final DatabaseSQLiteFood databasesqlitefood = new DatabaseSQLiteFood();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String schedule;
        String type;
        String time;
        int price;
        String ingredients;
        byte[] photo;
        int registro =0;

        String campos="";
        campos = validateCampo(campoName.getText().toString(), campoTime.getText().toString(), campoPrice.getText().toString(), campoIngredients.getText().toString());

        if(campos.length()>0){
            Toast.makeText(getApplicationContext(), "Verificar Campos: " + campos, Toast.LENGTH_SHORT).show();
        }else
        {
            name = campoName.getText().toString();
            schedule = horariosPlato(campoMorning, campoAfternoon, campoEvening);
            type = campoMain.isChecked() ? "Main food" : "Entry";
            time = campoTime.getText().toString();
            price = Integer.parseInt(campoPrice.getText().toString());
            ingredients = campoIngredients.getText().toString();
            photo = imageViewToByte(campoPhoto);

            informationFood();
            registro = databasesqlitefood.insertFood( name, schedule, type, time, price, ingredients, photo);
            Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
            limpiar();
            databasesqlit.close();
        }
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

    /*
     * Validar campos: Vacios o nulo
     * */
    String validateCampo (String name, String time, String price, String ingredients){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += time !=null && time.trim().length()>0? "" :campoTimep.getText().toString() + "\n";
        campos += price !=null && price.trim().length()>0? "" :campoPrice.getHint() + "\n";
        campos += ingredients !=null && ingredients.trim().length()>0?"" : campoIngredients.getHint() + "\n";

        return campos;
    }



    private String horariosPlato(CheckBox morning, CheckBox afternoon, CheckBox evening)
    {
        String schedule="";
        if(morning.isChecked())
        {
            schedule = "Morning";
        }

        if(afternoon.isChecked())
        {
            schedule = schedule + " Afternoon";
        }

        if(evening.isChecked())
        {
            schedule = schedule + " Evening";
        }
        return  schedule;
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
            campoPhoto.setImageURI(path);

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
