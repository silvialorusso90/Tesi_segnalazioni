package com.example.tesi_segnalazioni.ui.send;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesi_segnalazioni.ChatListAdapter;
import com.example.tesi_segnalazioni.Messaggio;
import com.example.tesi_segnalazioni.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    //UI
    private EditText mInputText;
    private Button mButtonInvia;

    private ChatListAdapter chatListAdapter;
    private RecyclerView rvChatMsg;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        root = getLayoutInflater().inflate(R.layout.fragment_send, container, false);

        mAuth = FirebaseAuth.getInstance();

        initUI();

        //Riferimento alla RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChatMsg.setLayoutManager(linearLayoutManager);

        //Riferimento alla locazione del database generale
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //Creo l'oggetto Adapter
        chatListAdapter = new ChatListAdapter(getActivity(), mDatabaseReference, mAuth.getCurrentUser().getDisplayName());
        rvChatMsg.setAdapter(chatListAdapter);



        return root;
    }

    private void initUI() {
        mInputText = (EditText) root.findViewById(R.id.et_messaggio);
        mButtonInvia = (Button) root.findViewById(R.id.btn_invia);
        rvChatMsg = (RecyclerView) root.findViewById(R.id.rv_chat);

        //Tasto enter
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                inviaMessaggio();
                return true;
            }
        });

        mButtonInvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviaMessaggio();

            }
        });
    }

    private void inviaMessaggio() {

        String inputMsg = mInputText.getText().toString();
        if(!inputMsg.equals("")){

            //Nuovo oggetto di tipo messaggio
            Messaggio chat = new Messaggio(inputMsg, mAuth.getCurrentUser().getDisplayName());

            //Salvare il messaggio sul Database cloud
            mDatabaseReference.child("Chat").push().setValue(chat);

            mInputText.setText("");
        }
    }

    //Quando l'activity entra in onstop possiamo rimuovere il listener
    @Override
    public void onStop() {
        super.onStop();

        chatListAdapter.clean();
    }





    /*private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }*/
}