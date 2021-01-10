package com.sivamalabrothers.namazvakitleri;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class AylikNamazVaktileriGoster extends AppCompatActivity implements  SharedPreferences.OnSharedPreferenceChangeListener {


    private ProgressDialog progressDialog;
    ArrayList<String> aylikNamazVakitleri;
    ArrayList<String> imsakiyeAdapterVeri;
    static String URL = "";
    String konum = "";
    String aylikVeri = "";
    String hangiAydayiz = "";

    SharedPreferences preferences, ayarlar;
    LinearLayout arkaplan;

    TextView sehirAdi;
    TextView header;
    TextView r1,r2,r3,r4,r5,r6,r7,r8,r9,r10;
    TextView r11,r12,r13,r14,r15,r16,r17,r18,r19,r20;
    TextView r21,r22,r23,r24,r25,r26,r27,r28,r29,r30,r31;

    ArrayList<TextView> satirlar;




    private static final int  CUSTOM_DIALOG_ID1 = 2;
    private static final int  CUSTOM_DIALOG_ID2 = 3;

    private InterstitialAd interstitial;
    // private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/2032247067";

    private static final String REKLAM_ID = "ca-app-pub-3183404528711365/6362693486";
    private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/2231876788";

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aylik_namaz_vakitleri);


        initViews();


        animasyonUygula();
        reklam_yukle();

        if(checkInternet()) {
            new FetchAylikBilgiler().execute();

        }// html sayfasından istediğimiz verileri  çekmek için



    }


    private void animasyonUygula(){
        if(Build.VERSION.SDK_INT >=21){
            Slide enterTransition = new Slide();
            enterTransition.setDuration(400);
            enterTransition.setSlideEdge(Gravity.BOTTOM);
            getWindow().setEnterTransition(enterTransition);
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




    private boolean checkInternet() {

        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            new AlertDialog.Builder(AylikNamazVaktileriGoster.this)
                    .setTitle(getResources().getString(R.string.app_name))
                    .setMessage("Internet bağlantınızı kontrol ediniz.")
                    .setPositiveButton("OK", null).show();
            return false;
        }else{
            return true;
        }
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

        satirlar = new ArrayList<TextView>();

        sehirAdi = findViewById(R.id.sehirAdi);
        sehirAdi.setText(konum+" Aylık Namaz Vakitleri");

        String appName = getResources().getString(R.string.app_name);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(konum+" "+appName);

        header = findViewById(R.id.header);

        r1 = findViewById(R.id.row1);
        satirlar.add(r1);
        r2 = findViewById(R.id.row2);
        satirlar.add(r2);
        r3 = findViewById(R.id.row3);
        satirlar.add(r3);
        r4 = findViewById(R.id.row4);
        satirlar.add(r4);
        r5 = findViewById(R.id.row5);
        satirlar.add(r5);
        r6 = findViewById(R.id.row6);
        satirlar.add(r6);
        r7 = findViewById(R.id.row7);
        satirlar.add(r7);
        r8 = findViewById(R.id.row8);
        satirlar.add(r8);
        r9 = findViewById(R.id.row9);
        satirlar.add(r9);
        r10 = findViewById(R.id.row10);
        satirlar.add(r10);


        r11 = findViewById(R.id.row11);
        satirlar.add(r11);
        r12 = findViewById(R.id.row12);
        satirlar.add(r12);
        r13 = findViewById(R.id.row13);
        satirlar.add(r13);
        r14 = findViewById(R.id.row14);
        satirlar.add(r14);
        r15 = findViewById(R.id.row15);
        satirlar.add(r15);
        r16 = findViewById(R.id.row16);
        satirlar.add(r16);
        r17 = findViewById(R.id.row17);
        satirlar.add(r17);
        r18 = findViewById(R.id.row18);
        satirlar.add(r18);
        r19 = findViewById(R.id.row19);
        satirlar.add(r19);
        r20 = findViewById(R.id.row20);
        satirlar.add(r20);

        r21 = findViewById(R.id.row21);
        satirlar.add(r21);
        r22 = findViewById(R.id.row22);
        satirlar.add(r22);
        r23 = findViewById(R.id.row23);
        satirlar.add(r23);
        r24 = findViewById(R.id.row24);
        satirlar.add(r24);
        r25 = findViewById(R.id.row25);
        satirlar.add(r25);
        r26 = findViewById(R.id.row26);
        satirlar.add(r26);
        r27 = findViewById(R.id.row27);
        satirlar.add(r27);
        r28 = findViewById(R.id.row28);
        satirlar.add(r28);
        r29 = findViewById(R.id.row29);
        satirlar.add(r29);
        r30 = findViewById(R.id.row30);
        satirlar.add(r30);
        r31 = findViewById(R.id.row31);
        satirlar.add(r31);



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


    private ArrayList<String> splitData(String data){
        ArrayList<String> splitDatam = new ArrayList<String>();
        if(data.length() != 0) {
            String[] arrOfStr = data.split(" ");
            Collections.addAll(splitDatam, arrOfStr);
        }
        return splitDatam;
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




    private class FetchAylikBilgiler extends AsyncTask<Void, Void, Void> {

        String aylikVakitler = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AylikNamazVaktileriGoster.this);
            progressDialog.setTitle(Html.fromHtml(getResources().getString(R.string.html_progresbaslik)));
            progressDialog.setMessage(Html.fromHtml(getResources().getString(R.string.html_progresmetin)));
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                //enableSSLSocket();
                //System.setProperty("jsse.enableSNIExtension","false");
                Document doc  = Jsoup.connect(URL).get();    // web siteye bağlantıyı gerçeleştirme
                Elements  elementsAy = doc.select("div[id=tab-1]");
                // table[class=table vakit-table] class ismitable vakit-table olan verileri çekmek için
                aylikVakitler = elementsAy.text();
                aylikVeri = aylikVakitler;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            setAylikBilgiler();
            showDialog(CUSTOM_DIALOG_ID1);
            progressDialog.dismiss();
        }

        private void  setAylikBilgiler(){

            aylikNamazVakitleri = splitData(aylikVakitler);
            // XXX için namaz vakitleri imsat sabah öğle ikindi akşam yatsı  13 kelimeyi temizle
            for(int i=0; i<13; i++){
                if(aylikNamazVakitleri.size()>0)
                    aylikNamazVakitleri.remove(0);
            }

            ArrayList<String> aylikNamazVakitleriAdapterVeri = new ArrayList<>();
            String x = "";
            for(int i=0; i<aylikNamazVakitleri.size(); i++){
                if(!aylikNamazVakitleri.get(i).contains(":")){
                    x =  aylikNamazVakitleri.get(i)+ "."+ayDonustur(aylikNamazVakitleri.get(i+1))
                            + "."+aylikNamazVakitleri.get(i+2);

                    aylikNamazVakitleriAdapterVeri.add(x);
                    for(int j = 0; j<4; j++)
                        if(aylikNamazVakitleri.size()>0)
                            aylikNamazVakitleri.remove(i);
                    aylikNamazVakitleriAdapterVeri.add(aylikNamazVakitleri.get(i));
                }else
                    aylikNamazVakitleriAdapterVeri.add(aylikNamazVakitleri.get(i));

            }

            aylikNamazVakitleri = aylikNamazVakitleriAdapterVeri;

            imsakiyeAdapterVeri = new ArrayList<String>();
            String satir = "";

            //Toast.makeText(getApplicationContext(),aylikNamazVakitleri.size()+"",Toast.LENGTH_LONG).show();


            if(aylikNamazVakitleri.size() != 0){

                for(int i = 0 ; i < aylikNamazVakitleri.size(); i++) {
                    satir = satir +"  "+aylikNamazVakitleri.get(i);
                    if(i % 7 == 6){
                        imsakiyeAdapterVeri.add(satir);
                        satir = "";
                    }
                }
            }

           satir = "  Miladi Tarih İmsak Güneş Öğle İkindi Akşam Yatsı ";
            header.setText(satir);

            for(int i = 0 ; i < imsakiyeAdapterVeri.size(); i++) {
               satirlar.get(i).setText(imsakiyeAdapterVeri.get(i));
            }




        }

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
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AylikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn);
            }
            return  true;

        }else if(id == R.id.anasayfa){

            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AylikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.pusula){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AylikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.konum){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AylikNamazVaktileriGoster.this);
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
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AylikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.yenile){
            if(checkInternet())
                new FetchAylikBilgiler().execute();

            return  true;
        }else if(id == R.id.aylik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AylikNamazVaktileriGoster.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn);
            }

            return  true;
        }else if(id == R.id.haftalik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AylikNamazVaktileriGoster.this);
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
