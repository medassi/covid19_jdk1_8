/*
 * Anthony MEDASSI copyleft.
 */
package com.medassi.coronavirus.net;

import com.medassi.coronavirus.models.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WSCovid {

    private static final boolean LOCAL = false;
    private static WSCovid instance;
    private static final String BASEURL = "https://www.data.gouv.fr/fr/datasets/r/58aee810-ddd2-4359-85eb-5cfd899cd1ce";

    private WSCovid() {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    public static WSCovid getInstance() {
        if (instance == null) {
            instance = new WSCovid();
        }
        return instance;
    }

    public String getFromHttps(String urlHttps) {
        String retour = "";
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };

        SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            URL url = new URL(urlHttps);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                retour += inputLine;
            }
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(WSCovid.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException ex) {
            Logger.getLogger(WSCovid.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retour;
    }

    private String getFromFile(InputStream resource) {
        String retour = "";
        Scanner sc = new Scanner(resource);
        while (sc.hasNext()) {
            retour += sc.next();
        }
        sc.close();
        return retour;
    }

    public ArrayList<Data> getDatas() {
        ArrayList<Data> datas = new ArrayList<>();
        try {
            String retour = null;
            retour = LOCAL
                    ? getFromFile(getClass().getResourceAsStream("/datas/datas-21032020.json"))
                    : getFromHttps(BASEURL);
            JSONParser parser = new JSONParser();
            JSONArray jsona = (JSONArray) parser.parse(retour);
            for (int i = 0; i < jsona.size(); i++) {
                JSONObject jsono = (JSONObject) jsona.get(i);
                Data d = new Data(jsono);
                datas.add(d);
            }
        } catch (ParseException ex) {
            Logger.getLogger(WSCovid.class.getName()).log(Level.SEVERE, null, ex);
        }
        return datas;
    }
}
