package main.URLShortener;
import main.URLShortener.Request;
import main.URLShortener.Response;
import java.util.concurrent.ConcurrentHashMap;

import java.util.ArrayList;

public class memory {
    private static ConcurrentHashMap<String, Request> savedRequests = new ConcurrentHashMap<>();

    public static Response addURL(Request req){
        String code = req.getShortenedUrl();
        savedRequests.putIfAbsent(code, req);

        if (findReqWithCode(code) == null) return new Response("Error adding to memory", 400);

        return new Response("Added new URL successfully", 200);
    }

    public static Request findReqWithCode(String code){

        return savedRequests.getOrDefault(code, null);
    }

    public static int getLength(){
        return savedRequests.size();
    }

    public static void clearHashMap(){
        savedRequests.clear();
    }
}
