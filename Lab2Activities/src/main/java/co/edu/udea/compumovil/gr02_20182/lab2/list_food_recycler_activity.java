package co.edu.udea.compumovil.gr02_20182.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.Adaptador.AdapterDataRecycler_food;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Comida;

public class list_food_recycler_activity extends AppCompatActivity {

    List<Comida> comidaList;
    RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food_recycler_activity);

        setupActionBar();
        llenarRecycler();
    }

    public void llenarRecycler()
    {       // Define final variables since they have to be accessed from inner class
        final DatabaseSQLite databaseSqlite = DatabaseSQLite.getInstance(this);
        databaseSqlite.open();
       // Toast.makeText(getApplicationContext(), "SIZE comidad: " + databaseSqlite.getListComida().size(), Toast.LENGTH_SHORT).show();
        comidaList = databaseSqlite.getListComida(); //recibir lista

        recycler= (RecyclerView) findViewById(R.id.recyclerFood);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion

        AdapterDataRecycler_food adapter = new AdapterDataRecycler_food(comidaList);
        recycler.setAdapter(adapter);
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
