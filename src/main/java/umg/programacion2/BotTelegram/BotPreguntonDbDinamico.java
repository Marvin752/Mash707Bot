package umg.programacion2.BotTelegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import umg.programacion2.botConfig.BotConfiguracion;
import umg.programacion2.model.Cuestionario;
import umg.programacion2.model.Pregunta;
import umg.programacion2.model.Respuesta;
import umg.programacion2.service.CuestionarioService;
import umg.programacion2.service.PreguntaService;
import umg.programacion2.service.RespuestaService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotPreguntonDbDinamico extends TelegramLongPollingBot{

    private final Map<Long, Integer> indicePregunta = new HashMap<>();
    private final Map<Long, String> seccionActiva = new HashMap<>();
    private final Map<String, String[]> preguntas = new HashMap<>();


    @Override
    public String getBotUsername() {
        return BotConfiguracion.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return BotConfiguracion.getBotToken();
    }



    public BotPreguntonDbDinamico() throws SQLException {

//obtener todos los cuestionarios en una lista
        List<Cuestionario> cuestionarios = new CuestionarioService().getAllCuestionarios();

        //iterar sobre la lista de cuestionarios e insertarlos en preguntas
        for (Cuestionario cuestionario : cuestionarios) {

            //getPreguntasByCuestionarioId
            List<Pregunta> TodasPreguntas = new PreguntaService().getPreguntasByCuestionarioId(cuestionario.getId());
            String[] arrayPreguntas = new String[TodasPreguntas.size()];
            for (int i = 0; i < TodasPreguntas.size(); i++) {
                arrayPreguntas[i] = TodasPreguntas.get(i).getPreguntaTexto();
            }

            preguntas.put("SECTION_" + cuestionario.getId(), arrayPreguntas);
        }

        // Inicializa los cuestionarios con las preguntas.
//        preguntas.put("SECTION_1", new String[]{"🤦‍♂️1.1- Estas aburrido?", "😂😂 1.2- Te bañaste hoy?", "🤡🤡 Pregunta 1.3"});
//        preguntas.put("SECTION_2", new String[]{"Pregunta 2.1", "Pregunta 2.2", "Pregunta 2.3"});
//        preguntas.put("SECTION_3", new String[]{"Pregunta 3.1", "Pregunta 3.2", "Pregunta 3.3"});
//        preguntas.put("SECTION_4", new String[]{"Pregunta 1.1", "edad"});
    }

    @Override
    public void onUpdateReceived(Update actualizacion) {
        if (actualizacion.hasMessage() && actualizacion.getMessage().hasText()) {
            String messageText = actualizacion.getMessage().getText();
            long chatId = actualizacion.getMessage().getChatId();



            if (messageText.equals("/menu")) {
                //sendMenuRespuesta(chatId);
                sendMenu(chatId);
            } else if (seccionActiva.containsKey(chatId)) {
                manejaCuestionario(chatId, messageText);
            }
        } else if (actualizacion.hasCallbackQuery()) { //es una respusta de un boton
            String callbackData = actualizacion.getCallbackQuery().getData();
            long chatId = actualizacion.getCallbackQuery().getMessage().getChatId();
            inicioCuestionario(chatId, callbackData);
        }
    }




    private void sendMenu(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Selecciona una sección:");



        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Crea los botones del menú
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

    private List<InlineKeyboardButton> crearFilaBoton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);
        return row;
    }

    private void inicioCuestionario(long chatId, String section) {
        seccionActiva.put(chatId, section);
        indicePregunta.put(chatId, 0);
        enviarPregunta(chatId);
    }

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


    //esta es la función que podemos almacenar las respuestas y controlar qué responde el usuario
    private void manejaCuestionario(long chatId, String response) {
        Respuesta resp = new Respuesta();

        String section = seccionActiva.get(chatId);
        int index = indicePregunta.get(chatId);

        if (section.contains("SECTION_4") && index == 1) {
            sendText(chatId, "Respusta de Edad analizando.... ");

            int edad;
            try {
                edad = Integer.parseInt(response);
            } catch (NumberFormatException e) {
                sendText(chatId, "La edad ingresada no es válida. Por favor, ingresa un número.");
                return;
            }


            //evaluar si la edad está en un rango valido para una persona y que sea un número
            if (edad < 0 || edad > 120) {
                sendText(chatId, "La edad ingresada no es válida. Por favor, ingresa un número entre 0 y 120.");
                return;
            }
        }

        resp.setRespuestaTexto(response);
        resp.setTelegramId(chatId);
        resp.setSeccion(seccionActiva.get(chatId));
        resp.setPreguntaId(indicePregunta.get(chatId));
        new RespuestaService().saveRespuesta(resp);
        sendText(chatId, "Datos Guardados Exitosamente " + response);

        indicePregunta.put(chatId, index + 1);

        enviarPregunta(chatId);
    }





    private void sendText(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}