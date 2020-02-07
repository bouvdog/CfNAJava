import map.*;
import org.junit.jupiter.api.Test;
import tablesandcharts.TerrainEffectsChartDefault;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMapSectionDefault {

    MapSectionDefault ms = new MapSectionDefault("B", "5701");

    Theater th = TheaterDefault.getInstance();

    @Test
    public void givenHexNumber_returnTerrainInHex() throws Exception {
        MapSection ms = th.getMap("B");
        TerrainEffectsChartDefault.TerrainTypes terrain = ms.getTerrainInHex(5703);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.HEAVY_VEGETATION, terrain);
    }

    @Test
    public void givenHexNumberNotInMap_returnNull() {
        MapSection ms = th.getMap("B");
        HexValueNotOnMap thrown = assertThrows(HexValueNotOnMap.class, () -> ms.getTerrainInHex(9999));
    }

    @Test
    public void givenMapSection_whenHexSideAndHexNumberProvided_returnTerrainType() throws Exception {

        // 5402, NW-down_escarpment, NE-down escarpment
        Optional<TerrainEffectsChartDefault.TerrainTypes> terrain = ms.getTerrainOnSide(5402, HexDefault.HexSide.NW);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.DOWN_ESCARPMENT, terrain.get());

        // Road network
        // 4905, W-track,E-track,SE-track
        TerrainEffectsChartDefault.TerrainTypes roadType = ms.getRoad(4905, HexDefault.HexSide.SE);
        assertEquals(TerrainEffectsChartDefault.TerrainTypes.TRACK, roadType);
    }

    @Test
    public void givenMapSection_whenHexSideHexNumberAndCombatTypeProvided_returnNumberOfShifts() throws Exception {
        int leftShifts = ms.defensiveBenefit(5209, HexDefault.HexSide.W,
                TerrainEffectsChartDefault.Columns.CLOSE_ASSAULT);

        // Two left shifts for rough and one left shift for wadi (assuming dry)
        assertEquals(-3, leftShifts);

        // 5706, NW-down_slope, W-down_slope
        // 5705,rough
        // If there is a close assault from 5706 to 5705, we will have shifts for the rough terrain in 5705,
        // but also the shift to attacking down slope from 5706.
        int shifts = ms.defensiveBenefit(5706, HexDefault.HexSide.W,
                TerrainEffectsChartDefault.Columns.CLOSE_ASSAULT);
        assertEquals(-1, shifts);

        shifts = ms.defensiveBenefit(5708, HexDefault.HexSide.W,
                TerrainEffectsChartDefault.Columns.CLOSE_ASSAULT);
        assertEquals(-2, shifts);
    }

    @Test
    public void givenMapSection_whenMoving_thenReturnCPACostForAdjacentHex() throws Exception {
        String cpaCost = ms.cpaCostToEnter(5209, HexDefault.HexSide.W,
                TerrainEffectsChartDefault.Columns.NON_MOTORIZED);
        assertEquals(4, cpaCost);

    }
}
