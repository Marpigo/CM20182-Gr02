package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.Adaptador.AdapterDataRecycler_food;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLiteFood;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Comida;

public class list_food_recycler_activity extends AppCompatActivity {

    public static List<Comida> comidaList;
    public static  RecyclerView recycler;
    public static list_food_Detalle_Fragment dialogoPersonalizado;

    public static String name, hour, type, time, preci, ingredient;
    public static byte [] photodetall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_recycler_activity);

        setupActionBar();
        llenarRecycler();
    }

    public void llenarRecycler()
    {
        // Define final variables since they have to be accessed from inner class
        DatabaseSQLiteFood databasesqlitefood = new DatabaseSQLiteFood();
        final DatabaseSQLite databaseSqlite = DatabaseSQLite.getInstance(this);

        databaseSqlite.open();
       // Toast.makeText(getApplicationContext(), "SIZE comidad: " + databaseSqlite.getListComida().size(), Toast.LENGTH_SHORT).show();
        comidaList = databasesqlitefood.getListComida(); //recibir lista

        recycler= (RecyclerView) findViewById(R.id.recyclerFood);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion
        AdapterDataRecycler_food adapter = new AdapterDataRecycler_food(comidaList);

        /*metodo onclik de seleccion de las comida*/
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override//Este es el metodo onclick generado en el adaptador
            public void onClick(View view) {

                name = comidaList.get(recycler.getChildAdapterPosition(view)).getName();
                hour = comidaList.get(recycler.getChildAdapterPosition(view)).getSchedule();
                type = comidaList.get(recycler.getChildAdapterPosition(view)).getType();
                time = comidaList.get(recycler.getChildAdapterPosition(view)).getTime();
                preci = comidaList.get(recycler.getChildAdapterPosition(view)).getPreci() +"";
                ingredient = comidaList.get(recycler.getChildAdapterPosition(view)).getIngredient();
                photodetall = comidaList.get(recycler.getChildAdapterPosition(view)).getPhoto();
                openDetalleFood();
            }
        });
        recycler.setAdapter(adapter);
    }



    public void openDetalleFood() {

        dialogoPersonalizado = new list_food_Detalle_Fragment();
        dialogoPersonalizado.show(getSupportFragmentManager(), "personalizado");


        android.app.Fragment frag = getFragmentManager().findFragmentByTag("personalizado");

        if (frag != null) {
            getFragmentManager().beginTransaction().remove(frag).commit();
        }


    }


    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            //setTitle(@Val);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


}