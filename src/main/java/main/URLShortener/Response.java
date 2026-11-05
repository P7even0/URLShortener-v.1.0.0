package main.URLShortener;

public record Response (String message, int exitStatus){
    public Response(String message, int exitStatus){
        this.message = message;
        this.exitStatus = exitStatus;
    }

    public int getExitStatus(){
        return this.exitStatus;
    }
}
