package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view) {

        openNavigationDrawer();

    }


    private void openNavigationDrawer() {
        Intent miIntent = new Intent(LoginActivity.this, ServiciosActivityNavigationDrawer.class);
        startActivity(miIntent);
        finish();

    }
}
