package com.sivamalabrothers.namazvakitleri;


import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Switch;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import java.util.ArrayList;


public class EzanDinle extends AppCompatActivity implements View.OnClickListener {


    Switch sabahSwitch, ezanSwitch, ikindiSwitch, aksamSwitch, yatsiSwitch;
    MediaPlayer ses;
    ArrayList<MediaPlayer> sesler;
    MediaPlayer control ;

    private static final String REKLAM_ID = "ca-app-pub-3183404528711365/6909488394";
    private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/9344080040";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ezan_dinle);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // geri butonuna basıldığında çalışır
        assert actionBar != null;
        actionBar.setTitle("Ezan-ı Muhammedi");

        sabahSwitch =  findViewById(R.id.sabahSwitch);
        sabahSwitch.setOnClickListener(this);

        ezanSwitch =  findViewById(R.id.ezanSwitch);
        ezanSwitch.setOnClickListener(this);

        ikindiSwitch =  findViewById(R.id.ikindiSwitch);
        ikindiSwitch.setOnClickListener(this);

        aksamSwitch =  findViewById(R.id.aksamSwitch);
        aksamSwitch.setOnClickListener(this);

        yatsiSwitch =  findViewById(R.id.yatsiSwitch);
        yatsiSwitch.setOnClickListener(this);

        sesler = new ArrayList<>();
        sesleriOlustur();
        control = null;

        reklam_yukle();

    }

    @Override
    public void onClick(View view) {
        String statusSwitch1 ="", statusSwitch2 = "";
        int id = view.getId();
        switch (id){
            case R.id.sabahSwitch:
                if (sabahSwitch.isChecked())
                    sesCikar(sesler.get(0));
                else
                    sesKapat(0);
                break;
            case R.id.ezanSwitch:
            case R.id.ikindiSwitch:
            case R.id.aksamSwitch:
            case R.id.yatsiSwitch:
                if (ezanSwitch.isChecked() || ikindiSwitch.isChecked() || aksamSwitch.isChecked() || yatsiSwitch.isChecked())
                    sesCikar(sesler.get(1));
                else
                    sesKapat(1);
                break;

        }

    }

    public void sesleriOlustur(){

        ses = MediaPlayer.create(getApplicationContext(),R.raw.sabah);
        sesler.add(ses);
        ses = MediaPlayer.create(getApplicationContext(),R.raw.ogle);
        sesler.add(ses);

    }

    public void sesKapat(int i){

            if(sesler.get(i).isPlaying()) {
                sesler.get(i).seekTo(0);
                sesler.get(i).pause();
            }

    }

    public void sesCikar(MediaPlayer mSes){

        if(calanSesVarmi() == null)
            mSes.start();
        else {
            ArrayList<Integer> gelen = calanSesVarmi();
            for(int i = 0; i<gelen.size();i++)
                sesKapat(gelen.get(i));
            mSes.start();
        }
    }

    public ArrayList<Integer> calanSesVarmi(){

        ArrayList<Integer> calanlar = new ArrayList<>();
        for(int i = 0; i<sesler.size(); i++)
            if (sesler.get(i).isPlaying())
                calanlar.add(i);

        return null;
    }



    private void reklam_yukle(){

        AdView  adView = new AdView(this);
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

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ezan,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ayarlar){

            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EzanDinle.this);
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.anasayfa){

            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EzanDinle.this);
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.pusula){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EzanDinle.this);
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.konum){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EzanDinle.this);
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                krn.putExtra("neredenGeliyor","Diger");
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                krn.putExtra("neredenGeliyor","Diger");
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.aylik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EzanDinle.this);
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.haftalik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(EzanDinle.this);
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
