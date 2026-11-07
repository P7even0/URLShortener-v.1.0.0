package main.URLShortener;

public record Request(int id, String shortenedURL, String originalURL){
    public Request(int id, String shortenedURL, String originalURL){
        this.id = id;
        this.shortenedURL =  shortenedURL;
        this.originalURL = originalURL;
    }

    public String getOriginalUrl(){
        return this.originalURL;
    }

    public String getShortenedUrl(){
        return this.shortenedURL;
    }
}

