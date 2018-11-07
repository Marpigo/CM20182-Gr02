package co.edu.edua.compumovil.gr02_20182.lab4;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;

import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import java.util.List;

import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.DrinkFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.FoodFirebase;
import co.edu.edua.compumovil.gr02_20182.lab4.Firebase.MiFirebaseInstanceIdService;
import co.edu.edua.compumovil.gr02_20182.lab4.Fragment.AcercaDeFragment;
import co.edu.edua.compumovil.gr02_20182.lab4.Fragment.ConfigurationFragment;
import co.edu.edua.compumovil.gr02_20182.lab4.Fragment.FragmentListDrinkRecycler;
import co.edu.edua.compumovil.gr02_20182.lab4.Fragment.FragmentListFoodRecycler;
import co.edu.edua.compumovil.gr02_20182.lab4.Fragment.PerfilFragment;
import co.edu.edua.compumovil.gr02_20182.lab4.Fragment.ServicesBlankFragment;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Bebida;
import co.edu.edua.compumovil.gr02_20182.lab4.Models.Comida;

public class ServiciosNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListDrinkRecycler.OnFragmentInteractionListener,
        FragmentListFoodRecycler.OnFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener {


    static List<Bebida> recibirListDrink;
    static List<Comida> recibirListFood;



    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;


    public static boolean syncronizar = false;
    ProgressDialog progreso;
    //Van a permitir establecer la conexion con nuestro servicio web services

    MenuItem searchItem;
    public static int buscarrecycler =0; //1. busca drink, 2. Buscar

    //MiFirebaseInstanceIdService miFirebaseInstanceIdService = new MiFirebaseInstanceIdService(); ////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicios_navigation_drawer);

      //  miFirebaseInstanceIdService.registerTokenServidor(); ////////////////////////////////borrame


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*insertar regist*/

        final FloatingActionsMenu fabgrupo = (FloatingActionsMenu) findViewById(R.id.fabGrupo);
        com.getbase.floatingactionbutton.FloatingActionButton fabdrink =  (FloatingActionButton) findViewById(R.id.fabDrink);
        com.getbase.floatingactionbutton.FloatingActionButton fabfood  =  (FloatingActionButton) findViewById(R.id.fabFood);


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


        autenticadoUser();
        iniciarFirebaseListDrink();
        iniciarFirebaseListFood();
    }

    void iniciarFirebaseListDrink()
    {
        DrinkFirebase drinkFirebase = new  DrinkFirebase();
        drinkFirebase.limpiarLista();
        drinkFirebase.cargarListDrink();
        recibirListDrink = DrinkFirebase.drinkList;
    }

    void iniciarFirebaseListFood()
    {
        FoodFirebase foodFirebase = new FoodFirebase();
        foodFirebase.limpiarLista();
        foodFirebase.cargarListFood();
        recibirListFood = FoodFirebase.foodList;
    }




    public void autenticadoUser()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.servicios_navigation_drawer, menu);
         openFragmentServices(); ///////////////////////////////////////


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

            //Validar si hay conexion de internet
            ConnectivityManager con = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkinfo = con.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isConnected()) {
                iniciarFirebaseListDrink();
                iniciarFirebaseListFood();

            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.s_web_not_conexion), Toast.LENGTH_SHORT).show();
            }

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


    private void openActividadFood() {
        Intent miIntent = new Intent(ServiciosNavigationDrawer.this, FoodActivity.class);
        startActivity(miIntent);
    }
    private void openActividadDdrink() {
        Intent miIntent = new Intent(ServiciosNavigationDrawer.this, DrinkActivity.class);
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
        Intent miIntent = new Intent(ServiciosNavigationDrawer.this, UsuarioAtivity.class);
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
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {

                    openLoguin();

                } else {
                    Toast.makeText(getApplicationContext(), "No se pudo cerrar la session", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openLoguin() {
        Intent miIntent = new Intent(ServiciosNavigationDrawer.this, LoguinTabbed.class);
        startActivity(miIntent);
        finish();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}