package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

abstract class TransactionPage {
    private final SelenideElement cardNumber_Field = $$(".input__inner").findBy(text("Номер карты")).$("input");
    private final SelenideElement month_Field = $$(".input__inner").findBy(text("Месяц")).$("input");
    private final SelenideElement year_Field = $$(".input__inner").findBy(text("Год")).$("input");
    private final SelenideElement cardOwner_Field = $$(".input__inner").findBy(text("Владелец")).$("input");
    private final SelenideElement cvv_Field = $$(".input__inner").findBy(text("CVC/CVV")).$("input");
    private final SelenideElement sendRequestBtn = $$("button").findBy(text("Продолжить"));


    public void fillCardForms(String cardNumber, String month, String year, String cardOwner, String cvv) {
        cardNumber_Field.sendKeys(cardNumber);
        month_Field.sendKeys(month);
        year_Field.sendKeys(year);
        cardOwner_Field.sendKeys(cardOwner);
        cvv_Field.sendKeys(cvv);
        sendRequestBtn.click();
    }

    public void checkSuccess() {
        $$(".notification__content").findBy(text("Операция одобрена Банком.")).shouldBe(visible, Duration.ofSeconds(30));
    }

    public void checkFail() {
        $$(".notification__content").findBy(text("Ошибка! Банк отказал в проведении операции.")).shouldBe(visible, Duration.ofSeconds(30));
    }

    public void checkCardNumberText(String toCheck) {
        $$(".input__inner").findBy(text("Номер карты")).$(".input__sub").shouldHave(text(toCheck), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkMonthText(String toCheck) {
        $$(".input__inner").findBy(text("Месяц")).$(".input__sub").shouldHave(text(toCheck), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkYearText(String toCheck) {
        $$(".input__inner").findBy(text("Год")).$(".input__sub").shouldHave(text(toCheck), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkOwnerText(String toCheck) {
        $$(".input__inner").findBy(text("Владелец")).$(".input__sub").shouldHave(text(toCheck), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkCVVText(String toCheck) {
        $$(".input__inner").findBy(text("CVC/CVV")).$(".input__sub").shouldHave(text(toCheck), Duration.ofSeconds(3)).shouldBe(visible);
    }
}