package com.sivamalabrothers.namazvakitleri;


import java.util.ArrayList;

public class GirisMenuItem {

    private String adi;
    private int imgId;

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public static ArrayList<GirisMenuItem> getGirisMenuItems(){

        ArrayList<GirisMenuItem> girisMenuItemArrayList = new ArrayList<GirisMenuItem>();
        int[] resimler={

                android.R.drawable.ic_menu_compass,
                android.R.drawable.ic_dialog_map,
                android.R.drawable.ic_menu_my_calendar,
                R.drawable.ic_menu_manage,
                android.R.drawable.ic_menu_my_calendar,
                R.drawable.ic_vol_type_speaker_dark,
                android.R.drawable.ic_menu_agenda


        };


    /*    String [] menuItems = {

                "KAYDET",
                "SÖZLÜK",
                "ARA",
                "SİL",
                "GÜNCELLE"


        };*/

        for(int i=0; i< resimler.length; i++){
            GirisMenuItem girisMenuItem = new GirisMenuItem();
           // girisMenuItem.setAdi(menuItems[i]);
            girisMenuItem.setImgId(resimler[i]);

            girisMenuItemArrayList.add(girisMenuItem);
        }

        return girisMenuItemArrayList;
    }

}
