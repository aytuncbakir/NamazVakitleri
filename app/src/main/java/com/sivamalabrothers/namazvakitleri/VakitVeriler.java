package com.sivamalabrothers.namazvakitleri;



public class VakitVeriler {

    String konum;
    String url;


    public VakitVeriler() {

    }

    public VakitVeriler(String konum, String url) {
        this.konum = konum;
        this.url = url;
    }

    public String getKonum() {
        return konum;
    }

    public void setKonum(String konum) {
        this.konum = konum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
