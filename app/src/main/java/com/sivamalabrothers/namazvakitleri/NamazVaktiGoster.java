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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class NamazVaktiGoster extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, View.OnClickListener {

    private ProgressDialog progressDialog;
    TextView hucre1,hucre2,hucre3,hucre4,hucre5,hucre6;
    Button ayetButon, hadisButon, sunnetButon;
    TextView ayetHadisSunnet;

    private static int ayetHadisSunnetCount = 0;

    ArrayList<String> gunlukNamazVakitleri;
    ArrayList<String> gunlukKibleGunesBilgiler;

    Bundle gelenVeri;
    static String URL = "";
    String konum = "";
    String hangiAydayiz = "";
    String hangiGundeyiz = "";
    private boolean vakitYatsiyiGectiMi = false;

    TextView ayyilMiladi,ayyilHicri;
    SharedPreferences preferences, ayarlar;
    RelativeLayout arkaplan;

    RecyclerView recyclerView;

    NamazVakitleri namazVakitleri;

    String gun_ay_yil = "";
    ArrayList<String> gunayyil = new ArrayList<>();

    String gun_ay_yil_hicri = "";
    ArrayList<String> gunayyilhicri = new ArrayList<>();


    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    TextView mTextViewCountDown;
    String timeVakit = "";
    String yatsiVakit = "";
    int yatsiHour, yatsiMinute;
    int timeHour, timeMinute;

    private static final String REKLAM_ID = "ca-app-pub-3183404528711365/6199041951";
    private static final String REKLAM_ID1 = "ca-app-pub-3183404528711365/5431635635";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.namaz_vakitleri_goster);

        initViews();
        animasyonUygula();
        reklam_yukle();

        GirisMenuCustomAdapter girisMenuCustomAdapter =
                new GirisMenuCustomAdapter(this,GirisMenuItem.getGirisMenuItems());
        recyclerView.setAdapter(girisMenuCustomAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        FetchGunlukBilgiler ft = new FetchGunlukBilgiler();
        if(checkInternet())
           ft.execute();

        setGunAyYil();

        if(checkInternet())
            new FetchHicri().execute();

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

    public  void tiklananMenuItem(int position) {
        String uygulamaLinki = "";
        //Toast.makeText(getApplicationContext(),position+"",Toast.LENGTH_LONG).show();

        if(position == 0){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                krn.putExtra("position",position);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                krn.putExtra("position",position);
                startActivity(krn);
            }

        }else if(position == 1){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                krn.putExtra("position",position);
                krn.putExtra("neredenGeliyor","Diger");
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVakitleri.class);
                krn.putExtra("position",position);
                krn.putExtra("neredenGeliyor","Diger");
                startActivity(krn);
            }

        }else if(position == 2){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),HaftalikNamazVaktileriGoster.class);
                krn.putExtra("position",position);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),HaftalikNamazVaktileriGoster.class);
                krn.putExtra("position",position);
                startActivity(krn);
            }

        }else if(position == 3){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                krn.putExtra("position",position);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                krn.putExtra("position",position);
                startActivity(krn);
            }

        }else if(position == 4){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                krn.putExtra("position",position);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                krn.putExtra("position",position);
                startActivity(krn);
            }

        }else if(position == 5){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                krn.putExtra("position",position);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                krn.putExtra("position",position);
                startActivity(krn);
            }

        }else if(position == 6){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),MubarekGunVeGeceler.class);
                krn.putExtra("position",position);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),MubarekGunVeGeceler.class);
                krn.putExtra("position",position);
                startActivity(krn);
            }

        }

    }


    private String setTimeInput(int index){
        String str;
        setTimeVakit(index);
        //Toast.makeText(NamazVaktiGoster.this,"Timevakit: "+timeVakit,Toast.LENGTH_LONG).show();
        splitTimeVakit(timeVakit);

        int sumMinute = saatDakikaAyarla(index);
        str = String.valueOf(sumMinute);

        return str;
    }

    private int saatDakikaAyarla(int index){

        int hour = getPhoneTimeHour();
        int minute = getPhoneTimeMinute();

       // Toast.makeText(NamazVaktiGoster.this,"saat:"+timeHour+"."+timeMinute,Toast.LENGTH_LONG).show();

        int hValue = 0;
        int mValue = 0;



        if(hour == 0)
            hour = 24;


            if (hour <= timeHour) {
                if (minute <= timeMinute) {
                    hValue = timeHour - hour;
                    mValue = timeMinute - minute;
                } else {
                    hValue = timeHour - hour - 1;
                    mValue = 60 + timeMinute - minute;
                }

            }

            //Toast.makeText(getApplicationContext()," hour: "+hour+" index: "+index,Toast.LENGTH_LONG).show();

        if (vakitYatsiyiGectiMi){

            hour = 0;
            if (hour <= timeHour) {
                if (minute <= timeMinute) {
                    hValue = timeHour - hour;
                    mValue = timeMinute - minute;
                } else {
                    hValue = timeHour - hour - 1;
                    mValue = 60 + timeMinute - minute;
                }

            }

        }


        int sumMinute = hValue * 60 + mValue;
        //Toast.makeText(NamazVaktiGoster.this,"Timevakit: "+sumMinute,Toast.LENGTH_LONG).show();
        return sumMinute;
    }

    private void setTimeVakit(int index){
        switch (index){
            case 0:
               timeVakit = gunlukNamazVakitleri.get(3);
                break;
            case 1:
                timeVakit = gunlukNamazVakitleri.get(5);
                break;
            case 2:
                timeVakit = gunlukNamazVakitleri.get(7);
                break;
            case 3:
                timeVakit = gunlukNamazVakitleri.get(9);
                break;
            case 4:
                timeVakit = gunlukNamazVakitleri.get(11);
                timeVakitAyarla(timeVakit);
                break;
            case 5:
                timeVakit = gunlukNamazVakitleri.get(1);
                break;
            default:
                timeVakit = gunlukNamazVakitleri.get(1);

        }
    }

    private void timeVakitAyarla(String s){
        String tmp,tmp1;
        tmp1 = s;
        tmp1 = tmp1.replace(':','0');
        int tmpVakit = Integer.parseInt(tmp1);
        if(tmpVakit < 60){
           tmp = s;
            tmp = tmp.replace(':','3');
            int x = Integer.parseInt(tmp);
            x = 24000 + x;
            String y = String.valueOf(x);

            y = y.replace('3',':');
            timeVakit = y;
        }


    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();
        mTimerRunning = true;
        updateWatchInterface();
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }
    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }
    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        mTextViewCountDown.setText(timeLeftFormatted);
    }
    private void updateWatchInterface() {
        if (mTimerRunning) {

        } else {

            if (mTimeLeftInMillis < 1000) {

            } else {

            }
            if (mTimeLeftInMillis < mStartTimeInMillis) {

            } else {

            }
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        /*
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }*/
    }
    @Override
    protected void onStart() {
        super.onStart();
        /*
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateWatchInterface();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                    mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }*/
    }

    private int getPhoneTimeHour(){
        Calendar instance = Calendar.getInstance();
        int hour = instance.get(Calendar.HOUR_OF_DAY);
       return hour;

    }

    private int getPhoneTimeMinute(){
        Calendar instance = Calendar.getInstance();

        int minute = instance.get(Calendar.MINUTE);
        return minute;
    }

    private String getCurrentTime(){

            Calendar cc = Calendar.getInstance();

            int mHour = cc.get(Calendar.HOUR_OF_DAY);
            int mMinute = cc.get(Calendar.MINUTE);

            //Toast.makeText(getApplicationContext(),"saat:"+mHour,Toast.LENGTH_LONG).show();

            if(mHour == 0)
                mHour = 24;
            mHour = mHour * 1000;
            mHour = mHour + mMinute;


        return String.valueOf(mHour);
    }

    private void getCurrentDate(){

        Date now = new Date();
        Date alsoNow = Calendar.getInstance().getTime();
        String nowAsString = new SimpleDateFormat("yyyy-MM-dd").format(now);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String gun = "";

        switch (day) {

            case Calendar.MONDAY:
                gun = "Pazartesi";
                break;
            case Calendar.TUESDAY:
                gun = "Salı";
                break;
            case Calendar.WEDNESDAY:
                gun = "Çarşamba";
                break;
            case Calendar.THURSDAY:
                gun = "Perşembe";
                break;
            case Calendar.FRIDAY:
                gun = "Cuma";
                break;
            case Calendar.SATURDAY:
                gun = "Cumartesi";
                break;
            case Calendar.SUNDAY:
                gun = "Pazar";
                break;
             default:
                    gun = "";
                break;
        }

        nowAsString = nowAsString+"-"+gun;
        gunAyYilKaydet("gun_ay_yil",nowAsString);

    }

    @SuppressLint("SetTextI18n")
    private void setGunAyYil() {
        getCurrentDate();

        gun_ay_yil =  preferences.getString("gun_ay_yil", "");
        if (!gun_ay_yil.equals("")) {

            gunayyil = splitDate(gun_ay_yil);
            if (gunayyil.size()>0) {
                hangiGundeyiz = String.valueOf(gunayyil.get(3));
                String cevrilenAy = aySayidanYaziyaCevir(gunayyil.get(1));
                String cevrilenTarih = gunayyil.get(2)+" "+cevrilenAy+" "+gunayyil.get(3)+" "+gunayyil.get(0);
                ayyilMiladi.setText(cevrilenTarih);

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setGunAyYilHicri() {

        gun_ay_yil_hicri =  preferences.getString("gun_ay_yil_hicri", "");
        if (!gun_ay_yil_hicri.equals("")) {
            gunayyilhicri = splitDateHicri(gun_ay_yil_hicri);
            if (gunayyilhicri.size()>0) {
                ayyilHicri.setText(gun_ay_yil_hicri);
            }
        }
    }

    private String aySayidanYaziyaCevir(String data){

        if(data.length() != 0) {

            switch (data){
                case "01":
                    data = "Ocak";
                    break;
                case "02":
                    data = "Şubat";
                    break;
                case "03":
                    data = "Mart";
                    break;
                case "04":
                    data = "Nisan";
                    break;
                case "05":
                    data = "Mayıs";
                    break;
                case "06":
                    data = "Haziran";
                    break;
                case "07":
                    data = "Temmuz";
                    break;
                case "08":
                    data = "Ağustos";
                    break;
                case "09":
                    data = "Eylül";
                    break;
                case "10":
                    data = "Ekim";
                    break;
                case "11":
                    data = "Kasım";
                    break;
                case "12":
                    data = "Aralık";
                    break;
                default:
                    data = "Ocak";
            }
            hangiAydayiz = data;
        }
        return data;
    }

    private   ArrayList<String> splitDateHicri(String satir){

        ArrayList<String> aList = new ArrayList<>();
        satir = satir.trim();

        String[] str;
        int i = 0;
        str = satir.split(" ");
        while(i < str.length) {
            aList.add(str[i]);
            i++;
        }
        return aList;
    }

    private void splitTimeVakit(String time){

        if(!time.equals(""))
            timeVakit = time.trim();
        else
            timeVakit = "12:00";

            String[] str;
            int i = 0;
            str = timeVakit.split(":");
            while (i < str.length) {
                if (i == 0){
                    timeHour = Integer.parseInt(str[i]);
                }
                else if (i == 1)
                    timeMinute = Integer.parseInt(str[i]);
                i++;
            }
    }



    private   ArrayList<String> splitDate(String satir){

        ArrayList<String> aList = new ArrayList<>();
        satir = satir.trim();

        String[] str;
        int i = 0;
        str = satir.split("-");
        while(i < str.length) {
            aList.add(str[i]);
            i++;
        }
        return aList;
    }

    private void animasyonUygula(){
        if(Build.VERSION.SDK_INT >=21){
            Slide enterTransition = new Slide();
            enterTransition.setDuration(400);
            enterTransition.setSlideEdge(Gravity.BOTTOM);
            getWindow().setEnterTransition(enterTransition);
        }
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
            new AlertDialog.Builder(NamazVaktiGoster.this)
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
        namazVakitleri = new NamazVakitleri();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ayarlar = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ayarlar.registerOnSharedPreferenceChangeListener(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        recyclerView = findViewById(R.id.recyclerview_giris);

        ayyilMiladi = findViewById(R.id.ayyilMiladi);
        ayyilHicri = findViewById(R.id.ayyilHicri);

        ayetButon = findViewById(R.id.ayetButon);
        ayetButon.setOnClickListener(this);
        hadisButon = findViewById(R.id.hadisButon);
        hadisButon.setOnClickListener(this);
        sunnetButon = findViewById(R.id.sunnetButon);
        sunnetButon.setOnClickListener(this);

        ayetHadisSunnet = findViewById(R.id.ayetHadisSunnet);
        ayetHadisSunnet.setMovementMethod(new ScrollingMovementMethod());
        initialSetAyetHadisSunnet();


        mTextViewCountDown =  findViewById(R.id.mTextViewCountDown);

        hucre1 = findViewById(R.id.hucre1);
        hucre2 = findViewById(R.id.hucre2);
        hucre3 = findViewById(R.id.hucre3);
        hucre4 = findViewById(R.id.hucre4);
        hucre5 = findViewById(R.id.hucre5);
        hucre6 = findViewById(R.id.hucre6);

        gelenVeri = getIntent().getExtras();
        if(gelenVeri != null){
            URL = gelenVeri.getString("url");
            konum = gelenVeri.getString("konum");
        }else{
            URL = preferences.getString("urlKayit","");
            konum = preferences.getString("konumKayit","");
        }
        //Toast.makeText(NamazVaktiGoster.this,URL,Toast.LENGTH_LONG).show();
        konum = splitDataString(URL);
        konum = splitKonum(konum);

        String str = konum;
        String konumTmp = str.substring(0,1).toUpperCase()+str.substring(1).toLowerCase();
        konum = konumTmp;
        arkaplan = findViewById(R.id.arkaplan);

    }

    private void initialSetAyetHadisSunnet() {
        String[] initialDatalar = new String[0];
        initialDatalar = getResources().getStringArray(R.array.ayetler);
        ayetButon.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
        ayetHadisSunnet.setText(initialDatalar[ayetHadisSunnetCount]);


    }

    private void setAyetHadisSunnet(int secenek){

        String[] datalar = new String[0];
        if(secenek == 0) {
            datalar = getResources().getStringArray(R.array.ayetler);
        }else  if(secenek == 1) {
            datalar = getResources().getStringArray(R.array.hadisler);
        }else  if(secenek == 2) {
            datalar =  getResources().getStringArray(R.array.sunnetler);
        }

        if(ayetHadisSunnetCount == datalar.length)
            ayetHadisSunnetCount = 0;
        else
            ayetHadisSunnetCount++;

        if(secenek == 2) {
            ayetHadisSunnet.setText("Efendimizin (SAV) Sünneti: " + datalar[ayetHadisSunnetCount]);

        }else {
            ayetHadisSunnet.setText(datalar[ayetHadisSunnetCount]);

        }

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

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.ayetButon:
                setAyetHadisSunnet(0);
                ayetButon.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                hadisButon.setBackgroundColor(getResources().getColor(R.color.color4));
                sunnetButon.setBackgroundColor(getResources().getColor(R.color.color4));
                break;

            case R.id.hadisButon:
                setAyetHadisSunnet(1);
                ayetButon.setBackgroundColor(getResources().getColor(R.color.color4));
                hadisButon.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                sunnetButon.setBackgroundColor(getResources().getColor(R.color.color4));
                break;

            case R.id.sunnetButon:
                setAyetHadisSunnet(2);
                ayetButon.setBackgroundColor(getResources().getColor(R.color.color4));
                hadisButon.setBackgroundColor(getResources().getColor(R.color.color4));
                sunnetButon.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));

                break;
        }

    }


    private class FetchHicri extends AsyncTask<Void, Void, Void> {

        String gunHicri =  "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NamazVaktiGoster.this);
            progressDialog.setTitle(Html.fromHtml(getResources().getString(R.string.html_progresbaslik)));
            progressDialog.setMessage(Html.fromHtml(getResources().getString(R.string.html_progresmetin)));
            progressDialog.setIndeterminate(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try{
                Document doc  = Jsoup.connect(URL).get();    // web siteye bağlantıyı gerçeleştirme
                Elements  elementsHicri = doc.select("div[class=ti-hicri]");
                // table[class=table vakit-table] class ismitable vakit-table olan verileri çekmek için
                gunHicri = elementsHicri.text();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            gunAyYilKaydet("gun_ay_yil_hicri",gunHicri);
            setGunAyYilHicri();
            progressDialog.dismiss();
        }

    }

    private class FetchGunlukBilgiler extends AsyncTask<Void, Void, Void> {

        String gunlukBilgiler =  "";
        String gunlukVakitler = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try{

                Document doc  = Jsoup.connect(URL).get();    // web siteye bağlantıyı gerçeleştirme
                Elements elementsGun = doc.select("div[class=today-day-info-container]");
                // table[class=table vakit-table] class ismitable vakit-table olan verileri çekmek için
                gunlukBilgiler = elementsGun.text();

                Elements  elementsGunVakit = doc.select("div[class=today-pray-times]");
                // table[class=table vakit-table] class ismitable vakit-table olan verileri çekmek için
                gunlukVakitler = elementsGunVakit.text();

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setGunlukBilgiler();

            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            String appName = getResources().getString(R.string.app_name);
            actionBar.setTitle(konum+" "+appName);

        }

        private void setGunlukBilgiler(){

            if(gunlukBilgiler.equals("")){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                startActivity(browserIntent);
            }

            if(!gunlukBilgiler.equals(""))
                gunlukKibleGunesBilgiler = splitData(gunlukBilgiler);
            else{
                gunlukKibleGunesBilgiler = new ArrayList<>();
                gunlukKibleGunesBilgiler.add("Data");
            }

            if(gunlukKibleGunesBilgiler.size()>=12){
                kibleKaydet("kible",gunlukKibleGunesBilgiler.get(2));
            }

            if(!gunlukVakitler.equals(""))
                gunlukNamazVakitleri = splitData(gunlukVakitler);
            else{
                gunlukNamazVakitleri = new ArrayList<>();
                gunlukNamazVakitleri.add("Data");
            }

            int index = 0;
            String currentTime;
            if(gunlukNamazVakitleri.size() >= 12) {

                hucre1.setText(gunlukNamazVakitleri.get(1));
                hucre2.setText(gunlukNamazVakitleri.get(3));
                hucre3.setText(gunlukNamazVakitleri.get(5));
                hucre4.setText(gunlukNamazVakitleri.get(7));
                hucre5.setText(gunlukNamazVakitleri.get(9));
                hucre6.setText(gunlukNamazVakitleri.get(11));

                currentTime = getCurrentTime();
                //Toast.makeText(NamazVaktiGoster.this,currentTime,Toast.LENGTH_LONG).show();
                index = setColorCurrentTime(currentTime);
                setHucreColor(index);

            }

            String input = setTimeInput(index);
            //Toast.makeText(NamazVaktiGoster.this,"input:"+input,Toast.LENGTH_LONG).show();
            long millisInput = Long.parseLong(input) * 60000;
            setTime(millisInput);
            startTimer();
        }

        private void setHucreColor(int index){
            switch (index){
                case 0:
                    hucre1.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                    break;
                case 1:
                    hucre2.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                    break;
                case 2:
                    hucre3.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                    break;
                case 3:
                    hucre4.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                    break;
                case 4:
                    hucre5.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                    break;
                case 5:
                    hucre6.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));
                    break;
                default:
                    hucre1.setBackgroundColor(getResources().getColor(R.color.gecerliVakitColor));

            }
        }

        private  int setColorCurrentTime(String time){

            int crrntTime = Integer.parseInt(time);

            ArrayList<Integer> currentTimes = setCurrentTime();

            int index = 0;

                for(int i = 0 ; i< currentTimes.size(); i++) {
                    if (crrntTime < currentTimes.get(i)){
                        index = i - 1;
                        vakitYatsiyiGectiMi = false;
                        break;
                    }
                }

                if(crrntTime > currentTimes.get(currentTimes.size()-1)){
                    index = 5;
                    vakitYatsiyiGectiMi = true;
                }

                if(index == -1)
                    index = 5;



            //Toast.makeText(NamazVaktiGoster.this,"index: "+index+ " Tel saat :"+crrntTime+ " Vakit :"+currentTimes.get(currentTimes.size()-1),Toast.LENGTH_LONG).show();

            return index;
        }

        private ArrayList<Integer> setCurrentTime(){

            ArrayList<Integer> currentTimes = new ArrayList<>();

            String tmp = gunlukNamazVakitleri.get(1);
            for(int i = 1 ; i<12; i = i + 2){
                tmp = gunlukNamazVakitleri.get(i);
                tmp = tmp.replace(':','0');
                //Toast.makeText(NamazVaktiGoster.this,tmp,Toast.LENGTH_LONG).show();
                if(i == 11) {
                    int hour24 = Integer.parseInt(tmp);
                    hour24 = 24000 + hour24;
                    currentTimes.add(hour24);
                }
                else
                    currentTimes.add(Integer.parseInt(tmp));
                tmp = "";
            }
            return  currentTimes;

        }

    }

    private ArrayList<String> splitData(String data){
        ArrayList<String> splitDatam = new ArrayList<String>();
        if(data.length() != 0) {
            String[] arrOfStr = data.split(" ");
            Collections.addAll(splitDatam, arrOfStr);
        }
        return splitDatam;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    public void gunAyYilKaydet(String id, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(id,value);
        editor.apply();
    }


    public void kibleKaydet(String id, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(id,value);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_namaz_goster,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.ayarlar){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Ayarlar.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.pusula){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),Pusula.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.konum){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
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
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),EzanDinle.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.yenile){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),NamazVaktiGoster.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.aylik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn,options.toBundle());
            }else {
                Intent krn = new Intent(getApplicationContext(),AylikNamazVaktileriGoster.class);
                startActivity(krn);
            }
            return  true;
        }else if(id == R.id.haftalik){
            if(Build.VERSION.SDK_INT>=21 ){
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(NamazVaktiGoster.this);
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
