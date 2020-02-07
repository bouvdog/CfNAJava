package map;

public class HexValueNotOnMap extends RuntimeException {
    public HexValueNotOnMap(String msg) {
        super(msg);
    }
}
