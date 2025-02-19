package kz.talgat;

public class MesosResources {
    private static String uri = "http://localhost:5050/api/v1/scheduler";
    private static TypeRequest tr = new TypeRequest();
    private static PostClass post = new PostClass();

    public static void main(String[] args) {
        String st = post.sendPost(uri, tr.subscriber());
        System.out.println(st);
        String st2 = post.sendPost(uri, tr.getOffers());
        System.out.println(st2);
    }
}
