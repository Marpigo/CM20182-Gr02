package co.edu.udea.compumovil.gr02_20182.lab2;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

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

        MainActivity.conn=new SQLite_OpenHelper(getActivity(),"bdrestaurant",null,1);

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



    private void consultUser() {

        SQLiteDatabase db= MainActivity.conn.getReadableDatabase();
        String[] parametros={user_login };
        String[] campos={Constantes.CAMPO_NAME,Constantes.CAMPO_EMAIL};

        try {
            Cursor cursor =db.query(Constantes.TABLA_USUARIO,campos,Constantes.CAMPO_NAME+"=?", parametros,null,null,null);
            cursor.moveToFirst();
            name_profil.setText(cursor.getString(0));
            email_profil.setText(cursor.getString(1));

            byte[] blob = cursor.getBlob(2);

            Bitmap image = toBitmap(blob);
            photo_profil.setImageBitmap(image);

            cursor.close();
        }catch (Exception e){
          //  Toast.makeText(getActivity(), "Consulta fallida: " + e, Toast.LENGTH_LONG).show();
         }

    }

    public static Bitmap toBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }






}
