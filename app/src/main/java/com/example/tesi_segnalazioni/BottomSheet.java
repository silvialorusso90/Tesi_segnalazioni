package com.example.tesi_segnalazioni;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tesi_segnalazioni.segnalazioni.AutoveloxActivity;
import com.example.tesi_segnalazioni.segnalazioni.IncidenteActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheet extends BottomSheetDialogFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.bottom_sheet, container, false);

        ImageView incidente = v.findViewById(R.id.incidente);
        ImageView autovelox = v.findViewById(R.id.autovelox);

        incidente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), IncidenteActivity.class);
                startActivity(i);

            }
        });

        autovelox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), AutoveloxActivity.class);
                startActivity(i);

            }
        });
        return v;

    }


    public BottomSheet() {
        // Required empty public constructor
    }

}
