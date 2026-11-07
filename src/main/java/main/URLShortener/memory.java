package main.URLShortener;
import main.URLShortener.Request;
import main.URLShortener.Response;
import java.util.concurrent.ConcurrentHashMap;

import java.util.ArrayList;

public class memory {
    private static ConcurrentHashMap<String, Request> savedRequests = new ConcurrentHashMap<>();

    public static Response addURL(Request req){
        String code = req.getShortenedUrl();
        if (savedRequests.contains(req)){
            return new Response("Code already exists", 400);
        }
        savedRequests.putIfAbsent(code, req);

        return new Response("Added new URL successfully", 200);
    }

    public static Request findReqWithCode(String code){

        return savedRequests.getOrDefault(code, null);


    }
}
