package com.nanodegree.topnews;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nanodegree.topnews.newslist.NewsListActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseRemoteConfig remoteConfig;
    //    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initRemoteConfig();

        // Obtain the FirebaseAnalytics instance.
//        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    private void initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfig.getInstance();

        remoteConfig.setDefaults(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        remoteConfig.setConfigSettings(remoteConfigSettings);
        fetchRemoteConfigValues();
    }

    private void fetchRemoteConfigValues() {
        long cacheExpiration = 3600;

        //expire the cache immediately for development mode.
        if (remoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        remoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // task successful. Activate the fetched data
                            remoteConfig.activateFetched();

                            Intent intent = new Intent(SplashScreenActivity.this, NewsListActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //task failed
                            showErrorDialog();
                        }
                    }
                });
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.message_network_error);
        builder.setNeutralButton("EXIT", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                SplashScreenActivity.this.finish();
            }
        });
    }

}
