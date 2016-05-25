import java.net.URLEncoder;
public class Starter {
        public static void main(String args[]){
            try{
                VKAPI vkapi = new VKAPI(
                        "5479273",
                        "2ad400117fca49c5dedd20d849890e26c09b2a4a89446974845f249c0505f3cf02540f77212fbdadcf901");
                //vkapi.auth("5479273");
                String message = "Вк совсем ахуел";
                String convert = URLEncoder.encode(message);
                System.out.println(convert);
                vkapi.sendMessage("224005125", convert);

                //TODO:Додумать работу бота, авторизацию, обработку куда слать сообщения и придумать команды, запустить бота на сервере
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }