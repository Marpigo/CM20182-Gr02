package co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;

public class DatabaseSQLiteDrink {

    private static DatabaseSQLite databasesqlit;


    public List<Bebida> getListBebida() {
        Bebida bebidas = null;
        List<Bebida> bebidaList = new ArrayList<>();

        try{

            Cursor cursor = databasesqlit.database.rawQuery("SELECT * FROM bebida", null);
            while (cursor.moveToNext()){
                bebidas=new Bebida();
                bebidas.setId(cursor.getInt(0));
                bebidas.setName(cursor.getString(1));
                bebidas.setPrice(cursor.getInt(2));
                bebidas.setIngredients(cursor.getString(3));
                bebidas.setPhoto(cursor.getBlob(4));
                bebidaList.add(bebidas);
            }
        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), "Notificacion  " + e, Toast.LENGTH_SHORT).show();
        }
        return bebidaList;
    }


    public  int insertDrink(String name, double price, String ingredients, byte[] photo) {
        int registro  =0;
        try
        {
            SQLiteDatabase bd = databasesqlit.database ;

            ContentValues values=new ContentValues();
            values.put(Constantes.CAMPO_NAME_B, name);
            values.put(Constantes.CAMPO_PRECIO_B,price);
            values.put(Constantes.CAMPO_INGREDIENTES_B, ingredients);
            values.put(Constantes.CAMPO_PHOTO_B,photo);

            long idResultante = bd.insert(Constantes.TABLA_BEBIDA,null,values);
            registro = (int) idResultante;

        }catch (Exception e){

        }
        return registro;
    }



    }
