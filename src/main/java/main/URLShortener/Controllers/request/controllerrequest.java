package main.URLShortener.Controllers.request;
import main.URLShortener.Request;
import main.URLShortener.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import main.URLShortener.memory;
import java.net.URI;


@RestController
public class controllerrequest {

    @GetMapping("/{code}")
    public ResponseEntity<?> redirectToUrl(@PathVariable("code") String code){
        Request req = memory.findReqWithCode(code);

        if (req.getShortenedUrl().equals("Invalid URL")) return ResponseEntity.status(404).body("Invalid URL");

        return ResponseEntity.status(302).location(URI.create(req.getOriginalUrl())).build();
    }

}
