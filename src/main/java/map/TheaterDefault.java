package map;

import tablesandcharts.TerrainEffectsChartDefault;

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
        /*
        MapSection sectionA = MapSectionDefault.create("A");
        MapSection sectionB = MapSectionDefault.create("B");
        MapSection sectionC = MapSectionDefault.create("C");
        MapSection sectionD = MapSectionDefault.create("D");
        MapSection sectionE = MapSectionDefault.create("E");
        sections.put("A",sectionA);
        sections.put("B",sectionB);
        sections.put("C",sectionC);
        sections.put("D",sectionD);
        sections.put("E",sectionE);

         */

    }


}
