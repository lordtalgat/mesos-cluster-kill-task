package kz.talgat;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import javax.json.*;

public class PostClass {
    private HttpURLConnection con = null;


    public PostClass() {
    }

    public String sendPost(String uri, JsonObject result) {
        URL url = null;
        try {
            url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Host", "masterhost:5050");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);
            StringWriter sw = new StringWriter();
            try (JsonWriter writer = Json.createWriter(sw)) {
                writer.writeObject(result);
            }
            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(sw.toString());
            osw.flush();
            osw.close();
            System.out.println(con.getResponseCode()+" "+LocalDateTime.now());
            if (con.getResponseCode() == 201 || con.getResponseCode()==200 || con.getResponseCode()==202) {
            StringBuilder sb = new StringBuilder();
            Reader reader = new InputStreamReader(con.getInputStream());
            while (true) {
                int ch = reader.read();
                if (ch == -1) {
                    break;
                }
                sb.append((char) ch);
            }
            return sb.toString();
              }else{
                getErrorStream(con);
            }
        } catch (IOException e) {
            getErrorStream(con);
        }
        return "0";
    }

    public String sendPost(String uri, String result) {
        URL url = null;
        try {
            url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();


            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Host", "masterhost:5050");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setDoInput(true);

            StringWriter sw = new StringWriter();
            sw.write(result);
            OutputStream os = con.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            osw.write(sw.toString());
            osw.flush();
            osw.close();
            System.out.println(con.getResponseCode());
            if (con.getResponseCode() == 201 || con.getResponseCode()==200 || con.getResponseCode()==202) {
                StringBuilder sb = new StringBuilder();
                Reader reader = new InputStreamReader(con.getInputStream());
                while (true) {
                    int ch = reader.read();
                    if (ch == -1) {
                        break;
                    }
                    sb.append((char) ch);
                }
                return sb.toString();
            }else{
                getErrorStream(con);
            }
        } catch (IOException e) {
            getErrorStream(con);
        }
        return "0";
    }

    private void getErrorStream(HttpURLConnection con){
        try {
            if (con.getResponseCode() > 399) {
                StringBuilder sb = new StringBuilder();
                Reader reader = new InputStreamReader(con.getErrorStream());
                while (true) {
                    int ch = 0;
                    try {
                        ch = reader.read();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (ch == -1) {
                        break;
                    }
                    sb.append((char) ch);
                }
                System.out.println(sb.toString());
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public String sendGet(String uri, JsonObject subscriber) {
        URL url = null;
        try {
            url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            if (con.getResponseCode()>199 && con.getResponseCode()<300){
                StringBuilder sb = new StringBuilder();
                Reader reader = new InputStreamReader(con.getInputStream());
                while (true) {
                    int ch = reader.read();
                    if (ch == -1) {
                        break;
                    }
                    sb.append((char) ch);
                }
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }


}
