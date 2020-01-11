import map.MapSection;
import map.MapSectionDefault;
import org.junit.jupiter.api.Test;
import tablesandcharts.TerrainEffectsChartDefault;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMapSection {

    @Test
    public void givenHexNumber_returnTerrainInHex() throws Exception {
        MapSectionDefault ms = new MapSectionDefault();
        ms.buildAMapSection("B", 5701);
        ms.buildTerrainInHex("B");
        var terrain = ms.getTerrainInHex(5703);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.HEAVY_VEGETATION, terrain);
    }

    /*
    TEST_METHOD(givenHexNumberAndSide_returnTerrain)
    {
        MapSection m;
        Chart c;
        m.buildAMapSection(string("B"), c);

        // 5604, SW-up escarpment, SE-up escarpment, E-up slope
        auto t = m.getTerrainOnSide(5604, E);
        Assert::AreEqual(static_cast<int>(t), static_cast<int>(UP_SLOPE));
    }

    TEST_METHOD(givenHexNumber_returnRoadHexSides)
    {
        MapSection m;
        Chart c;
        m.buildAMapSection(string("B"), c);

        //5410, W - road, E - road, SE - track, SW - track
        auto r = m.getRoad(5410, SE);

    }

    TEST_METHOD(givenHexNumberAndMapSection_returnListOfTownAttributes)
    {
        MapSection m;
        Chart c;
        m.buildAMapSection(string("A"), c);

     */
}
