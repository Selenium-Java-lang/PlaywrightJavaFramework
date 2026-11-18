package pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {
    Page page;
    private static final String eventsBtn = "#nav-events";
    public EventsPage(Page page){
        this.page = page;
    }
    public void goTo(){
        page.locator(eventsBtn)
                .click(new Locator.ClickOptions().setTimeout(5000));

    }
    public Locator waitForEventsToLoad(){
        Locator eventCards = page.getByTestId("event-card");
        // Wait until at least the first event card is attached to the DOM
        eventCards.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.ATTACHED));
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }
    public Locator findEventCard(String titleCard){
        Locator eventCards = waitForEventsToLoad();
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));
        assertThat(targetCard).isVisible();
        return targetCard;
    }
    public int getSeatsCount(Locator targetCard){

        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(9000));
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);

        return Integer.parseInt(seatsText.split( " ")[0]);
    }
    public BookingFormPage proceedToBookingEvent(Locator targetCard){
        //Locator targetCard = findEventCard(titleCard);
        targetCard.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Book Now")).click();
        return new BookingFormPage(page);
    }
}
