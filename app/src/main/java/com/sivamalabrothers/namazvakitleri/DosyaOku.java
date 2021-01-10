package com.sivamalabrothers.namazvakitleri;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DosyaOku{

    private Context context;

    public DosyaOku(Context context){
        this.context = context;
    }

    public ArrayList<String> dosyadanyukle(String okunacakDosya){
        String dosyaYolu = "dualar/"+okunacakDosya;
        BufferedReader okumaTamponu = null;
        try{

                AssetManager manager = context.getAssets();

            okumaTamponu = new BufferedReader(
                        new InputStreamReader(manager.open(dosyaYolu), "UTF-8"));

                ArrayList<String> datalar = new ArrayList<String>();
                String satir = okumaTamponu.readLine();
                int i = 1;
                while (satir != null) {

                    if (!satir.equals("")) {
                        satir = i+"-"+satir;
                        datalar.add(satir);
                    }
                    satir = okumaTamponu.readLine();
                    i++;

                }
            okumaTamponu.close();
            return datalar;


        }catch(FileNotFoundException ex){
           ex.printStackTrace();
        }
        catch(IOException ex1){
            ex1.printStackTrace();
        }finally {
            if (okumaTamponu != null) {
                try {
                    okumaTamponu.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return null;

    }


}
