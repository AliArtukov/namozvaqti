package uz.pdp.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws IOException {
//        Connection connection = new HttpConnection();
//        connection.url("https://islom.uz/vaqtlar/27/5");
//        Document document = connection.get();
//        Element body = document.body();
//        System.out.println(body.text());

//        List<Region> list = new ArrayList<>();
//        list.add(new Region(3, "dla"));
//        list.add(new Region(2, "ala"));
//        list.add(new Region(1, "cla"));
//        list.add(new Region(5, "ola"));
//        list.add(new Region(4, "tla"));
//        System.out.println(list);
//        list.sort(Comparator.comparing(Region::getName));
//        System.out.println(list);


//        NamozVaqtiService namozVaqtiService = new NamozVaqtiService();
//        List<Region> regionList = namozVaqtiService.getRegionList();
//
//        for (Region region : regionList) {
//            System.out.println(region.getCode() + ":  " + region.getName());
//        }


//        LocalDate localDate = LocalDate.now();
//        DateFormat dateFormat = new SimpleDateFormat();
//        String format = dateFormat.format(localDate);
//        System.out.println(format);
        Date date = new Date();
        System.out.println(date.getTime());
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime.getDayOfMonth() + "." + localDateTime.getMonth() + "." + localDateTime.getYear());


    }
}
