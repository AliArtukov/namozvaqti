package uz.pdp.service;
/*
Created by Ali Artukov
*/

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NamozVaqtiService {
    static Integer[] regionCode = new Integer[]{1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 66, 67, 68, 69, 70, 71, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94};
    static String[] regionName = new String[]{"Андижон", "Бекобод", "Бишкек", "Бухоро", "Денов", "Жалолобод", "Жамбул", "Жиззах", "Жомбой", "Каттақўрғон", "Марғилон", "Навоий", "Наманган", "Нукус", "Нурота", "Самарқанд", "Туркистон", "Ўш", "Хива", "Хўжанд", "Чимкент", "Қарши", "Қўқон", "Тошкент", "Шаҳрихон", "Хўжаобод", "Қўрғонтепа", "Хонобод", "Поп", "Чуст", "Косонсой", "Чортоқ", "Учқўрғон", "Фарғона", "Олтиариқ", "Риштон", "Қува", "Олмаота", "Сайрам", "Ангрен", "Бурчмулла", "Олот", "Газли", "Қоровулбозор", "Қоракўл", "Пахтаобод", "Зомин", "Дўстлик", "Арнасой", "Ўсмат", "Ғаллаорол", "Учтепа", "Ўғиз", "Томди", "Конимех", "Қизилтепа", "Зарафшон", "Узунқудуқ", "Учқудуқ", "Мингбулоқ", "Тахтакўпир", "Чимбой", "Мўйноқ", "Олтинкўл", "Шуманай", "Қўнғирот", "Булоқбоши", "Термиз", "Қумқўрғон", "Бойсун", "Шеробод", "Урганч", "Хазорасп", "Хонқа", "Янгибозор", "Шовот", "Деҳқонобод", "Ғузор", "Косон", "Таллимаржон", "Муборак", "Душанбе", "Ашхабод", "Туркманобод", "Тошҳовуз", "Қарши", "Навои"};

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
        Elements bugunData = document.getElementsByClass("p_day bugun");
        String[] data = bugunData.text().split(" ");
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
                        "\uD83D\uDCE4 *Маълумотлар islom.uz сайтидан олинди.*";
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

        for (int i = 0; i < regionCode.length; i++)
            regions.add(new Region(regionCode[i], regionName[i]));

        regions.sort(Comparator.comparing(Region::getName));
        return regions;
    }

    public SendMessage startCommand(Long chatId) {
        String text =
                "*Намоз вақтлари* телеграм ботига хуш келибсиз.\nЎзингизга яқин туман тугмасини босинг";
        ReplyKeyboardMarkup buttons = getButtons();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
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
