package pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {
    Page page;
    private static final String event_Title = "#event-title-input";
    private static final String event_Description = "textarea[placeholder = 'Describe the event…']";
    private static final String event_Category = "//select[@id = 'category']";
    private static final String event_City = "city";
    private static final String event_Venue = "venue";
    private static final String event_Date = "Event Date & Time";
    private static final String ticket_Price = "Price ($)*";
    private static final String total_Seats = "#total-seats";
    private static final String add_EventBtn = "add-event-btn";
    private static final String Success_TOAST = "Event created!";
    public AdminEventsPage(Page page){
        this.page = page;
    }
    public void goTo(){
        //creating an event providing direct link of that event creation
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }
    public void createEvent(String eventTitle, String eventDescription,String eventCategory,
                            String eventCity, String eventVenue,String eventDate, String ticketPrice
    ,String totalSeats){
        page.locator(event_Title).fill(eventTitle,
                new Locator.FillOptions().setTimeout(9000));
        page.locator(event_Description).fill(eventDescription);
        page.locator(event_Category).selectOption(eventCategory);
        page.getByLabel(event_City).fill(eventCity);
        page.getByText(event_Venue).fill(eventVenue);
        // Fill the date and time using the label
        page.getByLabel(event_Date).fill(eventDate);
        page.getByText(ticket_Price).fill(ticketPrice);
        page.locator(total_Seats).fill(totalSeats);
        page.getByTestId(add_EventBtn).click();
        //event created
        assertThat(page.getByText(Success_TOAST)).isVisible();
    }
}
