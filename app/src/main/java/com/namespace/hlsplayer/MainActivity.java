package com.namespace.hlsplayer;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.sergivonavi.materialbanner.Banner;
import com.sergivonavi.materialbanner.BannerInterface;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;


public class MainActivity extends AppCompatActivity {
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
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.page_1:
                            replaceFragment(new PlayerFragment());
                            break;
                        case R.id.page_2:
                            replaceFragment(new AboutFragment());
                            break;

                    }
                    return true;
                });


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

        MaterialDialog mDialog = new MaterialDialog.Builder(MainActivity.this)
                .setTitle("About")
                .setMessage("Exo-player version used - 2.8.4")
                .setCancelable(false)
                .setPositiveButton("Got it", new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // Operation
                        dialogInterface.dismiss();
                    }
                })

                .build();

        // Show Dialog
        mDialog.show();


    }

    private void replaceFragment(Fragment Fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,Fragment);
        fragmentTransaction.commit();
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



