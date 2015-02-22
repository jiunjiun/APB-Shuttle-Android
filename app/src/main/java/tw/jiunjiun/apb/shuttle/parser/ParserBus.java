package tw.jiunjiun.apb.shuttle.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiun on 2015/2/23.
 */
public class ParserBus {
    public static String name = "";
    public static int kind = 0;
    public static boolean special = false;
    public static String note = "";
    public static String full_note = "";
    public static String depart = "";

    private final int master   = 1;
    private final int security = 2;
    private final int safety   = 3;
    private final int orange   = 4;

    public ParserBus(String json) {
        try {
            JSONObject mJSONObject = new JSONObject(json);
            JSONObject bus = new JSONObject(mJSONObject.getString("bus"));
            this.name    = bus.getString("name");
            this.kind    = bus.getInt("kind");
            this.special = bus.getBoolean("special");
            this.note    = bus.getString("note");
            this.depart  = bus.getString("depart");
            setFull_note();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setFull_note() {
        switch(kind) {
        case master:
            this.full_note = "局本部 - " + this.note;
            break;
        case security:
            this.full_note = "保安隊 - " + this.note;
            break;
        case safety:
            this.full_note = "安檢隊 - " + this.note;
            break;
        case orange:
            this.full_note = "亞通客運 - " + this.note;
            break;
        }
    }
}
