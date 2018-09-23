package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class fragment_list_drink__detalle extends DialogFragment implements TextView.OnEditorActionListener {

    TextView campoNameDetall, ampoPriceDetall, campoIngredientsDetall;
    ImageView imgdrink_detall;
    Button butcancelDetalldrink;

    public static  list_drink_recycler_Activity drinkdetalle;

    public interface NuevoDialogListener {
        void FinalizaCuadroDialogo(String texto);
    }

    // El contructor vacio es requerido para el dialogFragment
    public fragment_list_drink__detalle() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_fragment_list_drink__detalle, container, false);

        init(view);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle(" Detalle ");

        butcancelDetalldrink.setOnClickListener(new View.OnClickListener() {
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
        campoNameDetall = (TextView) view.findViewById(R.id.txtName_drink_detalle);
        ampoPriceDetall = (TextView) view.findViewById(R.id.txtPrice_drink_detalle);
        campoIngredientsDetall = (TextView) view.findViewById(R.id.txtIngredents_drink_detalle);
        imgdrink_detall = (ImageView) view.findViewById(R.id.imgDrink_detalle2);
        butcancelDetalldrink= (Button) view.findViewById(R.id.butClose_detall_drink);

    }

    public void informationFood() {

        /*Informacion de la captura*/
        campoNameDetall.setText(drinkdetalle.name);
        ampoPriceDetall.setText(drinkdetalle.preci);
        campoIngredientsDetall.setText(drinkdetalle.ingredient);


        byte[] data = drinkdetalle.potho;
        Bitmap image = toBitmap(data);
        imgdrink_detall.setImageBitmap(image);
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
