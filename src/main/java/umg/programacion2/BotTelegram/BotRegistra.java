package umg.programacion2.BotTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.programacion2.model.User;
import umg.programacion2.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class BotRegistra  extends TelegramLongPollingBot {

    private Map<Long, String> estadoConversacion = new HashMap<>();
    User usuarioConectado = null;
    UserService userService = new UserService();

    @Override
    public String getBotUsername() {
        return "Mash707_bot";
    }

    @Override
    public String getBotToken() {
        return "7321007724:AAHW-ULH1vn24my8PBW69ragjdYkREZScSo";
    }


    @Override
    public void onUpdateReceived(Update update) {


        //obtener el nombre y apellido del usuario en una variable
        String userFirstName = update.getMessage().getFrom().getFirstName();
        String userLastName = update.getMessage().getFrom().getLastName();
        String nickName = update.getMessage().getFrom().getUserName();
        long chat_id = update.getMessage().getChatId();
        String mensaje_Texto = update.getMessage().getText();

        try {
            String state = estadoConversacion.getOrDefault(chat_id, "");
            usuarioConectado = userService.getUserByTelegramId(chat_id);

            // Verificación inicial del usuario, si usuarioConectado es nullo, significa que no tiene registro de su id de telegram en la tabla
            if (usuarioConectado == null && state.isEmpty()) {
                sendText(chat_id, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + ", no tienes un usuario registrado en el sistema. Por favor ingresa tu correo electrónico:");
                estadoConversacion.put(chat_id, "ESPERANDO_CORREO");
                return;
            }

            // Manejo del estado ESPERANDO_CORREO
            if (state.equals("ESPERANDO_CORREO")) {
                processEmailInput(chat_id, mensaje_Texto);
                return;
            }

            sendText(chat_id, "Hola " + formatUserInfo(userFirstName, userLastName, nickName) + ", bienvenido al sistema de registro de la UMG. ¿En qué puedo ayudarte?");

        }catch (Exception e){
            sendText(chat_id, "Ocurrió un error al procesar tu mensaje. Por favor intenta de nuevo.");
        }
    }


    //funcion para formatear la información del usuario
    private String formatUserInfo(String firstName, String lastName, String userName) {
        return firstName + " " + lastName + " (" + userName + ")";
    }

    private String formatUserInfo(long chat_id, String firstName, String lastName, String userName) {
        return chat_id + " " + formatUserInfo(firstName, lastName, userName);
    }





    //verifica si el usurio está registrado en la tabla con su correo electrónico
    private void processEmailInput(long chat_id, String email) {
        sendText(chat_id, "Recibo su Correo: " + email);
        estadoConversacion.remove(chat_id); // Reset del estado
        try{
            usuarioConectado = userService.getUserByEmail(email);
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario por correo: " + e.getMessage());
            e.printStackTrace();
        }


        if (usuarioConectado == null) {
            sendText(chat_id, "El correo no se encuentra registrado en el sistema, por favor contacte al administrador.");
        } else {
            usuarioConectado.setTelegramid(chat_id);
            try {
                userService.updateUser(usuarioConectado);
            } catch (Exception e) {
                System.err.println("Error al actualizar el usuario: " + e.getMessage());
                e.printStackTrace();
            }

            sendText(chat_id, "Usuario actualizado con éxito!");
        }
    }





    //función para enviar mensajes
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
