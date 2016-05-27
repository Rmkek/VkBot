import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Bot {
    private static Logger log = Logger.getLogger(Bot.class.getName());

    boolean userIsBot(String JsonString) throws IOException {
        JsonObject rootObject = BotGSON.getJSONObject(JsonString, "response");
        JsonArray array = BotGSON.getJSONArray(rootObject, "items");
        JsonElement element2 = array.get(0);
        JsonObject JsonData = element2.getAsJsonObject();
        String userID = JsonData.get("user_id").toString();
        userID = userID.substring(0, userID.length());
        log.log(Level.INFO, "userIsBot method called. Returning bot id.");
        return userID.equals("328729931");
    }

    boolean UserIsGruzin(String lastMessage, VKAPI vkapi) throws IOException {
        JsonObject rootObject = BotGSON.getJSONObject(lastMessage, "response");
        JsonArray array = BotGSON.getJSONArray(rootObject, "items");
        JsonElement element2 = array.get(0);
        JsonObject JsonData = element2.getAsJsonObject();
        String userID = JsonData.get("user_id").toString();
        userID = userID.substring(0, userID.length());
        JsonParser parser1 = new JsonParser();
        JsonElement jsonElement1 = parser1.parse(vkapi.getUser(userID));
        JsonObject rootObject1 = jsonElement1.getAsJsonObject();
        JsonArray array1 = rootObject1.getAsJsonArray("response");
        JsonElement element3 = array1.get(0);
        JsonObject jsonData = element3.getAsJsonObject();
        log.log(Level.INFO, "UserIsGruzin method called. Returning gruzin id.");
        return jsonData.get("last_name").getAsString().equals("Бушуев");
    }
}
