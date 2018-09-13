package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;

/*
* Creamos una clase ConexionSQLiteHelper por que
* extiende SQLiteOpenhelper : que nos va permitir hace la conexion con la api sqlite
*
* */
public class ConexionSQLiteHelper extends SQLiteOpenHelper{



    /*Este contructor recibe
     name: nombre de la base de datos
     factory: null
     version: La version de bd
    * */
    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*
    * onCreate: Ejecuta nuestro scrip para crear las tablas
    * recibe la base de datos
    * */
    @Override
    public void onCreate(SQLiteDatabase bdrestaurant) {
        bdrestaurant.execSQL(Constantes.CREATE_USER_TABLE);

    }

    /*
    * Cada vez se instala la aplicacion nuevamente el upgrade:
    *   verifica si hay una base de datos la alimina y crea una nueva
    *
    * */
    @Override
    public void onUpgrade(SQLiteDatabase bdrestaurant, int versionAntigua, int versionNueva) {
        bdrestaurant.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(bdrestaurant);
    }

}
