import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$$;

public class DateChangeTest {
    private Faker faker;


    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldTestDateChange() {
        faker = new Faker(new Locale("ru"));

        String city = faker.address().cityName();
        Date initialDate = faker.date().future(60, 3, TimeUnit.DAYS);
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String initialMeetingDate = formatter.format(initialDate);
        Date changedDate = faker.date().future(90, 3, TimeUnit.DAYS);
        String changedMeetingDate = formatter.format(changedDate);
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().phoneNumber();
        String phoneFmt = String.format(phone, "+7 ### ### ## ##");
        phone = phoneFmt;


        SelenideElement form = $("form");
        form.$(By.cssSelector("[data-test-id='city'] input")).setValue(city);
        form.$("[data-test-id='date'] input").doubleClick().sendKeys("\b");
        form.$("[data-test-id='date'] .input__control").setValue(initialMeetingDate);
        form.$("[data-test-id='name'] .input__control").setValue(name);
        form.$("[data-test-id='phone'] .input__control").setValue(phone);
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$$("button .button__text").first().click();
        $(By.cssSelector("[data-test-id='success-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + initialMeetingDate));
        form.$("[data-test-id='date'] input").doubleClick().sendKeys("\b");
        form.$("[data-test-id='date'] .input__control").setValue(changedMeetingDate);
        $$("button .button__text").first().click();
        $(By.cssSelector("[data-test-id='replan-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("У вас уже запланирована встреча на другую дату. Перепланировать? Перепланировать"));
        $$("button .button__text").last().click();
        $(By.cssSelector("[data-test-id='success-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + changedMeetingDate));

    }

    @Test
    void shouldTestDeliveryDateChange() {
        var city = DataGenerator.generateCity("ru");
        var initialMeetingDate = DataGenerator.generateDate(4);
        var changedMeetingDate = DataGenerator.generateDate(7);
        var name = DataGenerator.generateName("ru");
        var phone = DataGenerator.generatePhone("ru");

        SelenideElement form = $("form");
        form.$(By.cssSelector("[data-test-id='city'] input")).setValue(city);
        form.$("[data-test-id='date'] input").doubleClick().sendKeys("\b");
        form.$("[data-test-id='date'] .input__control").setValue(initialMeetingDate);
        form.$("[data-test-id='name'] .input__control").setValue(name);
        form.$("[data-test-id='phone'] .input__control").setValue(phone);
        form.$("[data-test-id='agreement'] .checkbox__box").click();
        form.$$("button .button__text").first().click();
        $(By.cssSelector("[data-test-id='success-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + initialMeetingDate));
        form.$("[data-test-id='date'] input").doubleClick().sendKeys("\b");
        form.$("[data-test-id='date'] .input__control").setValue(changedMeetingDate);
        $$("button .button__text").first().click();
        $(By.cssSelector("[data-test-id='replan-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("У вас уже запланирована встреча на другую дату. Перепланировать? Перепланировать"));
        $$("button .button__text").last().click();
        $(By.cssSelector("[data-test-id='success-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(exactText("Встреча успешно запланирована на " + initialMeetingDate));
    }

}
