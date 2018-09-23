package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class list_food_Detalle_Fragment extends DialogFragment implements TextView.OnEditorActionListener {


    TextView campoNameDetall, campoHourDetall, campoTypeDetall, getCampoTimeDetall, getCampoPriceDetall, getCampoIngredientsDetall;
    ImageView imgfood_detall;
    Button butcancelDetall;

    public static  list_food_recycler_activity fooddetalle;

    public interface NuevoDialogListener {
        void FinalizaCuadroDialogo(String texto);
    }

    // El contructor vacio es requerido para el dialogFragment
    public list_food_Detalle_Fragment() {


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_list_food__detalle_, container, false);
        init(view);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(" Detalle ");

        butcancelDetall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getDialog().dismiss();
            }
        });

        informationFood();
        return  view;
    }

    public void init(View view)
    {
        campoNameDetall = (TextView) view.findViewById(R.id.txtName_food_detalle);
        campoHourDetall = (TextView) view.findViewById(R.id.txtSchule_food_detalle);
        campoTypeDetall = (TextView) view.findViewById(R.id.txtType_food_detalle);
        getCampoTimeDetall = (TextView) view.findViewById(R.id.txtTime_food_detalle);
        getCampoPriceDetall = (TextView) view.findViewById(R.id.txtPrice_food_detalle);
        getCampoIngredientsDetall = (TextView) view.findViewById(R.id.txtIngredents_food_detalle);
        imgfood_detall = (ImageView) view.findViewById(R.id.imgFood_detalle);
        butcancelDetall= (Button) view.findViewById(R.id.butClose_detall);
    }

    public void informationFood() {

        /*Informacion de la captura*/
        campoNameDetall.setText(fooddetalle.name);
        campoHourDetall.setText(fooddetalle.hour);
        campoTypeDetall.setText(fooddetalle.type);
        getCampoTimeDetall.setText(fooddetalle.time);
        getCampoPriceDetall.setText(fooddetalle.preci);
        getCampoIngredientsDetall.setText(fooddetalle.ingredient);
        getCampoIngredientsDetall.setText(fooddetalle.ingredient);

        byte[] data = fooddetalle.photodetall;
        Bitmap image = toBitmap(data);
        imgfood_detall.setImageBitmap(image);
    }

    public static Bitmap toBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        // Return input text to activity


        return true;
    }
}
