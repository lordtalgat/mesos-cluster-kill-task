package kz.talgat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.round;

public class Main {
    private static String uri = "http://localhost:5050/api/v1";
    private static String tearDown = "http://localhost:5050/master/teardown";

    private PostClass post = new PostClass();
    private TypeRequest tr = new TypeRequest();
    private ActionRequest ar = new ActionRequest();
    private ParseJson pr = new ParseJson();
    private GetClass gt = new GetClass();

    public static void main(String[] args) {
        if (args.length > 0) {
            uri = args[0] + "/api/v1";
            tearDown = args[0] + "/master/teardown";
        }
        System.out.println(uri + ";" + tearDown);

        Main m = new Main();
        try {
            m.killInactive();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getOffers() {
        String st = post.sendPost(uri, tr.getOffers());
        System.out.println(st);
    }

    private void getMetrics() {
        String st = post.sendPost(uri, tr.getMetrics());
        System.out.println(st);
    }

    private void roles() {
        String json = "{\"type\":\"UPDATE_QUOTA\",\"update_quota\":{\"force\":false,\"quota_configs\":[{\"role\":\"test1\",\"limits\":{\"cpus\":{\"value\":10},\"mem\":{\"value\":2048},\"disk\":{\"value\":4096}}},{\"role\":\"test2\",\"limits\":{\"cpus\":{\"value\":1},\"mem\":{\"value\":256},\"disk\":{\"value\":512}}}]}}";
        String st = post.sendPost(uri, json);
        System.out.println(st);
    }

    private void resorcesSet() {
        String st = post.sendPost(uri, tr.getAgents());
        HashMap<String, String> hm = pr.getAgent(st);
        hm.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    private void killByActivity(String id, String url) {
        String out = gt.makeGetUrl(url);
        System.out.println(out);
    }

    private void killResourcesIsZero(int forKill) {
        String st = post.sendPost(uri, tr.getFrameworks());
        HashMap<String, Double> hm = pr.getFrameWorksResources(st);
        hm.forEach((k, v) -> killZero(k, v, forKill));
    }

    private void killInactive() throws IOException, ParseException {
        String st = post.sendPost(uri, tr.getFrameworks());
        HashSet<String> hs = pr.getFrameWorksActives(st, "slot:production");
        hs.forEach((k) -> killFramework(k));
    }

    private void killZero(String frameWorksId, Double cpus, int forKill) {
        if (cpus == forKill) {
            String st = post.sendPost(tearDown, ar.setTearDown(frameWorksId));
            System.out.println(cpus + " " + frameWorksId);
        }
    }

    private void killFramework(String frameWorksId) {
        String st = post.sendPost(tearDown, ar.setTearDown(frameWorksId));
        System.out.println("Kill " + frameWorksId);
    }

    private void typeOffers() {
        String st = post.sendPost(uri, tr.getOffers());
        System.out.println(st);
    }

    private static long nanosecondToDateCompare(long nanoseconds) {
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

    private int hoursDifference(Date date1, Date date2) {
        Long milliToHour = 1000L * 60 * 60;
        return (int) ((date1.getTime() - date2.getTime()) / milliToHour);
    }

    private void killAll() {
        String st = post.sendPost(uri, tr.getFrameworks());
        HashMap<String, Long> hm = pr.getFrameWorks(st);
        hm.forEach((k, v) -> killThem(k, v));
    }

    private void getWebUI() {
        String st = post.sendPost(uri, tr.getFrameworks());
        HashMap<String, String> hm = pr.getFrameWorksUrls(st);
        ParseJson pj = new ParseJson();
        HashMap<String, Date> hm2 = null;
        try {
            hm2 = pj.checkWorkTime(hm);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hm2.forEach((k, v) -> killHim(k, v));
    }

    private void killThem(String frameWorksId, Long nanoseconds) {
        if (nanosecondToDateCompare(nanoseconds) > 8) {
            String st = post.sendPost(tearDown, ar.setTearDown(frameWorksId));
            if (st.equals("0")) System.out.println(nanosecondToDateCompare(nanoseconds) + " " + frameWorksId);
            else System.out.println("error=" + st);
        }
    }

    private void killHim(String frameWorksId, Date date) {
        if (hoursDifference(new Date(), date) > 1) {
            String st = post.sendPost(tearDown, ar.setTearDown(frameWorksId));
            System.out.println("Kill=" + st);
            if (st.equals("0")) System.out.println(frameWorksId + " " + hoursDifference(new Date(), date) + " " + date.toString());
            else System.out.println("error=" + st);
        }
    }
}
