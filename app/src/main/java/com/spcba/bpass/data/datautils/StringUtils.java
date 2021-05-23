package com.spcba.bpass.data.datautils;

import java.util.Calendar;
import java.util.Date;

public class StringUtils {

    public static String generatePlatNumber() {

        int letter1 = 65 + (int) (Math.random() * (90 - 65));
        int letter2 = 65 + (int) (Math.random() * (90 - 65));
        int letter3 = 65 + (int) (Math.random() * (90 - 65));


        int number1 = (int) (Math.random() * 10);
        int number2 = (int) (Math.random() * 10);
        int number3 = (int) (Math.random() * 10);


        return "" + (char) (letter1) + ((char) (letter2)) +
                ((char) (letter3)) + "-" + number1 + number2 + number3;

    }

    public static String formatDate(Date date) {
        String stringDate = String.valueOf(date);

        return stringDate.replace(" 00:00:00 GMT+08:00 ", ",");

    }

    public static String formatTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String min = String.valueOf(calendar.get(Calendar.MINUTE));

        if (calendar.get(Calendar.HOUR_OF_DAY) >= 12) {
            if (calendar.get(Calendar.MINUTE) == 0)
                return hour + ":" + min + "0" + "PM";
            else
                return hour + ":" + min + "PM";


        } else {
            if (calendar.get(Calendar.MINUTE) == 0)
                return hour + ":" + min + "0" + "AM";
            else
                return hour + ":" + min + "AM";
        }
    }


}
