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
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Comida;


public class FoodFirebase {

    static DatabaseReference mDatabase; //Referencia a la base de datos
    static StorageReference mStorageRef; // para referenciar la foto a guardar Storage

    public static List<Comida> foodList = new ArrayList<>();


    public FoodFirebase() {
        inicilizarFirebase();
    }


    void inicilizarFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void limpiarLista() {
        if (foodList != null)
        {
            foodList.clear();
        }
    }

    public final List<Comida> getListaFood() {

        return foodList;
    }



    public void insertFood(final String name, final String schedule, final String type, final String time, final String preci, final String ingredient, final Uri filePath)
    {

        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_COMIDA).child(filePath.getLastPathSegment());
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
                    Comida datos = new Comida();
                    datos.setId(UUID.randomUUID().toString()); //id automatico random
                    datos.setName(name);
                    datos.setSchedule(schedule);
                    datos.setType(type);
                    datos.setTime(time);
                    datos.setPreci(preci);
                    datos.setIngredient(ingredient);
                    datos.setImagen(downloadLink.toString());

                    mDatabase.child(Constantes.TABLA_COMIDA).child(datos.getId()).setValue(datos);
                }
            }
        });
    }


    public  void cargarListFood() {

        //estamos dentro del nodo usuario
        mDatabase.child(Constantes.TABLA_COMIDA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()) //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                {
                    //Itero dentro de cada uno de los push o key subido de usuarios
                    mDatabase.child(Constantes.TABLA_COMIDA).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Comida food = snapshot.getValue(Comida.class); //Obtenemos los valores que solo estan declarado en Usuario models
                            foodList.add(food);

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


    public void deleteFood(String id) {
        mDatabase.child(Constantes.TABLA_COMIDA).child(id).removeValue();
    }


    public void updateFood(final String id, final String name, final String schedule, final String type, final String time, final String preci, final String ingredient, final Uri filePath)
    {
        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_COMIDA).child(filePath.getLastPathSegment());
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
                    Comida datos = new Comida();
                    datos.setId(id); //id actualizar
                    datos.setName(name);
                    datos.setSchedule(schedule);
                    datos.setType(type);
                    datos.setTime(time);
                    datos.setPreci(preci);
                    datos.setIngredient(ingredient);
                    datos.setImagen(downloadLink.toString());

                    mDatabase.child(Constantes.TABLA_COMIDA).child(id).setValue(datos);
                }
            }
        });
    }



}
