package map;

import tablesandcharts.TerrainEffectsChart;
import tablesandcharts.TerrainEffectsChartDefault;

import java.util.Map;

public interface MapSection {

    TerrainEffectsChartDefault.TerrainTypes getTerrainInHex(int hexNumber);
    TerrainEffectsChartDefault.TerrainTypes getTerrainOnSide(int hexNumber, HexDefault.HexSide hs);
    TerrainEffectsChartDefault.TerrainTypes getRoad(int hexNumber, HexDefault.HexSide hs);
    Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes> getRoads(int hexNumber);

    Integer defensiveBenefit(int hexNumber, HexDefault.HexSide direction, TerrainEffectsChartDefault.Columns c);

    Hex getHex(int hexNumber);
}
