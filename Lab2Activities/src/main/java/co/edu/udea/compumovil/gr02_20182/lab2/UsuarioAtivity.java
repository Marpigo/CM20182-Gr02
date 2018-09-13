package co.edu.udea.compumovil.gr02_20182.lab2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;




/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */


public class UsuarioAtivity extends AppCompatActivity {


    ImageView campoPhoto;
    EditText campoName, campoEmail, campoPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ativity);

        campoPhoto = (ImageView) findViewById(R.id.imgPhoto);
        campoName = (EditText) findViewById(R.id.ediNameUser);
        campoEmail = (EditText) findViewById(R.id.ediEmailUser);
        campoPassword = (EditText) findViewById(R.id.ediPasswordUser);

    }


    public void onClick(View view) {

        switch (view.getId()){
            case R.id.butLoguin2:

                if(validateString(campoName.getText().toString()) && validateString(campoEmail.getText().toString()) && validateString(campoPassword.getText().toString()))
                {
                    saveUser();
                }else {
                    Toast.makeText(getApplicationContext(), "Verificar: Campos vacios", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.imgPhoto:
                chargerPhote();
                break;
        }
    }


    private void saveUser() {

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bdrestaurant", null,1);
        SQLiteDatabase db = conn.getWritableDatabase();/*Abro la bd para editar*/

        /*
        *Hacer el registro de forma rapida con SQLite   (Tambien se puede hacer mediante codigo SQL)
        *con ContentValue (una clave y valor asociado)
        **/
        ContentValues values = new ContentValues();
        values.put(Constantes.CAMPO_PHOTO, campoPhoto.getImageAlpha());
        values.put(Constantes.CAMPO_NAME, campoName.getText().toString());
        values.put(Constantes.CAMPO_PHOTO, campoEmail.getText().toString());
        values.put(Constantes.CAMPO_PHOTO, campoPassword.getText().toString());
        /*
        * Insertamos en la base de datos
        * La insercion devuelve Long, Recibe el nombre de la tabla, le decimos que nos devuelva el campo nombre
        * */

        Long nameResultantes = db.insert(Constantes.TABLA_USUARIO, Constantes.CAMPO_NAME, values);

        /*
        * Imprimimos un Toast
        * **/
        Toast.makeText(getApplicationContext(), "Name :" + nameResultantes, Toast.LENGTH_SHORT).show();

    }

    /*
    * Validar campos: Vacios o nulo
    * */
    Boolean validateString (String text){
        return text!=null && text.trim().length()>0; //Valido si el texto es diferente null y texto quitado los espacios es > 0 sera valido

    }

    private void chargerPhote() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"smensajebebida"),10);

    }

}
