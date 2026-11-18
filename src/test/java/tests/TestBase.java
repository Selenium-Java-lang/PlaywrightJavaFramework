package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    public static Playwright playwright;
    public static Browser browser;
    public static Page page;
    String baseUrl;
    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {

        Properties prop = new Properties();
        FileInputStream file = new FileInputStream("src/test/Resources/config.properties");
        prop.load(file);
        // to run through mvn test command the code we need to replace
        // prop.getProperty("browser") with System.getProperty("browser")
        // for this we are creating a ternary operator to handle this so that
        // we don't need to modify the code fully
        //mvn test -Psmoke -Dbrowser = chrome
        // here -P indicates profile and -D indicates System property
        //mvn test -Psmoke -Dbrowser=chrome
        String browserName = System.getProperty("browser")!=null ? System.getProperty("browser"):
                prop.getProperty("browser");
        //String browserName = prop.getProperty("browser");
        String envName = System.getProperty("env")!=null ? System.getProperty("env"):
                prop.getProperty("env");
        playwright = Playwright.create();
        if(browserName.equals("firefox")){
            browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }else if(browserName.equals("webkit")){
            browser = playwright.webkit().launch();
        }else {
            browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        }
        page = browser.newPage();
        // to use the env name using mvn command we updated our code so that
        // we can easily provide env name using mvn command like mvn test -Psmoke -Dbrowser=chrome -Denv=qa
        baseUrl = prop.getProperty(envName+".baseurl");
       // baseUrl = prop.getProperty("qa.baseurl");


    }
    @AfterMethod
    public void tearDown(){
        // 6. Clean up resources
        //context.close();
        browser.close();
        playwright.close();
    }




}
