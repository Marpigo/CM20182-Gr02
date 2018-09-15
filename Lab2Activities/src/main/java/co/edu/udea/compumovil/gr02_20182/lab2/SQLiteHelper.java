package co.edu.udea.compumovil.gr02_20182.lab2;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public void queryData(String sql) {

            SQLiteDatabase bdrestaurant = getWritableDatabase();
            bdrestaurant.execSQL(sql);

    }
    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }



    // onCreate: Ejecuta nuestro scrip para crear las tablasrecibe la base de datos
    @Override
    public void onCreate(SQLiteDatabase bdrestaurant) {
        // bdrestaurant.execSQL(Constantes.CREATE_USER_TABLE);
        //bdrestaurant.execSQL(Constantes.CREATE_FOOD_TABLE);

    }

    //Cada vez se instala la aplicacion nuevamente el upgrade: verifica si hay una base de datos la alimina y crea una nueva
    @Override
    public void onUpgrade(SQLiteDatabase bdrestaurant, int versionAntigua, int versionNueva) {

        bdrestaurant.execSQL("DROP TABLE IF EXISTS comida");
        onCreate(bdrestaurant);

        bdrestaurant.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(bdrestaurant);

        bdrestaurant.execSQL("DROP TABLE IF EXISTS bebida");
        onCreate(bdrestaurant);

    }

    public void insertDataUser(String name, String email, String password, byte[] image) {
        try {
            SQLiteDatabase bdrestaurant = getWritableDatabase();
            String sql = "INSERT INTO USUARIO VALUES (?, ?, ?, ?)";

            SQLiteStatement statement = bdrestaurant.compileStatement(sql);
            statement.clearBindings();

            statement.bindString(1, name);
            statement.bindString(2, email);
            statement.bindString(3, password);
            statement.bindBlob(4, image);

            statement.executeInsert();
            statement.close();

        } catch (Exception e) {
            Toast.makeText(new MainActivity(), "Inserccion fallida: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void insertDataDrink(Integer id, String name, Integer price, String ingredients, byte[] image) {
        try {
            SQLiteDatabase bdrestaurant = getWritableDatabase();
            String sql = "INSERT INTO BEBIDA VALUES (?, ?, ?, ?, ?)";

            SQLiteStatement statement = bdrestaurant.compileStatement(sql);
            statement.clearBindings();

            statement.bindLong(1, id);
            statement.bindString(2, name);
            statement.bindLong(3, price);
            statement.bindString(4, ingredients);
            statement.bindBlob(5, image);

            statement.executeInsert();
            statement.close();

        } catch (Exception e) {
            Toast.makeText(new MainActivity(), "Inserccion fallida: " + e, Toast.LENGTH_SHORT).show();
        }
    }




    public void insertDataFood(Integer id, String name, String schedule, String type, String time, Integer price, String ingredients, byte[] photo) {
        try {

            SQLiteDatabase bdrestaurant = getWritableDatabase();
            String sql = "INSERT INTO COMIDA VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = bdrestaurant.compileStatement(sql);
            statement.clearBindings();

            statement.bindLong(1,  id);
            statement.bindString(2, name);
            statement.bindString(3, schedule);
            statement.bindString(4, type);
            statement.bindString(5, time);
            statement.bindLong(6, price);
            statement.bindString(7, ingredients);
            statement.bindBlob(8, photo);
            statement.executeInsert();
            statement.close();

        } catch (SQLiteException e) {
            Toast.makeText(new MainActivity(), "Inserccion fallida: " + e, Toast.LENGTH_SHORT).show();
        }


    }



}
