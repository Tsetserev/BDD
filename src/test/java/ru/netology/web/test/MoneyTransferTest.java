package ru.netology.web.test;

import com.codeborne.selenide.Configuration;

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

    @Test
    void shouldTransferMoneyFromSecondCardToFirstCard() {
        var dashboardPage = new DashboardPage();

        int expectedFC = dashboardPage.getCardBalance("1") + 1000;
        int expectedSC = dashboardPage.getCardBalance("2") - 1000;

        dashboardPage.getMoneyTransferFromSecondToFirst();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("2"), "1000");

        int actualFC = dashboardPage.getCardBalance("1");
        int actualSC = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(expectedFC, actualFC);
        Assertions.assertEquals(expectedSC, actualSC);
    }

    @Test
    void shouldTransferMoneyFromFirstCardToSecondCard() {
        var dashboardPage = new DashboardPage();

        int expectedSC = dashboardPage.getCardBalance("2") + 5000;
        int expectedFC = dashboardPage.getCardBalance("1") - 5000;

        dashboardPage.getMoneyTransferFromFirstToSecond();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("1"), "5000");

        int actualSC = dashboardPage.getCardBalance("2");
        int actualFC = dashboardPage.getCardBalance("1");

        Assertions.assertEquals(expectedSC, actualSC);
        Assertions.assertEquals(expectedFC, actualFC);
    }

    @Test
    void shouldReloadCardBalance() {
        var dashboardPage = new DashboardPage();

        int expectedFC = dashboardPage.getCardBalance("1");
        int expectedSC = dashboardPage.getCardBalance("2");

        dashboardPage.reloadBalance();

        int actualFC = dashboardPage.getCardBalance("1");
        int actualSC = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(expectedFC, actualFC);
        Assertions.assertEquals(expectedSC, actualSC);
    }

    @Test
    void shouldCancelMoneyTransfer() {
        var dashboardPage = new DashboardPage();

        int expectedFC = dashboardPage.getCardBalance("1");
        int expectedSC = dashboardPage.getCardBalance("2");

        dashboardPage.getMoneyTransferFromSecondToFirst();
        dashboardPage.cancelMoneyTransfer();

        int actualFC = dashboardPage.getCardBalance("1");
        int actualSC = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(expectedFC, actualFC);
        Assertions.assertEquals(expectedSC, actualSC);
    }

    @Test
    void shouldNotTransferIfAccountIsNotSpecified() {
        var dashboardPage = new DashboardPage();

        dashboardPage.getMoneyTransferFromSecondToFirst();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo(""), "1000");

        moneyTransferPage.getError();
    }

    @Test
    void shouldTransferFullAmountFromAccount() {
        var dashboardPage = new DashboardPage();
        String balance = String.valueOf((dashboardPage.getCardBalance("2")));

        int expectedSC = 0;
        int expectedFC = dashboardPage.getCardBalance("1") + dashboardPage.getCardBalance("2");

        dashboardPage.getMoneyTransferFromSecondToFirst();
        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("2"), balance);

        int actualFC = dashboardPage.getCardBalance("1");
        int actualSC = dashboardPage.getCardBalance("2");

        Assertions.assertEquals(expectedFC, actualFC);
        Assertions.assertEquals(expectedSC, actualSC);
    }

    @Test
    void shouldTryToTransferFromAccountWithBalanceEqualZero() {
        var dashboardPage = new DashboardPage();
        int checkCard = dashboardPage.getCardBalance("1");
        String cardID1;
        String cardID2;
        String balance1;
        String balance2;

        if (checkCard > 0) {
            cardID1 = "1";
            cardID2 = "2";
            balance1 = String.valueOf(dashboardPage.getCardBalance(cardID1));
            balance2 = String.valueOf(dashboardPage.getCardBalance(cardID2));
            dashboardPage.getMoneyTransferFromFirstToSecond();
        } else {
            cardID1 = "2";
            cardID2 = "1";
            balance1 = String.valueOf(dashboardPage.getCardBalance(cardID2));
            balance2 = String.valueOf(dashboardPage.getCardBalance(cardID1));
            dashboardPage.getMoneyTransferFromSecondToFirst();
        }
        int expectedFC = -100;
        int expectedSC = Integer.parseInt(balance2) + Integer.parseInt(balance1) + 100;

        var moneyTransferPage = new MoneyTransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo(cardID1), String.valueOf(Integer.parseInt(balance1) + 100));

        int actualFC = dashboardPage.getCardBalance(cardID1);
        int actualSC = dashboardPage.getCardBalance(cardID2);

        Assertions.assertEquals(expectedFC, actualFC);
        Assertions.assertEquals(expectedSC, actualSC);
    }
}