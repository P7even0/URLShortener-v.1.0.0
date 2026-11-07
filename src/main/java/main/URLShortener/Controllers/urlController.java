package main.URLShortener.Controllers;
import main.URLShortener.isValid;
import main.URLShortener.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import main.URLShortener.memory;
import main.URLShortener.Request;
import main.URLShortener.shortener;

import java.net.URI;
import java.util.Map;

@RestController
public class urlController {
    private static final Object lock = new Object();
    public static int idCount = 0;
    @GetMapping("/{code}")
    public ResponseEntity<?> fetchOriginalUrl(@PathVariable("code") String code){
        Request req = memory.findReqWithCode(code);

        if (req == null) return ResponseEntity.status(404).body("Not a valid URL");

        return ResponseEntity.status(302).location(URI.create(req.getOriginalUrl())).build();
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> createShortenedUrl(@RequestBody Map<String, String> content){
        String code = shortener.generateUrl();
        String originalUrl = content.get("url");

        ResponseEntity response = isValid.validateURL(originalUrl);

        if (!(response.getStatusCode().equals(HttpStatus.OK))) return response;

        ResponseEntity<?> res = concurrencyCreateNewUrl(code, originalUrl);
        return res;
    }

    public static ResponseEntity<?> concurrencyCreateNewUrl(String code, String originalUrl){
        synchronized (lock) {
            code = shortener.checkCode(code);
            Response res = memory.addURL(new Request(idCount, code, originalUrl));

            if (res.getExitStatus() == 200) {
                idCount++;
                return ResponseEntity.status(200).body(("Successfully created URL with code: ") + code);
            }


            return ResponseEntity.status(400).body("Malformed request.");

        }
    }



}
