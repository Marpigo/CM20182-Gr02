package co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab3.Constants.Constantes;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.Comida;

public class DatabaseSQLiteFood {


    private static DatabaseSQLite databasesqlit;


    public List<Comida> getListComida() {
        Comida comida = null;
        List<Comida> comidaList = new ArrayList<>();
        try{

            Cursor cursor = databasesqlit.database.rawQuery("SELECT * FROM comida", null);
            while (cursor.moveToNext()){
                comida=new Comida();
                comida.setId(cursor.getInt(0));
                comida.setName(cursor.getString(1));
                comida.setSchedule(cursor.getString(2));
                comida.setType(cursor.getString(3));
                comida.setTime(cursor.getString(4));
                comida.setPreci(cursor.getInt(5));
                comida.setIngredient(cursor.getString(6));
                comida.setPhoto(cursor.getBlob(7));

                comidaList.add(comida);
            }
        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), "Notificacion  " + e, Toast.LENGTH_SHORT).show();
        }
        return comidaList;
    }



    public  int insertFood(String name, String schedule, String type, String time, int price, String ingredients, byte[] photo) {
        int registro  =0;
        try
        {
           SQLiteDatabase bd = databasesqlit.database;

            ContentValues values=new ContentValues();
            values.put(Constantes.CAMPO_NAME_C, name);
            values.put(Constantes.CAMPO_HORARIO_C, schedule);
            values.put(Constantes.CAMPO_TIPO_C,type);
            values.put(Constantes.CAMPO_TIME_C,time);
            values.put(Constantes.CAMPO_PRECIO_C,price);
            values.put(Constantes.CAMPO_INGREDIENTES_C, ingredients);
            values.put(Constantes.CAMPO_PHOTO_C,photo);

            long idResultante = bd.insert(Constantes.TABLA_COMIDA,null,values);
            registro = (int) idResultante;

        }catch (Exception e){

        }
        return  registro;
    }

}
