package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;

/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */
public class UsuarioAtivity extends AppCompatActivity {


    ImageView campoPhoto;
    EditText campoName, campoEmail, campoPassword;

    SQLite_OpenHelper conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ativity);

        conn=new SQLite_OpenHelper(getApplicationContext(),"bdrestaurant",null,1);

        init();
        setupActionBar();
    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public  void init()
    {
        campoName = (EditText) findViewById(R.id.ediNameUser);
        campoEmail = (EditText) findViewById(R.id.ediEmailUser);
        campoPassword = (EditText) findViewById(R.id.ediPasswordUser);
        campoPhoto = (ImageView) findViewById(R.id.imgPhoto);
    }

    public void onClick(View view) {

        switch (view.getId()){
            case R.id.butLoguin2:

                if(validateString(campoName.getText().toString()) && validateString(campoEmail.getText().toString()) && validateString(campoPassword.getText().toString()))
                {
                    insertUsers();
                }else {
                    Toast.makeText(getApplicationContext(), "Verificar: Campos vacios", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.imgPhoto:
                imagenGallery();
                break;
        }
    }

    private  void insertUserssql()
    {
        try {
            SQLite_OpenHelper conn=new SQLite_OpenHelper(this,"bdrestaurant",null,1);
            SQLiteDatabase db=conn.getWritableDatabase();

            String insert="INSERT INTO "+Constantes.TABLA_USUARIO
                    +" ( " +Constantes.CAMPO_NAME+","+Constantes.CAMPO_EMAIL+","+Constantes.CAMPO_PASSWORD+","+Constantes.CAMPO_PHOTO+")" +
                    " VALUES ('"+campoName.getText().toString()+"', '"+campoEmail.getText().toString()+"', '"
                    +campoPassword.getText().toString()+"','" +imageViewToByte(campoPhoto)+ "')";
            db.execSQL(insert);
            db.close();
            limpiar();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Insercion fallida : " +e, Toast.LENGTH_SHORT).show();
        }

    }


    private void insertUsers() {
        SQLite_OpenHelper conn=new SQLite_OpenHelper(this,"bdrestaurant",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Constantes.CAMPO_NAME,campoName.getText().toString());
        values.put(Constantes.CAMPO_EMAIL,campoEmail.getText().toString());
        values.put(Constantes.CAMPO_PASSWORD,campoPassword.getText().toString());
        values.put(Constantes.CAMPO_PHOTO,imageViewToByte(campoPhoto));

        Long idResultante=db.insert(Constantes.TABLA_USUARIO,Constantes.CAMPO_NAME,values);
        Toast.makeText(getApplicationContext(),"Registro: "+idResultante,Toast.LENGTH_SHORT).show();
        db.close();
        limpiar();
    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    /*
    * Validar campos: Vacios o nulo
    * */
    Boolean validateString (String text){
        return text!=null && text.trim().length()>0; //Valido si el texto es diferente null y texto quitado los espacios es > 0 sera valido

    }


    private void imagenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccionar la aplicación"),10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            Uri path = data.getData();
            campoPhoto.setImageURI(path);
        }
    }

    private void limpiar() {
        campoName.setText("");
        campoEmail.setText("");
        campoPassword.setText("");
        campoPhoto.setImageResource(R.drawable.ic_person_red_24dp);
    }


}
