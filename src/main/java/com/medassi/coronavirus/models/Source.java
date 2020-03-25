/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medassi.coronavirus.models;

import org.json.simple.JSONObject;

/**
 *
 * @author Anthony Médassi
 */
class Source {
    private String nom ;
    private String url ;
    private String archive ;
    public Source(JSONObject jsono) {
        this.nom = jsono.get("nom") != null ? jsono.get("nom").toString() : "";
        this.url = jsono.get("url") != null ? jsono.get("url").toString() : "";
        this.archive = jsono.get("archive") != null ? jsono.get("archive").toString() : "";
    }

    public String getNom() {
        return nom;
    }

    public String getUrl() {
        return url;
    }

    public String getArchive() {
        return archive;
    }
    
    
}
