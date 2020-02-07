package map;

public class MapErrorResponseMessage {
    private String message;

    public MapErrorResponseMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
