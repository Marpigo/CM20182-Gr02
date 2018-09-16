package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;


public class ListDrinkFragment extends Fragment {


    private SQLite_Local mSQLite_local;
    private ListView lvBebida;
    private List<Bebida> listBebida = new ArrayList<Bebida>();
    private ArrayAdapter<Bebida> ArrayAdapterBebida;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_drink, container, false);


        //consultaBebidas();
       // popularList();
    }

    /*
    private void popularList() {
        mSQLite_local = new SQLite_Local(this);
        listBebida.clear();
        listBebida = mSQLite_local.getBebidas();
        ArrayAdapterBebida = new ArrayAdapter<Bebida>(this, and)
    }

    private void consultaBebidas() {
        mSQLite_local = new SQLite_Local(getActivity().getApplicationContext());

        File database = getActivity().getApplicationContext().getDatabasePath(SQLite_Local.NOMBREBD);
        if(database.exists() == false)
        {
            mSQLite_local.getReadableDatabase();
            if(copiaSQLite(this))
            {


            }
        }
    }

    private boolean copiaSQLite(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open(SQLite_Local.NOMBREBD);
        String outFile = SQLite_Local.LOCALBD + SQLite_Local.NOMBREBD;
        OutputStream outputStream = new FileOutputStream(outFile);
        byte[] buff = new  byte[1024];
        int length = 0;

        while ((length = inputStream.read(buff))>0)
        {
            outputStream.write(buff, 0, length);
        }
        outputStream.flush();
        outputStream.close();
        return  true;
    }
*/
}
