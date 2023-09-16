package ru.netology.tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import jdk.jfr.Label;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.ConnectToDB;
import ru.netology.pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.netology.data.DataGenerator.unregisteredCardNumber;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.ErrorNotifications.*;

public class DebitTests {
    private final String site = "http://localhost:8080/";

    @BeforeAll
    static void addWatcher() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void removeWatcher() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void openSite() {
        open(site);
    }

    @Test
    @Label("1. Покупка с оплатой по дебетовой карте со статусом APPROVED:")
    public void shouldBuyByDebit_Valid() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
        assertEquals("APPROVED", afterTransact.getStatus());
    }

    @Test
    @Label("2. Заполнение данных владельца через тире по дебетовой карте:")
    public void shouldBuyByDebit_IfOwnerThroughDash() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), dashedOwner(), validCVV());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("3. Заполнение данных владельца через пробел по дебетовой карте:")
    public void shouldBuyByDebit_IfOwnerThroughSpace() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("7. Заполнение данных владельца кириллическими символами по дебетовой карте:")
    public void shouldNotBuyByDebit_IfOwnerCyrillic() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), cyrillicOwner(), validCVV());
        debitPage.checkOwnerText(incorrectFormat);
    }

    @Test
    @Label("8. Покупка без заполненных форм с данными по дебетовой карте:")
    public void shouldNotBuyByDebit_IfEmptyForms() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.checkCardNumberText(emptyForm);
        debitPage.checkCVVText(emptyForm);
        debitPage.checkOwnerText(emptyForm);
        debitPage.checkMonthText(emptyForm);
        debitPage.checkYearText(emptyForm);
    }

    @Test
    @Label("9. Покупка с пустым кодом карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVEmpty() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), empty());
        debitPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("10. Покупка с кодом карты, заполненным только нулями по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVZeroes() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), zeroCVV());
        debitPage.checkCVVText(incorrectValue);
    }

    @Test
    @Label("11. Покупка с кодом карты, заполненным буквами по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVChars() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), chars());
        debitPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("12. Покупка с кодом карты, заполненным иероглифами по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVHieroglyphs() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), hieroglyphs());
        debitPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("13. Покупка с кодом карты с длинной больше максимальной по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVMore3() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), fourDigitCVV());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("14. Покупка с кодом карты с длинной, меньше валидной по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVLess3() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), digits());
        debitPage.checkCVVText(incorrectFormat);
    }

    @Test
    @Label("15. Покупка с кодом карты, который отрицательное число по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVNegative() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), negativeDigits());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("16. Покупка со специальными символами в коде карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCVVSpecSym() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), specialSymbols());
        debitPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("17. Покупка с незаполненным владельцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfOwnerEmpty() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), empty(), validCVV());
        debitPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("18. Покупка с заполненным цифрами владельцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfOwnerDigits() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), digits(), validCVV());
        debitPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("19. Покупка с заполненным иероглифами владельцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfOwnerHieroglyphs() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), hieroglyphs(), validCVV());
        debitPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("20. Покупка с заполненным специальными символами владельцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfOwnerSpecSym() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), specialSymbols(), validCVV());
        debitPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("21. Покупка с незаполненным годом по дебетовой карте:")
    public void shouldNotBuyByDebit_IfEmptyYear() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                empty(), validOwner(), validCVV());
        debitPage.checkYearText(emptyForm);
    }

    @Test
    @Label("22. Покупка заполненным буквами годом по дебетовой карте:")
    public void shouldNotBuyByDebit_IfYearChars() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                chars(), validOwner(), validCVV());
        debitPage.checkYearText(emptyForm);
    }

    @Test
    @Label("23. Покупка заполненным иероглифами годом по дебетовой карте:")
    public void shouldNotBuyByDebit_IfHieroglyphsYear() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                hieroglyphs(), validOwner(), validCVV());
        debitPage.checkYearText(incorrectFormat);
    }

    @Test
    @Label("24. Покупка заполненным истёкшим годом по дебетовой карте:")
    public void shouldNotBuyByDebit_IfExpiredYear() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                expiredYear(), validOwner(), validCVV());
        debitPage.checkYearText(expiredCardDate);
    }

    @Test
    @Label("25. Покупка заполненным недействующим годом по дебетовой карте:")
    public void shouldNotBuyByDebit_IfInactiveYear() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                inactiveYear(), validOwner(), validCVV());
        debitPage.checkYearText(incorrectCardDate);
    }

    @Test
    @Label("26. Покупка заполненным отрицательным годом по дебетовой карте:")
    public void shouldNotBuyByDebit_IfNegativeYear() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                negativeYear(), validOwner(), validCVV());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("27. Покупка заполненным специальными символами годом по дебетовой карте:")
    public void shouldNotBuyByDebit_IfSpecSymYear() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), validMonth(),
                specialSymbols(), validOwner(), validCVV());
        debitPage.checkYearText(emptyForm);
    }

    @Test
    @Label("28. Покупка с незаполненным месяцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfEmptyMonth() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), empty(),
                validYear(), validOwner(), validCVV());
        debitPage.checkMonthText(emptyForm);
    }

    @Test
    @Label("29. Покупка заполненным буквами месяцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCharsMonth() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), chars(),
                validYear(), validOwner(), validCVV());
        debitPage.checkMonthText(emptyForm);
    }

    @Test
    @Label("30. Покупка заполненным иероглифами месяцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfHieroglyphsMonth() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), hieroglyphs(),
                validYear(), validOwner(), validCVV());
        debitPage.checkMonthText(incorrectFormat);
    }

    @Test
    @Label("31. Покупка заполненным отрицательным месяцем по дебетовой карте:")
    public void shouldBuyByDebit_IfNegativeDigitsMonth() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), negativeDigits(),
                validYear(), validOwner(), validCVV());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("32. Покупка заполненным специальными символами месяцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfSpecSymMonth() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), specialSymbols(),
                validYear(), validOwner(), validCVV());
        debitPage.checkMonthText(emptyForm);
    }

    @Test
    @Label("33. Покупка заполненным нулями месяцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfZeroesMonth() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), zeroes(),
                validYear(), validOwner(), validCVV());
        debitPage.checkMonthText(incorrectCardDate);
    }


    @Test
    @Label("34. Покупка заполненным некорректным месяцем по дебетовой карте:")
    public void shouldNotBuyByDebit_IfInvalidMonth() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumber(), invalidMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkMonthText(incorrectCardDate);
    }

    @Test
    @Label("35. Покупка с незаполненным номером карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfEmptyCardNumber() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(empty(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkCardNumberText(emptyForm);
    }

    @Test
    @Label("36. Покупка заполненным буквами номером карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCharsCardNumber() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(chars(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkCardNumberText(incorrectFormat);
    }

    @Test
    @Label("37. Покупка заполненным нулями номером карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfZeroesCardNumber() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(zeroesCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkCardNumberText(incorrectValue);
    }

    @Test
    @Label("38. Покупка заполненным иероглифами номером карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfHieroglyphsCardNumber() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(hieroglyphs(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkCardNumberText(incorrectFormat);
    }

    @Test
    @Label("39. Покупка с длинной больше 16 символов номера карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCardNumberMore16() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumberPlus1(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("40. Покупка с длинной меньше 16 символов номера карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCardNumberLess16() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(approvedCardNumberMinus1(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkCardNumberText(incorrectFormat);
    }

    @Test
    @Label("41. Покупка с незарегистрированным номером карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCardNumberUnregistered() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(unregisteredCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkFail();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
        assertEquals("DECLINED", afterTransact.getStatus());
    }

    @Test
    @Label("42. Покупка со специальными символами номера карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfCardNumberSpecSym() {
        MainPage mainPage = new MainPage();
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(specialSymbols(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkCardNumberText(emptyForm);
    }

    @Test
    @Label("43. Покупка со статусом DECLINED номером карты по дебетовой карте:")
    public void shouldNotBuyByDebit_IfDeclined() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("payment_entity");
        var debitPage = mainPage.buyByDebit();
        debitPage.fillCardForms(declinedCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        debitPage.checkFail();
        var afterTransact = ConnectToDB.getLastPaymentData("payment_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
        assertEquals("DECLINED", afterTransact.getStatus());
    }
}
