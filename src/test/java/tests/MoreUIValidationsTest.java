package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationsTest {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        //starting trace to record test and generate trace zip file
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractice/");



    }
    @AfterMethod
    public void tearDown(){
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }
    @Test(groups = {"smoke"})
    public void childWindowHandle(){
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(()-> blinkingTexts.first().click());
        //blinkingTexts.first().click();
        newPage.waitForLoadState();
       String childText =  newPage.locator(".red").textContent();
       String emailId = childText.split("at ")[1].split(" ")[0];
       System.out.println(emailId);
       //page.getByLabel("username").fill(emailId);
       page.getByLabel("Username").fill("rahulshettyacademy");
       page.getByLabel("Password:").fill("Learning@830$3mK2");

    }
    @Test(groups = "smoke")
    public void uiControls(){
        Locator userRdButton = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions()
                .setName("User"));
        userRdButton.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Okay")).click();
        Assert.assertTrue(userRdButton.isChecked());
        Locator checkBoxTerms =page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions()
                .setName("I Agree to the terms and conditions"));
        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());
        page.getByRole(AriaRole.COMBOBOX).selectOption("Consultant");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

    }
}
