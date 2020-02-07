package map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import tablesandcharts.TerrainEffectsChart;
import tablesandcharts.TerrainEffectsChartDefault;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// TODO: make this a Singleton to avoid the file reads that would occur on each object instantiation
public class MapSectionDefault implements MapSection {

    private int map_height = 10;
    private int map_width = 10;
    private String DASH = "-";

    private Map<Integer, Hex> aMapSection = new HashMap<>();
    private Map<Integer, TerrainEffectsChartDefault.TerrainTypes> terrainInHex = new HashMap<>();

    private Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>> terrainOnSides;
    private Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>> roadNetwork;

    public MapSectionDefault(String mapSection, String startHexNumber) {
        buildAMapSection(mapSection, Integer.valueOf(startHexNumber));
        try {
            buildTerrainInHex(mapSection);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    // It is very unlikely that in the final version of the game assistant that hexnumbers not
    // on maps will be submitted.
    @Override
    public TerrainEffectsChartDefault.TerrainTypes getTerrainInHex(int hexNumber) throws HexValueNotOnMap {
        TerrainEffectsChartDefault.TerrainTypes terrain = terrainInHex.get(hexNumber);
        if (terrain == null) {
            throw new HexValueNotOnMap("Hex number " + hexNumber + " not on map section.");
        }
        return terrain;
    }

    @Override
    public Optional<TerrainEffectsChartDefault.TerrainTypes> getTerrainOnSide(int hexNumber, HexDefault.HexSide hs) {
        TerrainEffectsChartDefault.TerrainTypes terrain = terrainInHex.get(hexNumber);
        if (terrain == null) {
            throw new HexValueNotOnMap("Hex number " + hexNumber + " not on map section.");
        }

        // There are two cases here: 1) where the hex has no hexside terrain at all and 2) where
        // there is no hexside terrain in the provided direction.
        Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes> sides = terrainOnSides.get(hexNumber);
        if (sides == null) {
            return Optional.empty();
        }
        TerrainEffectsChartDefault.TerrainTypes onSideTerrain = terrainOnSides.get(hexNumber).get(hs);
        return Optional.of(onSideTerrain);
    }

    @Override
    public TerrainEffectsChartDefault.TerrainTypes getRoad(int hexNumber, HexDefault.HexSide hs) {
        return roadNetwork.get(hexNumber).get(hs);
    }

    @Override
    public Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes> getRoads(int hexNumber) {
        return null;
    }


    @Override
    public Integer defensiveBenefit(final int hexNumber, final HexDefault.HexSide direction, final TerrainEffectsChartDefault.Columns c) {
        List<String> values = hexAndHexSideCosts(hexNumber, direction, c);
        return convertShiftsToNumbers(values.get(0)) + convertShiftsToNumbers(values.get(1));
    }

    @Override
    public String cpaCostToEnter(int hexNumber, HexDefault.HexSide direction, TerrainEffectsChartDefault.Columns modality) {
        List<String> values = hexAndHexSideCosts(hexNumber, direction, modality);
        return calculateCPACost(values);
    }

    private String calculateCPACost(List<String> values) {
        int cost = values.stream().mapToInt(Integer::valueOf).sum();
        return String.valueOf(cost);
    }

    private List<String> hexAndHexSideCosts(final int hexNumber, final HexDefault.HexSide direction,
                                            final TerrainEffectsChartDefault.Columns c) {
        int targetHex = hexNumberFromDirection(hexNumber, direction);
        TerrainEffectsChartDefault.TerrainTypes inHex = getTerrainInHex(targetHex);
        Optional<TerrainEffectsChartDefault.TerrainTypes> onSideOfHex = getTerrainOnSide(hexNumber, direction);
        TerrainEffectsChart tec = TerrainEffectsChartDefault.getInstance();
        String inHexValue = tec.readChart(inHex, c).getValue();
        String hexSideValue;

        // If hexside terrain doesn't exist, then the 'cost' (in CPA or shifts) is zero.
        if (!onSideOfHex.isEmpty()) {
            hexSideValue = tec.readChart(onSideOfHex.get(), c).getValue();
        } else {
            hexSideValue = "0";
        }
        List<String> values = new ArrayList<>();
        values.add(inHexValue);
        values.add(hexSideValue);
        return values;
    }


    // Left shifts are negative, right shifts are positive and terrain without shifts has zero shifts
    // When we calculate shifts we want to determine the hex side terrain of the origin hex (attacker)
    // and the hex terrain of the start hex. Directionality is important because of slopes and
    // escarpments. Up and down slope/escarpment have different shifts.
    Integer convertShiftsToNumbers(final String shifts) {
        Integer value = 0;
        if (shifts.contains("L")) {
            String[] shift = shifts.split("L");
            value = Integer.valueOf(shift[1]) * -1;
        } else if (shifts.contains("R")) {
            String[] shift = shifts.split("R");
            value = Integer.valueOf(shift[1]);
        }
        return value;
    }

    // To make sense of this method, you need to look at the map.
    int hexNumberFromDirection(final int startHex, final HexDefault.HexSide direction) {
        int targetHex = 0;
        switch (direction) {
            case W:
                targetHex = startHex - 1;
                break;
            case E:
                targetHex = startHex + 1;
                break;
            case NW:
                targetHex = (isEven(startHex)) ? startHex + 100 : startHex + 99;
                break;
            case NE:
                targetHex = (isEven(startHex)) ? startHex + 99 : startHex + 100;
                break;
            case SW:
                targetHex = (isEven(startHex)) ? startHex - 100 : startHex - 99;
                break;
            case SE:
                targetHex = (isEven(startHex)) ? startHex - 99 : startHex - 100;
                break;
        }
        return targetHex;
    }

    private boolean isEven(final int startHex) {
        int masked = startHex / 100;
        boolean even = false;
        if (masked % 2 == 0) {
            even = true;
        }
        return even;
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
    // the optimization to remove them.
    void buildAMapSection(String section, int startHex) {
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

        buildTerrainInHex(section);
        terrainOnSides = buildHexSideTerrain(section, "HexSideTerrain.csv");
        roadNetwork = buildHexSideTerrain(section, "Road.csv");
    }

    // Assumption: there is only one terrain type in a hex. Fortifications and other 'additions' that apply to the
    // hex will be in another table.
    void buildTerrainInHex(final String section) {
        String fileToRead = "ChartsAndTables/Map" + section + "TerrainInHex.csv";

        try {
            InputStream cvsFile = getClass()
                    .getClassLoader()
                    .getResourceAsStream(fileToRead);
            BufferedReader reader = new BufferedReader(new InputStreamReader(cvsFile));
            Iterable<CSVRecord> mapTerrain = CSVFormat.RFC4180.withHeader(TerrainEffectsChartDefault.Columns.class).parse(reader);

            for (CSVRecord r : mapTerrain) {
                // A 'multihex' terrain is a way to reduce typing in the data files. Since so much of the terrain is repeated
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
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    Predicate<CSVRecord> notEmptyRecord = r -> r.get(0).length() > 0;

    private Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>>
    buildHexSideTerrain(final String section, final String type) {
        Map<Integer, Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>> terrainTypeOnSides
                = new HashMap<>();
        try {
            String fileToRead = "ChartsAndTables/Map" + section + type;

            InputStream cvsFile = getClass()
                    .getClassLoader()
                    .getResourceAsStream(fileToRead);
            BufferedReader reader = new BufferedReader(new InputStreamReader(cvsFile));
            Iterable<CSVRecord> hexSideTerrain = CSVFormat.RFC4180.withHeader(TerrainEffectsChartDefault.Columns.class).parse(reader);

            // 5604, SW-up escarpment, SE-up escarpment, E-up slope
            Integer hexNumber;
            for (CSVRecord r : hexSideTerrain) {
                // there seems to be an empty record at the end of the 'file'
                if (notEmptyRecord.test(r)) {
                    hexNumber = Integer.valueOf(r.get(0));
                    terrainTypeOnSides.put(hexNumber, buildSideTerrain(r));
                }
            }
            reader.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return terrainTypeOnSides;
    }

    Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes>
    buildSideTerrain(final CSVRecord sides) {
        Map<HexDefault.HexSide, TerrainEffectsChartDefault.TerrainTypes> sideTerrain;

        Iterable<String> i = () -> sides.iterator();
        Stream<String> streamOfSides = StreamSupport.stream(i.spliterator(), false);
        sideTerrain = streamOfSides
                // We skip the first element because it is the hex number and not a hexside-to-terrain mapping
                .skip(1)
                .map(s -> s.split(DASH))
                .collect(Collectors.toMap(a -> HexDefault.HexSide.valueOf(a[0].trim()),
                        a -> TerrainEffectsChartDefault.TerrainTypes.valueOf(a[1].toUpperCase())));
        return sideTerrain;
    }

}
