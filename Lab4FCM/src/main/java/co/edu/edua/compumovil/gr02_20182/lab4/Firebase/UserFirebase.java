package co.edu.edua.compumovil.gr02_20182.lab4.Firebase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Usuario;
import co.edu.edua.compumovil.gr02_20182.lab4.R;
import co.edu.edua.compumovil.gr02_20182.lab4.SQLiteconexion.DatabaseSQLite;


public class UserFirebase {

    private static GoogleApiClient googleApiClient;
    static DatabaseReference mDatabase; //Referencia a la base de datos
    static FirebaseAuth firebaseAuth;
    static StorageReference mStorageRef; // para referenciar la foto a guardar Storage

    public static List<Usuario> usuarioList = new ArrayList<>();
    public static int logueado = 0; //1.Gollge, 2.Usuario

    public UserFirebase() {
        inicilizarFirebase();
    }


    void inicilizarFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(); //Inicia la referencia con la base de datos en el nodo principal 'mRootReference'
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void limpiarLista() {
        if (usuarioList != null)
        {
            usuarioList.clear();
        }
    }

    public final List<Usuario> getListaUsuarios() {

        return usuarioList;
    }





    public void insertUser(final String name, final String email, final String password, Uri filePath)
    {
        final boolean registro = false;
        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_USUARIO).child(filePath.getLastPathSegment());
        //final StorageReference fotoRef = mStorageRef.child("Fotos").child(firebaseAuth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
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

                    Usuario datosuser = new Usuario();
                    datosuser.setId(UUID.randomUUID().toString()); //id automatico random
                    datosuser.setName(name);
                    datosuser.setEmail(email);
                    datosuser.setPassword(password);
                    datosuser.setImagen(downloadLink.toString());
                    datosuser.setAutenticado(0);
                    mDatabase.child(Constantes.TABLA_USUARIO).child(datosuser.getId()).setValue(datosuser);

                }
            }
        });
    }


    public  void cargarListUsuario() {

        //estamos dentro del nodo usuario
        mDatabase.child(Constantes.TABLA_USUARIO).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot: Nos devuelve  un solo valor de los tipos de usuarios

                for(final DataSnapshot snapshot: dataSnapshot.getChildren()) //getChildren: obtiene los datos de cada nodo de dataSnapshot, lo almacena en snapshot
                {
                    //Itero dentro de cada uno de los push o key subido de usuarios
                    mDatabase.child(Constantes.TABLA_USUARIO).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Usuario user = snapshot.getValue(Usuario.class); //Obtenemos los valores que solo estan declarado en Usuario models
                            usuarioList.add(user);
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


    public void deleteUsers(String id) {
        mDatabase.child(Constantes.TABLA_USUARIO).child(id).removeValue();
    }


    public void updateUser(final String id, final String name, final String email, final String password, Uri filePath)
    {
        final boolean registro = false;
        //Subimos la imagen un direcotorio Fotos, nombre de la foto filepath
        final StorageReference fotoRef = mStorageRef.child("Fotos").child(Constantes.TABLA_USUARIO).child(filePath.getLastPathSegment());
        //final StorageReference fotoRef = mStorageRef.child("Fotos").child(firebaseAuth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
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

                    Usuario datosuser = new Usuario();
                    datosuser.setId(id); //id actualizar
                    datosuser.setName(name);
                    datosuser.setEmail(email);
                    datosuser.setPassword(password);
                    datosuser.setImagen(downloadLink.toString());
                    datosuser.setAutenticado(1);
                    mDatabase.child(Constantes.TABLA_USUARIO).child(id).setValue(datosuser);

                }
            }
        });
    }



}
