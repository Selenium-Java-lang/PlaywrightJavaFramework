package pageObjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {
    Page page;
    private static final String Ticket_Count_ID = "(//button[normalize-space()='+'])[1]";
    private static final String Full_Name_PlaceHolder = "Your full name";
    private static final String Customer_Email = "customer-email";
    private static final String Customer_Phone = "phone";
    public BookingFormPage(Page page){
        this.page = page;

    }
    public void fillAndConfirm(String fullName, String email, String phone){
        page.locator(Ticket_Count_ID).click();
        page.getByPlaceholder(Full_Name_PlaceHolder).fill(fullName);
        page.getByTestId(Customer_Email).fill(email);
        page.getByLabel(Customer_Phone).fill(phone);
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        String successText = page.getByText("Booking Confirmed! \uD83C\uDF89").textContent();
        System.out.println(successText);
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    }

}
