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
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.ErrorNotifications.*;

public class CreditTests {

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
    @Label("4. Покупка с оплатой в кредит, картой со статусом APPROVED:")
    public void shouldBuyByCredit_Valid() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
        assertEquals("APPROVED", afterTransact.getStatus());
    }

    @Test
    @Label("5. Заполнение данных владельца через тире в кредит:")
    public void shouldBuyByCredit_IfOwnerThroughDash() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), dashedOwner(), validCVV());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("6. Заполнение данных владельца через пробел:")
    public void shouldBuyByCredit_IfOwnerThroughSpace() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }


    @Test
    @Label("44. Покупка без заполненных форм с данными по кредиту:")
    public void shouldNotBuyByCredit_IfEmptyForms() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.checkCardNumberText(emptyForm);
        creditPage.checkCVVText(emptyForm);
        creditPage.checkOwnerText(emptyForm);
        creditPage.checkMonthText(emptyForm);
        creditPage.checkYearText(emptyForm);
    }

    @Test
    @Label("45. Покупка с пустым кодом карты по кредиту:")
    public void shouldNotBuyByCredit_IfCVVEmpty() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), empty());
        creditPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("46. Покупка с кодом карты, заполненным только нулями по кредиту:")
    public void shouldNotBuyByCredit_IfCVVZeroes() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), zeroCVV());
        creditPage.checkCVVText(incorrectValue);
    }

    @Test
    @Label("47. Покупка с кодом карты, заполненным буквами по кредиту:")
    public void shouldNotBuyByCredit_IfCVVChars() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), chars());
        creditPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("48. Покупка с кодом карты, заполненным иероглифами по кредиту:")
    public void shouldNotBuyByCredit_IfCVVHieroglyphs() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), hieroglyphs());
        creditPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("49. Покупка с кодом карты с длинной больше трёх символов по кредиту:")
    public void shouldNotBuyByCredit_IfCVVMore3() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), fourDigitCVV());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("50. Покупка с кодом карты с длинной, меньше валидной по кредиту:")
    public void shouldNotBuyByCredit_IfCVVLess3() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), digits());
        creditPage.checkCVVText(incorrectFormat);
    }

    @Test
    @Label("51. Покупка с отрицательным кодом карты по кредиту:")
    public void shouldNotBuyByCredit_IfCVVNegative() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), negativeDigits());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("52. Покупка с кодом карты в котором специальные символы по кредиту:")
    public void shouldNotBuyByCredit_IfCVVSpecSym() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), validOwner(), specialSymbols());
        creditPage.checkCVVText(emptyForm);
    }

    @Test
    @Label("53. Покупка с незаполненным владельцем по кредиту:")
    public void shouldNotBuyByCredit_IfOwnerEmpty() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), empty(), validCVV());
        creditPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("54. Покупка с заполненным цифрами владельцем по кредиту:")
    public void shouldNotBuyByCredit_IfOwnerDigits() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), digits(), validCVV());
        creditPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("55. Покупка с заполненным иероглифами владельцем по кредиту:")
    public void shouldNotBuyByCredit_IfOwnerHieroglyphs() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), hieroglyphs(), validCVV());
        creditPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("56. Покупка с заполненным специальными символами владельцем по кредиту:")
    public void shouldNotBuyByCredit_IfOwnerSpecSym() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), specialSymbols(), validCVV());
        creditPage.checkOwnerText(emptyForm);
    }

    @Test
    @Label("57. Заполнение данных владельца кириллическими символами по кредиту:")
    public void shouldNotBuyByCredit_IfOwnerCyrillic() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                validYear(), cyrillicOwner(), validCVV());
        creditPage.checkOwnerText(incorrectFormat);
    }

    @Test
    @Label("58. Покупка с незаполненным годом по кредиту:")
    public void shouldNotBuyByCredit_IfEmptyYear() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                empty(), validOwner(), validCVV());
        creditPage.checkYearText(emptyForm);
    }

    @Test
    @Label("59. Покупка заполненным буквами годом по кредиту:")
    public void shouldNotBuyByCredit_IfYearEmptyChars() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                chars(), validOwner(), validCVV());
        creditPage.checkYearText(emptyForm);
    }

    @Test
    @Label("60. Покупка заполненным иероглифами годом по кредиту:")
    public void shouldNotBuyByCredit_IfHieroglyphsYear() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                hieroglyphs(), validOwner(), validCVV());
        creditPage.checkYearText(emptyForm);
    }

    @Test
    @Label("61. Покупка заполненным истёкшим годом по кредиту:")
    public void shouldNotBuyByCredit_IfExpiredYear() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                expiredYear(), validOwner(), validCVV());
        creditPage.checkYearText(expiredCardDate);
    }

    @Test
    @Label("62. Покупка заполненным недействующим годом по кредиту:")
    public void shouldNotBuyByCredit_IfInactiveYear() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                inactiveYear(), validOwner(), validCVV());
        creditPage.checkYearText(incorrectCardDate);
    }

    @Test
    @Label("63. Покупка заполненным отрицательным годом по кредиту:")
    public void shouldNotBuyByCredit_IfNegativeYear() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                negativeYear(), validOwner(), validCVV());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("64. Покупка заполненным специальными символами годом по кредиту:")
    public void shouldNotBuyByCredit_IfSpecSymYear() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), validMonth(),
                specialSymbols(), validOwner(), validCVV());
        creditPage.checkYearText(emptyForm);
    }

    @Test
    @Label("65. Покупка с незаполненным месяцем по кредиту:")
    public void shouldNotBuyByCredit_IfEmptyMonth() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), empty(),
                validYear(), validOwner(), validCVV());
        creditPage.checkMonthText(emptyForm);
    }

    @Test
    @Label("66. Покупка заполненным буквами месяцем по кредиту:")
    public void shouldNotBuyByCredit_IfCharsMonth() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), chars(),
                validYear(), validOwner(), validCVV());
        creditPage.checkMonthText(emptyForm);
    }

    @Test
    @Label("67. Покупка заполненным иероглифами месяцем по кредиту:")
    public void shouldNotBuyByCredit_IfHieroglyphsMonth() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), hieroglyphs(),
                validYear(), validOwner(), validCVV());
        creditPage.checkMonthText(emptyForm);
    }

    @Test
    @Label("68. Покупка заполненным отрицательным месяцем по кредиту:")
    public void shouldNotBuyByCredit_IfNegativeDigitsMonth() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), negativeDigits(),
                validYear(), validOwner(), validCVV());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("69. Покупка заполненным специальными символами месяцем по кредиту:")
    public void shouldNotBuyByCredit_IfSpecSymMonth() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), specialSymbols(),
                validYear(), validOwner(), validCVV());
        creditPage.checkMonthText(emptyForm);
    }

    @Test
    @Label("70. Покупка заполненным нулями месяцем по кредиту:")
    public void shouldNotBuyByCredit_IfZeroesMonth() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), zeroes(),
                validYear(), validOwner(), validCVV());
        creditPage.checkMonthText(incorrectCardDate);
    }


    @Test
    @Label("71. Покупка заполненным несуществующим месяцем по кредиту:")
    public void shouldNotBuyByCredit_IfInvalidMonth() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumber(), invalidMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkMonthText(incorrectCardDate);
    }

    @Test
    @Label("72. Покупка с незаполненным номером карты по кредиту:")
    public void shouldNotBuyByCredit_IfEmptyCardNumber() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(empty(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkCardNumberText(emptyForm);
    }

    @Test
    @Label("73. Покупка заполненным буквами номером карты по кредиту:")
    public void shouldNotBuyByCredit_IfCharsCardNumber() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(chars(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkCardNumberText(emptyForm);
    }

    @Test
    @Label("74. Покупка заполненным нулями номером карты по кредиту:")
    public void shouldNotBuyByCredit_IfZeroesCardNumber() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(zeroesCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkCardNumberText(incorrectValue);
    }

    @Test
    @Label("75. Покупка заполненным иероглифами номером карты по кредиту:")
    public void shouldNotBuyByCredit_IfHieroglyphsCardNumber() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(hieroglyphs(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkCardNumberText(emptyForm);
    }

    @Test
    @Label("76. Покупка с длинной больше 16 символов номера карты по кредиту:")
    public void shouldNotBuyByCredit_IfCardNumberMore16() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumberPlus1(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkSuccess();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
    }

    @Test
    @Label("77. Покупка с длинной меньше 16 символов номера карты по кредиту:")
    public void shouldNotBuyByCredit_IfCardNumberLess16() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(approvedCardNumberMinus1(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkCardNumberText(incorrectFormat);
    }

    @Test
    @Label("78. Покупка с незарегистрированным номером карты по кредиту:")
    public void shouldNotBuyByCredit_IfCardNumberUnregistered() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        creditPage.fillCardForms(unregisteredCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkFail();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
        assertEquals("DECLINED", afterTransact.getStatus());
    }

    @Test
    @Label("79. Покупка со специальными символами номера карты по кредиту:")
    public void shouldNotBuyByCredit_IfCardNumberSpecSym() {
        MainPage mainPage = new MainPage();
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(specialSymbols(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkCardNumberText(emptyForm);
    }

    @Test
    @Label("80. Покупка со статусом DECLINED номером карты по кредиту:")
    public void shouldNotBuyByCredit_IfDeclined() {
        MainPage mainPage = new MainPage();
        var beforeTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        var creditPage = mainPage.buyByCredit();
        creditPage.fillCardForms(declinedCardNumber(), validMonth(),
                validYear(), validOwner(), validCVV());
        creditPage.checkFail();
        var afterTransact = ConnectToDB.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeTransact.getId(), afterTransact.getId());
        assertEquals("DECLINED", afterTransact.getStatus());
    }
}
