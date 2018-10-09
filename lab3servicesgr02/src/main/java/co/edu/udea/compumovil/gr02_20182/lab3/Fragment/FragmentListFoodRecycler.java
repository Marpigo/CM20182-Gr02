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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab3.Adapter.AdapterDataRecycler_food;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.Comida;
import co.edu.udea.compumovil.gr02_20182.lab3.R;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteFood;

public class FragmentListFoodRecycler extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{



    private FragmentListDrinkRecycler.OnFragmentInteractionListener mListener;

    /*Variables recycler Comida*/
    ArrayList<Comida> foodList;
    RecyclerView recyclerFood;

    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services
    RequestQueue request;
    JsonObjectRequest jsonobjectrequest;

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

        referentRecyclerFood(view);
        openWebServices();

        return  view;
    }

    private void referentRecyclerFood(View view) {
       foodList = new ArrayList<>();
       recyclerFood= (RecyclerView) view.findViewById(R.id.recyclerFood);
       recyclerFood.setLayoutManager(new LinearLayoutManager(getContext()));
       recyclerFood.setHasFixedSize(true);

       request = Volley.newRequestQueue(getContext());
    }

    public  void openWebServices() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage(getString(R.string.s_web_loading));
        progreso.show();

        String server ="192.168.1.1";
        String url = "http://"+server+"/REST/wsJSONConsultarListaC.php";

        Log.i( "URL: ", url);

        //Enviamos la informacion a volley. Realiza el llamado a la url, e intenta conectarse a nuestro servicio REST
        jsonobjectrequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonobjectrequest);
    }

    @Override
    public void onResponse(JSONObject response) {

            progreso.hide();

            AdapterDataRecycler_food adapter=new AdapterDataRecycler_food(foodList);
            recyclerFood.setAdapter(adapter);


            udateList(response);//Actualizar lista
            Log.i( "Tamaño2: ", foodList.size()+"");

            //metodo onclik de seleccion de las comida
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override//Este es el metodo onclick generado en el adaptador
                public void onClick(View view) {
                    alertDialogBasico(foodList.get(recyclerFood.getChildAdapterPosition(view)));
                }
            });
            recyclerFood.setAdapter(adapter);



    }

    /*Actualizar la ArrayList con el Array comidaArrJson*/
    public void udateList(JSONObject response) {
        Comida comida = null;
        JSONArray json = response.optJSONArray("comidaArrJson");
        try {

            for (int i = 0; i < json.length(); i++) {
                comida = new Comida();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);

                comida.setId(jsonObject.optInt("id"));
                comida.setName(jsonObject.optString("name"));
                comida.setSchedule(jsonObject.optString("schedule"));
                comida.setType(jsonObject.optString("type"));
                comida.setTime(jsonObject.optString("time"));
                comida.setPreci(jsonObject.optInt("preci"));
                comida.setIngredient(jsonObject.optString("ingredient"));
                 comida.setPhoto(jsonObject.optString("photo"));
                foodList.add(comida);
             }
          } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error " + " "+response, Toast.LENGTH_LONG).show();
            progreso.hide();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(), getString(R.string.s_web_not_query) + " " + error.toString(), Toast.LENGTH_SHORT).show();
        Log.i( getString(R.string.s_web_not_query), error.toString());
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
