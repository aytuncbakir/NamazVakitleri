package com.sivamalabrothers.namazvakitleri;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

public class HaftalikNamazVaktileriGoster extends AppCompatActivity implements  SharedPreferences.OnSharedPreferenceChangeListener {


    private ProgressDialog progressDialog;
    ArrayList<String> aylikNamazVakitleri;
    ArrayList<String> imsakiyeAdapterVeri;
    static String URL = "";
    String konum = "";
    String aylikVeri = "";
    String hangiAydayiz = "";

    ArrayList<String> haftalikNamazVakitleri;

    SharedPreferences preferences, ayarlar;
    LinearLayout arkaplan;

    TextView h1,h2,h3,h4,h5,h6,h7;
    TextView h8,h9,h10,h11,h12,h13,h14;
    TextView h15,h16,h17,h18,h19,h20,h21;
    TextView h22,h23,h24,h25,h26,h27,h28;
    TextView h29,h30,h31,h32,h33,h34,h35;
    TextView h36,h37,h38,h39,h40,h41,h42;
    TextView h43,h44,h45,h46,h47,h48,h49;

    private static final int  CUSTOM_DIALOG_ID1 = 2;
    private static final int  CUSTOM_DIALOG_ID2 = 3;

    private InterstitialAd interstitial;
    //private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/2032247067";

    private static final String REKLAM_ID = "ca-app-pub-3183404528711365/1033171723";
    private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/3467763379";

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.haftalik_namaz_vakitleri);

        initViews();


        animasyonUygula();
       reklam_yukle();

        if(checkInternet()) {
            new FetchHaftalikBilgiler().execute();

        }// html sayfasından istediğimiz verileri  çekmek için



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
            enterTransition.setDuration(400);
            enterTransition.setSlideEdge(Gravity.BOTTOM);
            getWindow().setEnterTransition(enterTransition);
        }
    }

   /* private void reklam_yukle(){
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
    }*/


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private boolean checkInternet() {

        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(HaftalikNamazVaktileriGoster.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Internet bağlantınızı kontrol ediniz.")
                    .setPositiveButton("OK", null).show();
            return false;
        }else{
            return true;
        }
    }

    private  String splitKonum(String satir){

        ArrayList<String> aList = new ArrayList<>();
        satir = satir.trim();

        String[] str;
        int i = 0;
        str = satir.split("-");
        while(i < str.length) {
            aList.add(str[i]);
            i++;

        }
        satir = aList.get(0);
        return satir;
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {

        arkaplan = findViewById(R.id.arkaplan);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ayarlar.registerOnSharedPreferenceChangeListener(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        URL = preferences.getString("urlKayit","");
        konum = preferences.getString("konumKayit","");


        konum = splitDataString(URL);
        konum = splitKonum(konum);

        String str = konum;
        String konumTmp = str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
        konum = konumTmp;

        String appName = getResources().getString(R.string.app_name);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(konum+" "+appName);


       TextView sehirAdi = findViewById(R.id.sehirAdi);

        sehirAdi = findViewById(R.id.sehirAdi);
        sehirAdi.setText(konum+" Haftalik Namaz Vakitleri");


        h1= findViewById(R.id.h1);
        h2= findViewById(R.id.h2);
        h3= findViewById(R.id.h3);
        h4= findViewById(R.id.h4);
        h5= findViewById(R.id.h5);
        h6= findViewById(R.id.h6);
        h7= findViewById(R.id.h7);

        h8= findViewById(R.id.h8);
        h9= findViewById(R.id.h9);
        h10= findViewById(R.id.h10);
        h11= findViewById(R.id.h11);
        h12= findViewById(R.id.h12);
        h13= findViewById(R.id.h13);
        h14= findViewById(R.id.h14);

        h15= findViewById(R.id.h15);
        h16= findViewById(R.id.h16);
        h17= findViewById(R.id.h17);
        h18= findViewById(R.id.h18);
        h19= findViewById(R.id.h19);
        h20= findViewById(R.id.h20);
        h21= findViewById(R.id.h21);

        h22= findViewById(R.id.h22);
        h23= findViewById(R.id.h23);
        h24= findViewById(R.id.h24);
        h25= findViewById(R.id.h25);
        h26= findViewById(R.id.h26);
        h27= findViewById(R.id.h27);
        h28= findViewById(R.id.h28);

        h29= findViewById(R.id.h29);
        h30= findViewById(R.id.h30);
        h31= findViewById(R.id.h31);
        h32= findViewById(R.id.h32);
        h33= findViewById(R.id.h33);
        h34= findViewById(R.id.h34);
        h35= findViewById(R.id.h35);

        h36= findViewById(R.id.h36);
        h37= findViewById(R.id.h37);
        h38= findViewById(R.id.h38);
        h39= findViewById(R.id.h39);
        h40= findViewById(R.id.h40);
        h41= findViewById(R.id.h41);
        h42= findViewById(R.id.h42);

        h43= findViewById(R.id.h43);
        h44= findViewById(R.id.h44);
        h45= findViewById(R.id.h45);
        h46= findViewById(R.id.h46);
        h47= findViewById(R.id.h47);
        h48= findViewById(R.id.h48);
        h49= findViewById(R.id.h49);





    }

    private  String splitDataString(String satir){

        ArrayList<String> aList = new ArrayList<>();
        satir.trim();

        String str[];
        int i = 0;

        str = satir.split("/");
        while(i < str.length) {
            aList.add(str[i]);
            i++;
        }
        satir = aList.get(aList.size()-1);
        return satir;
    }


    private ArrayList<String> splitData(String data){
        ArrayList<String> splitDatam = new ArrayList<String>();
        if(data.length() != 0) {
            String[] arrOfStr = data.split(" ");
            Collections.addAll(splitDatam, arrOfStr);
        }
        return splitDatam;
    }



    private class FetchHaftalikBilgiler extends AsyncTask<Void, Void, Void> {

        String haftalikVakitler = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try{

                Document doc  = Jsoup.connect(URL).get();    // web siteye bağlantıyı gerçeleştirme
                Elements  elementsHafta = doc.select("div[id=tab-0]");  // table[class=table vakit-table] class ismitable vakit-table olan verileri çekmek için
                haftalikVakitler = elementsHafta.text();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            setHaftalikBilgiler();


        }



        private void  setHaftalikBilgiler(){

            haftalikNamazVakitleri = splitData(haftalikVakitler);

            // XXX için namaz vakitleri imsat sabah öğle ikindi akşam yatsı  13 kelimeyi temizle
            for(int i=0; i<13; i++){
                if(haftalikNamazVakitleri.size()>0)
                    haftalikNamazVakitleri.remove(0);
            }

            ArrayList<String> haftalikNamazVakitleriAdapterVeri = new ArrayList<>();


            String x = "";
            for(int i=0; i<haftalikNamazVakitleri.size(); i++){
                if(!haftalikNamazVakitleri.get(i).contains(":")){
                    x =  haftalikNamazVakitleri.get(i)+ "."+ayDonustur(haftalikNamazVakitleri.get(i+1))
                            + "."+haftalikNamazVakitleri.get(i+2);

                    haftalikNamazVakitleriAdapterVeri.add(x);
                    for(int j = 0; j<4; j++)
                        haftalikNamazVakitleri.remove(i);

                    haftalikNamazVakitleriAdapterVeri.add(haftalikNamazVakitleri.get(i));
                }else
                    haftalikNamazVakitleriAdapterVeri.add(haftalikNamazVakitleri.get(i));
            }

            haftalikNamazVakitleri = haftalikNamazVakitleriAdapterVeri;

            //Toast.makeText(getApplicationContext(),x+"",Toast.LENGTH_LONG).show();



            if (haftalikNamazVakitleri.size() >= 49) {
                h1.setText(haftalikNamazVakitleri.get(0));
                h2.setText(haftalikNamazVakitleri.get(1));
                h3.setText(haftalikNamazVakitleri.get(2));
                h4.setText(haftalikNamazVakitleri.get(3));
                h5.setText(haftalikNamazVakitleri.get(4));
                h6.setText(haftalikNamazVakitleri.get(5));
                h7.setText(haftalikNamazVakitleri.get(6));

                h8.setText(haftalikNamazVakitleri.get(7));
                h9.setText(haftalikNamazVakitleri.get(8));
                h10.setText(haftalikNamazVakitleri.get(9));
                h11.setText(haftalikNamazVakitleri.get(10));
                h12.setText(haftalikNamazVakitleri.get(11));
                h13.setText(haftalikNamazVakitleri.get(12));
                h14.setText(haftalikNamazVakitleri.get(13));

                h15.setText(haftalikNamazVakitleri.get(14));
                h16.setText(haftalikNamazVakitleri.get(15));
                h17.setText(haftalikNamazVakitleri.get(16));
                h18.setText(haftalikNamazVakitleri.get(17));
                h19.setText(haftalikNamazVakitleri.get(18));
                h20.setText(haftalikNamazVakitleri.get(19));
                h21.setText(haftalikNamazVakitleri.get(20));

                h22.setText(haftalikNamazVakitleri.get(21));
                h23.setText(haftalikNamazVakitleri.get(22));
                h24.setText(haftalikNamazVakitleri.get(23));
                h25.setText(haftalikNamazVakitleri.get(24));
                h26.setText(haftalikNamazVakitleri.get(25));
                h27.setText(haftalikNamazVakitleri.get(26));
                h28.setText(haftalikNamazVakitleri.get(27));

                h29.setText(haftalikNamazVakitleri.get(28));
                h30.setText(haftalikNamazVakitleri.get(29));
                h31.setText(haftalikNamazVakitleri.get(30));
                h32.setText(haftalikNamazVakitleri.get(31));
                h33.setText(haftalikNamazVakitleri.get(32));
                h34.setText(haftalikNamazVakitleri.get(33));
                h35.setText(haftalikNamazVakitleri.get(34));

                h36.setText(haftalikNamazVakitleri.get(35));
                h37.setText(haftalikNamazVakitleri.get(36));
                h38.setText(haftalikNamazVakitleri.get(37));
                h39.setText(haftalikNamazVakitleri.get(38));
                h40.setText(haftalikNamazVakitleri.get(39));
                h41.setText(haftalikNamazVakitleri.get(40));
                h42.setText(haftalikNamazVakitleri.get(41));

                h43.setText(haftalikNamazVakitleri.get(42));
                h44.setText(haftalikNamazVakitleri.get(43));
                h45.setText(haftalikNamazVakitleri.get(44));
                h46.setText(haftalikNamazVakitleri.get(45));
                h47.setText(haftalikNamazVakitleri.get(46));
                h48.setText(haftalikNamazVakitleri.get(47));
                h49.setText(haftalikNamazVakitleri.get(48));

            }

        }

    }

    private String ayDonustur(String data){

        if(data.length() != 0) {
            hangiAydayiz = data;
            switch (data){
                case "Ocak":
                    data = "01";
                    break;
                case "Şubat":
                    data = "02";
                    break;
                case "Mart":
                    data = "03";
                    break;
                case "Nisan":
                    data = "04";
                    break;
                case "Mayıs":
                    data = "05";
                    break;
                case "Haziran":
                    data = "06";
                    break;
                case "Temmuz":
                    data = "07";
                    break;
                case "Ağustos":
                    data = "08";
                    break;
                case "Eylül":
                    data = "09";
                    break;
                case "Ekim":
                    data = "10";
                    break;
                case "Kasım":
                    data = "11";
                    break;
                case "Aralık":
                    data = "12";
                    break;
                default:
                    data = "01";
                    break;
            }

        }
        return data;
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_aylik_namaz_goster,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ayarlar){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HaftalikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn);
            }
            return  true;

        }else if(id == R.id.anasayfa){

            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HaftalikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.pusula){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HaftalikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.konum){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HaftalikNamazVaktileriGoster.this);
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
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HaftalikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.yenile){
            if(checkInternet())
                new FetchHaftalikBilgiler().execute();

            return  true;
        }else if(id == R.id.aylik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(HaftalikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn);
            }

            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


}
