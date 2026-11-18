package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {
    Playwright playwright;
    Browser browser;
    Page page;
    @BeforeMethod
    public void setUp(){
        playwright = Playwright.create();
        //to run in firefox browser
        // Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //to run in chrome browser
         browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // to run in webkit
        //Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // to run in our own system browser version / browser
        //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
       page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        // to wait for locator we can use
        page.setDefaultTimeout(9000);// it will applicable for all the locators
        //global assertion timeout 7 seconds
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);
    }
    //invoke browser and open it in icognito browser and land to home page
    @Test
    public void DemoTest(){

        System.out.println(page.title());
        // by default wait time for actions is : 10 seconds and for assertions 5 seconds
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        //locators
        //page.getByLabel("Email").fill("chuha@gmail.com");
        page.getByPlaceholder("you@email.com").fill("chuha@gmail.com");
        page.getByLabel("password").fill("QAWSplm@1234");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions()
                .setName("Browse Events →"))).isVisible();
        //creating an event providing direct link of that event creation
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
        page.locator("#event-title-input").fill("Infosys Interview",
                new Locator.FillOptions().setTimeout(9000));
        page.locator("textarea[placeholder = 'Describe the event…']").fill("first playwright interview with java");
        page.locator("//select[@id = 'category']").selectOption("Workshop");
        page.getByLabel("city").fill("Bengaluru");
        page.getByText("venue").fill("Element Mall Banaswadi Bengaluru");
        // Fill the date and time using the label
        page.getByLabel("Event Date & Time").fill("2026-12-25T14:30");
        page.getByText("Price ($)*").fill("2000");
        page.locator("#total-seats").fill("200");
        page.getByTestId("add-event-btn").click();
        //event created
        assertThat(page.getByText("Event created!")).isVisible();
//find newly created event in the events page
        page.locator("#nav-events")
                .click(new Locator.ClickOptions().setTimeout(5000));
        Locator eventCards = page.getByTestId("event-card");
        // Wait until at least the first event card is attached to the DOM
        eventCards.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        System.out.println(eventCards.count());
//visibility of the card which i added
        Locator targetCard = eventCards.filter(new Locator.FilterOptions()
                .setHasText("Infosys Interview"));
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(9000));
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        int seatNumBeforeBooking = Integer.parseInt(seatsText.split( " ")[0]);
        targetCard.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Book Now")).click();
        page.locator("(//button[normalize-space()='+'])[1]").click();
        page.getByPlaceholder("Your full name").fill("Prem singh Dhumal");
        page.getByTestId("customer-email").fill("chuha@gmail.com");
        page.getByLabel("phone").fill("9778899987");
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        String successText = page.getByText("Booking Confirmed! \uD83C\uDF89").textContent();
        System.out.println(successText);
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("View My Bookings")).click();
        //verify in booking history
        Locator bookingCards = page.getByTestId("booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();
        //seat count reduction check
        page.locator("#nav-events")
                .click();
        page.waitForTimeout(1000);// to create random wait time
        Locator eventCardsAfterBookings = page.getByTestId("event-card");
        // Wait until at least the first event card is attached to the DOM
        eventCardsAfterBookings.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
//visibility of the card which i added
        Locator targetCardAfterBookings = eventCardsAfterBookings.filter(new Locator.FilterOptions()
                .setHasText("Infosys Interview"));
        assertThat(targetCardAfterBookings).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(9000));
        String seatsTextAfterBookings = targetCard.getByText("seats").innerText();
        System.out.println(seatsTextAfterBookings);
        //afterbookings < BeforeBookings
        int seatNumAfterBooking = Integer.parseInt(seatsTextAfterBookings.split(" ")[0]);

        Assert.assertTrue(seatNumBeforeBooking > seatNumAfterBooking);

    }
    @AfterMethod
    public void tearDown(){
        // 6. Clean up resources
        //context.close();
        browser.close();
        playwright.close();
    }
}
