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
}
