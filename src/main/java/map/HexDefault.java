package map;

public class HexDefault implements Hex {

    private int q;
    private int r;
    private int s;

    private int hexNumber;

    public enum HexSide {
        NW,
        NE,
        E,
        SE,
        SW,
        W
    }

    public static Hex create(int q, int r, int s, int hexNumber) {
        return new HexDefault(q, r, s, hexNumber);
    }

    private HexDefault(int q, int r, int s, int hexNumber) {
        this.q = q;
        this.r = r;
        this.s = s;
        this.hexNumber = hexNumber;
    }

    @Override
    public Hex add(Hex a, Hex b) {
        return null;
    }

    @Override
    public int moveInto(int hexNumber, HexSide side) {
        return 0;
    }

    @Override
    public int moveOutOf(int hexNumber, HexSide side) {
        return 0;
    }
}
