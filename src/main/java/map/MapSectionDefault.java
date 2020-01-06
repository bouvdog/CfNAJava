package map;

import tablesandcharts.TerrainEffectsChartDefault;

import java.util.Map;

public class MapSectionDefault implements MapSection {

    private int map_height = 10;
    private int map_width = 10;

    private Map<Integer, Hex> mapSection;
    private Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>> roadNetwork;
    private Map<Integer, TerrainEffectsChartDefault.TerrainTypes> terrainInhex;
    private Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>> terrainOnSides;

    public static MapSection create(String section) {

        return null;
    }

    @Override
    public TerrainEffectsChartDefault.TerrainTypes getTerrainInHex(int hexNumber) {
        return terrainInhex.get(hexNumber);
    }

    @Override
    public TerrainEffectsChartDefault.TerrainTypes getTerrainOnSide(int hexNumber, HexDefault.HexSide hs) {
        return terrainOnSides.get(hexNumber).get(hs);
    }

    @Override
    public TerrainEffectsChartDefault.TerrainTypes getRoad(int hexNumber, HexDefault.HexSide hs) {
        return null;
    }

    @Override
    public Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes> getRoads(int hexNumber) {
        return null;
    }

    @Override
    public int defensiveBenefit(int hexNumber, HexDefault.HexSide direction) {
        return 0;
    }

    @Override
    public Hex getHex(int hexNumber) {
        return null;
    }

    private boolean isMultiHexPresent(String s)
    {
        boolean present = false;
        if (s.contains("-"))
        {
            present = true;
        }
        return present;
    }

    // This method is most easily understood by looking a one of the map sections
    // The current implementation only builds two 10x10 hex grids from map sections
    // A and B as a test for the algorithms associated with map data (movement and
    // combat).
    //
    // This is why 'startHex' is specified. The code builds a 10x10 set starting from
    // the specified hex number.
    //
    // Each map is 33/34 hexes across and 60 hexes 'tall'. Map A has a large section of ocean
    // that is not represented/printed but the space is used for a map of Malta and other
    // table and charts. In this program, these ocean hexes exist until I can figure out a
    // slight optimization to remove them.
    private void buildAMapSection(String section, int startHex)
    {
        int hexNumber = startHex;
        int column = 1;

        for (int s = 0; s < map_height; s++) {
            int s_offset = s / 2;
            for (int q = -s_offset; q < map_width - s_offset; q++) {
                mapSection.put(hexNumber + column, HexDefault.create(q, -q - s, s, hexNumber));
                column++;
            }
            column = 1;
            hexNumber = hexNumber + 100;
        }

        //buildTerrainInHex(section);
        //buildHexSideTerrain(section);
        //terrain_effects_chart_ = tec;
    }
}
