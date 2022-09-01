package ru.netology.web.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import com.codeborne.selenide.commands.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.*;

import static com.codeborne.selenide.Selenide.*;

class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        Configuration.holdBrowserOpen = true;
    }
    private String intToString(int cardBalance) {
        return Integer.toString(cardBalance);
    }
    @Test
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        var dashboardPage = new DashboardPage();
        var moneyTransferPage = new MoneyTransferPage();

        int expected = dashboardPage.getCardBalance("1") + 1000;

        dashboardPage.getMoneyTransferFromSecondToFirst();
        moneyTransferPage.moneyTransfer(DataHelper.getSecondCardInfo(), "1000");
        int actual = dashboardPage.getCardBalance("1");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecondCard() {
        var dashboardPage = new DashboardPage();
        var moneyTransferPage = new MoneyTransferPage();

        int expected = dashboardPage.getCardBalance("2") + 5000;

        dashboardPage.getMoneyTransferFromFirstToSecond();
        moneyTransferPage.moneyTransfer(DataHelper.getFirstCardInfo(), "5000");
        int actual = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldReloadCardBalance() {
        var dashboardPage = new DashboardPage();
        var moneyTransferPage = new MoneyTransferPage();

        int expected = dashboardPage.getCardBalance("2");

        dashboardPage.reloadBalance();
        int actual = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldCancelMoneyTransfer() {
        var dashboardPage = new DashboardPage();
        var moneyTransferPage = new MoneyTransferPage();

        int expected = dashboardPage.getCardBalance("2");

        dashboardPage.getMoneyTransferFromSecondToFirst();
        dashboardPage.cancelMoneyTransfer();
        int actual = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNotTransferIfAccountIsNotSpecified() {
        var dashboardPage = new DashboardPage();
        var moneyTransferPage = new MoneyTransferPage();

        dashboardPage.getMoneyTransferFromSecondToFirst();
        moneyTransferPage.moneyTransferWithKnownMistake("", "1000");

        moneyTransferPage.getError().shouldBe(Condition.visible);
    }

    @Test
    void shouldTransferFullAmountFromAccount() {
        var dashboardPage = new DashboardPage();
        var transferPage = new MoneyTransferPage();
        String balance = intToString(dashboardPage.getCardBalance("1"));

        int expected = 0;

        dashboardPage.getMoneyTransferFromFirstToSecond();
        transferPage.moneyTransfer(DataHelper.getFirstCardInfo(), balance);
        int actual = dashboardPage.getCardBalance("1");

        Assertions.assertEquals(expected, actual);
    }
}
