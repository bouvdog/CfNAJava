package map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {

    MapSectionDefault aMapSection = new MapSectionDefault("B", "5701");

    @RequestMapping("/map/terraininhex")
    public String getTerrainInHex(@RequestParam(value = "hexnumber") String hexNumber) {
        String value = aMapSection.getTerrainInHex(Integer.valueOf(hexNumber)).toString();
        return value;
    }
}
