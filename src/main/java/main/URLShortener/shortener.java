package main.URLShortener;


public class shortener {
    public static int LENGTH = 8;
    public static String availableIn = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._~";

    public static String generateUrl(){
        String result = "";
        for (int i = 0; i < LENGTH; i++){
            result += availableIn.charAt((int) ((Math.random() * (availableIn.length()))));
        }

        return result;

    }



}
