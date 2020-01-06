package tablesandcharts;

import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// Relative file paths internal to the project are assumed to be correct
// Given that the location of the project could be different on different systems
// we have a properties files to assist with installation.
public class TerrainEffectsChartDefault implements TerrainEffectsChart {

    public enum TerrainTypes {
        TERRAIN_TYPE,
        CLEAR, GRAVEL, SALT_MARSH, HEAVY_VEGETATION, ROUGH, MOUNTAIN, DELTA, DESERT, MAJOR_CITY,
        SWAMP, VILLAGE_BIR_OASIS, RAILROAD, ROAD, TRACK, RIDGE, UP_SLOPE, DOWN_SLOPE, UP_ESCARPMENT,
        DOWN_ESCARPMENT, WADI, MAJOR_RIVER, MINOR_RIVER, FORT_LEVEL_ONE, FORT_LEVEL_TWO, FORT_LEVEL_THREE,
        FRIENDLY_MINEFIELD, ENEMY_MINEFIELD
    }

    public enum Columns {
        TERRAIN_TYPE,
        NON_MOTORIZED, MOTORIZED, BREAKDOWN, BARRAGE, ANTI_ARMOR, CLOSE_ASSAULT, STACKING_LIMIT
    }

    private static TerrainEffectsChartDefault instance = null;

    private String NOTE_NUMBER = "note_number";

    private final Map<TerrainTypes, Map<String, String>> TEC = new EnumMap<>(TerrainTypes.class);
    private final Map<String, String> TECNotes;

    private String pathToTables = "";

    public static TerrainEffectsChartDefault getInstance() {
        if (instance == null) {
            instance = new TerrainEffectsChartDefault();
        }
        return instance;
    }

    // The Terrain Effects Chart has several field with notes. The notes describe further rules
    // that are contextual and outside the context of the Terrain Effects Chart.
    private TerrainEffectsChartDefault() {

        //setProperties();
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(pathToTables
                            + "ChartsAndTables/8.37-TerrainEffectsChart.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Iterable<CSVRecord> stringTEC = CSVFormat.RFC4180.withHeader(Columns.class).parse(reader);

            Map<String, Map<String,String>> intermediateStep = null;
            intermediateStep = StreamSupport.stream(stringTEC.spliterator(), false)
                    // Header row is skipped
                    .skip(1)
                    .map(CSVRecord::toMap)
                    .collect(Collectors.toMap(e -> e.get("TERRAIN_TYPE").toUpperCase(), Function.identity()));
            intermediateStep.forEach((k, v) -> TEC.put(TerrainTypes.valueOf(k), v));

            reader.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(pathToTables + "ChartsAndTables/notes-for-TEC.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            Iterable<CSVRecord> csvNotes = CSVFormat.RFC4180
                    .withHeader("row", "column", NOTE_NUMBER).parse(reader);
            boolean skipFirst = true;
            HashMap<String, String> row;

            // Convert the table from CSV to a Map
            TECNotes = StreamSupport.stream(csvNotes.spliterator(), false)
                    // Header row is skipped
                    .skip(1)
                    .map(CSVRecord::toMap)
                    .collect(Collectors.toMap(e->e.get("row").toUpperCase()
                    + "|"
                    + e.get("column").toUpperCase(), e->e.get(NOTE_NUMBER)));


            reader.close();
        } catch (Exception ex) {
            // TODO: comment file location assumptions in readme
            throw new RuntimeException(ex.getMessage());
        }
    }


    // Awkward that there is a two stop process but I couldn't figure out how to covert a keytype from a map in a
    // stream.
    public Map<TerrainTypes, Map<String, String>> buildTECFromStream() {
        Map<String, Map<String,String>> step1 = null;
        Map<TerrainTypes, Map<String, String>> tecChart = new EnumMap<>(TerrainTypes.class);
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(pathToTables
                            + "ChartsAndTables/8.37-TerrainEffectsChart.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Iterable<CSVRecord> stringTEC = CSVFormat.RFC4180.withHeader(Columns.class).parse(reader);

            step1 = StreamSupport.stream(stringTEC.spliterator(), false)
                            // Header row is skipped
                            .skip(1)
                            .map(CSVRecord::toMap)
                            .collect(Collectors.toMap(e -> e.get("TERRAIN_TYPE"), Function.identity()));

            step1.forEach((k, v) -> tecChart.put(TerrainTypes.valueOf(k.toUpperCase()), v));

        } catch (Exception e) {}
        return tecChart;
    }

    void setProperties() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("Paths.properties");
            props.load(fis);
            pathToTables = props.getProperty("tablesPath");
        } catch (Exception e) {
            throw new RuntimeException("Cannot find Paths file. Should be at '.' ");
        }
    }


    public TECReturn readChart(TerrainTypes r, Columns c) {
        Optional<String> value = ifNoNoteNoNull(r, c);
        TECReturn ret = new TECReturn(TEC.get(r).get(c.toString()), value.orElse("No Note"));
        return ret;
    }

    // TODO: stacking limit rules will be implemented outside of the TEC
    Optional<String> ifNoNoteNoNull(TerrainTypes r, Columns c) {
        String key = r + "|" + c;
        String note = null;
        if (TECNotes.containsKey(key)) {
            note = TECNotes.get(key);
        } else {
            note = TECNotes.get(r + "|" + TerrainTypes.TERRAIN_TYPE.toString());
        }
        return Optional.ofNullable(note);
    }
}
