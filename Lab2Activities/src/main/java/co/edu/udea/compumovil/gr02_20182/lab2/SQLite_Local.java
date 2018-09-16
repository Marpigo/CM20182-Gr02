package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab2.entidades.Bebida;

public class SQLite_Local extends SQLiteOpenHelper{

    public  static  final String NOMBREBD ="bdrestaurant.sqlite";
    public  static  final String LOCALBD ="/data/data/co.edu.udea.compumovil.gr02_20182.lab2/databases/";
    public static final int VERSION =1;
    private  Context mContext;
    private SQLiteDatabase mSQliteDatabase;

    public SQLite_Local(Context context) {
        super(context, NOMBREBD, null, VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public  void openDatabase(){
        String dbPath = mContext.getDatabasePath(NOMBREBD).getPath();
        if(mSQliteDatabase !=null && mSQliteDatabase.isOpen()){
            return;
        }
        mSQliteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }



    public void closeDatabase()
    {
        if(mSQliteDatabase != null){
            mSQliteDatabase.close();
        }
    }

    public List<Bebida> getBebidas(){
        openDatabase();
        mSQliteDatabase = this.getWritableDatabase();
        List<Bebida> listBebida = new ArrayList<Bebida>();
        String sql  = "SELECT *FROM bebida ORDER BY name ASC";
        Cursor registro = mSQliteDatabase.rawQuery(sql, null);
        if(registro.getCount()>0)
        {
            if(registro.moveToFirst()){
                do{
                    Bebida beb = new Bebida();
                    beb.setName(registro.getString(1));
                    listBebida.add(beb);
                }while(registro.moveToNext());
            }
        }
        registro.close();
        mSQliteDatabase.close();
        return  listBebida;
    }


    public List<Bebida> getListBebida() {
        Bebida bebida = null;
        Bitmap bitmap;
        List<Bebida> bebidaList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mSQliteDatabase.rawQuery("SELECT * FROM bebida", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            byte[] blob = cursor.getBlob(4);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            bitmap = BitmapFactory.decodeStream(bais);
            Toast.makeText(new MainActivity(), "Consulta fallida: " + cursor.getString(1).toString(), Toast.LENGTH_LONG).show();
            bebida = new Bebida(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), blob[0]);
            bebidaList.add(bebida);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return bebidaList;
    }

}
