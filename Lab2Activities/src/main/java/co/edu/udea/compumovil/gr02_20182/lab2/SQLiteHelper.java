package co.edu.udea.compumovil.gr02_20182.lab2;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        } catch (Exception e) {
            Toast.makeText(new MainActivity(), "Inserccion fallida: " + e, Toast.LENGTH_SHORT).show();
        }


    }


    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }



    // onCreate: Ejecuta nuestro scrip para crear las tablasrecibe la base de datos
    @Override
    public void onCreate(SQLiteDatabase bdrestaurant) {
      //  bdrestaurant.execSQL(Constantes.CREATE_USER_TABLE);

    }

    //Cada vez se instala la aplicacion nuevamente el upgrade: verifica si hay una base de datos la alimina y crea una nueva
    @Override
    public void onUpgrade(SQLiteDatabase bdrestaurant, int versionAntigua, int versionNueva) {
        bdrestaurant.execSQL("DROP TABLE IF EXISTS usuario");
        onCreate(bdrestaurant);
    }


}
