package com.example.tesi_segnalazioni.segnalazioni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tesi_segnalazioni.LocationHelper;
import com.example.tesi_segnalazioni.NavigationActivity;
import com.example.tesi_segnalazioni.R;
import com.example.tesi_segnalazioni.ui.gallery.GalleryFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IncidenteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "inc";

    Spinner spinner;
    Button btn;
    String item, sLatitude, sLongitude;
    TextView lat, lon;
    float latitude, longitude;
    int idTimeMillis = (int) (System.currentTimeMillis() / 1000);

    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidente);

        initUI();

        mDatabase = FirebaseDatabase.getInstance();

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getApplicationContext(),  R.array.gravita_incidente, android.R.layout.simple_spinner_dropdown_item);
        //creazione dell'adapter per lo spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner click listener
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        //legge la latitudine
        myRef = mDatabase.getReference("Current Location").child("latitude");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                latitude = dataSnapshot.getValue(float.class);
                Integer i = Integer.valueOf((int) (latitude*1000));
                sLatitude = String.valueOf(i);
                lat.setText(sLatitude);
                Log.d(TAG, "Value latitude is: " + latitude);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //legge la longitudine
        myRef = mDatabase.getReference("Current Location").child("longitude");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                longitude = dataSnapshot.getValue(float.class);
                Integer i = Integer.valueOf((int) (longitude*1000));
                sLongitude = String.valueOf(i);
                lon.setText(sLongitude);
                Log.d(TAG, "Value longitude is: " + longitude);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    private void initUI() {
        spinner = (Spinner)findViewById(R.id.spinner);
        btn = (Button) findViewById(R.id.btn_invia);
        lat = (TextView) findViewById(R.id.textView6);
        lon = (TextView) findViewById(R.id.textView7);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //prendo il valore dell'elemento dello spinner selezionato
        item = parent.getItemAtPosition(position).toString();

        //visualizzo l'elemento selezionato
        /*Snackbar.make(view, item, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
         */
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }


    public void inviaSegnalazione(View view) {
        LocationHelper helper = new LocationHelper(idTimeMillis, latitude, longitude, item);

        myRef = mDatabase.getReference("Incidenti");
        myRef.child(String.valueOf(idTimeMillis)).setValue(helper);

        Intent i = new Intent(this, NavigationActivity.class);
        finish();
        startActivity(i);
    }


}
