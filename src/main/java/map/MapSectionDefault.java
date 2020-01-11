package map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import tablesandcharts.TerrainEffectsChartDefault;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MapSectionDefault implements MapSection {

    private int map_height = 10;
    private int map_width = 10;
    private String DASH = "-";

    private Map<Integer, Hex> aMapSection = new HashMap<>();
    private Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>> roadNetwork
            = new HashMap<>();
    private Map<Integer, TerrainEffectsChartDefault.TerrainTypes> terrainInHex = new HashMap<>();
    private Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>> terrainOnSides
            = new HashMap<>();

    @Override
    public TerrainEffectsChartDefault.TerrainTypes getTerrainInHex(int hexNumber) {
        return terrainInHex.get(hexNumber);
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

    private boolean isMultiHexPresent(String s) {
        boolean present = false;
        if (s.contains("-")) {
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
     public void buildAMapSection(String section, int startHex) {
        int hexNumber = startHex;
        int column = 1;

        for (int s = 0; s < map_height; s++) {
            int s_offset = s / 2;
            for (int q = -s_offset; q < map_width - s_offset; q++) {
                aMapSection.put(hexNumber + column, HexDefault.create(q, -q - s, s, hexNumber));
                column++;
            }
            column = 1;
            hexNumber = hexNumber + 100;
        }

        //buildTerrainInHex(section);
        //buildHexSideTerrain(section);
        //terrain_effects_chart_ = tec;
    }

    // Assumption: there is only one terrain type in a hex. Fortifications and other 'additions' that apply to the
    // hex will be in another table.
    public void buildTerrainInHex(final String section) throws Exception {
        String fileToRead = "ChartsAndTables/Map" + section + "TerrainInHex.csv";

        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(fileToRead);
        var reader = new BufferedReader(new InputStreamReader(is));
        Iterable<CSVRecord> mapTerrain = CSVFormat.RFC4180.withHeader(TerrainEffectsChartDefault.Columns.class).parse(reader);

        /*
        Map<String, Map<String,String>> intermediateStep = null;
        intermediateStep = StreamSupport.stream(stringTEC.spliterator(), false)
                // Header row is skipped
                .skip(1)
                .map(CSVRecord::toMap)
                .collect(Collectors.toMap(e -> e.get("TERRAIN_TYPE").toUpperCase(), Function.identity()));
        intermediateStep.forEach((k, v) -> TEC.put(TerrainEffectsChartDefault.TerrainTypes.valueOf(k), v));
        reader.close();
        */

        for (CSVRecord r : mapTerrain) {
            // a 'multihex' terrain is a way to reduce typing in the data files. Since so much of the terrain is repeated
            // on contiguous hexes, the terrain can be represented by a range of hex numbers separated by a dash. For instance,
            // 5009-5010. This is followed by a comma (CSV format file) and the terrain 'name' or description. For instance,
            // 5009-5010,gravel.
            if (isMultiHexPresent(r.get(0))) {
                String[] startAndEnd = r.get(0).split(DASH);
                int start = Integer.valueOf(startAndEnd[0]);
                int end = Integer.valueOf(startAndEnd[1]);
                for (int i = start; i <= end; i++) {
                    terrainInHex.put(i, TerrainEffectsChartDefault.TerrainTypes.valueOf(r.get(1).trim().toUpperCase()));
                }
            } else {
                terrainInHex.put(Integer.valueOf(r.get(0)),
                        TerrainEffectsChartDefault.TerrainTypes.valueOf(r.get(1).trim().toUpperCase()));
            }
        }
    }
}
