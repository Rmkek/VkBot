import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
public class VKAPI {

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

        public VKAPI(String appId, String accessToken) throws IOException {
            this.accessToken = accessToken;
            if (accessToken == null || accessToken.isEmpty()) {
                auth(appId);
                throw new Error("Need access token");
            }
        }

        public void auth(String appId) throws IOException {
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

        public String getHistory(String userId, int offset, int count, boolean rev) throws IOException {
            return invokeApi("messages.getHistory", Params.create()
                    .add("user_id", userId)
                    .add("offset", String.valueOf(offset))
                    .add("count", String.valueOf(count))
                    .add("rev", rev ? "1" : "0"));
        }

        public String getAlbums(String userId) throws IOException {
            return invokeApi("photos.getAlbums", Params.create()
                    .add("owner_id", userId)
                    .add("photo_sizes", "1")
                    .add("thumb_src", "1"));
        }

        public String sendMessage(String user_id, String message) throws IOException{
            //TODO: GROUPS, ETC/
            // /if(user_id.subSequence(0,2).equals("id"))
            String methodInvoke= "https://api.vk.com/method/{METHOD_NAME}?{PARAMETERS}&access_token={ACCESS_TOKEN}";
            String reqUrl = methodInvoke
                          //"https://api.vk.com/method/messages.send?user_id=35933425&message=LUL&access_token=
                    .replace("{METHOD_NAME}", "messages.send")
                    .replace("{PARAMETERS}", "user_id=" + user_id + "&" + "message=" + message)
                    .replace("{ACCESS_TOKEN}", "f434aaf96312c9de4e90a915b51ed55b33b8b008b131ffd86a5153afdb56040741ecf8e0c68278e3650b3");
            return invokeApi(reqUrl);
        }

        public String invokeApi(String method, Params params) throws IOException {
            final String parameters = (params == null) ? "" : params.build();
            String reqUrl = API_REQUEST
                    .replace("{METHOD_NAME}", method)
                    .replace("{ACCESS_TOKEN}", accessToken)
                    .replace("{PARAMETERS}&", parameters);
            return invokeApi(reqUrl);
        }

        public static String invokeApi(String requestUrl) throws IOException {
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

            public static Params create() {
                return new Params();
            }

            private final HashMap<String, String> params;

            private Params() {
                params = new HashMap<>();
            }

            public Params add(String key, String value) {
                params.put(key, value);
                return this;
            }

            public String build() {
                if (params.isEmpty()) return "";
                final StringBuilder result = new StringBuilder();
                params.keySet().stream().forEach(key -> {
                    result.append(key).append('=').append(params.get(key)).append('&');
                });
                return result.toString();
            }
        }

}
