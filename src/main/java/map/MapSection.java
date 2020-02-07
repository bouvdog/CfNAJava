package map;

import tablesandcharts.TerrainEffectsChartDefault;

import java.util.Map;
import java.util.Optional;

public interface MapSection {

    TerrainEffectsChartDefault.TerrainTypes getTerrainInHex(int hexNumber);
    Optional<TerrainEffectsChartDefault.TerrainTypes> getTerrainOnSide(int hexNumber, HexDefault.HexSide hs);
    TerrainEffectsChartDefault.TerrainTypes getRoad(int hexNumber, HexDefault.HexSide hs);
    Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes> getRoads(int hexNumber);

    Integer defensiveBenefit(int hexNumber, HexDefault.HexSide direction, TerrainEffectsChartDefault.Columns c);

    String cpaCostToEnter(int hexNumber, HexDefault.HexSide direction,
                          TerrainEffectsChartDefault.Columns modality);

    Hex getHex(int hexNumber);
}
