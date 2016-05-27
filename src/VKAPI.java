import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class VKAPI {
        private static final String API_VERSION = "5.21";
        private static final String AUTH_URL = "https://oauth.vk.com/authorize"
                + "?client_id={APP_ID}"
                + "&scope={PERMISSIONS}"
                + "&redirect_uri={REDIRECT_URI}"
                + "&display={DISPLAY}"
                + "&v={API_VERSION}"
                + "&response_type=token";
        private static final String API_REQUEST = "https://api.vk.com/method/{METHOD_NAME}"
                + "?{PARAMETERS}"
                + "&access_token={ACCESS_TOKEN}"
                + "&v=" + API_VERSION;
    private static final String API_REQUEST_NO_TOKEN = "https://api.vk.com/method/{METHOD_NAME}"
            + "?{PARAMETERS}"
            + "&v=" + API_VERSION;
    private static Logger log = Logger.getLogger(VKAPI.class.getName());
        private final String accessToken;

    VKAPI(String appId, String accessToken) throws IOException {
            this.accessToken = accessToken;
            if (accessToken == null || accessToken.isEmpty()) {
                auth(appId);
                throw new Error("Need access token");
            }
        }

    public static VKAPI with(String appId, String accessToken) throws IOException {
        return new VKAPI(appId, accessToken);
    }

    private static String invokeApi(String requestUrl) throws IOException {
        final StringBuilder result = new StringBuilder();
        final URL url = new URL(requestUrl);
        try (InputStream is = url.openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            reader.lines().forEach(result::append);
        }
        log.log(Level.INFO, "invokeApi used, result: " + result.toString());
        return result.toString();
    }

    private void auth(String appId) throws IOException {
         String reqUrl = AUTH_URL
                    .replace("{APP_ID}", appId)
                    .replace("{PERMISSIONS}", "photos,messages")
                    .replace("{REDIRECT_URI}", "https://oauth.vk.com/blank.html")
                    .replace("{DISPLAY}", "page")
                    .replace("{API_VERSION}", API_VERSION);
            try {
                Desktop.getDesktop().browse(new URL(reqUrl).toURI());
            } catch (URISyntaxException ex) {
                throw new IOException(ex);
            }
        }

        public String getDialogs() throws IOException {
            return invokeApi("messages.getDialogs", null);
        }

        String getHistory(String receiver, String userId, int offset, int count, boolean rev) throws IOException {

            String reqURL = API_REQUEST
                    .replace("{METHOD_NAME}", "messages.getHistory")
                    .replace("{PARAMETERS}","offset=" + offset + "&"+
                            "count=" +count +"&" + "user_id=" + userId)
                    .replace("{ACCESS_TOKEN}", accessToken);
            return invokeApi(reqURL);
}

        public String getAlbums(String userId) throws IOException {
            return invokeApi("photos.getAlbums", Params.create()
                    .add("owner_id", userId)
                    .add("photo_sizes", "1")
                    .add("thumb_src", "1"));
        }

    String getPhotos(String owner_id, String album_id, int count) throws IOException {
        String reqURL = API_REQUEST_NO_TOKEN
                .replace("{METHOD_NAME}", "photos.get")
                .replace("{PARAMETERS}", "owner_id=" + owner_id + "&" + "album_id=" + album_id + "&rev=0&extended=0&photo_sizes=0&" + "count=" + count);
        return invokeApi(reqURL);
    }

    /*
        If message is going to user - pass a user, and id
        if message is going to group - pass chat id
     */
    String sendMessage(String messageReceiver,String receiverID, String message) throws IOException{
            if(messageReceiver.equals("user")){
                messageReceiver = "user_id=";
            } else if(messageReceiver.equals("group")){
                messageReceiver = "chat_id=";
            }
            String reqUrl = API_REQUEST
                    .replace("{METHOD_NAME}", "messages.send")
                    .replace("{PARAMETERS}", messageReceiver + receiverID + "&" + "message=" + URLEncoder.encode(message))
                    .replace("{ACCESS_TOKEN}", accessToken);
            return invokeApi(reqUrl);
        }

    String sendMessage(String messageReceiver, String receiverID, String message, String attachment) throws IOException {
        if (messageReceiver.equals("user")) {
            messageReceiver = "user_id=";
        } else if (messageReceiver.equals("group")) {
            messageReceiver = "chat_id=";
        }
        //https://api.vk.com/method/messages.send?user=224005125&message=message&attachment=photo-73598440_372211073"&access_token={ACCESS_TOKEN}"&v=" + API_VERSION;
        String reqUrl = API_REQUEST
                .replace("{METHOD_NAME}", "messages.send")                                                            //photo-73598440_372211073
                .replace("{PARAMETERS}", messageReceiver + receiverID + "&" + "message=" + URLEncoder.encode(message) + "&" + "attachment=" + attachment + "&notification=0")
                .replace("{ACCESS_TOKEN}", accessToken);
        return invokeApi(reqUrl);
    }

         String getUser(String user_id) throws IOException{
            String reqUrl = API_REQUEST
                    .replace("{METHOD_NAME}", "users.get")
                    .replace("{PARAMETERS}", "user_ids=" + user_id)
                    .replace("&access_token={ACCESS_TOKEN}", "");
            return invokeApi(reqUrl);
        }

        private String invokeApi(String method, Params params) throws IOException {
            final String parameters = (params == null) ? "" : params.build();
            String reqUrl = API_REQUEST
                    .replace("{METHOD_NAME}", method)
                    .replace("{ACCESS_TOKEN}", accessToken)
                    .replace("{PARAMETERS}&", parameters);
            return invokeApi(reqUrl);
        }

        private static class Params {

            private final HashMap<String, String> params;

            private Params() {
                params = new HashMap<>();
            }

            static Params create() {
                return new Params();
            }

             Params add(String key, String value) {
                params.put(key, value);
                return this;
            }

             String build() {
                if (params.isEmpty()) return "";
                final StringBuilder result = new StringBuilder();
                params.keySet().stream().forEach(key -> {
                    result.append(key).append('=').append(params.get(key)).append('&');
                });
                return result.toString();
            }
        }

}
