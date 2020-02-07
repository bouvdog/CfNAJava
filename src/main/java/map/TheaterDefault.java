package map;

import java.util.HashMap;
import java.util.Map;

public class TheaterDefault implements Theater {
    private static TheaterDefault instance = null;

    private Map<String, MapSection> sections;

    public static TheaterDefault getInstance() {
        if (instance == null) {
            instance = new TheaterDefault();
        }
        return instance;
    }

    private TheaterDefault() {
        sections = new HashMap<>();
        //MapSection sectionA = new MapSectionDefault("A", "1101");
        MapSection sectionB = new MapSectionDefault("B", "5701");
        //sections.put("A",sectionA);
        sections.put("B",sectionB);
    }

    @Override
    public MapSection getMap(final String section) {
        return sections.get(section);
    }


}
