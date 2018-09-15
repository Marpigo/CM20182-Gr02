package co.edu.udea.compumovil.gr02_20182.lab1;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.TimePickerDialog;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ComidaActivity extends AppCompatActivity implements View.OnClickListener {

    Button bhora;
    TextView ttiemo;
    private int hora, minuto;

    ImageView imagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.s_my_title3);
        setContentView(R.layout.activity_comida);

        bhora = (Button)findViewById(R.id.btHora);
        ttiemo = (TextView)findViewById(R.id.txttiemp);
        bhora.setOnClickListener(this);


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



}
