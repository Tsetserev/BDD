package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

    private static final ElementsCollection cards = $$(".list__item div");
    private static final String balanceStart = "баланс: ";
    private static final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public static int getCardBalance(String id) {
        String cardBalance = String.valueOf(cards.findBy(Condition.attribute("data-test-id")));
        return extractBalance(cardBalance);
    }

    private static int extractBalance(String cardBalance) {
        val start = cardBalance.indexOf(balanceStart);
        val finish = cardBalance.indexOf(balanceFinish);
        val value = cardBalance.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public static DashboardPage getMoneyTransferFromSecondToFirst() {
        $$("[data-test-id=\"action-deposit\"]").first().click();
        $("[data-test-id=\"amount\"] input").setValue(String.valueOf(1000));
        $("[data-test-id=\"from\"] input").setValue(DataHelper.getSecondCardInfo().getNumber());
        $("[data-test-id=\"action-transfer\"]").click();
        return new DashboardPage();
    }

    public static DashboardPage getMoneyTransferFromSecondToFirstWithMistakeInAccountNumber() {
        $$("[data-test-id=\"action-deposit\"]").first().click();
        $("[data-test-id=\"amount\"] input").setValue(String.valueOf(1000));
        $("[data-test-id=\"from\"] input").setValue("5559 0000 0000 0003");
        $("[data-test-id=\"action-transfer\"]").click();
        $("[data-test-id=\"error-notification\"]")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Произошла ошибка"));
        return new DashboardPage();
    }

    public static DashboardPage getMoneyTransferFromSecondToFirstWithMissingAccountNumber() {
        $$("[data-test-id=\"action-deposit\"]").first().click();
        $("[data-test-id=\"amount\"] input").setValue(String.valueOf(1000));
        $("[data-test-id=\"from\"] input").setValue("");
        $("[data-test-id=\"action-transfer\"]").click();
        $("[data-test-id=\"error-notification\"]")
                .shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Произошла ошибка"));
        return new DashboardPage();
    }
    public static DashboardPage getMoneyTransferFromFirstToSecond() {
        $$("[data-test-id=\"action-deposit\"]").last().click();
        $("[data-test-id=\"amount\"] input").setValue(String.valueOf(5000));
        $("[data-test-id=\"from\"] input").setValue(DataHelper.getFirstCardInfo().getNumber());
        $("[data-test-id=\"action-transfer\"]").click();
        return new DashboardPage();
    }

    public static DashboardPage cancelMoneyTransfer() {
        $$("[data-test-id=\"action-deposit\"]").last().click();
        $("[data-test-id=\"amount\"] input").setValue(String.valueOf(5000));
        $("[data-test-id=\"from\"] input").setValue(DataHelper.getFirstCardInfo().getNumber());
        $x("//*[contains(text(),'Отмена')]").click();
        return new DashboardPage();
    }

    public static DashboardPage reloadBalance() {
        $("[data-test-id=\"action-reload\"]").click();
        return new DashboardPage();
    }

}

