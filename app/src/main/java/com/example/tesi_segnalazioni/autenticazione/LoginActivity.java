package com.example.tesi_segnalazioni.autenticazione;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesi_segnalazioni.MainActivity;
import com.example.tesi_segnalazioni.MapsActivity;
import com.example.tesi_segnalazioni.NavigationActivity;
import com.example.tesi_segnalazioni.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText mNomeUtente;
    EditText mPassword;

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void updateUI(FirebaseUser currentUser) {
        //Se l'utente è loggato andare in MainActivity

        //prendo l'utente corrente
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //verifica se c'è un utente loggato
        if (user != null) {

            //prendo il campo email dell'utente loggato
            String email = user.getEmail();
            String name = user.getDisplayName();

            //vado in MainActivity
            /*Intent i = new Intent(this, MainActivity.class);
            i.putExtra("msg", name);
            finish();
            startActivity(i);

             */



            Intent i = new Intent(this, NavigationActivity.class);
            i.putExtra("name", name);
            i.putExtra("email", email);
            finish();
            startActivity(i);

        }

    }

    public void btnLoginClick(View view) {
        Log.d("LoginActivity", "Login Button Click");

        //Collega le variabili ai Widgets
        mNomeUtente = (EditText)findViewById(R.id.etRegName);
        mPassword = (EditText)findViewById(R.id.etRegPass);

        //estrae le stringhe
        String nomeUtente = mNomeUtente.getText().toString();
        String password = mPassword.getText().toString();

        Log.d("LoginActivity",nomeUtente);
        Log.d("LoginActivity",password);

        //validazione campi
        if(!(nomeUtente.length() > 7) || !(nomeUtente.contains("@"))) {
            Toast.makeText(this, "Email non valida", Toast.LENGTH_SHORT).show();
            return;
        } else if(!(password.length() > 7)){
            Toast.makeText(this, "Password non valida", Toast.LENGTH_SHORT).show();
            return;
        }else
            //login firebase
            loginUser(nomeUtente, password);

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }

    public void tvRegistratiClick(View view) {

        Log.d("LoginActivity", "Registrati Click");

        Intent intent1 = new Intent(this, RegisterActivity.class);
        finish();
        startActivity(intent1);

    }

}
