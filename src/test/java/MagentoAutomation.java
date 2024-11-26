
import org.openqa.selenium.By;
import org.apache.commons.lang3.RandomStringUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.List;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import java.time.Duration;

//import java.util.concurrent.TimeUnit;

public class MagentoAutomation {
    WebDriver driver;

    @BeforeClass
    public void setup() {
    	   WebDriverManager.chromedriver().setup();
    	    driver = new ChromeDriver();
            driver.get("https://magento.softwaretestingboard.com/");
    	    driver.manage().window().maximize();
    }

    @Test
    public void createAccount() {
        // Create an account
        driver.findElement(By.linkText("Create an Account")).click();
        driver.findElement(By.id("firstname")).sendKeys("user3");
        driver.findElement(By.id("lastname")).sendKeys("test2");
        driver.findElement(By.id("email_address")).sendKeys("usertest3@example.com");
        driver.findElement(By.id("password")).sendKeys("Test@1234");
        driver.findElement(By.id("password-confirmation")).sendKeys("Test@1234");
        driver.findElement(By.xpath("//button[@title='Create an Account']")).click();
        // Locate the success message element
        WebElement successMessage = driver.findElement(By.xpath("//div[@data-ui-id='message-success']//div"));

        // Validate the success message text
        String expectedMessage = "Thank you for registering with Main Website Store.";
        String actualMessage = successMessage.getText();
        if (actualMessage.equals(expectedMessage)) {
            System.out.println("Registration message validation passed: " + actualMessage);
        } else {
            System.out.println("Registration message validation failed. Expected: " + expectedMessage + ", but got: " + actualMessage);
        }

    }
    @Test

    public void addProductsToCompare() {
    	 try {
             WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

             // Navigate to "Hot Sellers" section
             WebElement hotSellersLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//h2[contains(text(), 'Hot Sellers')]")));
             hotSellersLink.click();

             // Wait for the products to load
             wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("li.product-item")));

             // Add the first two products to the compare list
             List<WebElement> products = driver.findElements(By.cssSelector("li.product-item"));
             if (products.size() < 2) {
                 throw new Exception("Not enough products found to add to compare.");
             }

             Actions actions = new Actions(driver);

             for (int i = 0; i < 2; i++) {
                 WebElement compareButton = products.get(i).findElement(By.cssSelector("a.action.tocompare"));
                 actions.moveToElement(products.get(i)).perform(); // Hover to make the button visible
                 wait.until(ExpectedConditions.elementToBeClickable(compareButton));
                 compareButton.click();

                 // Validate success message
                 WebElement compareMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-ui-id='message-success']//div")));
                 String actualCompareMessage = compareMessage.getText();
                 if (actualCompareMessage.contains("You added")) {
                     System.out.println("Compare message validation passed: " + actualCompareMessage);
                 } else {
                     System.out.println("Compare message validation failed. Got: " + actualCompareMessage);
                 }

                 // Validate the counter behind the add to compare link
                 WebElement compareCounter = driver.findElement(By.xpath("//a[contains(@href, 'compare')]//span[@class='counter-number']"));
                 if (compareCounter.getText().equals(String.valueOf(i + 1))) {
                     System.out.println("Compare counter validation passed for item " + (i + 1) + ": " + (i + 1) + " item(s).");
                 } else {
                     System.out.println("Compare counter validation failed. Expected: " + (i + 1) + ", Got: " + compareCounter.getText());
                 }
             }

             // Navigate to the Compare Products page
             WebElement compareLink = driver.findElement(By.xpath("//a[contains(@href, 'compare')]"));
             compareLink.click();

             // Validate that two items are displayed
             List<WebElement> compareItems = driver.findElements(By.xpath("//table[@id='product-comparison']//tr[@class='item-row']"));
             if (compareItems.size() == 2) {
                 System.out.println("Compare Products page validation passed: 2 items found.");
             } else {
                 System.out.println("Compare Products page validation failed. Expected 2 items but found: " + compareItems.size());
             }

         } catch (Exception e) {
             e.printStackTrace();
             System.err.println("Test failed: " + e.getMessage());
         }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
