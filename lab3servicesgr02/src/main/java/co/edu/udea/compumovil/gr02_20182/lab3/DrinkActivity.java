package co.edu.udea.compumovil.gr02_20182.lab3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteDrink;

public class DrinkActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText campoName;
    EditText campoPrice;
    EditText campoIngredients;
    ImageView campoPhoto;
    Button butregister;

    TextView campoNameInfo, campoPriceInfo, campoIngredientsInfo;

    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services
    RequestQueue request;
    JsonObjectRequest jsonobjectrequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        init();

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
            actionBar.setTitle(getString(R.string.s_register) +" " + getString(R.string.s_drink));
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

        request = Volley.newRequestQueue(this);

    }



    private void insertDrink()
    {
        DatabaseSQLiteDrink databasesqlitedrink = new DatabaseSQLiteDrink();
        final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
        databasesqlit.open();

        String name;
        double price;
        String ingredients;
        byte[] photo;
        int registro =0;
        String campos="";

        campos = validateCampo(campoName.getText().toString(), campoPrice.getText().toString(), campoIngredients.getText().toString());

        if(campos.length()>0){

            Toast.makeText(getApplicationContext(), "Verificar Campos: " + campos, Toast.LENGTH_SHORT).show();

        }else
        {
            name = campoName.getText().toString();
            price = Integer.parseInt(campoPrice.getText().toString());
            ingredients = campoIngredients.getText().toString();
            photo = imageViewToByte(campoPhoto);

            informationFood();
            registro = databasesqlitedrink.insertDrink(name, price, ingredients, photo);
            Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
            limpiar();
            databasesqlit.close();
        }
    }


    /*
     * Validar campos: Vacios o nulo
     * */
    String validateCampo (String name, String price, String ingredients){
        String campos;
        campos = name !=null && name.trim().length()>0? "" : "\n" + campoName.getHint() + "\n";
        campos += price !=null && price.trim().length()>0? "" :campoPrice.getHint() + "\n";
        campos += ingredients !=null && ingredients.trim().length()>0?"" : campoIngredients.getHint() + "\n";
        return campos;
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

    /*Metodos que nos permite Volley*/
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
