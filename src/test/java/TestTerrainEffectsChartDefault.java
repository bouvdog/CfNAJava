import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import tablesandcharts.TECReturn;
import tablesandcharts.TerrainEffectsChart;
import tablesandcharts.TerrainEffectsChartDefault;

public class TestTerrainEffectsChartDefault {

    @Test
    void givenRowAndColumn_thenReturnCellValue() {
        TerrainEffectsChartDefault tec = TerrainEffectsChartDefault.getInstance();
        TECReturn ret = tec.readChart(TerrainEffectsChartDefault.Rows.GRAVEL, TerrainEffectsChartDefault.Columns.MOTORIZED);
        assertEquals("2", ret.getValue());
        assertEquals("No Note", ret.getNote());
    }

    // Three possible test  cases
    // 1) note value is attached to a value
    // 2) note value is attached to a column
    // 3) note value is attached to a row(terrain type)
    @Test
    void givenRowAndColumnWithNote_thenReturnValueAndNote() {
        TerrainEffectsChartDefault tec = TerrainEffectsChartDefault.getInstance();

        // This is a test attached to a value
        TECReturn ret = tec.readChart(TerrainEffectsChartDefault.Rows.ROAD, TerrainEffectsChartDefault.Columns.NON_MOTORIZED);
        assertEquals("1", ret.getValue());
        assertEquals("6", ret.getNote());

        // This is a test where note is attached to a row header or type
        ret = tec.readChart(TerrainEffectsChartDefault.Rows.SALT_MARSH, TerrainEffectsChartDefault.Columns.CLOSE_ASSAULT);
        assertEquals("R1", ret.getValue());
        String note = ret.getNote();
        assertEquals("2", note);

    }

    @Test
    void givenCellValue_thenRecognizeNotePresence() {
        TerrainEffectsChartDefault tec = TerrainEffectsChartDefault.getInstance();
    }
}
