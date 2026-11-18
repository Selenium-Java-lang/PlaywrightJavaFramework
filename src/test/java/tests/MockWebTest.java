package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {
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
    @Test(description = "sandbox banner is shown when 6 events are returned")
    public void DemoTest() {


        page.getByPlaceholder("you@email.com").fill("chuha@gmail.com");
        page.getByLabel("password").fill("QAWSplm@1234");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions()
                .setName("Browse Events →"))).isVisible();
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/Resources/events_6.json"))
        ));
        //creating an event providing direct link of that event creation
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        // page.waitForTimeout(7000);
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        Assert.assertEquals(eventCards.count(), 6);
        assertThat(page.locator(".mx-1").first()).isVisible();
        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/Resources/events_4.json"))
        ));
        //creating an event providing direct link of that event creation
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        Locator eventCards1 = page.getByTestId("event-card");
        assertThat(eventCards1.first()).isVisible();
        Assert.assertEquals(eventCards.count(), 4);
        assertThat(page.locator(".mx-1").first()).isHidden();
    }
        @Test
        public void routeResumeTest() {
        //login code for event page
            page.getByPlaceholder("you@email.com").fill("chuha@gmail.com");
            page.getByLabel("password").fill("QAWSplm@1234");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
            assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions()
                    .setName("Browse Events →"))).isVisible();
            //route.resume()
            page.getByTestId("nav-bookings").click();
            page.route("**/api/bookings**", route -> route.resume(
                    new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/72116")
            ));

            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();
            //assertThat(page.getByText("Access Denied")).isVisible();

        }


    @AfterMethod
    public void tearDown(){
        // 6. Clean up resources
        //context.close();
        browser.close();
        playwright.close();
    }
}
