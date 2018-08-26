package co.edu.udea.compumovil.gr02_20182.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class BebidasActivity extends AppCompatActivity {

    ImageView imagen;
    EditText camponombre, campoprecio, campoingredientes;
    TextView txttnombre, txttprecio, txttingredientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.mi_titulo3);
        setContentView(R.layout.activity_bebidas);

        imagen = (ImageView) findViewById(R.id.imgBebida);
        /*
        *Referencias los campos con id de elemento y se hace casting
        * */
        camponombre = (EditText) findViewById(R.id.ediNombre);
        campoprecio = (EditText) findViewById(R.id.ediPrecio);
        campoingredientes = (EditText) findViewById(R.id.ediIngredientes);
        txttnombre = (TextView) findViewById(R.id.txtnombre);
        txttprecio = (TextView) findViewById(R.id.txtprecio);
        txttingredientes = (TextView) findViewById(R.id.txtIngredientes);

        cargarBebidas();
        
        
    }


    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imgBebida:
                cargarImagen();
                break;

            case R.id.btnRegistrar:
                guardarBebidas();
                break;
        }

    }

    private void cargarBebidas() {
        /*Leemos el archivo de preferencias llamado credenciales*/
        SharedPreferences preferences = getSharedPreferences
                ("credenciales", Context.MODE_PRIVATE);

        String name = preferences.getString("name",".");
        int price = preferences.getInt("price", 0);
        String ingredients = preferences.getString("ingredients", ".");

        txttnombre.setText(name);
        txttprecio.setText(price+"");
        txttingredientes.setText(ingredients);

    }


    private void guardarBebidas() {

        /*Creamos un objeto preferencias que es un archivo de preferencias llamado credenciales,
        * de modo privado*/
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);

        /*
        *Obtener los que tenemos en el el sistema y se almacena en los String
        * */

        String nombre = camponombre.getText().toString();
        int precio = Integer.parseInt(campoprecio.getText().toString());
        String ingredientes = campoingredientes.getText().toString();

        /*
        * Le asignar al archivo credenciales los datos capturados
        * */
        SharedPreferences.Editor editor =   preferences.edit();
        editor.putString("name", nombre); /*Primer parametro es el nombre del campo en el html*/
        editor.putInt("price", precio);
        editor.putString("ingredients", ingredientes);

        txttnombre.setText(nombre);
        txttprecio.setText(precio+"");
        txttingredientes.setText(ingredientes);



        editor.commit();
    }

    private void cargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"smensajebebida"),10);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri path=data.getData();
            imagen.setImageURI(path);
        }
    }


}
