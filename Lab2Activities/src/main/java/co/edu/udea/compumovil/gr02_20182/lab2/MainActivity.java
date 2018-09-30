package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //public static SQLite_OpenHelper conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // sqLiteHelper = new SQLiteHelper(getApplicationContext(), "bdrestaurant.sqlite", null, 1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openLoguin();
            }
        }, 3000);

    }


    private void openLoguin() {
        Intent miIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(miIntent);
        finish();

    }





}
