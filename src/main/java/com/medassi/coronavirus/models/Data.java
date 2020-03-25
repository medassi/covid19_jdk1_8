/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medassi.coronavirus.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Anthony Médassi
 */
public class Data implements Comparable<Data> {

    private Date date;
    private final Source source;
    private final String SourceType;
    private final String nom;
    private final String code;
    private final int casConfirmes;
    private final int reanimation;
    private final int hospitalises;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ObservableList<Victime> victimes;

    public Data(JSONObject jsono) {
        try {
            this.date = jsono.get("date") != null ? simpleDateFormat.parse(jsono.get("date").toString()) : null;
        } catch (ParseException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.source = jsono.get("source") != null ? new Source((JSONObject) jsono.get("source")) : null;
        this.SourceType = jsono.get("sourceType") != null ? jsono.get("sourceType").toString() : "";
        this.nom = jsono.get("nom") != null ? jsono.get("nom").toString() : "";
        this.code = jsono.get("code").toString();
        this.casConfirmes = jsono.get("casConfirmes") != null ? Integer.parseInt(jsono.get("casConfirmes").toString()) : -1;
        this.reanimation = jsono.get("reanimation") != null ? Integer.parseInt(jsono.get("reanimation").toString()) : -1;
        this.hospitalises = jsono.get("hospitalises") != null ? Integer.parseInt(jsono.get("hospitalises").toString()) : -1;
        if (jsono.get("victimes") != null) {
            victimes = FXCollections.observableArrayList() ;
            JSONArray jsona = (JSONArray) jsono.get("victimes") ;
            for( int i=0 ; i<jsona.size() ; i++ ){
                victimes.add(new Victime((JSONObject) jsona.get(i))) ;
            }
        }
    }

    public Date getDate() {
        return date;
    }

    public Source getSource() {
        return source;
    }

    public String getSourceType() {
        return SourceType;
    }

    public String getNom() {
        return nom;
    }

    public String getCode() {
        return code;
    }

    public int getCasConfirmes() {
        return casConfirmes;
    }

    public ObservableList<Victime> getVictimes() {
        return victimes;
    }

    @Override
    public String toString() {
        return simpleDateFormat.format(this.date) + "["+nom+"] -> " +casConfirmes ;
    }

    @Override
    public int compareTo(Data d) {
        return this.code.compareTo(d.code) ;
    }
    

}
