package uz.pdp.service;
/*
Created by Ali Artukov
*/

import lombok.SneakyThrows;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class NamozBot extends TelegramLongPollingBot {
    public static void main(String[] args) throws TelegramApiException {
        System.out.println("Working...");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        telegramBotsApi.registerBot(new NamozBot());
    }

    @Override
    public String getBotUsername() {
        return "Enter your telegram bot username...";
    }

    public String getBotToken() {
        return "Enter your telegram bot token...";
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        SendMessage sendMessage;
        Message message = update.getMessage();
        NamozVaqtiService namozVaqtiService = new NamozVaqtiService();
        Long chatId = message.getChatId();
        String text = message.getText();

        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);

        if (text.equals("/start")) {
            sendMessage = namozVaqtiService.startCommand(chatId);
        } else {
            sendMessage = namozVaqtiService.getResult(chatId, text);
        }
        execute(sendMessage);
        System.out.println(message.getFrom().getFirstName() + ": " + text);
    }
}
