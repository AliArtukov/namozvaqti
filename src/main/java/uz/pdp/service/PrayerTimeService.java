package uz.pdp.service;
/*
Created by Ali Artukov
*/

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.pdp.model.ProjectProperties;
import uz.pdp.model.Region;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PrayerTimeService {

    public static SendChatAction getChatAction(Long chatId) {

        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(ActionType.TYPING);
        return sendChatAction;
    }

    public String getData(String name) throws IOException {
        Integer code = getRegionCode(name);
        LocalDateTime localDateTime = LocalDateTime.now();
        int monthValue = localDateTime.getMonth().getValue();

        Connection connection = new HttpConnection();
        String url = "https://islom.uz/vaqtlar/";
        url = url + code + "/" + monthValue;
        connection.url(url);
        Document document = connection.get();
        Elements aClass = document.getElementsByClass("row city_prayer_block");
        String header = aClass.get(0).getElementsByClass("col-12").text();
        Elements todayData = document.getElementsByClass("p_day bugun");
        if (todayData.isEmpty()){
            todayData = document.getElementsByClass("juma bugun");
        }
        String[] data = todayData.text().split(" ");
        String date = localDateTime.getDayOfMonth() + "-" + localDateTime.getMonth() + " (" + data[2] + ")";

        String answer =
                "\uD83D\uDD4C *" + header + "*\n\n" +
                        "\uD83D\uDCC6 " + date + " куни учун:\n" +
                        "*Тонг (Саҳарлик):*  " + data[3] + "\n" +
                        "*Қуёш:*  " + data[4] + "\n" +
                        "*Пешин:*  " + data[5] + "\n" +
                        "*Аср:*  " + data[6] + "\n" +
                        "*Шом (Ифтор):*  " + data[7] + "\n" +
                        "*Хуфтон:*  " + data[8] + "\n\n" +
                        ProjectProperties.FOOTER_MESSAGE;
        return answer;
    }

    private Integer getRegionCode(String name) {
        List<Region> regionList = getRegionList();
        for (Region region : regionList) {
            if (region.getName().equals(name)) {
                return region.getCode();
            }
        }
        return 27; // Tashkent = 27
    }

    public ReplyKeyboardMarkup getButtons() {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        List<Region> regionsMap = getRegionList();

        for (Region region : regionsMap) {
            KeyboardButton button = new KeyboardButton(region.getName());
            KeyboardRow row = new KeyboardRow();
            row.add(button);
            keyboardRows.add(row);
        }
        return new ReplyKeyboardMarkup(keyboardRows);
    }

    public List<Region> getRegionList() {
        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < ProjectProperties.REGION_CODE.length; i++) {
            regions.add(new Region(ProjectProperties.REGION_CODE[i], ProjectProperties.REGION_NAME[i]));
        }
        regions.sort(Comparator.comparing(Region::getName));
        return regions;
    }

    public SendMessage startCommand(Long chatId) {
        ReplyKeyboardMarkup buttons = getButtons();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(ProjectProperties.START_MESSAGE);
        sendMessage.setParseMode("markdown");
        sendMessage.setReplyMarkup(buttons);
        return sendMessage;
    }

    public SendMessage getResult(Long chatId, String text) throws IOException {
        SendMessage editMessageText = new SendMessage();
        String data = getData(text);
        editMessageText.setChatId(chatId);
        editMessageText.setText(data);
        editMessageText.setParseMode("markdown");
        return editMessageText;
    }
}
