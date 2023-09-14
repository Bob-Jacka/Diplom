package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final SelenideElement heading = $(".heading_size_l");
    private static final SelenideElement debitCardBuyBtn = $(byText("Купить")).parent().parent();
    private static final SelenideElement creditCardBuyBtn = $(byText("Купить в кредит"));

    public MainPage() {
        heading.shouldBe(Condition.visible);
        debitCardBuyBtn.shouldBe(Condition.visible);
        creditCardBuyBtn.shouldBe(Condition.visible);
    }

    public DebitPage buyByDebit() {
        debitCardBuyBtn.click();
        return new DebitPage();
    }

    public CreditPage buyByCredit() {
        creditCardBuyBtn.click();
        return new CreditPage();
    }
}
