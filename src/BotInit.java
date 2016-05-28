import java.util.logging.Level;
import java.util.logging.Logger;

public class BotInit {
    private static final String APP_ID = "5479273";
    private static Logger log = Logger.getLogger(BotInit.class.getName());

    public static void main(String args[]) {
        try {
            VKAPI vkapi = new VKAPI(
                    "5479273",
                    "0d5a9af5e7dcea8b6608f44d16b7d6f0cf00731d2915c892d8463f6ba286281fd5f7cf19a1a5a88a0e686");
            // 0706200967383b57acb47a8cc44dd3bae9314b629d9ea1c56cdd122b04160da4399d60bc0b942f6510ae8");
            //VKAPI.auth(APP_ID);
            //Scanner sc = new Scanner(System.in);
            //System.out.println("Введите accessToken, он открылся в новой вкладке в браузере.");
            //String accessToken = sc.nextLine();
            // VKAPI vkapi = new VKAPI(APP_ID, accessToken);
            Bot bot = new Bot();
            BotActions botActions = new BotActions();
            StringBuilder groupMessage = new StringBuilder();
            StringBuilder rawMessage = new StringBuilder();
            while (true) {
                rawMessage.append(vkapi.getHistory("group", "2000000001", 0, 1, false));
                groupMessage.append(botActions.getLastHistoryMessage(rawMessage.toString()));
                log.log(Level.INFO, "Got messages rawMessage and groupMessage: " + rawMessage + " " + groupMessage);
                if (groupMessage.toString().equals("Годноту")) {
                    StringBuilder concatMaster = new StringBuilder();
                    concatMaster.append("photo-73598440_");
                    concatMaster.append(botActions.getPhotoMemes((vkapi.getPhotos("-73598440", "216920632", 500))));
                    String finalString = concatMaster.toString();
                    vkapi.sendMessage("group", "1", "Держи, дружище!", finalString);
                }
                /*if(groupMessage.toString().contains("!погода ")){TODO: ДОДЕЛАТЬ ПОГОДУ
                    //String saveMessage = groupMessage.toString();
                    String SaveMessage = groupMessage.substring(8, groupMessage.length());
                    //saveMessage.substring(8, saveMessage.length());
                    System.out.println(SaveMessage);
                    String returning = botActions.getTemp(vkapi.getWeather(SaveMessage));
                    int temp1 = Integer.parseInt(returning);
                    temp1 = ((temp1 -32)*5)/9;
                    vkapi.sendMessage("group", "1", "Погода: " +temp1 + " градусов цельсия.");
                }*/
                if (botActions.equalsCitatku(groupMessage.toString())) {
                    vkapi.sendMessage("group", "1", "", "wall-119328367_" + botActions.getWallNumber(vkapi.getWall("-119328367", "100")));
                }
                if (botActions.equalsROK(groupMessage.toString())) {
                    vkapi.sendMessageWithSticker("group", "1", "", "2050");
                }
                if (groupMessage.toString().equals("!песня")) {
                    String[] songArray = {"Ленинград - Мамба", "Без Билета – Мечтатели", "JetBoss – That's not my name", "Мумий тролль – Забавы"
                            , "Awoltalk – Wylin Out", "Muzzy – The Factory", "Conro – City Lights", "Hush – Vonk", "Ozzy Osbourne – Dreamer", "Jetta – I'd Love to Change the World",
                            "Halsey – Castle", "Imany feat. Filatov – Don't be so shy", "Queen – Friends will be Friends", "AC\\DC - TNT", "Michael Jackson - Billie Jean," +
                            "Varien - Lilith", "Varien - Valkyrie", "System Of A Down – Lonely Day"};
                    int RandomNumber = (int) (Math.random() * songArray.length - 1);
                    String[] wordsArray = {"Грустненькая", "Люблю её", "еее Роцк", "Топчик", "В наушниках самое то", "Т о п 4 и к", "Для души", "Спешл фор ю",
                            "Для седбойчиков"};
                    int RandomNumber2 = (int) (Math.random() * wordsArray.length - 1);
                    vkapi.sendMessage("group", "1", wordsArray[RandomNumber2] + ": " + songArray[RandomNumber]);
                }
                if (groupMessage.toString().contains("!ответь")) {
                    String[] wordArray = {"да", "нет", "маловероятно", "скорее всего", "сложновато", "хех мда", "я хз вообще, братан"};
                    int RandomNumber = (int) (Math.random() * 7);
                    vkapi.sendMessage("group", "1", wordArray[RandomNumber]);
                }
                if (groupMessage.toString().equals("Пореверсим")) {
                    log.log(Level.INFO, "Entering Reverse mode!");
                    vkapi.sendMessage("group", "1", "Reverse mode!");
                    while (true) {
                        groupMessage.append(botActions.getLastHistoryMessage(vkapi.getHistory("group", "2000000001", 0, 1, false)));
                        if (groupMessage.toString().equals("сревеР")) {
                            log.log(Level.INFO, "Exiting reverse mode.");
                            vkapi.sendMessage("group", "1", "Выхожу из режима реверсов(нет)(да)");
                            break;
                        }
                        StringBuilder reverse = new StringBuilder();
                        reverse.append(groupMessage);
                        vkapi.sendMessage("group", "1", reverse.reverse().toString());
                        reverse.delete(0, reverse.length());
                        groupMessage.delete(0, groupMessage.length());
                        Thread.sleep(10000);
                    }
                }

                if (botActions.isLastMessageMem(groupMessage.toString())) {
                    vkapi.sendMessage("group", "1", "10 мертвых пол кокеров из 10!");
                    log.log(Level.INFO, "LastMessageMem called. Nice meme! sent");
                }

                if (botActions.isLastMessageKM(groupMessage.toString())) {
                    if (Math.random() < 0.5) {
                        vkapi.sendMessage("group", "1", "Орёл!");
                    } else {
                        vkapi.sendMessage("group", "1", "Решка!");
                    }
                    log.log(Level.INFO, "LastMessageKM called.");
                }
                if (botActions.isLastMessageSMILEK(groupMessage.toString())) {
                    vkapi.sendMessage("group", "1", "&#128515;");
                    log.log(Level.INFO, "LastMessageSMILEK called.");
                    if (bot.userIsBot(vkapi.getHistory("group", "2000000001", 0, 1, false)) && botActions.isLastMessageSMILEK(groupMessage.toString())) {
                        Thread.sleep(1500);
                        vkapi.sendMessage("group", "1", "&#128293;");
                        log.log(Level.INFO, "LastMessageSMILEK called.(2) Sent fire_smile");
                    }
                }
                if (botActions.UserIsGruzin(rawMessage.toString(), vkapi)) {
                    vkapi.sendMessage("group", "1", "Слышь бля, грузин бля, ну-ка тиха, бля!");
                    log.log(Level.INFO, "Gruzin called.");
                }

                rawMessage.delete(0, rawMessage.length());
                groupMessage.delete(0, groupMessage.length());
                Thread.sleep(2500);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
