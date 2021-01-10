package com.sivamalabrothers.namazvakitleri;


import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Pusula extends AppCompatActivity implements SensorEventListener ,SharedPreferences.OnSharedPreferenceChangeListener,
                                                 View.OnClickListener {
    // define the display assembly compass picture
    private ImageView image;
    private ImageView image1;
    SharedPreferences preferences, ayarlar;

    FloatingActionButton fabInfo, fabHarita;

    RelativeLayout arkaplan;
    int derece = 0;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;

    TextView tvHeading;

    private InterstitialAd interstitial;
    private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/5416140064";



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pusula_activity);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // geri butonuna basıldığında çalışır
        assert actionBar != null;
        actionBar.setTitle("Pusula");

        // our compass image
        image = (ImageView) findViewById(R.id.imageViewCompass);
        image1 = (ImageView) findViewById(R.id.imageViewYon);
        arkaplan = findViewById(R.id.arkaplan);

        fabInfo = findViewById(R.id.fabpaylas);
        fabInfo.setOnClickListener( this);

        fabHarita = findViewById(R.id.fabHarita);
        fabHarita.setOnClickListener( this);


        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ayarlari_yukle();
        //reklam_yukle();

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

            case R.id.fabpaylas:
                infoGoster();
                break;

            case R.id.fabHarita:
                haritaGoster();
                break;


        }
    }

    private void reklam_yukle(){
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(REKLAM_ID1);

        AdRequest adRequest = new AdRequest.Builder().build();

        interstitial.loadAd(adRequest);

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });
    }

    private void haritaGoster(){
        String url = "https://namazvakitleri.diyanet.gov.tr/tr-TR/kible";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void infoGoster(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Pusula.this);

        builder1.setMessage("En doğru sonuca elde etmek için metal ve elektronik eşyalardan uzak tutunuz. " +
                "Önce cihazınızı yatay konumda sağa sola çevirerek pusulanın dönmesini sağlayınız. " +
                "Sonra cihazınızı yatay konumda sabit tuttuğunuzda kıbleyi gösterecektir.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

            AlertDialog alert = builder1.create();
        alert.show();
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ayarlari_yukle() {

        derece = Integer.valueOf(preferences.getString("kible",""));
        ayarlar.registerOnSharedPreferenceChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        ayarlari_yukle();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra1 = new RotateAnimation(
                currentDegree +derece,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra1.setDuration(210);

        // set the animation after the end of the reservation status
        ra1.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        image1.startAnimation(ra1);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pusula,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ayarlar){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Pusula.this);
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn);
            }
            return  true;

        }else if(id == R.id.anasayfa){

            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Pusula.this);
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.konum){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Pusula.this);
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                krn.putExtra("neredenGeliyor","Diger");
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                krn.putExtra("neredenGeliyor","Diger");
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.ezan){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Pusula.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.aylik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Pusula.this);
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.haftalik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Pusula.this);
                Intent krn = new Intent(getApplicationContext(),HaftalikNamazVaktileriGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),HaftalikNamazVaktileriGoster.class);
                startActivity(krn);
            }

            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}