import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BotGSON {
    JsonObject getResponse(String JsonString){
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(JsonString).getAsJsonObject();
        JsonObject rootObject = jsonElement.getAsJsonObject(); //Объект для получение "response"
        JsonObject childObject1 = rootObject.getAsJsonObject("response"); //получение "response"
        return childObject1;
    }


}
