import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.logging.Level;
import java.util.logging.Logger;

class BotGSON extends Bot {
    private static Logger log = Logger.getLogger(BotGSON.class.getName());

    static JsonObject getJSONObject(String JsonString, String parse) {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(JsonString).getAsJsonObject();
        JsonObject rootObject = jsonElement.getAsJsonObject();
        log.log(Level.INFO, "getJSONObject method called, returning object");
        return rootObject.getAsJsonObject(parse);
    }

    static JsonArray getJSONArray(JsonObject rootObject, String parse) {
        log.log(Level.INFO, "getJSONArray method called, returning JsonArray");
        return rootObject.getAsJsonArray(parse);
    }

}
