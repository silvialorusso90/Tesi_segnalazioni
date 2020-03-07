package com.example.tesi_segnalazioni;

import android.content.Intent;
import android.os.Bundle;
import com.example.tesi_segnalazioni.autenticazione.LoginActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationActivity extends AppCompatActivity{

    private static final String TAG = "NavigationActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        name = b.getString("name");
        email = b.getString("email");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //modifica della view nello header della navigationView
        View headerViewName = navigationView.getHeaderView(0);
        TextView navHeaderName = (TextView) headerViewName.findViewById(R.id.nav_header_name);
        navHeaderName.setText(name);

        View headerViewEmail = navigationView.getHeaderView(0);
        TextView navHeaderEmail = (TextView) headerViewEmail.findViewById(R.id.nav_header_email);
        navHeaderEmail.setText(email);

        View headerViewImg = navigationView.getHeaderView(0);
        ImageView imgProfile = (ImageView) headerViewImg.findViewById(R.id.img_profile);
        imgProfile.setVisibility(View.INVISIBLE);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    //chiamato quando un item del menu viene selezionato
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //se è stato premuto il tasto logout
        if(id == R.id.logoutItem){
            Log.i(TAG, "Logout selezionato");

            //logout
            mAuth.signOut();
            updateUI();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void updateUI() {
        //se non c'è un utente loggato va in loginActivity
        //prendo l'utente corrente
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //verifica se c'è un utente loggato
        if (currentUser == null) {

            //vado in LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);

        }
    }


    public void profilo(View view) {
        Intent intent = new Intent(this, ProfiloActivity.class);
        intent.putExtra("nome", name);
        intent.putExtra("email", email);
        startActivity(intent);

    }
}
