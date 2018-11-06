package co.edu.edua.compumovil.gr02_20182.lab4.Firebase;

import com.google.firebase.database.FirebaseDatabase;

//Clase para la persistencia de los datos
//Se llama tambien desde e Manifest
public class MyFirebaseApp extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
