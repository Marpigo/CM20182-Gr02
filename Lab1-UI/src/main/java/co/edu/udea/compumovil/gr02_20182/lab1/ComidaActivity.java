package co.edu.udea.compumovil.gr02_20182.lab1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.TimePickerDialog;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ComidaActivity extends AppCompatActivity implements View.OnClickListener {

    Button bhora;
    TextView ttiemo;
    private int hora, minuto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.mi_titulo2);
        setContentView(R.layout.activity_comida);

        bhora = (Button)findViewById(R.id.btHora);
        ttiemo = (TextView)findViewById(R.id.txtTiempo);
        bhora.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v ==bhora){
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
    }
}
