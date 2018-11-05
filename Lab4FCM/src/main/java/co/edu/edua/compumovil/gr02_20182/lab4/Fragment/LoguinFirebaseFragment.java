package co.edu.edua.compumovil.gr02_20182.lab4.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.UserFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Usuario;
import co.edu.edua.compumovil.gr02_20182.lab4.R;
import co.edu.edua.compumovil.gr02_20182.lab4.ServiciosNavigationDrawer;
import co.edu.edua.compumovil.gr02_20182.lab4.UsuarioAtivity;


public class LoguinFirebaseFragment extends Fragment {


    static List<Usuario> usuarioList;

    private Button button;
    private Activity activity;
    EditText campoName;/*Usaurio a buscar, perfil*/
    EditText campoPassword;
    UsuarioAtivity user = new UsuarioAtivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_loguin_firebase, container, false);
        init(view);


        iniciarFirebaseList();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();

                String name = campoName.getText().toString();
                String password = campoPassword.getText().toString();


                if( validateCampo(name, password))
                {
                    UserFirebase.logueado  = 2;
                    openNavigationDrawer();
                }
            }
        });

        return  view;
    }

    void iniciarFirebaseList()
    {
        UserFirebase userFirebase = new  UserFirebase();
        userFirebase.limpiarLista();
        userFirebase.cargarListUsuario();
        usuarioList = userFirebase.getListaUsuarios();
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        return super.shouldShowRequestPermissionRationale(permission);
    }

    private void openNavigationDrawer() {
        Intent miIntent = new Intent(getContext(), ServiciosNavigationDrawer.class);
        startActivity(miIntent);
        //finish();
    }

    public void init(View view){

        button = (Button)view.findViewById(R.id.butLoguinF); //button de google
        //campo a buscar
        campoName = (EditText) view.findViewById(R.id.ediName_loguinF);
        campoPassword = (EditText) view.findViewById(R.id.ediPass_loguinF);

     //   mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
      //  mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    /*
     * Validar campos: Vacios o nulo
     * */
    boolean validateCampo (String name, String password){
        int  vericar = 0;

        campoName.setError(null);
        campoPassword.setError(null);

        if (TextUtils.isEmpty(name))
        {campoName.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(password))
        {campoPassword.setError(getString(R.string.s_requerimiento).toString()); vericar +=  1;}


        vericar += autenticarUser(name, password)?0:1;

       return vericar>0?false:true;
    }


    boolean autenticarUser(String name, String pass)
    {
        boolean autenticado = false;
        int i = 0;
        for(i = 0; (i < usuarioList.size() && !autenticado); i++)
        {
            if(usuarioList.get(i).getName().equals(name) && usuarioList.get(i).getPassword().equals(pass))
            {
                autenticado = true;
                usuarioList.get(i).setAutenticado(1);
            }
        }


        if(!autenticado)
        {
            Toast.makeText(activity, getString(R.string.s_Firebase_user_no_valido), Toast.LENGTH_SHORT).show();
        }
        return  autenticado;
    }



}
