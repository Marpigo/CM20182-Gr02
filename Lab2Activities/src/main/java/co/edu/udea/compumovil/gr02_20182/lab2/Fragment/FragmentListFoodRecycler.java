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
import co.edu.udea.compumovil.gr02_20182.lab2.Adaptador.AdapterDataRecycler_food;
import co.edu.udea.compumovil.gr02_20182.lab2.R;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion.DatabaseSQLiteFood;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Comida;

public class FragmentListFoodRecycler extends Fragment {



    private FragmentListDrinkRecycler.OnFragmentInteractionListener mListener;

    /*Variables recycler Comida*/
    public static List<Comida> comidaList;
    RecyclerView recyclerC;
    public list_food_Detalle_Fragment dialogoPersonalizadoC;
    public String nameC, hourC, typeC, timeC, preciC, ingredientC;
    public byte [] photodetallC;


    public FragmentListFoodRecycler() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_list_food_recycler, container, false);

        openRecyclerFood(view);

        return  view;
    }

    public void openRecyclerFood(View vista)
    {
        // Define final variables since they have to be accessed from inner class
        DatabaseSQLiteFood databasesqlitefood = new DatabaseSQLiteFood();
        final DatabaseSQLite databaseSqlite = DatabaseSQLite.getInstance(getContext());
        databaseSqlite.open();
        comidaList = databasesqlitefood.getListComida(); //recibir lista
        recyclerC= (RecyclerView) vista.findViewById(R.id.recyclerFood);
        recyclerC.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterDataRecycler_food adapter = new AdapterDataRecycler_food(comidaList);
        //metodo onclik de seleccion de las comida
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override//Este es el metodo onclick generado en el adaptador
            public void onClick(View view) {
                alertDialogBasico(comidaList.get(recyclerC.getChildAdapterPosition(view)));
            }
        });
        recyclerC.setAdapter(adapter);
    }


    public void alertDialogBasico(Comida comida){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getString(R.string.s_schedule) + ": " + comida.getSchedule() +
                "\n" + getString(R.string.s_type) + ": " +    comida.getType() +
                "\n" + getString(R.string.s_time) + ": " +    comida.getTime() +
                "\n" + getString(R.string.s_price) + ": " +    comida.getPreci() +
                "\n" + getString(R.string.s_ingredents) + ": " +    comida.getIngredient())
                .setTitle(comida.getName());


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


        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


            if (context instanceof FragmentListDrinkRecycler.OnFragmentInteractionListener) {
                mListener = (FragmentListDrinkRecycler.OnFragmentInteractionListener) context;
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
