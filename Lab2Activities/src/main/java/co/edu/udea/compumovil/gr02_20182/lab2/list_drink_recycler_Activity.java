package co.edu.udea.compumovil.gr02_20182.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.Adaptador.AdapterDataRecycler_drink;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLiteDrink;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;

public class list_drink_recycler_Activity extends AppCompatActivity {

    List<Bebida> bebidaList;
    RecyclerView recycler;
    public static String name, preci, ingredient;
    public static byte[] potho;

    public static fragment_list_drink__detalle dialogoPersonalizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drink_recycler_);

        setupActionBar();

        llenarRecycler();

    }

    public void llenarRecycler()
    {

        DatabaseSQLiteDrink databasesqlitedrink = new DatabaseSQLiteDrink();
        final DatabaseSQLite databaseSqlite = DatabaseSQLite.getInstance(this);
        databaseSqlite.open();
      //  Toast.makeText(getApplicationContext(), "SIZE : " + databaseSqlite.getListBebida().size(), Toast.LENGTH_SHORT).show();
        bebidaList = databasesqlitedrink.getListBebida(); //recibir lista


        recycler= (RecyclerView) findViewById(R.id.recyclerDrink);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion

        AdapterDataRecycler_drink adapter = new AdapterDataRecycler_drink(bebidaList);
        recycler.setAdapter(adapter);

        /*metodo onclik de seleccion de las bebida*/
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override//Este es el metodo onclick generado en el adaptador
            public void onClick(View view) {

                name = bebidaList.get(recycler.getChildAdapterPosition(view)).getName();
                preci = bebidaList.get(recycler.getChildAdapterPosition(view)).getPrice() +"";
                ingredient = bebidaList.get(recycler.getChildAdapterPosition(view)).getIngredients();
                potho = bebidaList.get(recycler.getChildAdapterPosition(view)).getPhoto();

                openDetalleDrink();

            }
        });
        recycler.setAdapter(adapter);

    }


    public void openDetalleDrink() {

        dialogoPersonalizado = new fragment_list_drink__detalle();
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
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}
