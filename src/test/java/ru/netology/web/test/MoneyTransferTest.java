package ru.netology.web.test;

import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.*;

import static com.codeborne.selenide.Selenide.*;

class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() {
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var DashboardPage = ru.netology.web.page.DashboardPage.getMoneyTransferFromSecondToFirst();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV2() {
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var DashboardPage = ru.netology.web.page.DashboardPage.getMoneyTransferFromFirstToSecond();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV3Cancel() {
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var DashboardPage = ru.netology.web.page.DashboardPage.cancelMoneyTransfer();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV4Reload() {
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var DashboardPage = ru.netology.web.page.DashboardPage.reloadBalance();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV5MistakeInAccountNumber() {
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var DashboardPage = ru.netology.web.page.DashboardPage.getMoneyTransferFromSecondToFirstWithMistakeInAccountNumber();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV5TheAccountNumberIsMissing() {
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var DashboardPage = ru.netology.web.page.DashboardPage.getMoneyTransferFromSecondToFirstWithMissingAccountNumber();
    }
}


