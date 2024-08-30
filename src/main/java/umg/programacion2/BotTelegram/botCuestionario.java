package umg.programacion2.BotTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.programacion2.model.User;
import umg.programacion2.model.User2;
import umg.programacion2.service.UserService;
import umg.programacion2.service.UserService2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class botCuestionario extends TelegramLongPollingBot {
    private Map<Long, String> estadoConversacion = new HashMap<>();
    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();
    User usuarioConectado = null;
    UserService userService = new UserService();
    UserService2 userService2 = new UserService2();

    @Override
    public String getBotUsername() {
        return "Mash707_bot";
    }

    @Override
    public String getBotToken() {
        return "7321007724:AAHW-ULH1vn24my8PBW69ragjdYkREZScSo";
    }

    public botCuestionario() {
        // Inicializa los cuestionarios con las preguntas.
        preguntas.put("SECTION_1", new String[]{"🤦‍♂1.1- Estas aburrido?", "😂😂 1.2- Te bañaste hoy?", "🤡🤡 Pregunta 1.3"});
        preguntas.put("SECTION_2", new String[]{"Pregunta 2.1 ¿Vas a ser honesto?", "Pregunta 2.2 ¿Cuantos años tenes?", "Pregunta 2.3 ¿A poco si Pinshilin? 🤓"});
        preguntas.put("SECTION_3", new String[]{"Pregunta 3.1", "Pregunta 3.2", "Pregunta 3.3"});
        preguntas.put("SECTION_4", new String[]{"Pregunta 4.1 ¿Tenes hambre?", "Pregunta 4.2 ¿Cuantos años tienes??", "Pregunta 4.3 ¿Que vamo a come?"});
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
           String userFirstName = update.getMessage().getFrom().getFirstName();
            String userLastName = update.getMessage().getFrom().getLastName();
            String nickName = update.getMessage().getFrom().getUserName();
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            try {
                String state = estadoConversacion.getOrDefault(chatId, "");
                usuarioConectado = userService.getUserByTelegramId(chatId);

                if (usuarioConectado == null && state.isEmpty()) {
                    sendText(chatId, "Hola, no tienes un usuario registrado en el sistema. Por favor ingresa tu correo electrónico:");
                    estadoConversacion.put(chatId, "ESPERANDO_CORREO");
                    return;
                }

                if (state.equals("ESPERANDO_CORREO")) {
                    processEmailInput(chatId, messageText);
                    return;
                }

                if (messageText.equals("/menu")) {
                    sendMenu(chatId);
                } else if (seccionActiva.containsKey(chatId)) {
                    manejaCuestionario(chatId, messageText);
                }

            } catch (Exception e) {
                sendText(chatId, "Ocurrió un error al procesar tu mensaje. Por favor intenta de nuevo.");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            inicioCuestionario(chatId, callbackData);
        }
    }

    // Método para formatear la información del usuario
    private String formatUserInfo(String firstName, String lastName, String userName) {
        return firstName + " " + lastName + " (" + userName + ")";
    }

    private void processEmailInput(long chatId, String email) {

        sendText(chatId, "Recibo su Correo: " + email);
        estadoConversacion.remove(chatId); // Reset del estado
        try {
            usuarioConectado = userService.getUserByEmail(email);
        } catch (Exception e) {
            System.err.println("Error al obtener el usuario por correo: " + e.getMessage());
            e.printStackTrace();
        }

        if (usuarioConectado == null) {
            sendText(chatId, "El correo no se encuentra registrado en el sistema, por favor contacte al administrador.");
        } else {
            usuarioConectado.setTelegramid(chatId);
            try {
                userService.updateUser(usuarioConectado);
            } catch (Exception e) {
                System.err.println("Error al actualizar el usuario: " + e.getMessage());
                e.printStackTrace();
            }
            sendText(chatId, "Usuario actualizado con éxito!");
            sendText(chatId, "Envía /menu para iniciar el cuestionario");
        }

    }

    // Método para enviar el menú
    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una sección:");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        rows.add(crearFilaBoton("Sección 1", "SECTION_1"));
        rows.add(crearFilaBoton("Sección 2", "SECTION_2"));
        rows.add(crearFilaBoton("Sección 3", "SECTION_3"));
        rows.add(crearFilaBoton("Sección 4", "SECTION_4"));

        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Método para crear botones en el menú
    private List<InlineKeyboardButton> crearFilaBoton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    // Método para iniciar el cuestionario
    private void inicioCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);
        enviarPregunta(chatId);
    }

    // Método para enviar la siguiente pregunta del cuestionario
    private void enviarPregunta(long chatId) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        if (index < questions.length) {
            sendText(chatId, questions[index]);
        } else {
            sendText(chatId, "¡Has completado el cuestionario!");
            seccionActiva.remove(chatId);
            indicePregunta.remove(chatId);
        }
    }

    // Método para manejar el cuestionario y guardar las respuestas
    private void manejaCuestionario(long chatId, String response) {
        String seccion = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);
        String[] questions = preguntas.get(seccion);

        // Verificar si estamos en la sección 2 y en la pregunta de la edad
        if ("SECTION_4".equals(seccion) && index == 1) { // Pregunta de edad en índice 1
            if (!esEdadValida(response)) {
                sendText(chatId, "Por favor ingresa una edad válida entre 15 y 100.");
                return; // No continuar hasta que la edad sea válida
            }
        }

        // Crear la respuesta usando User2
        User2 user2 = new User2();
        user2.setSeccion(seccion);
        user2.setTelegramId(chatId);
        user2.setPreguntaId(index);
        user2.setRespuestaTexto(response);

        // Guardar la respuesta en la base de datos utilizando UserService2
        try {
            userService2.createUser(user2);  // Guarda la respuesta en la base de datos
            sendText(chatId, "Tu respuesta fue: " + response);
        } catch (SQLException e) {
            e.printStackTrace();
            sendText(chatId, "Ocurrió un error al guardar tu respuesta. Por favor intenta de nuevo.");
        }

        // Continuar con el cuestionario
        indicePregunta.put(chatId, index + 1);
        enviarPregunta(chatId);
    }

    // Método para validar que la edad esté en un rango permitido
    private boolean esEdadValida(String respuesta) {
        try {
            int edad = Integer.parseInt(respuesta);
            return edad >= 15 && edad <= 100;
        } catch (NumberFormatException e) {
            return false; // No es un número válido
        }
    }


    // Función para enviar mensajes
    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())  // Who are we sending a message to
                .text(what).build();     // Message content
        try {
            execute(sm);  // Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);  // Any error will be printed here
        }
    }
}
