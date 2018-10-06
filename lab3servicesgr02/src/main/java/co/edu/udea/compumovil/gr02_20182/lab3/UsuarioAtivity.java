package co.edu.udea.compumovil.gr02_20182.lab3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.List;

import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Models.Usuario;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteUser;

/*
* Activity para registrar los usuarios:
*  Se captura los datos de la activity, para insertar en la bdrestaurant
*
* */
public class UsuarioAtivity extends AppCompatActivity {


    ImageView campoPhoto;
    EditText campoName, campoEmail, campoPassword;

    Button butregistrar;
    public static int modo = 0; /*0.Nuevo, 1.Modificar*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_ativity);

        init();
        setupActionBar();

        if (modo == 1)
        {
            userQuery();
        }

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
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUser);
        butregistrar = (Button) findViewById(R.id.butRegistrarUser);
        campoPhoto = (ImageView) findViewById(R.id.imgPhotoUser);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.butRegistrarUser:
                String campos = "";

                campos = validateCampo(campoName.getText().toString(), campoEmail.getText().toString(), campoPassword.getText().toString());

                if (campos.length() == 0) {

                    if (modo == 0 ){
                        insertUser();
                        limpiar();
                    }else if(modo == 1){
                        updateUser();
                        PerfilFragment.user_login = campoName.getText().toString();
                        PerfilFragment.user_pass = campoPassword.getText().toString();
                        modo =0;
                        limpiar();
                    }

                    }else{
                        Toast.makeText(getApplicationContext(), "Verificar Campos: " + campos, Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case R.id.imgPhotoUser:
                        imagenGallery();
                        break;
        }
    }

    private void insertUser() {
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String email;
        String password;
        byte[] photo;
        int registro = 0;

         name = campoName.getText().toString();
         email = campoEmail.getText().toString();
         password = campoPassword.getText().toString();
         photo = imageViewToByte(campoPhoto);

         registro = databasesqliteduser.insertUser(name, email, password, photo);
         Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
         databasesqlit.close();
    }

    public static byte[] imageViewToByte (ImageView image){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    /*
         * Validar campos: Vacios o nulo
         * */
        String validateCampo (String name, String email, String password){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += email !=null && email.trim().length()>0? "" :campoEmail.getHint() + "\n";
        campos += password !=null && password.trim().length()>0?"" : campoPassword.getHint() + "\n";
        return campos;
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

    public void  userQuery() {
        Boolean uservalido = false;
        List<Usuario> userList;
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String password;

        userList = databasesqliteduser.getUser(PerfilFragment.user_login, PerfilFragment.user_pass);
        campoName.setText(userList.get(0).getName());
        campoEmail.setText(userList.get(0).getEamil());
        campoPassword.setText(userList.get(0).getPassword());
        byte[] data = userList.get(0).getPhoto();
        Bitmap image = toBitmap(data);
        campoPhoto.setImageBitmap(image);
        databasesqlit.close();
    }

    public static Bitmap toBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    private void updateUser() {
        DatabaseSQLiteUser databasesqliteduser = new DatabaseSQLiteUser();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        String email;
        String password;
        byte[] photo;
        int registro = 0;

        name = campoName.getText().toString();
        email = campoEmail.getText().toString();
        password = campoPassword.getText().toString();
        photo = imageViewToByte(campoPhoto);
        registro = databasesqliteduser.updateUser(PerfilFragment.user_login, name, email, password, photo);
        Toast.makeText(getApplicationContext(), getString(R.string.s_user_update) + " " + registro, Toast.LENGTH_SHORT).show();
        databasesqlit.close();
    }



}
