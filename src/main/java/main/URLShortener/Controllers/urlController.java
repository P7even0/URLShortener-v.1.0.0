package main.URLShortener.Controllers;

import main.URLShortener.Response;
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
    public static int idCount = 0;
    @GetMapping("/{code}")
    public ResponseEntity<?> fetchOriginalUrl(@PathVariable("code") String code){
        Request req = memory.findReqWithCode(code);
        System.out.println(req);
        memory.printReqs();
        if (req == null) return ResponseEntity.status(404).body("Not a valid URL");

        return ResponseEntity.status(302).location(URI.create(req.getOriginalUrl())).build();
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> createShortenedUrl(@RequestBody Map<String, String> content){
        String code = shortener.generateUrl();
        String originalUrl = content.get("url");

        Response res = memory.addURL(new Request(idCount, code, originalUrl));

        if (res.getExitStatus() == 200){
            idCount++;
            return ResponseEntity.status(200).body(("Successfully created URL with code: ") + code);
        }

        return ResponseEntity.status(400).body("Malformed request.");
    }



}
