package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage extends TransactionPage {

    private final SelenideElement creditHeading = $$("h3").find((text("Кредит по данным карты")));

    public CreditPage() {
        super();
        creditHeading.shouldBe(Condition.visible);
    }
}
