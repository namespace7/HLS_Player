package com.namespace.hlsplayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class PlayerFragment extends Fragment {

    private Button play,demo;
    View view;
    String playable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        view = inflater.inflate(R.layout.fragment_player, container, false);
        final EditText url_data = view.findViewById(R.id.url_field) ;
        play= view.findViewById(R.id.play_button);
        play.setEnabled(false);
        demo= view.findViewById(R.id.demo_button);

        url_data.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Before user enters the text
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //On user changes the text
                if(s.toString().trim().length()==0) {
                       play.setEnabled(false);


                } else {
                        play.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                TextView text_chnaged = view.findViewById(R.id.text_view_id);
                text_chnaged.setText(editable);
                  play.setEnabled(URLUtil.isValidUrl(editable.toString()));

            }


        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s = url_data.getText().toString();
                if(s != null && !s.isEmpty() )
                {
                    playable = "yes";
                }
                else {
                    playable = "no";
                }

                Intent i = new Intent(getContext(), videoSwitch.class);
                i.putExtra("address_",s);
                i.putExtra("playable", playable);
                startActivity(i);
            }
        });

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playable = "no";


                Intent i = new Intent(getContext(), videoSwitch.class);
                i.putExtra("playable", playable);
                startActivity(i);
            }
        });

        return view;
    }
}