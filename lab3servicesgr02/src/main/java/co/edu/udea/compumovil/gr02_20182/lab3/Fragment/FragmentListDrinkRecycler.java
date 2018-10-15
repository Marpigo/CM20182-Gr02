package co.edu.udea.compumovil.gr02_20182.lab3.Fragment;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab3.Adapter.AdapterDataRecycler_drink;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.Bebida;
import co.edu.udea.compumovil.gr02_20182.lab3.Pattern.VolleySingleton;
import co.edu.udea.compumovil.gr02_20182.lab3.R;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteDrink;

public class FragmentListDrinkRecycler extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    private OnFragmentInteractionListener mListener;


    /*Variables recycler Comida*/
    ArrayList<Bebida> drinkList;
    RecyclerView recyclerDrink;

    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services
    JsonObjectRequest jsonobjectrequest;

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
        referentRecyclerFood(view);
        openWebServices();

        return view;
    }

    private void referentRecyclerFood(View view) {
        drinkList = new ArrayList<>();
        recyclerDrink= (RecyclerView) view.findViewById(R.id.recyclerDrink);
        recyclerDrink.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerDrink.setHasFixedSize(true);
    }

    public  void openWebServices() {
        progreso = new ProgressDialog(getContext());
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();

        String ipserver = getString(R.string.s_ip_000webhost);
        String url = ipserver+"/REST/wsJSONConsultarListaB.php";

        Log.i( "URL: ", url);

        //Enviamos la informacion a volley. Realiza el llamado a la url, e intenta conectarse a nuestro servicio REST
        jsonobjectrequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        //Instanciamos el patron singleton  - volleySingleton
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonobjectrequest);
    }

    @Override
    public void onResponse(JSONObject response) {

        progreso.hide();
        AdapterDataRecycler_drink adapter=new AdapterDataRecycler_drink(drinkList, getContext());
        recyclerDrink.setAdapter(adapter);

        udateList(response);//Actualizar lista
        //   Log.i( "Tamaño2: ", foodList.size()+"");

        //metodo onclik de seleccion de las comida
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override//Este es el metodo onclick generado en el adaptador
            public void onClick(View view) {
                alertDialogBasico(drinkList.get(recyclerDrink.getChildAdapterPosition(view)));
            }
        });
        recyclerDrink.setAdapter(adapter);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), getString(R.string.s_web_not_query) + " " + error.toString(), Toast.LENGTH_SHORT).show();
        Log.i( getString(R.string.s_web_not_query), error.toString());
    }

    /*Actualizar la ArrayList con el Array comidaArrJson*/
    public void udateList(JSONObject response) {
        Bebida bebida = null;
        JSONArray json = response.optJSONArray("bebidaArrJson");
        try {

            for (int i = 0; i < json.length(); i++) {
                bebida = new Bebida();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                bebida.setId(jsonObject.optInt("id"));
                bebida.setName(jsonObject.optString("name"));
                bebida.setPreci(jsonObject.optInt("preci"));
                bebida.setIngredients(jsonObject.optString("ingredient"));
                //comida.setPhoto(jsonObject.optString("photo"));
                bebida.setPhotoUrl(jsonObject.optString("photo"));
                drinkList.add(bebida);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error " + " "+response, Toast.LENGTH_LONG).show();
            progreso.hide();
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void alertDialogBasico(Bebida bebida){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getString(R.string.s_price) + ": " + bebida.getPreci() +
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
