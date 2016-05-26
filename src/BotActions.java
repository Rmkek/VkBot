import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

class BotActions {
    String getLastHistoryMessage(VKAPI vkapi) throws java.io.IOException{
        StringBuilder lastMessage = new StringBuilder();
        //String lastMessage = vkapi.getHistory("group", "2000000001", 0, 1, false);
        lastMessage.append(vkapi.getHistory("group", "2000000001", 0, 1, false));
        System.out.println("Строка, полученная из вк: " + lastMessage);
        /*
              Костыли, костылики родненькие!
              Автор не берет ответственность за Вашу сохранность во время прочтения следующего кода.
        */
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(lastMessage.toString()).getAsJsonObject();
        JsonObject rootObject = jsonElement.getAsJsonObject();
        JsonObject childObject1 = rootObject.getAsJsonObject("response");
        JsonArray array = childObject1.getAsJsonArray("items");
        JsonElement element2 = array.get(0);
        JsonObject JsonData = element2.getAsJsonObject();
        return JsonData.get("body").toString();
    }
    boolean isLastMessageMem(String lastMessage){
        Pattern p = Pattern.compile(("^.*мем.*$"), Pattern.UNICODE_CASE |Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(lastMessage);
        return m.matches();
    }
    boolean isLastMessageKM(String lastMessage){
        Pattern p = Pattern.compile("^.*!КМ.*$");
        Matcher m = p.matcher(lastMessage);
        return m.matches();
    }

    boolean isLastMessageSMILEK(String lastMessage){
       try {
           char[] array = {34, 34, 55357, 56835, 34};
           char[] array2 = {34, 55357, 56835, 34};
           int c = 0;
           if (lastMessage.length() != 5 && lastMessage.length() != 4) {
               return false;
           }
           if (lastMessage.length() == 5) {
               for (int i = 0; i < 5; i++) {
                   if (lastMessage.charAt(i) == array[i]) {
                       c++;
                   }
               }
               if (c == 5) {
                   return true;
               }
           }
           if (lastMessage.length() == 4) {
               c = 0;
               for (int i = 0; i < 5; i++) {
                   if (lastMessage.charAt(i) == array2[i]) {
                       c++;
                   }
               }
               if (c == 4) {
                   return true;
               }
           }
       } catch(java.lang.StringIndexOutOfBoundsException ex){
           ex.printStackTrace();
       }
           return false;
    }
    boolean UserIsGruzin(VKAPI vkapi) throws IOException{
        //TODO Создать общий метод для парсинга
        StringBuilder lastMessage = new StringBuilder();
        //String lastMessage = vkapi.getHistory("group", "2000000001", 0, 1, false);
        lastMessage.append(vkapi.getHistory("group", "2000000001", 0, 1, false));
        System.out.println("Строка, полученная из вк: " + lastMessage);
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(lastMessage.toString()).getAsJsonObject();
        JsonObject rootObject = jsonElement.getAsJsonObject();
        JsonObject childObject1 = rootObject.getAsJsonObject("response");
        JsonArray array = childObject1.getAsJsonArray("items");
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
        lastMessage.delete(0,lastMessage.length());
        return jsonData.get("last_name").toString().equals("Бушуев");
    }

}