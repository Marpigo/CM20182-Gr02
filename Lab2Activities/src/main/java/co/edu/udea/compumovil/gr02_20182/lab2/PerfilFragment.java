package co.edu.udea.compumovil.gr02_20182.lab2;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;


public class PerfilFragment extends Fragment {

    public static String user_login;

    TextView name_profil, email_profil;
    ImageView photo_profil;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*
        * No reconoce porque la findViewById: por que no se puede enlazar con un objeto
        *  que no este dentro de lavista, no es igual a un activity
        *  1. Crear una vista
        *  2. Enlazar los elementos
        *  3. Retorno la vista
        * */

        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        init(view);
        consultUser();
        return  view;


    }

    public  void init(View view)
    {
        name_profil = (TextView)view.findViewById(R.id.txtNameProfil);
        email_profil = (TextView)view.findViewById(R.id.txtEmailProfil);
        photo_profil = (ImageView)view.findViewById(R.id.imgPhotoProfile);
    }


    public void consultUser()
    {
        try {

            Cursor registros = MainActivity.sqLiteHelper.getData("SELECT * FROM usuario WHERE name='" +user_login+ "'");
            registros.moveToFirst();
            name_profil.setText("Name: " + registros.getString(0));
            email_profil.setText("Email: " + registros.getString(1));
            byte[] image = registros.getBlob(3);
           // photo_profil.setImageResource(image);

            registros.close();


        }catch (Exception e){
            Toast.makeText(getActivity(), "Consulta fallida: " + e, Toast.LENGTH_LONG).show();

        }
    }



}
