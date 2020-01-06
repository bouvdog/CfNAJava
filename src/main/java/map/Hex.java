package map;

public interface Hex {

    // TODO: what is the point of add()?
    Hex add(Hex a, Hex b);

    int moveInto(int hexNumber, HexDefault.HexSide side);

    int moveOutOf(int hexNumber, HexDefault.HexSide side);
}
