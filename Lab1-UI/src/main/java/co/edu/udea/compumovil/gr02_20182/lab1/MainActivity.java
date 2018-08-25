package co.edu.udea.compumovil.gr02_20182.lab1;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.mi_titulo);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cargarActivityServicios();
            }
        },4000);

    }

    private void cargarActivityServicios() {
        Intent miIntent = new Intent(MainActivity.this,ServicioActivity.class);
        startActivity(miIntent);
        finish();
    }
}
