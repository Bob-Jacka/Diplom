package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static final Faker fake = new Faker(new Locale("en"));
    private static final String approvedCardNumber = "4444 4444 4444 4441";
    private static final String declinedCardNumber = "4444 4444 4444 4442";

    public static String approvedCardNumber() {
        return approvedCardNumber;
    }

    public static String declinedCardNumber() {
        return declinedCardNumber;
    }

    public static String approvedCardNumberPlus1() {
        return approvedCardNumber + "1";
    }

    public static String unregisteredCardNumber() {
        return fake.number().digits(16);
    }

    public static String approvedCardNumberMinus1() {
        return approvedCardNumber.substring(0, approvedCardNumber.length() - 1);
    }

    public static String validYear() {
        return LocalDate.now().plusYears(1).
                format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String negativeYear() {
        return "-" + validYear();
    }

    public static String expiredYear() {
        return LocalDate.now().minusYears(1).
                format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String inactiveYear() {
        return "99";
    }

    public static String validMonth() {
        String fakeMonth = String.valueOf(fake.number().numberBetween(1, 13));
        return fakeMonth.length() == 1 ? "0" + fakeMonth : fakeMonth;
    }

    public static String invalidMonth() {
        return "13";
    }

    public static String negativeMonth() {
        return "-" + validMonth();
    }

    public static String dashedOwner() {
        return fake.name().firstName().toUpperCase() + "-" + fake.name().lastName().toUpperCase();
    }

    public static String cyrillicOwner() {
        Faker ruFake = new Faker(new Locale("ru"));
        return ruFake.name().firstName().toUpperCase() + " " + ruFake.name().lastName().toUpperCase();
    }

    public static String validOwner() {
        return fake.name().firstName().toUpperCase() + " " + fake.name().lastName().toUpperCase();
    }

    public static String validCVV() {
        return fake.number().digits(3);
    }

    public static String fourDigitCVV() {
        return fake.number().digits(4);
    }

    public static String zeroCVV() {
        return "000";
    }

    public static String negativeCVV() {
        return "-" + validCVV();
    }

    public static String twoDigitCVV() {
        return fake.number().digits(2);
    }

    public static String zeroes() {
        return "00";
    }

    public static String zeroesCardNumber() {
        return "0000 0000 0000 0000";
    }

    public static String empty() {
        return "";
    }

    public static String digitOwner() {
        return fake.regexify("[0-9]{4,}");
    }

    public static String chars() {
        return fake.regexify("[a-zA-Z]{5,}");
    }

    public static String hieroglyphs() {
        return "不要放在心上。";
    }

    public static String specialSymbols() {
        return "!@#$%^&*";
    }
}
