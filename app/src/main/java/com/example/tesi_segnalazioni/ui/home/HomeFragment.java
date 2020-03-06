package com.example.tesi_segnalazioni.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tesi_segnalazioni.BottomSheet;
import com.example.tesi_segnalazioni.LocationHelper;
import com.example.tesi_segnalazioni.R;
import com.example.tesi_segnalazioni.segnalazioni.IncidenteActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    private HomeViewModel homeViewModel;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    View mView;
    float latitude, longitude;
    private int zoom = 16;

    ImageButton mVoicebtn;

    DatabaseReference myRef;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //aggiorna continuamente la posizione dell'utente
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @SuppressLint("RestrictedApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        mVoicebtn = mView.findViewById(R.id.voiceBtn);

        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        myRef = FirebaseDatabase.getInstance().getReference();

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                BottomSheet button = new BottomSheet();
                button.show(getFragmentManager(), "open");
            }
        });

        mVoicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speack();
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.child("Incidenti").getChildren()) {
                    String key = child.getKey();
                    LocationHelper helper = child.getValue(LocationHelper.class);
                    LatLng incidente = new LatLng(helper.getLatitude(), helper.getLongitude());
                    if (helper.getGravita().equals("Lieve"))
                        mMap.addMarker(new MarkerOptions().position(incidente).title("incidente " +
                                helper.getGravita()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                    else if(helper.getGravita().equals("Moderata"))
                        mMap.addMarker(new MarkerOptions().position(incidente).title("incidente " +
                                helper.getGravita()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    else if(helper.getGravita().equals("Grave"))
                        mMap.addMarker(new MarkerOptions().position(incidente).title("incidente " +
                                helper.getGravita()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    else
                        mMap.addMarker(new MarkerOptions().position(incidente).title("incidente " +
                                helper.getGravita()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.incidente);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return mView;
    }

    private void speack() {
        //intent to show speech to text dialog
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dimmi qualcosa");

        //start intent
        try {
            //no error
            //show dialog
            startActivityForResult(i, REQUEST_CODE_SPEECH_INPUT);

        }catch (Exception e){
            //error
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQUEST_CODE_SPEECH_INPUT:{
                if(resultCode == RESULT_OK && null!=data){

                    //get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    //set to textview
                    String voice = result.get(0);

                    if(/*mTextTv.getText().toString()*/(voice.equals("Incidente")) || (voice.equals("incidente"))){
                        Intent i = new Intent(getContext(), IncidenteActivity.class);
                        startActivity(i);
                    }
                }
                break;
            }

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //mMap.setMyLocationEnabled(true);
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));
        locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                latitude = (float) location.getLatitude();
                longitude = (float) location.getLongitude();

                LocationHelper helper = new LocationHelper(latitude, longitude);

                FirebaseDatabase.getInstance().getReference("Current Location")
                        .setValue(helper).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //if(task.isSuccessful())
                           // Toast.makeText(getContext(), "Location saved", Toast.LENGTH_SHORT).show();
                    }
                });



                LatLng posizioneutente = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(posizioneutente).title("Posizione utente"));
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneutente, zoom));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //Se SDK < 23 (nel nostro caso non è possibile perchè abbiamo messo 23 come api minima)
        if(Build.VERSION.SDK_INT >= 23){

            //verifica permessi
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

                //non abbiamo il permesso quindi lo chiediamo
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                //abbiamo già i permessi
                //googleMap.setMyLocationEnabled(true);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location ultima_posizione = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(ultima_posizione != null){
                    LatLng posizioneutente = new LatLng(ultima_posizione.getLatitude(), ultima_posizione.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(posizioneutente).title("Ultima posizione"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneutente, zoom));

                }
                else
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);



            }

        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }


    }

}



/*extends Fragment {

    private HomeViewModel homeViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
            homeViewModel =
                    ViewModelProviders.of(this).get(HomeViewModel.class);
            View root = inflater.inflate(R.layout.fragment_send, container, false);
            final TextView textView = root.findViewById(R.id.text_send);
        homeViewModel.getText().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    textView.setText(s);
                }
            });
            return root;
        }

}
*/



