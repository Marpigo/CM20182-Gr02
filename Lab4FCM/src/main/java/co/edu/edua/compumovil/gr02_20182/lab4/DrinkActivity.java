package co.edu.edua.compumovil.gr02_20182.lab4;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.DrinkFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Bebida;


public class DrinkActivity extends AppCompatActivity{

    static List<Bebida> recibirListDrink;
    DrinkFirebase drinkFirebase = new  DrinkFirebase();
    ArrayAdapter<Bebida> arrayAdapterDrink;
    ListView listV_drink;
    Bebida bebidaSelected;


    private Uri filePath;
    public static int modo = 0; /*0.Nuevo, 1.Modificar*/

    EditText campoName;
    EditText campoPrice;
    EditText campoIngredients;
    ImageView campoPhoto;
    TextView campoId;


    Bitmap bitmaphoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        init();
        iniciarFirebaseList();
        setupActionBar();

        informationDrink();
    }

    public void onClick(View view) {
        String name = campoName.getText().toString();
        String price = campoPrice.getText().toString();
        String ingredients = campoIngredients.getText().toString();

        final String idU = campoId.getText().toString();

        boolean requerimientos = false;

        switch (view.getId()) {
            case R.id.imaSaveD:
                requerimientos = validateCampo(name, price, ingredients);
                if (requerimientos) {
                    if (modo == 0 ){ //Nuevo
                        drinkFirebase.insertDrink(name, price, ingredients, filePath);
                        Toast.makeText(getApplicationContext(), getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();

                        limpiar();
                    }else if(modo == 1){ //Modificar
                        drinkFirebase.updateDrink(idU, name, price, ingredients, filePath);
                        Toast.makeText(getApplicationContext(), getString(R.string.s_Firebase_registro), Toast.LENGTH_SHORT).show();
                        limpiar();
                    }
                }
                break;
            case R.id.imaNewD:
                limpiar();
                modo=0;
                break;

            case R.id.imaDeleteD:

                if (TextUtils.isEmpty(name))
                {
                    break;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.s_Firebase_eliminar) + ": " + campoName.getText().toString())
                        .setTitle(getString(R.string.s_Firebase_eliminar_continua));

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliminarDrink(idU);
                        limpiar();
                        modo =0;
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }

                });
                builder.show();
                break;

            case R.id.imgDrik_register:
                imagenGallery();
                break;
        }
    }

    void eliminarDrink(String id)
    {
        drinkFirebase.deleteDrink(id);
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
        campoId = (TextView)findViewById(R.id.texIdD);


        listV_drink = findViewById(R.id.lv_datosDrink);

    }

    void iniciarFirebaseList()
    {
        DrinkFirebase drinkFirebase = new  DrinkFirebase();
        drinkFirebase.limpiarLista();
        drinkFirebase.cargarListDrink();
        recibirListDrink = DrinkFirebase.drinkList;
    }

    private void informationDrink() {
        /*Informacion de la captura*/
        arrayAdapterDrink = new ArrayAdapter<Bebida>(DrinkActivity.this, android.R.layout.simple_list_item_1, recibirListDrink);
        listV_drink.setAdapter(arrayAdapterDrink);

        listV_drink.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                modo = 1;
                bebidaSelected = (Bebida) parent.getItemAtPosition(position);
                campoName.setText(bebidaSelected.getName());
                campoPrice.setText(bebidaSelected.getPrice());
                campoIngredients.setText(bebidaSelected.getIngredients());
                campoId.setText(bebidaSelected.getId());
                String imag = bebidaSelected.getImagen();
                Glide.with(DrinkActivity.this).load(imag).into(campoPhoto);

            }
        });
    }

    /*
    Validar campos: Vacios o nulo
   */
    boolean validateCampo (String name, String price, String ingredient){
        int vericar = 0;

        campoName.setError(null);
        campoPrice.setError(null);
        campoIngredients.setError(null);

        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();

        if (TextUtils.isEmpty(name))
        {campoName.setError(getString(R.string.s_requerimiento).toString());vericar = 1;}

        if (TextUtils.isEmpty(price))
        {campoPrice.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if (TextUtils.isEmpty(ingredient))
        {campoIngredients.setError(getString(R.string.s_requerimiento).toString()); vericar += 1;}

        if(filePath ==null)
        { Toast.makeText(this, "Imagen", Toast.LENGTH_SHORT).show();vericar += 1;}

        if(networkinfo == null && !networkinfo.isConnected()){
            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            vericar += 1;
        }
        return vericar>0?false:true;
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
            filePath = data.getData();
            //campoPhoto.setImageURI(path);

            try {
                bitmaphoto=MediaStore.Images.Media.getBitmap(this.getContentResolver(),filePath);
                campoPhoto.setImageBitmap(bitmaphoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bitmaphoto=redimensionarImagen(bitmaphoto,256,123);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();
        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }
    }

    private void limpiar() {
        campoName.setError(null);
        campoPrice.setError(null);
        campoIngredients.setError(null);

        campoName.setText("");
        campoPrice.setText("0");
        campoIngredients.setText("");
        campoPhoto.setImageResource(R.drawable.drink2);
        bitmaphoto = null;
    }


}
