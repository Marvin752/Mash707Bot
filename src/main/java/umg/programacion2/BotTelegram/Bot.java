package umg.programacion2.BotTelegram;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "Mash707_bot";
    }

    @Override
    public String getBotToken() {
        return "7321007724:AAHW-ULH1vn24my8PBW69ragjdYkREZScSo";
    }



    //El método onUpdateReceived(Update update) de la clase Bot se usa para manejar todas las actualizaciones que el
    // bot recibe.
    // Dependiendo del tipo de actualización, se toman diferentes acciones.

    @Override
    public void onUpdateReceived(Update update) {

        //Obtener informacion de la persona que manda los mensajes

        String nombre = update.getMessage().getFrom().getFirstName();
        String apellido = update.getMessage().getFrom().getLastName();
        String nickName = update.getMessage().getFrom().getUserName();


        //Se verifica si la actualización contiene un mensaje y si ese mensaje tiene texto.
        //Luego se procesa el contenido del mensaje y se responde según el caso.
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Hola " +nickName+ " Tu nombre es: " +nombre+ " y tu apellido es: " +apellido);
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            //manejo de mensajes
            if(message_text.toLowerCase().equals("hola")){
                sendText(chat_id, "👋 Hola "+nombre+ " gusto de saludarte ✅");
            }
            if(message_text.toLowerCase().equals("tu madre")){
                sendText(chat_id, "La tuya >:C");
            }
            if(message_text.toLowerCase().equals("sapo")){
                sendText(chat_id, "Perro mrda");
            }
            if(message_text.toLowerCase().equals("adios")){
                sendText(chat_id, "La tuya >:C");
            }


            System.out.println("User id: " + chat_id + " Message: " + message_text);

        }
    }


    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}
