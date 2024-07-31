import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AmazonTest {
    private WebDriver driver;
    private String baseUrl = "https://www.amazon.com.tr";
    private String username = "testerhzl0@gmail.com";
    private String password = "12345678tester!!";

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }
    @Test
    public void testAmazonSearchAndAddToCart() {
        driver.get(baseUrl);

        // Login
        driver.findElement(By.id("nav-link-accountList")).click();
        driver.findElement(By.id("ap_email")).sendKeys(username);
        driver.findElement(By.id("continue")).click();
        driver.findElement(By.id("ap_password")).sendKeys(password);
        driver.findElement(By.id("signInSubmit")).click();

        // Ara "iphone"
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("iphone");
        driver.findElement(By.id("nav-search-submit-button")).click();

        // İlk ürünü seç
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#search > div.s-desktop-width-max.s-desktop-content.s-opposite-dir.s-wide-grid-style.sg-row > div.sg-col-20-of-24.s-matching-dir.sg-col-16-of-20.sg-col.sg-col-8-of-12.sg-col-12-of-16 > div > span.rush-component.s-latency-cf-section > div.s-main-slot.s-result-list.s-search-results.sg-row > div:nth-child(11) > div > div > span > div > div > div.a-section.a-spacing-small.puis-padding-left-small.puis-padding-right-small > div.a-section.a-spacing-none.a-spacing-top-small.s-title-instructions-style > h2 > a")));
        String firstProductName = firstProduct.getText();
        WebElement firstProductPriceWholeElement = driver.findElement(By.cssSelector("#search > div.s-desktop-width-max.s-desktop-content.s-opposite-dir.s-wide-grid-style.sg-row > div.sg-col-20-of-24.s-matching-dir.sg-col-16-of-20.sg-col.sg-col-8-of-12.sg-col-12-of-16 > div > span.rush-component.s-latency-cf-section > div.s-main-slot.s-result-list.s-search-results.sg-row > div:nth-child(11) > div > div > span > div > div > div.a-section.a-spacing-small.puis-padding-left-small.puis-padding-right-small > div.a-section.a-spacing-none.a-spacing-top-small.s-price-instructions-style > div.a-row.a-size-base.a-color-base > div:nth-child(1) > a > span > span:nth-child(2) > span.a-price-whole"));
        WebElement firstProductPriceFractionElement = driver.findElement(By.cssSelector("#search > div.s-desktop-width-max.s-desktop-content.s-opposite-dir.s-wide-grid-style.sg-row > div.sg-col-20-of-24.s-matching-dir.sg-col-16-of-20.sg-col.sg-col-8-of-12.sg-col-12-of-16 > div > span.rush-component.s-latency-cf-section > div.s-main-slot.s-result-list.s-search-results.sg-row > div:nth-child(11) > div > div > span > div > div > div.a-section.a-spacing-small.puis-padding-left-small.puis-padding-right-small > div.a-section.a-spacing-none.a-spacing-top-small.s-price-instructions-style > div.a-row.a-size-base.a-color-base > div:nth-child(1) > a > span > span:nth-child(2) > span.a-price-fraction"));
        WebElement firstProductPriceSymbolElement = driver.findElement(By.cssSelector("#search > div.s-desktop-width-max.s-desktop-content.s-opposite-dir.s-wide-grid-style.sg-row > div.sg-col-20-of-24.s-matching-dir.sg-col-16-of-20.sg-col.sg-col-8-of-12.sg-col-12-of-16 > div > span.rush-component.s-latency-cf-section > div.s-main-slot.s-result-list.s-search-results.sg-row > div:nth-child(11) > div > div > span > div > div > div.a-section.a-spacing-small.puis-padding-left-small.puis-padding-right-small > div.a-section.a-spacing-none.a-spacing-top-small.s-price-instructions-style > div.a-row.a-size-base.a-color-base > div:nth-child(1) > a > span > span:nth-child(2) > span.a-price-symbol"));
        String firstProductPrice = firstProductPriceWholeElement.getText() + "," + firstProductPriceFractionElement.getText() + " " + firstProductPriceSymbolElement.getText() ;
        firstProduct.click();

        // Ürün ismini tut
        WebElement productTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));
        String detailProductName = productTitle.getText();

        // Seçilen ürün ile detay sayfasındaki ürünün aynı olup olmadığı kontrolü
        Assert.assertEquals(detailProductName, firstProductName, "Ürün ismi eşleşmedi!");

        // Ürünü sepete ekle
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button")));
        addToCartButton.click();

        // Sepet kontrolü
        WebElement cartButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-cart")));
        cartButton.click();

        // Seçilen ürün ile sepetteki ürün aynı mı
        WebElement cartProductTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.a-truncate-cut")));
        String cartProductName = cartProductTitle.getText();
        Assert.assertEquals(cartProductName, detailProductName, "Ürün ismi eşleşmedi!");

        // Sepetteki ürün fiyatı
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement cartProductPriceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".sc-product-price")));
        String cartProductPrice = cartProductPriceElement.getText();

        // Fiyat doğrulama
        Assert.assertEquals(cartProductPrice, firstProductPrice, "Ürün fiyatı eşleşmedi!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
