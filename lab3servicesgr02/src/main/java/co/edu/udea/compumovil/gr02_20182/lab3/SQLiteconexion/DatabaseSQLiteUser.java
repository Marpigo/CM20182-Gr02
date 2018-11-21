package co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.UrlQuerySanitizer;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab3.Constants.Constantes;
import co.edu.udea.compumovil.gr02_20182.lab3.LoginActivity;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.Usuario;

public class DatabaseSQLiteUser {

    private static DatabaseSQLite databasesqlit;

    Activity activity;

    public List<Usuario> getListUsuario() {
        Usuario usuarios = null;
        List<Usuario> usuarioList = new ArrayList<>();

        try{

            Cursor cursor = databasesqlit.database.rawQuery("SELECT * FROM usuario", null);
            while (cursor.moveToNext()){
                usuarios=new Usuario();
                usuarios.setName(cursor.getString(0));
                usuarios.setEmail(cursor.getString(1));
                usuarios.setPassword(cursor.getString(2));
                usuarios.setPhoto(cursor.getBlob(3));
                usuarioList.add(usuarios);
            }
        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), "Notificacion  " + e, Toast.LENGTH_SHORT).show();
        }
        return usuarioList;
    }

    public  int insertUser(int id, String name, String email, String password, byte[] photo) {
        int registro  =0;
        try
        {
            SQLiteDatabase bd = databasesqlit.database ;

            ContentValues values=new ContentValues();
            values.put(Constantes.CAMPO_ID, id);
            values.put(Constantes.CAMPO_NAME, name);
            values.put(Constantes.CAMPO_EMAIL,email);
            values.put(Constantes.CAMPO_PASSWORD, password);
            values.put(Constantes.CAMPO_PHOTO,photo);

            long idResultante = bd.insert(Constantes.TABLA_USUARIO,null,values);
            registro = (int) idResultante;

        }catch (Exception e){

        }
        return registro;
    }

    public void deleteUsers() {
        SQLiteDatabase bd = databasesqlit.database;
        bd.execSQL("DELETE FROM " + Constantes.TABLA_USUARIO);
        //bd.execSQL("DELETE FROM " + Constantes.TABLA_USUARIO+ " WHERE "+ Constantes.CAMPO_ID+"='"+value+"'");
    }


    public int updateUser(String nameFirst, String name, String email, String password, byte[] photo) {
        int registro  =0;
        SQLiteDatabase bd = databasesqlit.database;

        String[] parametros = {nameFirst};
        ContentValues values = new ContentValues();
        values.put(Constantes.CAMPO_NAME, name);
        values.put(Constantes.CAMPO_EMAIL, email);
        values.put(Constantes.CAMPO_PASSWORD, password);
        values.put(Constantes.CAMPO_PHOTO, photo);
        long idResultante = bd.update(Constantes.TABLA_USUARIO, values, Constantes.CAMPO_NAME + "=?", parametros);
        registro = (int) idResultante;
        return registro;
    }



        public List<Usuario> getUser(String user, String password) {
        Usuario usuarios = null;
        List<Usuario> usuarioList = new ArrayList<>();

        int i = 0;
            for (i = 0; i< usuarioList.size(); i++){

                Toast.makeText(LoginActivity.activity, "Usuario: "  + usuarioList.get(i).getName(), Toast.LENGTH_SHORT).show();

            }

        try{

            Cursor cursor = databasesqlit.database.rawQuery("SELECT name, email, password, photo FROM usuario WHERE name ='"+ user +"' AND password='" + password +"'", null);
            while (cursor.moveToNext()){
                usuarios=new Usuario();
                usuarios.setName(cursor.getString(0));
                usuarios.setEmail(cursor.getString(1));
                usuarios.setPassword(cursor.getString(2));
                usuarios.setPhoto(cursor.getBlob(3));
                usuarioList.add(usuarios);
            }
        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), "Notificacion  " + e, Toast.LENGTH_SHORT).show();
        }
        return usuarioList;
    }



}
