package tw.jiunjiun.apb.shuttle.parser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiun on 2015/2/25.
 */
public class ParseInfo {
    public static String orange_ref_img = "";
    public static String orange_ref_link = "";

    private JSONObject mJSONObject;
    private JSONObject apb;
    private JSONObject orange;

    public ParseInfo(String json) {
        try {
            mJSONObject = new JSONObject(new JSONObject(json).getString("info"));
            apb         = new JSONObject(mJSONObject.getString("apb"));
            orange      = new JSONObject(mJSONObject.getString("orange"));

            this.orange_ref_img  = orange.getString("ref_img");
            this.orange_ref_link = orange.getString("ref_link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String APBImgsUrl() {
        return this.APBImgsUrl("original");
    }

    public String APBImgsUrl(String type) {
        String result = "";
        try {
            JSONObject imgs_urls = new JSONObject(apb.getString("imgs_url"));
            switch(type) {
                default:
                case "original":
                    result = imgs_urls.getString("original");
                    break;
                case "desktop":
                    result = imgs_urls.getString("desktop");
                    break;
                case "tablet":
                    result = imgs_urls.getString("tablet");
                    break;
                case "mobile":
                    result = imgs_urls.getString("mobile");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
