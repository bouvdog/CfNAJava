package map;

public interface Hex {

    Hex add(Hex a, Hex b);

    int moveInto(int hexNumber, HexDefault.HexSide side);

    int moveOutOf(int hexNumber, HexDefault.HexSide side);
}
