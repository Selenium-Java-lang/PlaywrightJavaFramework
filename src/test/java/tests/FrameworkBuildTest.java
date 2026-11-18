package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pageObjects.*;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends TestBase{

    //String eventTitle = "Infosys Interview";
    //invoke browser and open it in icognito browser and land to home page
    @Test(groups = {"framework"},dataProvider = "jsonDataProvider", dataProviderClass = utils.DataProviderUtil.class)
    public void DemoTest(HashMap<String,String> input) throws IOException {
        LoginPage loginPage = new LoginPage(page,baseUrl);
        DashboardPage dashboardPage = loginPage.loginApplication();
        AdminEventsPage adminEventsPage = dashboardPage.waitForEventsToLoad();
        adminEventsPage.goTo();
        adminEventsPage.createEvent(input.get("titleName"),
                input.get("eventDescription"),
                input.get("eventCategory"),
                input.get("eventCity"),
                input.get("eventVenue"),
                input.get("eventDate"),input.get("ticketPrice"),input.get("totalSeats"));
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        eventsPage.waitForEventsToLoad();
        Locator targetCard = eventsPage.findEventCard(input.get("titleName"));
        int seatNumBeforeBooking = eventsPage.getSeatsCount(targetCard);
        BookingFormPage bookingFormPage = eventsPage.proceedToBookingEvent(targetCard);
        bookingFormPage.fillAndConfirm(input.get("fullName"),input.get("email"),input.get("phone"));






//find newly created event in the events page


//visibility of the card which i added




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
//        Locator targetCardAfterBookings = eventCardsAfterBookings.filter(new Locator.FilterOptions()
//                .setHasText("Infosys Interview"));
//        assertThat(targetCardAfterBookings).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(9000));
//        String seatsTextAfterBookings = targetCard.getByText("seats").innerText();
//        System.out.println(seatsTextAfterBookings);
//        //afterbookings < BeforeBookings
//        int seatNumAfterBooking = Integer.parseInt(seatsTextAfterBookings.split(" ")[0]);
//
//        Assert.assertTrue(seatNumBeforeBooking > seatNumAfterBooking);

    }

}
