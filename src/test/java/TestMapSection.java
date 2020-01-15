import map.HexDefault;
import map.MapSection;
import map.MapSectionDefault;
import org.junit.jupiter.api.Test;
import tablesandcharts.TerrainEffectsChartDefault;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMapSection {

    @Test
    public void givenHexNumber_returnTerrainInHex() throws Exception {
        MapSectionDefault ms = new MapSectionDefault("B", "5701");
        TerrainEffectsChartDefault.TerrainTypes terrain = ms.getTerrainInHex(5703);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.HEAVY_VEGETATION, terrain);
    }

    @Test
    public void givenMapSection_whenHexSideAndHexNumberProvided_returnTerrainType() throws Exception {
        MapSectionDefault ms = new MapSectionDefault("B", "5701");

        // 5402, NW-down_escarpment, NE-down escarpment
        TerrainEffectsChartDefault.TerrainTypes terrain = ms.getTerrainOnSide(5402, HexDefault.HexSide.NW);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.DOWN_ESCARPMENT, terrain);
    }
}
