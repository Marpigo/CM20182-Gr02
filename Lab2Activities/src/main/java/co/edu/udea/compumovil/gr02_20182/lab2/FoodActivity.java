package co.edu.udea.compumovil.gr02_20182.lab2;

import android.app.ActionBar;
import android.app.TimePickerDialog;
import android.content.Intent;
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

public class FoodActivity extends AppCompatActivity {

    EditText campoName;
    CheckBox campoMorning, campoAfternoon, campoEvening;
    RadioButton campoMain, campoEntry;
    TextView campoTime;
    EditText campoPrice;
    EditText campoIngredients;
    ImageView campoPhoto;
    Button butregister;
    Button butTime;

    TextView campoNameInfo, campoHourInfo, campoTypeInfo, getCampoTimeInfo, getCampoPriceInfo, getCampoIngredientsInfo;



    public static SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        init();

        sqLiteHelper = new SQLiteHelper(this, "bdrestaurant.sqlite", null, 1);
        sqLiteHelper.queryData(Constantes.CREATE_FOOD_TABLE);


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


    private  void insertFood()
    {
        try {

            Integer id = 1;
            String name = campoName.getText().toString();
            String schedule = horariosPlato(campoMorning, campoAfternoon, campoEvening);
            String type = campoMain.isChecked()?"Main food":"Entry";
            String time = campoTime.getText().toString();
            Integer price = Integer.parseInt(campoPrice.getText().toString());
            String ingredients = campoIngredients.getText().toString();
            byte[] imagen = imageViewToByte(campoPhoto);
            informationFood();

            sqLiteHelper.insertDataFood(0, name, schedule, type, time, price, ingredients, imagen);
            limpiar();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Insercion fallida : " +e, Toast.LENGTH_SHORT).show();
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

    private String horariosPlato(CheckBox morning, CheckBox afternoon, CheckBox evening)
    {
        String schedule="";
        if(morning.isChecked())
        {
            schedule = "Morning";
        }
        if(afternoon.isChecked())
        {
            schedule += " " + "Afternoon";
        }

        if(evening.isChecked())
        {
            schedule += " " + "Evening";
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
        Toast.makeText(new MainActivity(), "Registro exitoso!", Toast.LENGTH_SHORT).show();
        campoName.setText("");
        campoMorning.setChecked(false);
        campoAfternoon.setChecked(false);
        campoEvening.setChecked(false);
        campoMain.setChecked(false);
        campoEntry.setChecked(false);
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
