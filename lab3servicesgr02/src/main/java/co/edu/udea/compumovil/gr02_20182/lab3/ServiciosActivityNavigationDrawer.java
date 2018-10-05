package co.edu.udea.compumovil.gr02_20182.lab3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.AcercaDeFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.ConfigurationFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.FragmentListDrinkRecycler;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.FragmentListFoodRecycler;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.PerfilFragment;
import co.edu.udea.compumovil.gr02_20182.lab3.Fragment.ServicesBlankFragment;

public class ServiciosActivityNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentListDrinkRecycler.OnFragmentInteractionListener,
        FragmentListFoodRecycler.OnFragmentInteractionListener
        {


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
    public boolean onCreateOptionsMenu(Menu menu) {
       openFragmentServices();
        getMenuInflater().inflate(R.menu.servicios_activity_navigation_drawer, menu);
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
            openFragmentRecyclerFood();
        } else if (id == R.id.nav_drink) {
            openFragmentRecyclerDrink();
        } else if (id == R.id.nav_profile) {
            openFragmentPerfil();
        } else if (id == R.id.nav_configuration) {
            openFragmentConfiguration();
        } else if (id == R.id.nav_Sing_off) {
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
                openUsuarioActividad();
                break;
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
