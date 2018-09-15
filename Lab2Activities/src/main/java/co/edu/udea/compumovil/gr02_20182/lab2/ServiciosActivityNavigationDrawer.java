package co.edu.udea.compumovil.gr02_20182.lab2;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ServiciosActivityNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*insertar regist*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.Insert);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        }else if (id == R.id.action_return) {
       openFragmentServices();
    }

         return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        /*android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.escenario, new ServicesBlankFragment()).commit();
        */

        if (id == R.id.nav_food) {
            openFragmenListFood();
        } else if (id == R.id.nav_drink) {
            openFragmenListDrink();
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
                openActividadFood();
                break;

            case R.id.imgDrink:
                openActividadDdrink();
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


    private void openFragmenListDrink() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ListDrinkFragment()).commit();
    }

    private void openFragmenListFood() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new ListFoodFragment()).commit();
    }


    private void openFragmenActividadAcercaDe() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragmentContainers, new AcercaDeFragment()).commit();
     }


    private void openUsuarioActividad() {
        Intent miIntent = new Intent(ServiciosActivityNavigationDrawer.this, UsuarioAtivity.class);
        startActivity(miIntent);
    }




}
