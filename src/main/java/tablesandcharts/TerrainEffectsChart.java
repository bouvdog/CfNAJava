package tablesandcharts;

import java.util.function.Predicate;

public interface TerrainEffectsChart {

    Predicate<String> isNotePresent = s -> s.contains("^");

    TECReturn readChart(TerrainEffectsChartDefault.TerrainTypes r, TerrainEffectsChartDefault.Columns c);
}
