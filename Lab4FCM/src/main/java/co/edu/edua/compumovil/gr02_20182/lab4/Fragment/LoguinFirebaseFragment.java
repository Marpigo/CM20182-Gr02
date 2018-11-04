package co.edu.edua.compumovil.gr02_20182.lab4.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import co.edu.edua.compumovil.gr02_20182.lab4.R;
import co.edu.edua.compumovil.gr02_20182.lab4.ServiciosNavigationDrawer;


public class LoguinFirebaseFragment extends Fragment {


    private Button button;
    private Activity activity;
    EditText campoName;/*Usaurio a buscar, perfil*/
    EditText campoPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_loguin_firebase, container, false);
        init(view);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();

                String campos="";
                campos = validateCampo(campoName.getText().toString(), campoPassword.getText().toString());
                if(campos.length() == 0)
                {
                    openNavigationDrawer();
                }else{
                    Toast.makeText(activity, getString(R.string.s_Google_verificar) + ": "+ campos, Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return  view;
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

    }

    /*
     * Validar campos: Vacios o nulo
     * */
    String validateCampo (String name, String password){
        String campos;
        campos = !TextUtils.isEmpty(name)? "" : "\n" + campoName.getHint() + "\n";
        campos += !TextUtils.isEmpty(password)? "" : "\n" + campoPassword.getHint() + "\n";
        //campos += userValidate()?"" : "Usuario o contraseña no valido"+ "\n";
//        campos = sesionActiva()?"":campos;

        return campos;
    }

/*
    public boolean sesionActiva(){
        boolean activa = false;

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();

            if (result.isSuccess()) {
                activa = true;
            }
        }
        return activa;

    }
*/


}
