import map.HexDefault;
import map.MapSectionDefault;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tablesandcharts.TerrainEffectsChartDefault;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMapSectionDefault {

    MapSectionDefault ms = new MapSectionDefault("B", "5701");

    @Test
    public void givenHexNumber_returnTerrainInHex() throws Exception {
        TerrainEffectsChartDefault.TerrainTypes terrain = ms.getTerrainInHex(5703);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.HEAVY_VEGETATION, terrain);
    }

    @Test
    public void givenMapSection_whenHexSideAndHexNumberProvided_returnTerrainType() throws Exception {

        // 5402, NW-down_escarpment, NE-down escarpment
        TerrainEffectsChartDefault.TerrainTypes terrain = ms.getTerrainOnSide(5402, HexDefault.HexSide.NW);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.DOWN_ESCARPMENT, terrain);

        // Road network
        // 4905, W-track,E-track,SE-track
        terrain = ms.getRoad(4905, HexDefault.HexSide.SE);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.TRACK, terrain);
    }

    @Test
    public void givenMapSection_whenHexSideHexNumberAndCombatTypeprovided_returnNumberOfShifts() throws Exception {
        // 5402, NW-down_escarpment, NE-down escarpment
        int leftShifts = ms.defensiveBenefit(5209, HexDefault.HexSide.W,
                TerrainEffectsChartDefault.Columns.CLOSE_ASSAULT);

        // Two left shifts for rough and one left shift for wadi (assuming dry)
        assertEquals(3, leftShifts);
    }


}
