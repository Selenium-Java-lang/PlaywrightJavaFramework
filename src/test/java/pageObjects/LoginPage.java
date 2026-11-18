package pageObjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {
    Page page;
    String baseUrl;
    private static final String email_placeholder = "you@email.com";
    private static final String password_label = "password";
    public LoginPage(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }
    public DashboardPage loginApplication() throws IOException {

        page.navigate(baseUrl);
        System.out.println(page.title());
        // by default wait time for actions is : 10 seconds and for assertions 5 seconds
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        //locators
        //page.getByLabel("Email").fill("chuha@gmail.com");
        page.getByPlaceholder(email_placeholder).fill("chuha@gmail.com");
        page.getByLabel(password_label).fill("QAWSplm@1234");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage;
    }
}
