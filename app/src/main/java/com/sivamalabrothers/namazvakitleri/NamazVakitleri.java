package com.sivamalabrothers.namazvakitleri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;


public class NamazVakitleri extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
        , SharedPreferences.OnSharedPreferenceChangeListener{

    Spinner ulkespinner, ilspinner, ilcespinner;
    TextView baslik, ulke, il ,ilce, checkText;
    Button secimiGoster, kayitliKonumGoster;
    CheckBox hatirla;
    private  String URL = "";
    private String hatirlaURL;
    private String hatirlaIl;
    private String hatirlaIlce;

    Bundle gelenVeri;


    ArrayList<String> ulkeler, iller, ilceler;
    ArrayList<VakitVeriler> vakitVerilers;
    XMLDOMParser xmldomParser;

    String ulkeKontrol = "";
    String ilKontrol = "";
    String ilceKontrol = "";

    int ilpozisyon = 0;
    int ilcepozisyon = 0;


    String ilkGiris = "";

    private static final String REKLAM_ID = "ca-app-pub-3183404528711365/3097098482";
    private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/9373542857";

    SharedPreferences preferences, ayarlar;

    LinearLayout arkaplan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.namaz_vakitleri);

        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assert actionBar != null;
        actionBar.setTitle("Konum Seçiniz");

        initViews();
        animasyonUygula();
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
        if(Build.VERSION.SDK_INT >= 21)
            finishAfterTransition();
        else
            finish();
        return true;
    }




    private void setSpinners(View sp , ArrayList<String> data ) {

        int id = sp.getId();
        switch (id){
            case R.id.ulkespinner:
                ArrayAdapter<String> ulkeadapter =
                        new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_gorunum, data);
                ulkespinner.setAdapter(ulkeadapter);

                break;
            case R.id.ilspinner:
                ArrayAdapter<String> iladapter =
                        new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_gorunum, data);
                ilspinner.setAdapter(iladapter);

                break;
            case R.id.ilcespinner:
                ArrayAdapter<String> ilceadapter =
                        new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_gorunum, data);
                ilcespinner.setAdapter(ilceadapter);
                break;
        }

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
        LinearLayout reklam_layout1 = findViewById(R.id.reklam_layout1);
        reklam_layout1.addView(adView1);

        AdRequest adRequest1 = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView1.loadAd(adRequest1);

    }



    private void initViews() {



        checkText = findViewById(R.id.checkText);
        hatirla = findViewById(R.id.hatirla);
        hatirla.setOnClickListener(this);

        ulkespinner = findViewById(R.id.ulkespinner);
        ulkespinner.setOnItemSelectedListener(this);

        ilspinner = findViewById(R.id.ilspinner);
        ilspinner.setOnItemSelectedListener(this);

        ilcespinner = findViewById(R.id.ilcespinner);
        ilcespinner.setOnItemSelectedListener(this);
        ilcespinner.setEnabled(false);

        secimiGoster = findViewById(R.id.secimiGoster);
        secimiGoster.setOnClickListener(this);



        xmldomParser = new XMLDOMParser(this);
        xmldomParser.XMLRead("ulkeler/ulkeler.xml","ulke");
        ulkeler = xmldomParser.getUlkeler();
        setSpinners(ulkespinner,ulkeler);

        arkaplan = findViewById(R.id.arkaplan);
        arkaplan.setBackgroundColor(getResources().getColor(R.color.krem));
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        reklam_yukle();
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        String secilenulke ="", secilenil = "", secilenilce = "";
        String xmLUrl = "";
        switch (id){
            case R.id.ulkespinner:

                    secilenulke = ulkespinner.getSelectedItem().toString();
                    secilenulke = editString(secilenulke);
                    ulkeKontrol = secilenulke;


                xmLUrl = "ulkeler/"+secilenulke+"/"+secilenulke+".xml";
                //Toast.makeText(getApplicationContext(),xmLUrl,Toast.LENGTH_LONG).show();
                if(ulkeKontrol.equals("turkiye") || ulkeKontrol.equals("abd") || ulkeKontrol.equals("kanada")){
                    ilcespinner.setEnabled(true);
                    ilcespinner.setVisibility(View.VISIBLE);
                    xmldomParser.XMLRead(xmLUrl,"il");
                    iller = xmldomParser.getIller();
                    setSpinners(ilspinner,iller);
                }else{
                    ilcespinner.setEnabled(false);
                    ilcespinner.setVisibility(View.INVISIBLE);
                    xmldomParser.XMLRead(xmLUrl,"ilce");
                    ilceler = xmldomParser.getIlceler();
                    setSpinners(ilspinner,ilceler);
                    vakitVerilers = xmldomParser.getVakitVerilers();
                }
                xmLUrl = "";
                break;
            case R.id.ilspinner:

                        secilenil = ilspinner.getSelectedItem().toString();
                        ilpozisyon = ilspinner.getSelectedItemPosition();
                        secilenil =  editString(secilenil);
                        ilKontrol = secilenil;

                if(hatirla.isChecked()) {
                    Toast.makeText(getApplicationContext(),"seçildi",Toast.LENGTH_LONG).show();
                    hatirlaIl = secilenil;
                    secimimiKaydet("hatirlaIl",hatirlaIl);
                }


                xmLUrl = "ulkeler/"+ulkeKontrol+"/"+ilKontrol+".xml";
                if(ulkeKontrol.equals("turkiye") || ulkeKontrol.equals("abd") || ulkeKontrol.equals("kanada")) {

                    xmldomParser.XMLRead(xmLUrl,"ilce");
                    ilceler = xmldomParser.getIlceler();
                    vakitVerilers = xmldomParser.getVakitVerilers();
                    setSpinners(ilcespinner, ilceler);
                }

                //Toast.makeText(getApplicationContext(),ilpozisyon+"",Toast.LENGTH_LONG).show();
                xmLUrl = "";
                break;
            case R.id.ilcespinner:

                        secilenilce = ilcespinner.getSelectedItem().toString();
                        secilenilce =  editString(secilenilce);
                        ilcepozisyon = ilcespinner.getSelectedItemPosition();



                ilceKontrol = secilenilce;
                if(hatirla.isChecked()) {
                    Toast.makeText(getApplicationContext(),"seçildi",Toast.LENGTH_LONG).show();
                    hatirlaIlce = secilenilce;
                    secimimiKaydet("hatirlaIlce",hatirlaIlce);

                }
                xmLUrl = "ulkeler/"+ulkeKontrol+"/"+ilceKontrol+".xml";

                //Toast.makeText(getApplicationContext(),ilcepozisyon+"",Toast.LENGTH_LONG).show();
                xmLUrl = "";
                break;
        }
    }

    private  void  setURL(){


        if(ulkeKontrol.equals("turkiye") || ulkeKontrol.equals("abd") || ulkeKontrol.equals("kanada")) {
            URL = "https://namazvakitleri.diyanet.gov.tr" + vakitVerilers.get(ilcepozisyon).getUrl() + "/";
            if(hatirla.isChecked()) {
                hatirlaURL = URL;
                secimimiKaydet("hatirlaURL",hatirlaURL);
            }
        }
        else {
            URL = "https://namazvakitleri.diyanet.gov.tr" + vakitVerilers.get(ilpozisyon).getUrl() + "/";
            if(hatirla.isChecked()) {
                hatirlaURL = URL;
                secimimiKaydet("hatirlaURL", hatirlaURL);
            }
        }
        secimimiKaydet("secilenUlke", ulkeKontrol);
        secimimiKaydet("secilenIl", ilKontrol);


        //Toast.makeText(getApplicationContext(),vakitVerilers.get(ilcepozisyon).getUrl()+" URL:"+URL,Toast.LENGTH_LONG).show();
    }

    private String editString(String edit){

        edit = edit.toLowerCase();
        edit = edit.replace("ü","u");
        edit = edit.replace("ö","o");
        edit = edit.replace("ş","s");
        edit = edit.replace("ç","c");
        edit = edit.replace("ğ","g");
        edit = edit.replace("ı","i");

        return  edit;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    @Override
    public void onClick(View view) {


            int id = view.getId();
            switch (id) {
                case R.id.secimiGoster:

                    String neredenGeliyor = "Splash";
                    gelenVeri = getIntent().getExtras();
                    if(gelenVeri != null){
                        neredenGeliyor = gelenVeri.getString("neredenGeliyor");
                    }

                    assert neredenGeliyor != null;
                    if(neredenGeliyor.equals("Diger")){

                    }
                    setURL();
                    hatirla.setChecked(true);
                    Intent vakitGoster = new Intent(this, NamazVaktiGoster.class);

                    if (ulkeKontrol.equals("turkiye") || ulkeKontrol.equals("abd") || ulkeKontrol.equals("kanada")) {
                        vakitGoster.putExtra("konum", ilceKontrol);
                        vakitGoster.putExtra("url", URL);
                        secimimiKaydet("konumKayit", ilceKontrol);
                        secimimiKaydet("urlKayit", URL);
                    } else {
                        vakitGoster.putExtra("konum", ilKontrol);
                        vakitGoster.putExtra("url", URL);
                        secimimiKaydet("konumKayit", ilKontrol);
                        secimimiKaydet("urlKayit", URL);
                    }

                    ilkGiris = "girildi";
                    secimimiKaydet("ilkGiris", ilkGiris);
                    preferences.registerOnSharedPreferenceChangeListener(this);
                    startActivity(vakitGoster);
                    break;

                /*case R.id.kayitliKonumGoster:
                    hatirla.setChecked(true);
                    hatirlaURL = preferences.getString("hatirlaURL", "");
                    hatirlaIlce = preferences.getString("hatirlaIlce", "");
                    hatirlaIl = preferences.getString("hatirlaIl", "");

                    if (hatirlaURL != "") {
                        Intent vakitGoster1 = new Intent(this, NamazVaktiGoster.class);

                        if (ulkeKontrol.equals("turkiye") || ulkeKontrol.equals("abd") || ulkeKontrol.equals("kanada")) {
                            vakitGoster1.putExtra("konum", hatirlaIlce);
                            vakitGoster1.putExtra("url", hatirlaURL);

                            secimimiKaydet("konumKayit", hatirlaIlce);
                            secimimiKaydet("urlKayit", hatirlaURL);
                        } else {
                            vakitGoster1.putExtra("konum", hatirlaIl);
                            vakitGoster1.putExtra("url", hatirlaURL);
                            secimimiKaydet("konumKayit", hatirlaIl);
                            secimimiKaydet("urlKayit", hatirlaURL);
                        }
                        ilkGiris = "girildi";
                        secimimiKaydet("ilkGiris", ilkGiris);
                        preferences.registerOnSharedPreferenceChangeListener(this);
                        startActivity(vakitGoster1);
                    }
                    break;*/

            }



    }

    public void secimimiKaydet(String id, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(id,value);
        editor.apply();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}
