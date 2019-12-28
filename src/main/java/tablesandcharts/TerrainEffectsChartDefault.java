package tablesandcharts;

import java.io.FileInputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.csv.*;

// Relative file paths internal to the project are assumed to be correct
// Given that the location of the project could be different on different systems
// we have a properties files to assist with installation.
public class TerrainEffectsChartDefault implements TerrainEffectsChart {

    public enum Rows {TERRAIN_TYPE,
        CLEAR, GRAVEL, SALT_MARSH, HEAVY_VEGETATION, ROUGH, MOUNTAIN, DELTA, DESERT, MAJOR_CITY,
        SWAMP, VILLAGE_BIR_OASIS, RAILROAD, ROAD, TRACK, RIDGE, UP_SLOPE, DOWN_SLOPE, UP_ESCARPMENT,
        DOWN_ESCARPMENT, WADI, MAJOR_RIVER, MINOR_RIVER, FORT_LEVEL_ONE, FORT_LEVEL_TWO, FORT_LEVEL_THREE,
        FRIENDLY_MINEFIELD, ENEMY_MINEFIELD
    }

    public enum Columns { TERRAIN_TYPE,
        NON_MOTORIZED, MOTORIZED, BREAKDOWN, BARRAGE, ANTI_ARMOR, CLOSE_ASSAULT, STACKING_LIMIT
    }

    private static TerrainEffectsChartDefault instance = null;

    private String NOTE_NUMBER = "note_number";

    private final Map<Rows, HashMap<String, String>> TEC = new EnumMap<>(Rows.class);
    private final Map<String, String> TECNotes = new HashMap<>();

    private String pathToTables = "./";

    public static TerrainEffectsChartDefault getInstance() {
        if (instance == null) {
            instance = new TerrainEffectsChartDefault();
        }
        return instance;
    }

    // The Terrain Effects Chart has several field with notes. The notes describe further rules
    // that are contextual and outside the context of the Terrain Effects Chart.
    private TerrainEffectsChartDefault() {

        setProperties();

        try {
            Path path = Paths.get(pathToTables +
                    "ChartsAndTables/8.37-TerrainEffectsChart.csv");
            Reader reader = Files.newBufferedReader(path);
            Iterable<CSVRecord> stringTEC = CSVFormat.RFC4180.withHeader(Columns.class).parse(reader);

            boolean skipFirst = true;
            HashMap<String, String> row;
            String terrainType;
            for (CSVRecord csvRow : stringTEC) {
                if (skipFirst) {
                    skipFirst = false;
                    continue;
                } else {
                    row = new HashMap<>(csvRow.toMap());
                    terrainType = row.get(Columns.TERRAIN_TYPE.toString()).toUpperCase();
                    TEC.put(Rows.valueOf(terrainType), row);
                }
            }
            reader.close();
        } catch(Exception ex) {
            // TODO: comment file location assumptions in readme

            System.out.println(ex.getStackTrace());

            throw new RuntimeException(ex.getMessage());
        }

        try {

            Reader reader = Files.newBufferedReader(Paths.get(pathToTables +
                    "ChartsAndTables/notes-for-TEC.csv"));
            Iterable<CSVRecord> csvNotes = CSVFormat.RFC4180
                    .withHeader("row","column", NOTE_NUMBER).parse(reader);

            boolean skipFirst = true;
            HashMap<String, String> row;

            // Convert the table from CSV to a Map
            for (CSVRecord csvRow : csvNotes) {
                if (skipFirst) {
                    skipFirst = false;
                    continue;
                } else {
                    row = new HashMap<>(csvRow.toMap());
                    TECNotes.put(row.get("row").toUpperCase()
                            + "|"
                            + row.get("column").toUpperCase(), row.get(NOTE_NUMBER));
                }
            }

            reader.close();
        } catch(Exception ex) {
            // TODO: comment file location assumptions in readme
            throw new RuntimeException("notes-for-TEC.csv, or " +
                    "8.37-TerrainEffectsChartDefault.csv cannot be read");
        }
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


    public TECReturn readChart(Rows r, Columns c) {
        Optional<String> value = ifNoNoteNoNull(r, c);
        TECReturn ret = new TECReturn(TEC.get(r).get(c.toString()), value.orElse("No Note"));
        return ret;
    }

    // TODO: stacking limit rules will be implemented outside of the TEC
    Optional<String> ifNoNoteNoNull(Rows r, Columns c) {
        String key = r + "|" + c;
        String note = null;
        if (TECNotes.containsKey(key)) {
            note = TECNotes.get(key);
        } else {
            note = TECNotes.get(r + "|" + Rows.TERRAIN_TYPE.toString());
        }
        return Optional.ofNullable(note);
    }
}
