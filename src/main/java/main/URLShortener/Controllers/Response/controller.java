package main.URLShortener.Controllers.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import main.URLShortener.Response;
import main.URLShortener.shortener.shortener;
import main.URLShortener.Request;
import main.URLShortener.memory;
import java.util.Map;





@RestController
public class controller {
    public static int idCount = 0;
    @PostMapping("/shorten")
    public Response shortenURL(@RequestBody Map<String, String> request){
        String code = shortener.generateUrl();
        String URL = "http://localhost:8080/" + code ;
        Response res = memory.addURL(new Request(idCount, code, request.get("url")));

        if (res.getExitStatus() == 200){
            return new Response(URL, 200);
        }

        return new Response("Error adding URL", 400);


    }

}
