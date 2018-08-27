package co.edu.udea.compumovil.gr02_20182.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ServicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.mi_titulo1);
        setContentView(R.layout.activity_servicio);
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imgComidaC:
                cargarActivityComida();
                break;

            case R.id.imgBebidaC:
                cargarActivityBebida();
                break;
        }

    }

    private void cargarActivityComida() {
        Intent miIntent = new Intent(ServicioActivity.this,ComidaActivity.class);
        startActivity(miIntent);
    }

    private void cargarActivityBebida() {
        Intent miIntent = new Intent(ServicioActivity.this,BebidasActivity.class);
        startActivity(miIntent);
    }
}
