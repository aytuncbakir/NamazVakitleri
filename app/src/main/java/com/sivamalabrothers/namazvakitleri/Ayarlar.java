package com.sivamalabrothers.namazvakitleri;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;


public class Ayarlar extends AppCompatActivity {

    ListView listView;
    TextView textView;
    String[] listItem;


    private static final String REKLAM_ID = "ca-app-pub-3183404528711365/3189735239";
    private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/7370020374";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayarlar);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setTitle("Ayarlar");

        animasyonUygula();
        reklam_yukle();

        RelativeLayout arkaplan = findViewById(R.id.arkaplan);

        listView=(ListView)findViewById(R.id.ayarlarListe);
        //textView=(TextView)findViewById(R.id.textViewGunGece);
        listItem = getResources().getStringArray(R.array.ayarlar_liste);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.liste_gorunum, listItem);
        listView.setAdapter(adapter);
    }

    private void reklam_yukle(){

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(REKLAM_ID);
        LinearLayout reklam_layout = findViewById(R.id.reklam_layout);
        reklam_layout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        AdView adView1 = new AdView(this);
        adView1.setAdSize(AdSize.BANNER);
        adView1.setAdUnitId(REKLAM_ID1);
        LinearLayout reklam_layout1= findViewById(R.id.reklam_layout1);
        reklam_layout1.addView(adView1);
        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView1.loadAd(adRequest1);

    }

    private void animasyonUygula(){
        if(Build.VERSION.SDK_INT >=21){
            Slide enterTransition = new Slide();
            enterTransition.setDuration(300);
            enterTransition.setSlideEdge(Gravity.RIGHT);
            getWindow().setEnterTransition(enterTransition);
        }
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ayarlar,menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.anasayfa){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Ayarlar.this);
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.konum){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Ayarlar.this);
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.ezan){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Ayarlar.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.aylik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Ayarlar.this);
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.pusula){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Ayarlar.this);
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.haftalik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Ayarlar.this);
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
