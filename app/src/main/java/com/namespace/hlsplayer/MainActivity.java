package com.namespace.hlsplayer;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private Button play;
    String playable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final EditText url_data = (EditText) findViewById(R.id.url_field) ;
        play= (Button)findViewById(R.id.play_button);



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

                Intent i = new Intent(MainActivity.this, videoSwitch.class);
                i.putExtra("address_",s);
                i.putExtra("playable", playable);
                startActivity(i);
            }
        });

    }


}


