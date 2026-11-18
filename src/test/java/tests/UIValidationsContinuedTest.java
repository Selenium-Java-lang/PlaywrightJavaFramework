package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class UIValidationsContinuedTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    @BeforeMethod
    public void setUp(){
        playwright = Playwright.create();
         browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice");
    }
    @AfterMethod
    public void tearDown(){
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
        // 6. Clean up resources
        context.close();
        browser.close();
        playwright.close();
    }
    @Test
    public void popUPValidation(){
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        // to accept alert
        page.onDialog(Dialog::accept);
        // to dismiss the alert
        //page.onDialog(Dialog->Dialog.dismiss());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Alert")).click();
        //mouse hover action
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Mouse Hover")).hover();
        page.waitForTimeout(3000);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();
        //frame handling in playwright
        FrameLocator framePage = page.frameLocator("#courses-iframe");
        framePage.getByRole(AriaRole.LINK,
                new FrameLocator.GetByRoleOptions().setName("Learning Paths")).click();
        String textCheck = framePage.locator(".inner-box h1").textContent();
        System.out.println(textCheck);


    }
    @Test
    public void screenShotTest(){
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));
        Locator displayedEditBox = page.getByPlaceholder("Hide/show Example");
        displayedEditBox.screenshot(new Locator
                .ScreenshotOptions().setPath(Paths.get("editboxscreenshot.png")));
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().
                setPath(Paths.get("pagePostscreenshot.png")));
    }
}
