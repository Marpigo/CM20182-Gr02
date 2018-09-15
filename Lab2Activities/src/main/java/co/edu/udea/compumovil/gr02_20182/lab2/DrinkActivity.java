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
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import co.edu.udea.compumovil.gr02_20182.lab2.Constantes.Constantes;

public class DrinkActivity extends AppCompatActivity {

    EditText campoName;
    EditText campoPrice;
    EditText campoIngredients;
    ImageView campoPhoto;
    Button butregister;

    TextView campoNameInfo, campoPriceInfo, campoIngredientsInfo;

    SQLite_OpenHelper conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);


        init();


        conn=new SQLite_OpenHelper(getApplicationContext(),"bdrestaurant",null,1);

        butregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                insertDrink();
            }
        });

        campoPhoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                imagenGallery();
            }
        });

        setupActionBar();

    }

    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar= getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // actionBar.setTitle("");
        }
    }

    public  void init()
    {
        campoName = (EditText) findViewById(R.id.ediName_Drink);
        campoPrice= (EditText) findViewById(R.id.ediPreci_Drink);
        campoIngredients = (EditText) findViewById(R.id.ediIngredents_Drink);
        campoPhoto= (ImageView) findViewById(R.id.imgDrik_register);

        butregister = (Button) findViewById(R.id.butRegister_Drink);

        campoNameInfo = (TextView) findViewById(R.id.txtName_drink_information);
        campoPriceInfo = (TextView) findViewById(R.id.txtPrice_drink_information);
        campoIngredientsInfo = (TextView) findViewById(R.id.txtIngredents_drink_information);
    }



    private void insertDrink() {

        try
        {

            SQLite_OpenHelper conn=new SQLite_OpenHelper(this,"bdrestaurant",null,1);

            SQLiteDatabase db=conn.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(Constantes.CAMPO_ID_B,"0");
            values.put(Constantes.CAMPO_NAME_B,campoName.getText().toString());
            values.put(Constantes.CAMPO_PRECIO_B, campoPrice.getText().toString());
            values.put(Constantes.CAMPO_INGREDIENTES_B,campoIngredients.getText().toString());
            values.put(Constantes.CAMPO_PHOTO_B,imageViewToByte(campoPhoto));
            informationFood();

            Long idResultante=db.insert(Constantes.TABLA_BEBIDA,Constantes.CAMPO_NAME_B,values);
            Toast.makeText(getApplicationContext(),"Registro: "+idResultante,Toast.LENGTH_SHORT).show();
            db.close();
            limpiar();
    }catch (Exception e){
        Toast.makeText(getApplicationContext(), "Insercion fallida : " +e, Toast.LENGTH_SHORT).show();
    }
}


    private void informationFood() {
        /*Informacion de la captura*/
        campoNameInfo.setText(campoName.getText().toString());
        campoPriceInfo.setText(campoPrice.getText().toString());
        campoIngredientsInfo.setText(campoIngredients.getText().toString());
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void imagenGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccionar la aplicación"),10);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            Uri path = data.getData();
            campoPhoto.setImageURI(path);
        }
    }

    private void limpiar() {

        campoName.setText("");
        campoPrice.setText("0");
        campoIngredients.setText("");
        campoPhoto.setImageResource(R.drawable.drink2);
    }

}
