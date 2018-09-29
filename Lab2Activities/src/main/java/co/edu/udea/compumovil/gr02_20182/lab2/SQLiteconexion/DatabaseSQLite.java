package co.edu.udea.compumovil.gr02_20182.lab2.SQLiteconexion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;
import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Comida;

public class DatabaseSQLite {
    private SQLiteOpenHelper openHelper;
    public static SQLiteDatabase database = null;
    private static DatabaseSQLite instance;


    /**
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseSQLite(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseSQLite getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseSQLite(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getNames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT name FROM bebida", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * Read the BLOB data as byte[]
     *
     * @param name name of the place
     * @return image as byte[]
     */
    public byte[] getImage(String name) {
        byte[] data = null;
        Cursor cursor = database.rawQuery("SELECT photo FROM bebida WHERE name = ?", new String[]{name});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            data = cursor.getBlob(0);
            break;  // Assumption: name is unique
        }
        cursor.close();
        return data;
    }



}
