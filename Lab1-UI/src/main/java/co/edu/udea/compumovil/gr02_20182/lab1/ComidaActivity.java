package co.edu.udea.compumovil.gr02_20182.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.app.TimePickerDialog;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ComidaActivity extends AppCompatActivity implements View.OnClickListener {

    Button bhora;
    TextView ttiemo;
    private int hora, minuto;

    ImageView imagen;

    /*captura*/
    EditText camponombre, campohorario, campotipo, campohora, campoprecio, campoingredientes;
    /*reporte*/
    TextView txttnombre, txtthorario, txtttipo, txttiempo, txttprecio, txttingredientes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.my_title3);
        setContentView(R.layout.activity_comida);

        bhora = (Button)findViewById(R.id.btHora);
        ttiemo = (TextView)findViewById(R.id.txttiemp);
        bhora.setOnClickListener(this);


        //Referencias los campos con id de elemento y se hace casting

        /*
        camponombre = (EditText) findViewById(R.id.ediNombre2);
        campohorario = (EditText) findViewById(R.id.ediPrecio2);
        campotipo =  (RadioButton) findViewById(R.id.rdbPlato);
        campohora = (EditText) findViewById(R.id.ediIngredientes);
        campoprecio = (EditText) findViewById(R.id.ediPrecio2);
        campoingredientes = (EditText) findViewById(R.id.ediIngredientes2);


        txttnombre = (TextView) findViewById(R.id.txtNombrec);
        txtthorario = (TextView) findViewById(R.id.txtHorac);
        txtttipo = (TextView) findViewById(R.id.txtTipoc);
        txttiempo = (TextView) findViewById(R.id.txtTiempoc);
        txttprecio = (TextView) findViewById(R.id.txtPrecioc);
        txttingredientes = (TextView) findViewById(R.id.txtIngredientesc);
        */

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btHora:
                abrirReloj();
                break;

            case R.id.imgComida:
                cargarImagen();
                break;
        }
    }

    private void abrirReloj() {
        final Calendar calendario = Calendar.getInstance();
        hora = calendario.get(Calendar.HOUR_OF_DAY);
        minuto = calendario.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ttiemo.setText(hourOfDay+":"+minute);
            }
        },hora,minuto,false);
        timePickerDialog.show();
    }


    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"smensajebebida"),10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path=data.getData();
            imagen.setImageURI(path);
        }
    }


    /*

    private void guardarComida() {

        //*Creamos un objeto preferencias que es un archivo de preferencias llamado credenciales, de modo privado

        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        //Obtener los que tenemos en el el sistema y se almacena en los String



        String nombre = camponombre.getText().toString();
        int precio = Integer.parseInt(campoprecio.getText().toString());
        String ingredientes = campoingredientes.getText().toString();
        String horario = campohorario.getText().toString();
        String tipo = campotipo.getText().toString();
        String hora = campohora.getText().toString();

        // Le asignar al archivo credenciales los datos capturados

        SharedPreferences.Editor editor =   preferences.edit();
        editor.putString("name", nombre); //Primer parametro es el nombre del campo en el html
        editor.putString("time", horario);
        editor.putString("tip", tipo);
        editor.putString("hour", hora);
        editor.putInt("price", precio);
        editor.putString("ingredients", ingredientes);




        txttnombre.setText(nombre);
        txttprecio.setText(precio+"");
        txttingredientes.setText(ingredientes);
        txtthora.setText(hora);

        if(){

        }



        editor.commit();
    }
    */


}
