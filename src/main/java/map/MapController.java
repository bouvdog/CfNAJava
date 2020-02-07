package map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {

    Theater th = TheaterDefault.getInstance();

    @RequestMapping("/map/terraininhex")
    public String getTerrainInHex(@RequestParam(value = "hexnumber") String hexNumber) {
        MapSection ms = th.getMap("B");
        return ms.getTerrainInHex(Integer.valueOf(hexNumber)).toString();
    }
}
