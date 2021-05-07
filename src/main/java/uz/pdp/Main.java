package uz.pdp;
/*
Created by Ali Artukov
*/

import lombok.SneakyThrows;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.controller.PrayerTimeController;
import uz.pdp.model.ProjectProperties;
import uz.pdp.service.PrayerTimeService;

public class Main extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException {
        System.out.println(ProjectProperties.RUNNING_MESSAGE);
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new Main());
    }

    @Override
    public String getBotUsername() {
        return ProjectProperties.BOT_USERNAME;
    }

    public String getBotToken() {
        return ProjectProperties.BOT_TOKEN;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        String firstname = update.getMessage().getFrom().getFirstName();
        String lastname = update.getMessage().getFrom().getLastName();
        System.out.println(firstname + " " + lastname + ": " + text);

        execute(PrayerTimeService.getChatAction(chatId));
        execute(PrayerTimeController.updateController(update));
    }
}
