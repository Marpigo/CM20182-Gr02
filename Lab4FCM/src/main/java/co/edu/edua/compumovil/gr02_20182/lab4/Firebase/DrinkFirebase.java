package co.edu.edua.compumovil.gr02_20182.lab4.Firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import co.edu.edua.compumovil.gr02_20182.lab4.Constants.Constantes;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Bebida;


public class DrinkFirebase {

    static DatabaseReference mDatabase; //Referencia a la base de datos
    static StorageReference mStorageRef; // para referenciar la foto a guardar Storage

    public static List<Bebida> drinkList = new ArrayList<>();


    public DrinkFirebase() {
        inicilizarFirebase();
    }


    void inicilizarFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void limpiarLista() {
        if (drinkList != null)
        {
            drinkList.clear();
        }
    }

    public final List<Bebida> getListaDrink() {

        return drinkList;
    }





    public void insertDrink(final String name, final String price, final String ingredients, final Uri filePath)
    {

        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_BEBIDA).child(filePath.getLastPathSegment());
        fotoRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception { //subimos la foto al Storage con fotoRef.putFile
                if(!task.isSuccessful()){
                    throw new Exception();

                }

                return fotoRef.getDownloadUrl(); //una vez que suba todo, devuelve el link de descarga
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){ //si se pudo devolver el link, gurdo el resultado en dowloadLink
                    Uri downloadLink = task.getResult();
                    Bebida datosbebida = new Bebida();
                    datosbebida.setId(UUID.randomUUID().toString()); //id automatico random
                    datosbebida.setName(name);
                    datosbebida.setPrice(price);
                    datosbebida.setIngredients(ingredients);
                    datosbebida.setImagen(downloadLink.toString());

                    mDatabase.child(Constantes.TABLA_BEBIDA).child(datosbebida.getId()).setValue(datosbebida);
                }
            }
        });
    }


    public  void cargarListDrink() {

        //estamos dentro del nodo usuario
        mDatabase.child(Constantes.TABLA_BEBIDA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()) //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                {
                    //Itero dentro de cada uno de los push o key subido de usuarios
                    mDatabase.child(Constantes.TABLA_BEBIDA).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Bebida drink = snapshot.getValue(Bebida.class); //Obtenemos los valores que solo estan declarado en Usuario models
                            drinkList.add(drink);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void deleteDrink(String id) {
        mDatabase.child(Constantes.TABLA_BEBIDA).child(id).removeValue();
    }


    public void updateDrink(final String id, final String name, final String price, final String ingredients, Uri filePath)
    {
        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_BEBIDA).child(filePath.getLastPathSegment());
        fotoRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception { //subimos la foto al Storage con fotoRef.putFile
                if(!task.isSuccessful()){
                    throw new Exception();
                }
                return fotoRef.getDownloadUrl(); //una vez que suba todo, devuelve el link de descarga
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){ //si se pudo devolver el link, gurdo el resultado en dowloadLink

                    Uri downloadLink = task.getResult();
                    Bebida datosbebida = new Bebida();
                    datosbebida.setId(id); //id actualizar
                    datosbebida.setName(name);
                    datosbebida.setPrice(price);
                    datosbebida.setIngredients(ingredients);
                    datosbebida.setImagen(downloadLink.toString());

                    mDatabase.child(Constantes.TABLA_BEBIDA).child(id).setValue(datosbebida);
                }
            }
        });
    }



}
