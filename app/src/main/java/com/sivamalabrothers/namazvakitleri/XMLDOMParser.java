package com.sivamalabrothers.namazvakitleri;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;

import android.util.Log;


public class XMLDOMParser {


    private ArrayList<String> ulkeler;
    private ArrayList<String> iller;
    private ArrayList<String> ilceler;
    private ArrayList<VakitVeriler> vakitVerilers;
    Context context;


    public XMLDOMParser() {

    }

    public XMLDOMParser(Context context) {
        this.context = context;
    }


    // ulke nodes
    static final String NODE_ULKE = "ulke";

    // il nodes
    static final String NODE_IL = "il";

    // ilçe nodes
    static final String NODE_ILCE = "ilce";
    static final String NODE_IDV = "value";
    static final String NODE_URL = "data-url";

    //Returns the entire XML document
    public Document getDocument(InputStream inputStream) {
        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(inputStream);
            document = db.parse(inputSource);
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return document;
    }

    /*
     * I take a XML element and the tag name, look for the tag and get
     * the text content i.e for <employee><name>Kumar</name></employee>
     * XML snippet if the Element points to employee node and tagName
     * is name I will return Kumar. Calls the private method
     * getTextNodeValue(node) which returns the text value, say in our
     * example Kumar. */
    public String getValue(Element item, String name) {
        NodeList nodes = item.getElementsByTagName(name);
        return this.getTextNodeValue(nodes.item(0));
    }

    private final String getTextNodeValue(Node node) {
        Node child;
        if (node != null) {
            if (node.hasChildNodes()) {
                child = node.getFirstChild();
                while (child != null) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                    child = child.getNextSibling();
                }
            }
        }
        return "";
    }



    public void XMLRead(String xmlFile, String kategori) {
        XMLDOMParser parser = new XMLDOMParser();
        AssetManager manager = context.getAssets();
        ulkeler = new ArrayList<String>();
        iller = new ArrayList<String>();
        ilceler = new ArrayList<String>();
        vakitVerilers = new ArrayList<VakitVeriler>();
        try {

            InputStream stream;
            stream = manager.open(xmlFile);
            Document doc = parser.getDocument(stream);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();


            // kategori = ulke , il ,ilce
            NodeList nList = doc.getElementsByTagName(kategori);

            String id , url , ilce , il,ulke;

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);



                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    if(kategori.equals("ulke")) {
                        Element eElement = (Element) nNode;
                        ulke = eElement.getTextContent();
                        id =  eElement.getAttribute("value");
                        ulkeler.add(ulke);

                    }else if(kategori.equals("il")) {
                        Element eElement = (Element) nNode;
                        il = eElement.getTextContent();
                        iller.add(il);

                    }else if(kategori.equals("ilce")) {
                        VakitVeriler vakitVeri = new VakitVeriler();
                        Element eElement = (Element) nNode;
                        ilce = eElement.getTextContent();
                        id =  eElement.getAttribute("value");
                        url =  eElement.getAttribute("data-url");
                        vakitVeri.setKonum(ilce);
                        vakitVeri.setUrl(url);
                        vakitVerilers.add(vakitVeri);
                        ilceler.add(ilce);

                    }

                }
                if(kategori.equals("ulke")) {
                    this.setUlkeler(ulkeler);
                }else if(kategori.equals("il")) {
                    this.setIller(iller);
                }else if(kategori.equals("ilce")) {
                    this.setIlceler(ilceler);
                    this.setVakitVerilers(vakitVerilers);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<VakitVeriler> getVakitVerilers() {
        return vakitVerilers;
    }

    public void setVakitVerilers(ArrayList<VakitVeriler> vakitVerilers) {
        this.vakitVerilers = vakitVerilers;
    }


    public ArrayList<String> getIller() {
        return iller;
    }

    public void setIller(ArrayList<String> iller) {
        this.iller = iller;
    }

    public ArrayList<String> getUlkeler() {
        return ulkeler;
    }

    public void setUlkeler(ArrayList<String> ulkeler) {
        this.ulkeler = ulkeler;
    }

    public ArrayList<String> getIlceler() {
        return ilceler;
    }

    public void setIlceler(ArrayList<String> ilceler) {
        this.ilceler = ilceler;
    }



}