package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;

public class ListDrinkActivity extends AppCompatActivity {


    private SQLite_Local mSQLite_local;
    private ListView lvBebida;
    private List<Bebida> listBebida = new ArrayList<Bebida>();
    private ArrayAdapter<Bebida> ArrayAdapterBebida;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_drink);
        setupActionBar();

        init();

        try {
            consultaBebidas();

        } catch (IOException e) {
            e.printStackTrace();
        }
        popularList();

    }

    private void init() {
        lvBebida = (ListView) findViewById(R.id.ListDrink_local);
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
             actionBar.setTitle("List Drink");
        }
    }

    private void popularList() {
        mSQLite_local = new SQLite_Local(this);
        listBebida.clear();
        listBebida = mSQLite_local.getBebidas();
        ArrayAdapterBebida = new ArrayAdapter<Bebida>(this, android.R.layout.simple_expandable_list_item_1, listBebida);
        lvBebida.setAdapter(ArrayAdapterBebida);

    }

    private void consultaBebidas() throws IOException {
        mSQLite_local = new SQLite_Local(this);

        File database = getApplicationContext().getDatabasePath(SQLite_Local.NOMBREBD);
        if (database.exists() == false) {
            mSQLite_local.getReadableDatabase();
            if (copiaSQLite(this)) {
                notificacion("Se cargo la lista bebidas ok");
            }else
            {
                notificacion("Error al cargar la lista");
            }
        }
    }


    private void notificacion(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private boolean copiaSQLite(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open(SQLite_Local.NOMBREBD);
        String outFile = SQLite_Local.LOCALBD + SQLite_Local.NOMBREBD;
        OutputStream outputStream = new FileOutputStream(outFile);
        byte[] buff = new byte[1024];
        int length = 0;

        while ((length = inputStream.read(buff)) > 0) {
            outputStream.write(buff, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        return true;
    }

}
