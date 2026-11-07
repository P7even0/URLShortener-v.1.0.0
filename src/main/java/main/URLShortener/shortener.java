package main.URLShortener;
import main.URLShortener.memory;

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

    public static String checkCode(String code){
        while (memory.findReqWithCode(code) != null){
            code = generateUrl();
        }

        return code;
    }



}
