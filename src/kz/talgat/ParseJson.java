package kz.talgat;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ParseJson {
    public ParseJson() {

    }

    public HashMap getFrameWorks(String jsonStr) {
        HashMap<String, Long> hm = new HashMap();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        JsonObject get_frameworks = jsonObject.getJsonObject("get_frameworks");
        JsonArray frameworks = get_frameworks.getJsonArray("frameworks");

        for (int i = 0; i < frameworks.size(); i++) {
            JsonObject framework = frameworks.getJsonObject(i);

            JsonObject framework_info = framework.getJsonObject("framework_info");
            JsonObject id = framework_info.getJsonObject("id");
            String value = id.getString("value");

            JsonObject registered_time = framework.getJsonObject("registered_time");
            Long nanoseconds = 0L;
            try {
                nanoseconds = registered_time.getJsonNumber("nanoseconds").longValue();
            } catch (Exception e) {
                System.out.println("no nano " + value);
            }

            hm.put(value, nanoseconds);
        }
        return hm;
    }

    public HashMap getFrameWorksResources(String jsonStr) {
        HashMap<String, Double> hm = new HashMap();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        JsonObject get_frameworks = jsonObject.getJsonObject("get_frameworks");
        JsonArray frameworks = get_frameworks.getJsonArray("frameworks");

        for (int i = 0; i < frameworks.size(); i++) {
            JsonObject framework = frameworks.getJsonObject(i);

            JsonObject framework_info = framework.getJsonObject("framework_info");
            JsonObject id = framework_info.getJsonObject("id");
            String value = id.getString("value");

            JsonArray allocated_resources = framework.getJsonArray("allocated_resources");
            try {
                for (int j = 0; j < allocated_resources.size(); j++) {
                    JsonObject allocated_resource = allocated_resources.getJsonObject(j);
                    if (allocated_resource.getString("name").equals("cpus")) {
                        JsonObject scalar = allocated_resource.getJsonObject("scalar");
                        Double val = scalar.getJsonNumber("value").doubleValue();
                        hm.put(value, val);
                    }
                }
            } catch (Exception e) {
                hm.put(value, 0.0);
            }
        }
        return hm;
    }

    public HashSet getFrameWorksActives(String jsonStr, String excludes) throws IOException, ParseException {
        String[] exclude = excludes.split(",");
        HashSet<String> hs = new HashSet<>();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        JsonObject get_frameworks = jsonObject.getJsonObject("get_frameworks");
        JsonArray frameworks = get_frameworks.getJsonArray("frameworks");

        for (int i = 0; i < frameworks.size(); i++) {
            Boolean containsExclude = false;
            JsonObject framework = frameworks.getJsonObject(i);
            JsonObject framework_info = framework.getJsonObject("framework_info");
            JsonObject id = framework_info.getJsonObject("id");
            String key = id.getString("value");
            String name = framework_info.getString("name");
            Boolean connected = framework.getBoolean("connected");
            Boolean active = framework.getBoolean("active");
            JsonObject registered_time = framework.getJsonObject("registered_time");
            Long nanoseconds = 0L;
            try {
                nanoseconds = registered_time.getJsonNumber("nanoseconds").longValue();
            } catch (Exception e) {
                System.out.println("no nano registered_time");
            }

            for (int j = 0; j < exclude.length; j++) {
                if (name.contains(exclude[j])) containsExclude = true;
            }

            int cPus = 0;
            JsonArray allocated_resources = framework.getJsonArray("allocated_resources");
            try {
                for (int j = 0; j < allocated_resources.size(); j++) {
                    JsonObject allocated_resource = allocated_resources.getJsonObject(j);
                    if (allocated_resource.getString("name").equals("cpus")) {
                        JsonObject scalar = allocated_resource.getJsonObject("scalar");
                        cPus = scalar.getJsonNumber("value").intValue();
                    }
                }
            } catch (Exception e) {
            }
            if (!active && !connected && !containsExclude) hs.add(key);
        }
        return hs;
    }

    private int hoursDifference(Date date1, Date date2) {
        Long milliToHour = 1000L * 60 * 60;
        return (int) ((date1.getTime() - date2.getTime()) / milliToHour);
    }

    public HashMap getFrameWorksUrls(String jsonStr) {
        HashMap<String, String> hm = new HashMap();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        JsonObject get_frameworks = jsonObject.getJsonObject("get_frameworks");
        JsonArray frameworks = get_frameworks.getJsonArray("frameworks");

        for (int i = 0; i < frameworks.size(); i++) {
            JsonObject framework = frameworks.getJsonObject(i);

            JsonObject framework_info = framework.getJsonObject("framework_info");
            JsonObject id = framework_info.getJsonObject("id");
            String value = id.getString("value");

            String webui_url = framework_info.getString("webui_url");
            hm.put(value, webui_url);
        }
        return hm;
    }

    public HashMap getAgent(String jsonStr) {
        HashMap<String, String> hm = new HashMap();
        JsonReader jsonReader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        JsonObject get_agents = jsonObject.getJsonObject("get_agents");
        JsonArray agents = get_agents.getJsonArray("agents");

        for (int j = 0; j < agents.size(); j++) {
            JsonObject agent = agents.getJsonObject(j);
            JsonObject agent_info = agent.getJsonObject("agent_info");
            JsonObject id = agent_info.getJsonObject("id");
            String value = id.getString("value");

            String valueStr = "";
            JsonArray resources = agent_info.getJsonArray("resources");
            try {
                for (int i = 0; i < resources.size(); i++) {
                    JsonObject resource = resources.getJsonObject(i);
                    if (resource.getString("name").equals("cpus")) {
                        JsonObject scalar = resource.getJsonObject("scalar");
                        Double val = scalar.getJsonNumber("value").doubleValue();
                        valueStr = val.toString() + ";";
                    } else if (resource.getString("name").equals("mem")) {
                        JsonObject scalar = resource.getJsonObject("scalar");
                        Double val = scalar.getJsonNumber("value").doubleValue();
                        valueStr = valueStr + val.toString() + ";";
                    } else if (resource.getString("name").equals("disk")) {
                        JsonObject scalar = resource.getJsonObject("scalar");
                        Double val = scalar.getJsonNumber("value").doubleValue();
                        valueStr = valueStr + val.toString();
                    }
                }
            } catch (Exception e) {
            }
            hm.put(value, valueStr);
        }
        return hm;
    }

    public HashMap<String, Date> checkWorkTime(HashMap<String, String> address) throws ParseException, IOException {
        HashMap<String, Date> retur = new HashMap<String, Date>();
        for (String i : address.keySet()) {
            try {
                Date ss = js(address.get(i), i);
                if (ss != null) {
                    retur.put(i, ss);

                }
            } catch (Exception ex) {
                System.out.println("джоба уже нет " + address.get(i));
            }
        }
        return retur;
    }

    public Date js(String ad, String pi) throws ParseException {
        try {
            Date date_end;
            Integer exec = 0;
            Calendar cal = Calendar.getInstance();
            String resurse = ad + "/api/v1/applications/" + pi + "/executors";
            String resurse_json = jsonGetRequest(resurse);
            System.out.println(resurse);
            JsonReader jsonReader_exec = Json.createReader(new StringReader(resurse_json));
            JsonArray jsonArray_exec = jsonReader_exec.readArray();
            jsonReader_exec.close();
            exec = jsonArray_exec.size();

            String b = ad + "/api/v1/applications/" + pi + "/jobs";
            String a = jsonGetRequest(b);
            JsonReader jsonReader_chek = Json.createReader(new StringReader(a));
            JsonArray jsonObject_chek = jsonReader_chek.readArray();
            if (jsonObject_chek.size() > 0) {
                String loginToken = a.substring(1, a.length() - 1);
                JsonReader jsonReader = Json.createReader(new StringReader(loginToken));
                JsonObject jsonObject = jsonReader.readObject();
                jsonReader.close();
                if (jsonObject.getString("status").equals("SUCCEEDED") || jsonObject.getString("status").equals("FAILED")) {
                    date_end = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(jsonObject.getString("completionTime"));
                    if (exec > 17) {
                        return date_end;
                    } else {
                        cal.setTime(date_end);
                        cal.add(Calendar.HOUR_OF_DAY, 6);
                        return cal.getTime();
                    }
                } else {
                    System.out.println("В работе");
                    return null;

                }
            } else {
                String a1 = ad + "/api/v1/applications/";
                a = jsonGetRequest(a1);
                String loginToken = a.substring(1, a.length() - 1);
                JsonReader jsonReader = Json.createReader(new StringReader(loginToken));
                JsonObject jsonObject = jsonReader.readObject();
                jsonReader.close();
                JsonArray jsonArray = jsonObject.getJsonArray("attempts");
                JsonObject jsonObject1 = jsonArray.getJsonObject(0);
                date_end = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(jsonObject1.getString("lastUpdated"));
                if (exec > 17) {
                    return date_end;
                } else {
                    cal.setTime(date_end);
                    cal.add(Calendar.HOUR_OF_DAY, 6);
                    return cal.getTime();
                }
            }
        } catch (IOException ex) {
            return null;
        }
    }

    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public static String jsonGetRequest(String urlQueryString) throws IOException {
        StringBuilder sb = new StringBuilder();

        URL url = new URL(urlQueryString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("charset", "utf-8");
        connection.connect();
        Reader reader = new InputStreamReader(connection.getInputStream());

        while (true) {
            int ch = reader.read();
            if (ch == -1) {
                break;
            }
            sb.append((char) ch);
        }
        return sb.toString();
    }

    private long nanosecondToDateCompare(long nanoseconds) {
        String target = "1970/01/01 12:00 AM";  // Your given date string

        long millis = TimeUnit.MILLISECONDS.convert(nanoseconds, TimeUnit.NANOSECONDS);

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm aaa");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(target);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long newTimeInmillis = date.getTime() + millis;

        Date date2 = new Date(newTimeInmillis);
        LocalDateTime date3 = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault());
        LocalDateTime now1 = LocalDateTime.now();

        long diffInHours = java.time.Duration.between(date3, now1).toHours();

        return diffInHours;
    }

}
