import java.util.logging.Level;
import java.util.logging.Logger;

public class BotInit {
    private static Logger log = Logger.getLogger(BotInit.class.getName());

    public static void main(String args[]) {
        try {
            VKAPI vkapi = new VKAPI(
                    "5479273",
                    "0706200967383b57acb47a8cc44dd3bae9314b629d9ea1c56cdd122b04160da4399d60bc0b942f6510ae8");
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
                    concatMaster.append(botActions.getPhotoMemes((vkapi.getPhotos("-73598440", "216920632", 100))));
                    String finalString = concatMaster.toString();
                    vkapi.sendMessage("group", "1", "Держи, дружище!", finalString);
                }
                if (groupMessage.toString().equals("Пореверерсим")) {
                    log.log(Level.INFO, "Entering Reverse mode!");
                    vkapi.sendMessage("group", "1", "Reverse mode!");
                    while (true) {
                        groupMessage.append(botActions.getLastHistoryMessage(vkapi.getHistory("group", "2000000001", 0, 1, false)));
                        if (groupMessage.toString().equals("ПАВИДЛОВ, ЗАЕБАЛ")) {
                            log.log(Level.INFO, "Exiting reverse mode.");
                            vkapi.sendMessage("group", "1", "За что вы так со мной? Я простой детдомовец-бот, пришел вам пореверерсить, а вы меня обижаете :(");
                            break;
                        }
                        StringBuilder reverse = new StringBuilder();
                        reverse.append(groupMessage);
                        vkapi.sendMessage("group", "1", reverse.reverse().toString());
                        reverse.delete(0, reverse.length());
                        Thread.sleep(10000);
                    }
                }

                if (botActions.isLastMessageMem(groupMessage.toString())) {
                    vkapi.sendMessage("group", "1", "Nice meme!");
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
