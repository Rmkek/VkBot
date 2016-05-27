import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.net.URLEncoder;
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

        public static VKAPI with(String appId, String accessToken) throws IOException {
            return new VKAPI(appId, accessToken);
        }

        private final String accessToken;

    VKAPI(String appId, String accessToken) throws IOException {
            this.accessToken = accessToken;
            if (accessToken == null || accessToken.isEmpty()) {
                auth(appId);
                throw new Error("Need access token");
            }
        }

    void auth(String appId) throws IOException {
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
            if(receiver.equals("user")){
                receiver = "user_id=";
            } else if(receiver.equals("group")){
                receiver = "peer_id=";
            }

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
            System.out.println(reqUrl);
            return invokeApi(reqUrl);
        }

        private static String invokeApi(String requestUrl) throws IOException {
            final StringBuilder result = new StringBuilder();
            final URL url = new URL(requestUrl);
            try (InputStream is = url.openStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                reader.lines().forEach(result::append);
            }
            System.out.println(result.toString());
            return result.toString();
        }

        private static class Params {

             static Params create() {
                return new Params();
            }

            private final HashMap<String, String> params;

            private Params() {
                params = new HashMap<>();
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
