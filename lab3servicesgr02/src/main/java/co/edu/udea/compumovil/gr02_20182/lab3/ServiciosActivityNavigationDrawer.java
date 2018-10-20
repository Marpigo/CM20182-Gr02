package co.edu.udea.compumovil.gr02_20182.lab3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.AcercaDeFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.ConfigurationFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.FragmentListDrinkRecycler;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.FragmentListFoodRecycler;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.ServicesBlankFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Pattern.VolleySingleton;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLite;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteDrink;
import co.edu.udea.compumovil.gr02_20182.lab3.SQLiteconexion.DatabaseSQLiteFood;

public class ServiciosActivityNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListDrinkRecycler.OnFragmentInteractionListener,
        FragmentListFoodRecycler.OnFragmentInteractionListener
        {

            public static boolean syncronizar = false;
            ImageView campoPhotoD;
            ImageView campoPhotoF;
            public static byte[] photo;
            //Van a permitir establecer la conexion con nuestro servicio web services
            JsonObjectRequest jsonobjectrequest;
            ProgressDialog progreso;
            //Van a permitir establecer la conexion con nuestro servicio web services
            
            MenuItem searchItem;
           public static int buscarrecycler =0; //1. busca drink, 2. Buscar food

            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*insertar regist*/

        final FloatingActionsMenu fabgrupo = (FloatingActionsMenu) findViewById(R.id.fabGrupo);
        FloatingActionButton fabdrink = (FloatingActionButton) findViewById(R.id.fabDrink);
        FloatingActionButton fabfood  = (FloatingActionButton) findViewById(R.id.fabFood);
        campoPhotoD = (ImageView) findViewById(R.id.imageViewD);
        campoPhotoF = (ImageView) findViewById(R.id.imageViewF);





        fabfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActividadDdrink();
                fabgrupo.collapse();
            }
        });

        fabdrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActividadFood();
                fabgrupo.collapse();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
       openFragmentServices();
        getMenuInflater().inflate(R.menu.servicios_activity_navigation_drawer, menu);

        //MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.menu_buscador, menu);
        searchItem = menu.findItem(R.id.action_search_menu);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
       // searchItem = menu.findItem(R.id.action_search_menu).setVisible(false);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String buscar) {
                try {

                    if(buscarrecycler == 1) //buscar bebida
                    {
                        FragmentListDrinkRecycler.adapterdrink.getFilter().filter(buscar);
                    }else if (buscarrecycler == 2){ //buscar platos
                        FragmentListFoodRecycler.adapterfood.getFilter().filter(buscar);
                    }else{
                        Toast.makeText(getApplicationContext(), "No ha seleccionado una lista", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                }


                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
   if (id == R.id.action_close_navigation) {
            finish();

    }
         return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_food) {
            buscarrecycler = 2;
            openFragmentRecyclerFood();
        } else if (id == R.id.nav_drink) {
            buscarrecycler =1;
            openFragmentRecyclerDrink();
        } else if (id == R.id.nav_profile) {
            openFragmentPerfil();
        } else if (id == R.id.nav_syncronizar) {
            progreso = new ProgressDialog(this);
            progreso.setMessage(getString(R.string.s_web_loading));
            progreso.show();

            syncronizar = true;
            syncronizarDataLocal();
            syncronizar = false;
            progreso.hide();
        } else if (id == R.id.nav_configuration) {
            openFragmentConfiguration();
        } else if (id == R.id.nav_Sing_off) {
            singOff();
        } else if (id == R.id.nav_about) {
            openFragmenActividadAcercaDe();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgFood:
              //  openActividadFood();
                break;
            case R.id.imgDrink:
                //openActividadDdrink();
                break;
            case R.id.imgProfileEdition:
                UsuarioAtivity.modo = 1;/*Modo edicion*/
                openUsuarioActividad();
                break;
            case R.id.imageViewLogo:
                openFragmentServices();/*Presionar el logo muestra el menu*/
                break;
        }
    }

    private void syncronizarDataLocal(){

      //Validar si hay conexion de internet
        ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected()) {
            if(syncronizar)
            {
                openWebServiceLocaDrink();
                openWebServiceLocaFood();
                Toast.makeText(getApplicationContext(), "Update bd Local", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
        }
    }

    private void openActividadFood() {
        Intent miIntent = new Intent(ServiciosActivityNavigationDrawer.this, FoodActivity.class);
        startActivity(miIntent);
    }
    private void openActividadDdrink() {
        Intent miIntent = new Intent(ServiciosActivityNavigationDrawer.this, DrinkActivity.class);
        startActivity(miIntent);
    }
    private void openFragmentServices() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ServicesBlankFragment()).commit();

    }
    private void openFragmentConfiguration() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ConfigurationFragment()).commit();
    }
    private void openFragmentPerfil() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new PerfilFragment()).commit();
    }
    private void openFragmenActividadAcercaDe() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new AcercaDeFragment()).commit();
     }
    private void openUsuarioActividad() {
        Intent miIntent = new Intent(ServiciosActivityNavigationDrawer.this, UsuarioAtivity.class);
        startActivity(miIntent);
    }
    private void openFragmentRecyclerDrink() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListDrinkRecycler()).commit();
    }
    private void openFragmentRecyclerFood() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new FragmentListFoodRecycler()).commit();
    }

    private void singOff()
    {
        PerfilFragment.user_login ="";
        PerfilFragment.user_login ="";
        Intent miIntent = new Intent(ServiciosActivityNavigationDrawer.this, LoginActivity.class);
        startActivity(miIntent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

            public  void openWebServiceLocaDrink() {

                String ipserver = getString(R.string.s_ip_000webhost);
                String url = ipserver+"/REST/wsJSONConsultarListaB.php";

                //Enviamos la informacion a volley. Realiza el llamado a la url, e intenta conectarse a nuestro servicio REST
                jsonobjectrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        deleteDatosLocalDrink();
                        UpdateDatosLocaDrink(response);//Actualizar base local usuario

                        // Toast.makeText(getApplicationContext(), "Update bd Local", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_query) + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.i( getString(R.string.s_web_not_query), error.toString());
                    }
                });
                //Instanciamos el patron singleton  - volleySingleton
                VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonobjectrequest);
            }

            private void deleteDatosLocalDrink()
            {
                final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
                DatabaseSQLiteDrink databaseSQLitedrink = new DatabaseSQLiteDrink();
                databasesqlit.open();
                databaseSQLitedrink.deleteDrink();
                databasesqlit.close();
            }


            private void UpdateDatosLocaDrink(JSONObject response) {
                final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
                DatabaseSQLiteDrink databaseSQLitedrink = new DatabaseSQLiteDrink();
                databasesqlit.open();

                int id;
                String name;
                int preci;
                String ingredient;
                String photourl;
                //byte[] photo;
                int registro = 0;

                JSONArray json = response.optJSONArray("bebidaArrJson");
                try {

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        id = jsonObject.optInt("id");
                        name = jsonObject.optString("name");
                        preci = jsonObject.optInt("preci");
                        ingredient = jsonObject.optString("ingredient");
                        photourl = jsonObject.optString("photo");

                        openPhotoUrl(photourl, campoPhotoD);
                        photo = imageViewToByte(campoPhotoD);
                        registro = databaseSQLitedrink.insertDrink(id, name, preci, ingredient, photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "Error 2: ", e.toString());
                    Toast.makeText(this, "Error insertar bd: " + " "+response, Toast.LENGTH_LONG).show();
                    // progreso.hide();
                }
                // Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
                databasesqlit.close();
                //progreso.hide();

            }

            public  void openWebServiceLocaFood() {
                String ipserver = getString(R.string.s_ip_000webhost);
                String url = ipserver+"/REST/wsJSONConsultarListaC.php";

                //Enviamos la informacion a volley. Realiza el llamado a la url, e intenta conectarse a nuestro servicio REST
                jsonobjectrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        deleteDatosLocalFood();
                        UpdateDatosLocaFood(response);//Actualizar base local usuario

                        // Toast.makeText(getApplicationContext(), "Update bd Local", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_query) + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.i( getString(R.string.s_web_not_query), error.toString());
                    }
                });
                //Instanciamos el patron singleton  - volleySingleton
                VolleySingleton.getIntanciaVolley(this).addToRequestQueue(jsonobjectrequest);
            }

            private void deleteDatosLocalFood()
            {
                final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
                DatabaseSQLiteFood databaseSQLitedfood = new DatabaseSQLiteFood();
                databasesqlit.open();
                databaseSQLitedfood.deleteFood();
                databasesqlit.close();
            }


            private void UpdateDatosLocaFood(JSONObject response) {
                final DatabaseSQLite databasesqlit = DatabaseSQLite.getInstance(this);
                DatabaseSQLiteFood databaseSQLitedfood = new DatabaseSQLiteFood();
                databasesqlit.open();

                int id;
                String name;
                String schedule;
                String type;
                String time;
                double preci;
                String ingredient;
                String photourl;
                //byte[] photo;
                int registro = 0;

                JSONArray json = response.optJSONArray("comidaArrJson");
                try {

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonObject = null;
                        jsonObject = json.getJSONObject(i);

                        id = jsonObject.optInt("id");
                        name = jsonObject.optString("name");
                        schedule = jsonObject.optString("schedule");
                        type = jsonObject.optString("type");
                        time = jsonObject.optString("time");
                        preci = jsonObject.optDouble("preci");
                        ingredient = jsonObject.optString("ingredient");
                        photourl = jsonObject.optString("photo");

                        openPhotoUrl(photourl, campoPhotoF);
                        photo = imageViewToByte(campoPhotoF);
                        registro = databaseSQLitedfood.insertFood(id, name, schedule, type, time, preci, ingredient, photo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i( "Error 2: ", e.toString());
                    Toast.makeText(this, "Error insertar bd: " + " "+response, Toast.LENGTH_LONG).show();
                    // progreso.hide();
                }
                //Toast.makeText(getApplicationContext(), "Se inserto " + registro + " registro", Toast.LENGTH_SHORT).show();
                databasesqlit.close();
                //progreso.hide();

            }

            private void openPhotoUrl(String url, final ImageView campoPho) {
                url=url.replace(" ","%20");
                //  url = url.replace("","%20");
                ImageRequest imagerequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        campoPho.setImageBitmap(response);
                        // photo = imageViewToByte(response);

                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    }
                });
                VolleySingleton.getIntanciaVolley(this).addToRequestQueue(imagerequest);
            }

            public static byte[] imageViewToByte(ImageView image) {
                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                return byteArray;
            }


   }
