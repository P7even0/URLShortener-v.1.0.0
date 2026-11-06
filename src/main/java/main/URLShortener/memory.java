package main.URLShortener;
import main.URLShortener.Request;
import main.URLShortener.Response;

import java.util.ArrayList;

public class memory {
    public static ArrayList<Request> savedRequests = new ArrayList<Request>();

    public static Response addURL(Request req){
        savedRequests.add(req);

        return new Response("Added new URL successfully", 200);
    }

    public static Request findReqWithCode(String code){
        for (int i = 0; i  < savedRequests.size(); i++){
            if (savedRequests.get(i).getShortenedUrl().equals(code)) {
                return savedRequests.get(i);
            }
        }

        return new Request(-1, "Invalid URL", "Invalid URL");


    }

    public static void printReqs(){
        for (int i = 0; i  < savedRequests.size(); i++){
            System.out.println(savedRequests.get(i));
        }
    }

}
