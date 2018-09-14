package co.edu.udea.compumovil.gr02_20182.lab2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;




/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */


public class UsuarioAtivity extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;

    ImageView campoPhoto;
    EditText campoName, campoEmail, campoPassword;


    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ativity);

        init();
        sqLiteHelper = new SQLiteHelper(this, "bdrestaurant.sqlite", null, 1);
        sqLiteHelper.queryData(Constantes.CREATE_USER_TABLE);

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



    private  void insertUsers()
    {
        try {
            String name =campoName.getText().toString();
            String email = campoEmail.getText().toString();
            String password = campoPassword.getText().toString();
            byte[] imagen = imageViewToByte(campoPhoto);
            
            sqLiteHelper.insertDataUser(name, email, password, imagen);
            limpiar();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Insercion fallida : " +e, Toast.LENGTH_SHORT).show();
        }

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
        Toast.makeText(getApplicationContext(), "Registro exitoso!", Toast.LENGTH_SHORT).show();
        campoName.setText("");
        campoEmail.setText("");
        campoPassword.setText("");
        campoPhoto.setImageResource(R.drawable.ic_person_red_24dp);
    }


}
