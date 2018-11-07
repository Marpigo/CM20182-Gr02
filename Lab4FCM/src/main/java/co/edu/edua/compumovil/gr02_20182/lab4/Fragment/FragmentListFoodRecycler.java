package co.edu.edua.compumovil.gr02_20182.lab4.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import co.edu.edua.compumovil.gr02_20182.lab4.Adapter.AdapterDataRecycler_food;
import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.FoodFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.LoguinTabbed;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Comida;
import co.edu.edua.compumovil.gr02_20182.lab4.R;

import static com.android.volley.VolleyLog.TAG;


public class FragmentListFoodRecycler extends Fragment {


    private FragmentListDrinkRecycler.OnFragmentInteractionListener mListener;

    /*Variables recycler Comida*/
    public static List<Comida> comidaList;
    RecyclerView recyclerC;
    public String nameC, hourC, typeC, timeC, preciC, ingredientC;
    public byte[] photodetallC;

    public static AdapterDataRecycler_food adapterfood;

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

        comidaList = FoodFirebase.foodList; //recibir lista
        recyclerC= (RecyclerView) vista.findViewById(R.id.recyclerFood);
        recyclerC.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterfood = new AdapterDataRecycler_food(comidaList, getContext());
        recyclerC.setAdapter(adapterfood);
        //metodo onclik de seleccion de las comida
        adapterfood.setOnClickListener(new View.OnClickListener() {
            @Override//Este es el metodo onclick generado en el adaptador
            public void onClick(View view) {
                alertDialogBasico(comidaList.get(recyclerC.getChildAdapterPosition(view)));
            }
        });

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