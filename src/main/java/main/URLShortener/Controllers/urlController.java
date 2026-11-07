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




        if (req == null) return ResponseEntity.status(404).body("Not a valid URL");

        return ResponseEntity.status(302).location(URI.create(req.getOriginalUrl())).build();
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> createShortenedUrl(@RequestBody Map<String, String> content){
        String code = shortener.generateUrl();
        String originalUrl = content.get("url");

        if (originalUrl == null || originalUrl.isBlank()) return ResponseEntity.status(400).body("URL is blank or null");

        try {
            URI uri = URI.create(originalUrl);

            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) return ResponseEntity.status(400).body("Invalid scheme; use http or https");

            String host = uri.getHost();
            if (host == null || host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1")) return ResponseEntity.status(400).body("Invalid host");


            int port = uri.getPort();

            if ((port < -1 ) || port > 65535) return ResponseEntity.status(400).body("Invalid port");



        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("Bad request");
        }




        Response res = memory.addURL(new Request(idCount, code, originalUrl));

        if (res.getExitStatus() == 200){
            idCount++;
            return ResponseEntity.status(200).body(("Successfully created URL with code: ") + code);
        }

        return ResponseEntity.status(400).body("Malformed request.");
    }



}
