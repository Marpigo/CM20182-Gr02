package co.edu.udea.compumovil.gr02_20182.lab2.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import co.edu.udea.compumovil.gr02_20182.lab2.Adaptador.AdapterDataRecycler_drink;
import co.edu.udea.compumovil.gr02_20182.lab2.R;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLiteDrink;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;


public class FragmentListDrinkRecycler extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView recycler;
    List<Bebida> bebidaList;
    public static String name, preci, ingredient;
    public static byte[] potho;

    public FragmentListDrinkRecycler() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_list_drink_recycler, container, false);
        generarDatosRecycler(view);

        return view;
    }

    public void generarDatosRecycler(View vista)
    {
        final DatabaseSQLiteDrink databasesqlitedrink = new DatabaseSQLiteDrink();
        final DatabaseSQLite databaseSqlite = DatabaseSQLite.getInstance(getContext());
        databaseSqlite.open();
        recycler= (RecyclerView) vista.findViewById(R.id.recyclerDrink);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycler.setLayoutManager(new GridLayoutManager(this, 2)); ver en dos columna la informacion
        bebidaList = databasesqlitedrink.getListBebida(); //recibir lista

      //  Toast.makeText(getContext(), "SIZE bebida : " + databasesqlitedrink.getListBebida().size(), Toast.LENGTH_SHORT).show();
        AdapterDataRecycler_drink adapter = new AdapterDataRecycler_drink(bebidaList);//llenar el adaptador con la lista
        recycler.setAdapter(adapter);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        alertDialogBasico(bebidaList.get(recycler.getChildAdapterPosition(view)));
                        //interfaceComunicaFragmen.enviarBebida(bebidaList.get(recycler.getChildAdapterPosition(view)));
                    }
                });

    }

        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void alertDialogBasico(Bebida bebida){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getString(R.string.s_price) + ": " + bebida.getPrice() +
                "\n" + getString(R.string.s_ingredents) + ": " +    bebida.getIngredients())
                .setTitle(bebida.getName());


        builder.setPositiveButton("FAVORITO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.show();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

      public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
