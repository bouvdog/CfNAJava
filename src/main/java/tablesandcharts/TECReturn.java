package tablesandcharts;

import java.util.List;

public class TECReturn {
    private String value;

    // There can be only one note per terrain 'look up'
    private String note;

    public TECReturn(String value, String note) {
        this.value = value;
        this.note = note;
    }

    public String getValue() {
        return value;
    }

    public String getNote() {
        return note;
    }
}
