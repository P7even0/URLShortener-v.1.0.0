package main.URLShortener;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public class isValid {
    public static ResponseEntity<String> validateURL(String originalUrl){
        if (originalUrl == null || originalUrl.isBlank()) return ResponseEntity.status(400).body("URL is blank or null");

        try {
            URI uri = URI.create(originalUrl);

            String scheme = uri.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) return ResponseEntity.status(400).body("Invalid scheme; use http or https");

            String host = uri.getHost();
            if (host == null || host.equalsIgnoreCase("localhost") || host.equalsIgnoreCase("127.0.0.1")) return ResponseEntity.status(400).body("Invalid host");


            int port = uri.getPort();

            if ((port < -1 ) || port > 65535) return ResponseEntity.status(400).body("Invalid port");

            return ResponseEntity.status(200).body("URL validated");

        } catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body("Bad request");
        }
    }
}
