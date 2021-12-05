package com.namespace.hlsplayer;

//README
//this project was done by yashwant kr singh;
//this application allows users to play hls stream on android phone
//we can select video bitrate (video quality ) according to users internet speed.
// the user need to enter http url alone with stream name
// Example - http://10.18.10.121/mystreamname
// if user doesn't pass the url perimeter ,the appplication plays a hls stream which is pre defined in the app!!

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.TrackSelectionView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.snackbar.Snackbar;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;


public class videoSwitch extends AppCompatActivity {
    private PlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private View loading;
    private ImageButton changeQuality;
    private TrackGroupArray trackGroups;
    private TrackSelector trackSelector;
    private  MappingTrackSelector selector;
    private  TrackSelection.Factory videoTrackSelectionFactory;
    String space= "\n";
    Uri URL;
    private DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //will hide the status bar
        setContentView(R.layout.activity_video_switch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //will rotate the screen
        Bundle b = getIntent().getExtras();
        String playable = b.getString("playable","");

        String url = b.getString("address_", "");
        if (playable.equals("yes")) {
            URL = Uri.parse(url);
            // Toast.makeText(getBaseContext(), url, Toast.LENGTH_LONG).show();
            initializePlayer(bandwidthMeter);
        } else {
            Toast.makeText(getBaseContext(), "Address is empty or not Valid! Playing a pre-defined Url", Toast.LENGTH_LONG).show();
            //  Toast.makeText(getBaseContext(), "Player launched!", Toast.LENGTH_LONG).show();
            // URL = Uri.parse("https://s3-us-west-2.amazonaws.com/hls-playground/hls.m3u8");//multiple quality
            //URL = Uri.parse("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8");
            URL = Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8");
            // URL = Uri.parse("http://115.112.70.86/new.m3u8");
            //URL = Uri.parse("http://cbsnewshd-lh.akamaihd.net/i/CBSNHD_7@199302/index_700_av-p.m3u8");
            initializePlayer(bandwidthMeter);
        }

        // Toast.makeText(getBaseContext(), "Player launched!", Toast.LENGTH_LONG).show();
        player.addListener(new ExoPlayer.EventListener() {

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.d("_____________________ ", String.valueOf(space));

            }
            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch (playbackState) {
                    case ExoPlayer.STATE_READY:
                        loading.setVisibility(View.GONE);
                        break;
                    case ExoPlayer.STATE_BUFFERING:
                        loading.setVisibility(View.VISIBLE);
                        break;
                }
                if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED ||
                        !playWhenReady) {

                    simpleExoPlayerView.setKeepScreenOn(false);
                } else {
                    // This prevents the screen from getting dim/lock
                    simpleExoPlayerView.setKeepScreenOn(true);
                }
            }
            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }


            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                MaterialDialog mDialog = new MaterialDialog.Builder(videoSwitch.this)
                        .setTitle("Alert")
                        .setMessage("Error Occurred While Fetching the Url,Please check the URL\n" + error + "")
                        .setCancelable(false)
                        .setPositiveButton("Retry", new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // Operation
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Edit Url", new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                Intent myIntent = new Intent(videoSwitch.this, MainActivity.class);
                                videoSwitch.this.startActivity(myIntent);
                            }
                        })
                        .build();

                // Show Dialog
                mDialog.show();
                player.stop();

            }
            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }

        });
        player.setPlayWhenReady(true); //run file/link when ready to play.
    }

    private void initializePlayer(DefaultBandwidthMeter bandwidthMeter) {
        loading = findViewById(R.id.loading);
        changeQuality = findViewById(R.id.change_quality);
        videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        selector = (MappingTrackSelector) trackSelector;
        simpleExoPlayerView = new SimpleExoPlayerView(this);
        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setUseController(true);//set to true or false to see controllers
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setPlayer(player);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "StreamTest"), bandwidthMeter);
        MediaSource videoSource = new HlsMediaSource(URL, dataSourceFactory, 1, null, null);
        player.prepare(videoSource);
        changeQuality.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                change_Quality();
            }
        });
    }



    private void change_Quality() {

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = selector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            int rendererIndex = 0;

            Pair<AlertDialog, TrackSelectionView> dialogPair =
                    TrackSelectionView.getDialog(videoSwitch.this, "Available Quality", (DefaultTrackSelector) selector, rendererIndex);
            dialogPair.first.show();
        }
    }

    private void hideSystemUi() {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.release();

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
