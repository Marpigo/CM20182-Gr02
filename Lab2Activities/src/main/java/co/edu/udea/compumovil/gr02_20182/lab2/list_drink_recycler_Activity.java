package co.edu.udea.compumovil.gr02_20182.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.Adaptador.AdapterDataRecycler_drink;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;

public class list_drink_recycler_Activity extends AppCompatActivity {

    List<Bebida> bebidaList;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drink_recycler_);

        setupActionBar();

        llenarRecycler();

    }

    public void llenarRecycler()
    {       // Define final variables since they have to be accessed from inner class
        final DatabaseSQLite databaseSqlite = DatabaseSQLite.getInstance(this);
        databaseSqlite.open();
      //  Toast.makeText(getApplicationContext(), "SIZE : " + databaseSqlite.getListBebida().size(), Toast.LENGTH_SHORT).show();
        bebidaList = databaseSqlite.getListBebida(); //recibir lista

        recycler= (RecyclerView) findViewById(R.id.recyclerDrink);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion

        AdapterDataRecycler_drink adapter = new AdapterDataRecycler_drink(bebidaList);
        recycler.setAdapter(adapter);
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
