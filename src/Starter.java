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
                    System.out.println("Последняя строкa, полученная из вк, метод Starter: " + lastHistoryMessage.toString());
                    if(bot.getLastHistoryMessage(vkapi).equals("Пореверерсим")){
                        vkapi.sendMessage("group", "1", "Reverse mode!");
                        while(true){
                            if(bot.getLastHistoryMessage(vkapi).equals("ПАВИДЛОВ, ЗАЕБАЛ")){
                                vkapi.sendMessage("group", "1", "За что вы так со мной? Я простой детдомовец-бот, пришел вам пореверерсить, а вы меня обижаете :(");
                                break;
                            }
                                StringBuilder reverse = new StringBuilder();
                                reverse.append(bot.getLastHistoryMessage(vkapi));
                                vkapi.sendMessage("group", "1", reverse.reverse().toString());
                                reverse.delete(0, reverse.length());
                            Thread.sleep(10000);
                        }
                    }
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
                        if(bot.userIsBot(vkapi) && bot.isLastMessageSMILEK(lastHistoryMessage.toString())){
                            Thread.sleep(1000);
                            vkapi.sendMessage("group", "1", "&#128293;");
                        }
                    }
                    if(bot.UserIsGruzin(vkapi)){
                        vkapi.sendMessage("group", "1", "Слышь бля, грузин бля, ну-ка тиха, бля!");
                    }
                    lastHistoryMessage.delete(0, lastHistoryMessage.length());
                    Thread.sleep(2500);

                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }