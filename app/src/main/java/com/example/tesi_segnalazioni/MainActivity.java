package com.example.tesi_segnalazioni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tesi_segnalazioni.autenticazione.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.FAB);

        //Riceve i dati dell'intent ed estrarli con il metodo getExtras()

        Bundle b = getIntent().getExtras();
        String extra = b.getString("msg");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setTitle(mAuth.getCurrentUser().getDisplayName());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //prendo l'utente corrente
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //prendo il campo email dell'utente loggato
                String nome = user.getDisplayName();
                String email = user.getEmail();

                Intent i = new Intent(MainActivity.this, NavigationActivity.class);
                i.putExtra("name", nome);
                i.putExtra("email", email);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflating, inserimento del layout
        getMenuInflater().inflate(R.menu.menu_option, menu);

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

    private void updateUI() {
        //Se l'utente è loggato andare in MainActivity

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

}
