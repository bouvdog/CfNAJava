package tablesandcharts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    TerrainEffectsChart tec = TerrainEffectsChartDefault.getInstance();

    @RequestMapping("/tec")
    public TECReturn getTECValue(@RequestParam(value="terraintype", defaultValue="CLEAR") String terrainType,
                                 @RequestParam(value="action") String action) {
        TECReturn values = tec.readChart(TerrainEffectsChartDefault.TerrainTypes.valueOf(terrainType.toUpperCase()),
                TerrainEffectsChartDefault.Columns.valueOf(action.toUpperCase()));
        return values;
    }
}
