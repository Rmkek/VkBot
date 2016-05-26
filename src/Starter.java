import java.util.Random;

public class Starter {
        public static void main(String args[]) {
            try{
                VKAPI vkapi = new VKAPI(
                        "5479273",
                        "0706200967383b57acb47a8cc44dd3bae9314b629d9ea1c56cdd122b04160da4399d60bc0b942f6510ae8");
               // vkapi.auth("5479273");
                BotActions bot = new BotActions();
                StringBuilder lastHistoryMessage = new StringBuilder();
                while(true) {
                    lastHistoryMessage.append(bot.getLastHistoryMessage(vkapi));
                    if (bot.isLastMessageMem(lastHistoryMessage.toString())) {
                        vkapi.sendMessage("group", "1", "Найс рофлишь, братан");
                    }
                    if (bot.isLastMessageKM(lastHistoryMessage.toString())) {
                        if (Math.random()<0.5) {
                            vkapi.sendMessage("group", "1", "Орёл!");
                        } else {
                            vkapi.sendMessage("group", "1", "Решка!");
                        }
                    }
                    if(bot.isLastMessageSMILEK(lastHistoryMessage.toString())){
                        vkapi.sendMessage("group", "1", "&#128515;");
                    }
                    if(bot.UserIsGruzin(vkapi)){
                        vkapi.sendMessage("group", "1", "Слышь бля, грузин бля, ну-ка тиха, бля!");
                    }
                    lastHistoryMessage.delete(0, lastHistoryMessage.length() - 1);
                    Thread.sleep(2000);
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }