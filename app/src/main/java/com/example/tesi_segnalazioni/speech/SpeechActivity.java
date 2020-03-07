package com.example.tesi_segnalazioni.speech;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesi_segnalazioni.R;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechActivity extends AppCompatActivity {
    ImageView img;
    String ascolto;
    TextView t;
    TextToSpeech ts;
    Button btn;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);
        img = findViewById(R.id.imgMic);
        t = findViewById(R.id.txtSpeech);
        btn = findViewById(R.id.button2);
        ts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR)
                    ts.setLanguage(Locale.ITALY);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent to show speech to text dialog
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Dimmi");

                //start intent
                try {
                    //no error
                    //show dialog
                    startActivityForResult(i, REQUEST_CODE_SPEECH_INPUT);

                }catch (Exception e){
                    //error
                    Toast.makeText(SpeechActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ts.speak(a, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

         */
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
                    ascolto = result.get(0);
                    Toast.makeText(this, ascolto, Toast.LENGTH_SHORT).show();
                    t.setText(ascolto);
                    if (ascolto.equals("Ciao")){
                        ts.speak("hai detto ciao", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    else
                        ts.speak("cosa hai detto?", TextToSpeech.QUEUE_FLUSH, null);



                }
                break;
            }

        }

    }

    @Override
    protected void onPause() {
        if (ts != null){
            ts.stop();
            ts.shutdown();
        }
        super.onPause();
    }
}
