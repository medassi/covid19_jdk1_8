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
import org.json.simple.JSONObject;

/**
 *
 * @author Anthony Médassi
 */
public class Victime {
    private Date date ;
    private int age ;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Victime(JSONObject jsono) {
        try {
            this.date =  jsono.get("date") != null ? simpleDateFormat.parse( jsono.get("date").toString()):null;
            this.age = jsono.get("age") != null ? Integer.parseInt(jsono.get("age").toString()) : -1;
        } catch (ParseException ex) {
            Logger.getLogger(Victime.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getAge() {
        return age;
    }

    public Date getDate() {
        return date;
    }
    
    
    
}
