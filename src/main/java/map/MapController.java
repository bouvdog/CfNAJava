package map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tablesandcharts.TerrainEffectsChartDefault;

@RestController
public class MapController {

    Theater th = TheaterDefault.getInstance();

    @RequestMapping("/map/terraininhex")
    public String getTerrainInHex(@RequestParam(value = "hexnumber") String hexNumber) {
        MapSection ms = th.getMap("B");
        return ms.getTerrainInHex(Integer.valueOf(hexNumber)).toString();
    }

    @RequestMapping("/map/defensivebenefit")
    public String getDefensiveBenefit(@RequestParam(value="hexnumber") String hexNumber,
                                      @RequestParam(value="hexside") String hexSide,
                                      @RequestParam(value="modality") String modality) {
        MapSection ms = th.getMap("B");
        int value = ms.defensiveBenefit(Integer.valueOf(hexNumber),
                HexDefault.HexSide.valueOf(hexSide),
                TerrainEffectsChartDefault.Columns.valueOf(modality.toUpperCase()));
        return String.valueOf(value);
    }
}
