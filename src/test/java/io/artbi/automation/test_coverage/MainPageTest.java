package io.artbi.automation.test_coverage;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.artbi.automation.test_coverage.webdriver.listener.LocatorListener;
import io.qameta.allure.selenide.AllureSelenide;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.IOException;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class MainPageTest {
    private LocatorListener locatorsListener;
    MainPage mainPage = new MainPage();

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1280x800";
        Configuration.holdBrowserOpen = false;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        open();
        WebDriver driver = WebDriverRunner.getWebDriver();
        final EventFiringWebDriver eventFiringWebDriver = new EventFiringWebDriver(driver);
        locatorsListener = new LocatorListener();
        eventFiringWebDriver.register(locatorsListener);
        WebDriverRunner.setWebDriver(eventFiringWebDriver);
        open(MainPage.URL);
    }

    @Test
    public void search() {
        mainPage.searchField.shouldBe(visible)
                .setValue("werjh");

        ElementsCollection results = mainPage.searchResult.shouldBe(visible)
                .$$x("./li");

        Assertions.assertThat(results).allSatisfy(result -> {
            result.has(text("цукор"));
        });

        results.shouldHave(sizeGreaterThan(1))
                .get(0)
                .click();


    }


    @AfterEach
    public void stopDriver() throws IOException {
        locatorsListener.addAttachment();
    }
}


