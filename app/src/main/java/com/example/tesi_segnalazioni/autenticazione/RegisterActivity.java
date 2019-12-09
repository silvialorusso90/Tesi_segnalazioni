package com.example.tesi_segnalazioni.autenticazione;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tesi_segnalazioni.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    //Costanti
    static final String CHAT_PREFS = "Chatprefs";
    static final String NOME_KEY = "username";


    private FirebaseAuth mAuth;
    EditText mNome;
    EditText mEmail;
    EditText mPassword;
    EditText mConfermaPassword;

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inizializza la User Interface
        initUI();

        // Initializza Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }

    private void initUI() {
        mNome = (EditText)findViewById(R.id.etRegName);
        mEmail = (EditText)findViewById(R.id.etRegEmail);
        mPassword = (EditText)findViewById(R.id.etRegPass);
        mConfermaPassword = (EditText)findViewById(R.id.etRegPassConf);
    }

    private void createFirebaseUser(String email, String password, final String nome){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("AuthFirebaseRegister", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //showDialog("Successo", "Registrazione avvenuta con successo", android.R.drawable.ic_dialog_info);
                            //Toast.makeText(RegisterActivity.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                            salvaNome();

                            //carica il nome in firebase
                            setNome(nome);

                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            //libera la memoria di RegisterActivity
                            finish();
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("AuthFirebaseRegister", "createUserWithEmail:failure", task.getException());
                            showDialog("Errore", "Errore nella registrazione", android.R.drawable.ic_dialog_alert);

                        }

                    }
                });

    }

    //salvare il campo nome nelle SharedPreferences
    private void salvaNome(){
        String nome = mNome.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS, 0);
        prefs.edit().putString(NOME_KEY, nome).apply();
    }

    private void setNome(String nome){

        //prende l'utente corrente
        FirebaseUser user = mAuth.getCurrentUser();

        //crea una changerequest
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build();

        user.updateProfile(changeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("setNome", "Nome caricato con successo");
                }
                else {
                    Log.d("setNome", "Errore nel caricamento del nome");
                }
            }
        });


    }

    public void btnRegistratiClick(View view) {

        String nome = mNome.getText().toString();
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        Log.d("RegisterActivity", "Button Registrati clicked");
        if(!nomeValido(nome)){
            Toast.makeText(this,"Il nome deve contenere almeno 3 lettere", Toast.LENGTH_SHORT).show();
        }
        else if(!emailValida(email)){
            Toast.makeText(this,"L'email deve contenere la @", Toast.LENGTH_SHORT).show();
        }
        else if(!pwValida(password)){
            Toast.makeText(this,"La password deve avere almeno 8 caratteri", Toast.LENGTH_SHORT).show();
        }
        else {
            createFirebaseUser(email, password, nome);
        }
    }

    public void tvLoginClick(View view) {
        Log.d("RegisterActivity", "TextView Login clicked");

        Intent i = new Intent(this, LoginActivity.class);
        finish();
        startActivity(i);
    }

    //il nome deve avere almeno 3 lettere
    private boolean nomeValido(String nome){
        if (nome.length() > 2)
            return true;
        else
            return false;
    }

    //l'email deve contenere il carattere @
    private boolean emailValida(String email){
        return email.contains("@");
    }

    //la password deve avere 8 lettere
    private boolean pwValida(String password){
        String confermaPassword = mConfermaPassword.getText().toString();
        return confermaPassword.equals(password) && password.length() > 7;

    }

    //dialog da mostrare dopo la registrazione
    private void showDialog(String title, String message, int icon){
        //creiamo un oggetto anonimo
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(icon)
                .show();
    }
}
