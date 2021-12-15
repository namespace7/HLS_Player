package com.namespace.hlsplayer;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.sergivonavi.materialbanner.Banner;
import com.sergivonavi.materialbanner.BannerInterface;


public class MainActivity extends AppCompatActivity {
    private Button play;
    String playable;
    Banner banner;

    @Override
    protected void onResume() {
        super.onResume();
        if (checkConnection(getBaseContext())) {
            Snackbar.make(findViewById(android.R.id.content), "Application is online", Snackbar.LENGTH_LONG)
                    .show();
            // Its Available...
        } else {
            // Not Available...
            banner.show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final EditText url_data = (EditText) findViewById(R.id.url_field) ;
        play= (Button)findViewById(R.id.play_button);
        //play.setEnabled(false);

        banner = findViewById(R.id.banner);
        banner.setLeftButtonListener(new BannerInterface.OnClickListener() {
            @Override
            public void onClick(BannerInterface banner) {
                // do something
                banner.dismiss();
            }
        });
        banner.setRightButtonListener(new BannerInterface.OnClickListener() {
            @Override
            public void onClick(BannerInterface banner) {
                // do something
                Snackbar.make(findViewById(android.R.id.content), "Opening Wifi Setting", Snackbar.LENGTH_LONG)
                        .show();
                banner.dismiss();
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        if (checkConnection(getBaseContext())) {
            Snackbar.make(findViewById(android.R.id.content), "Available", Snackbar.LENGTH_LONG)
                    .show();
            // Its Available...
        } else {
            // Not Available...
            banner.show();
        }




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
                    Snackbar.make(findViewById(android.R.id.content), "Cannot be null", Snackbar.LENGTH_LONG)
                            .show();

                } else {
                    play.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
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

                Intent i = new Intent(MainActivity.this, videoSwitch.class);
                i.putExtra("address_",s);
                i.putExtra("playable", playable);
                startActivity(i);
            }
        });

    }
    /**
     * CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT
     */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }
}



