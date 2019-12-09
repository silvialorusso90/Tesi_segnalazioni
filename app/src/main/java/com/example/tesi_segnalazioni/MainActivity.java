package com.example.tesi_segnalazioni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.tesi_segnalazioni.autenticazione.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button button;

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
        setContentView(R.layout.activity_main);

        //Riceve i dati dell'intent ed estrarli con il metodo getExtras()
        Bundle b = getIntent().getExtras();
        String extra = b.getString("msg");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setTitle(mAuth.getCurrentUser().getDisplayName());

        button = (Button)findViewById(R.id.button);
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

    public void mappa(View view) {
        /*Intent i = new Intent(this, MapsActivity.class);
        finish();
        startActivity(i);

         */
    }
}
