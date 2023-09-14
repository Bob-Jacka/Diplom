package ru.netology.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class DebitPage extends TransactionPage {

    private final SelenideElement debitHeading = $$("h3").find((text("Оплата по карте")));

    public DebitPage() {
        super();
        debitHeading.shouldBe(Condition.visible);
    }
}
