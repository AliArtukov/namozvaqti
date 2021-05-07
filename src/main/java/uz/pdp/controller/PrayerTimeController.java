package uz.pdp.controller;
/*
Created by Ali Artukov
*/

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.model.ProjectProperties;
import uz.pdp.service.PrayerTimeService;

import java.io.IOException;

public class PrayerTimeController {

    public static SendMessage updateController(Update update) throws IOException {
        SendMessage sendMessage;
        Message message = update.getMessage();
        PrayerTimeService namozVaqtiService = new PrayerTimeService();
        Long chatId = message.getChatId();
        String text = message.getText();

        if (text.equals(ProjectProperties.START_COMMAND)) {
            sendMessage = namozVaqtiService.startCommand(chatId);
        } else {
            sendMessage = namozVaqtiService.getResult(chatId, text);
        }
        return sendMessage;
    }
}
