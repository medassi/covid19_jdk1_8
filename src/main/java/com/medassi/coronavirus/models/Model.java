/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medassi.coronavirus.models;

import com.medassi.coronavirus.net.WSCovid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Anthony Médassi
 */
public class Model {

    private ArrayList<Data> datas;
    private HashMap<String, ArrayList<Data>> hmByDep = new HashMap<>();
    private HashMap<String, ArrayList<Data>> hmByReg = new HashMap<>();
    private ArrayList<Data> lFrance = new ArrayList<>();
    private ArrayList<Data> lWorld = new ArrayList<>();

    public Model() {
        datas = WSCovid.getInstance().getDatas();
        splitDatas();
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    private void splitDatas() {
        for (Data d : datas) {
            String sub = d.getCode().substring(0, 3);
            if (sub.equals("DEP")) {
                if (hmByDep.containsKey(d.getCode())) {
                    if (!hmByDep.get(d.getCode()).get(hmByDep.get(d.getCode()).size() - 1).getDate().equals(d.getDate())) {
                        hmByDep.get(d.getCode()).add(d);
                    }
                } else {
                    ArrayList l = new ArrayList<>();
                    l.add(d);
                    hmByDep.put(d.getCode(), l);
                }
            } else if (sub.equals("REG")) {
                if (hmByReg.containsKey(d.getCode())) {
                    if (!hmByReg.get(d.getCode()).get(hmByReg.get(d.getCode()).size() - 1).getDate().equals(d.getDate())) {
                        hmByReg.get(d.getCode()).add(d);
                    }
                } else {
                    ArrayList l = new ArrayList<>();
                    l.add(d);
                    hmByReg.put(d.getCode(), l);
                }
            } else if (sub.equals("FRA")) {
                lFrance.add(d);
            } else if (sub.equals("WOR")) {
                lWorld.add(d);
            }
        }
        nettoyer(lFrance);
        nettoyer(lWorld);
    }

    private void nettoyer(ArrayList<Data> l) {
       //A faire
    }

    public ArrayList<String> getDepts() {
        Set<String> deptsSet = hmByDep.keySet();
        ArrayList<String> deptsArrayList = new ArrayList<>(deptsSet);
        Collections.sort(deptsArrayList);
        return deptsArrayList;
    }
    
    public ArrayList<String> getRegs() {
        Set<String> regsSet = hmByReg.keySet();
        ArrayList<String> regsArrayList = new ArrayList<>(regsSet);
        Collections.sort(regsArrayList);
        return regsArrayList;
    }
    

    public ArrayList<Data> getlWorld() {
        return lWorld;
    }

    public ArrayList<Data> getlFrance() {
        return lFrance;
    }

    public HashMap<String, ArrayList<Data>> getHmByDep() {
        return hmByDep;
    }

    public HashMap<String, ArrayList<Data>> getHmByReg() {
        return hmByReg;
    }
}
