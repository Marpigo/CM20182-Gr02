package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;

public class SQLite_OpenHelper extends SQLiteOpenHelper {

    public SQLite_OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constantes.CREATE_USER_TABLE);
        db.execSQL(Constantes.CREATE_DRINK_TABLE);
        db.execSQL(Constantes.CREATE_FOOD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.CREATE_USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.CREATE_DRINK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+Constantes.CREATE_FOOD_TABLE);
        onCreate(db);

    }
}
