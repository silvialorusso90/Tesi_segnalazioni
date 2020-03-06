package com.example.tesi_segnalazioni;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfiloActivity extends AppCompatActivity {

    String name, email;
    TextView txtName, txtEmail;
    private ImageView mImageView;

    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static int PICK_IMAGE = 123;
    public static final int GALLERY_REQUEST_CODE = 105;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        name = b.getString("nome");
        email = b.getString("email");

        initUI();

        txtName.setText(name);
        txtEmail.setText(email);
        mImageView.setImageURI(mImageUri);
    }

    private void initUI() {
        txtName = (TextView) findViewById(R.id.txt_nome);
        txtEmail = (TextView) findViewById(R.id.txt_email);
        mImageView = findViewById(R.id.img_profile);
    }

    public void create_dialog(View view) {
        Dialog d = new Dialog(ProfiloActivity.this);
        d.getWindow();
        d.setContentView(R.layout.dialog);
        d.show();
    }

    public void camera(View view) {
        Intent i = new Intent(ProfiloActivity.this, ImpostaProfilo.class);
        startActivity(i);
    }
}
