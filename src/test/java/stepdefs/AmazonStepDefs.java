package stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.Browser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by vtpetrov on 02.09.2016 г.
 * copyright 2016
 */

public class AmazonStepDefs {

    WebElement firstResult;
    WebElement detailsPageTypesAndPrices;
    List<WebElement> basketItemsList;
    String titleXpath = ".//span[contains(@class, 'sc-product-title')]";
    String typeXpath = ".//span[contains(@class, 'sc-product-binding')]";
    String priceXpath = ".//span[contains(@class, 'sc-product-price')]";
    String quantityXpath = ".//span[@class= 'a-dropdown-prompt']";

    @Given("^I open new \"(Firefox|Chrome)\" browser$")
    public void iOpenNewBrowser(String browserType) throws Throwable {
        int type = 0;
        if (browserType.equals("Firefox")) type = 1;
        if (browserType.equals("Chrome")) type = 2;

        Browser.start(type);
    }

    @And("^I visit the page \"([^\"]*)\"$")
    public void iVisitThePage(String url) throws Throwable {
        Browser.drv.get(url);
    }

    @Then("^The page \"([^\"]*)\" is correct and opened$")
    public void thePageIsCorrectAndOpened(String expectedPageUrl) throws Throwable {

        assertEquals("Page URL is not as expected", expectedPageUrl, Browser.drv.getCurrentUrl());

        String expectedAmazonPageTitle = "Amazon.co.uk: Low Prices in Electronics, Books, Sports Equipment & more";
        assertEquals("Amazon page TITLE is not as expected", expectedAmazonPageTitle, Browser.drv.getTitle());

    }

    @When("^I search for \"([^\"]*)\" in section Books$")
    public void iSearchForInSectionBooks(String searchTerm) throws Throwable {
        new Select(Browser.drv.findElement(By.id("searchDropdownBox"))).selectByVisibleText("Books");
        WebElement searchField = Browser.drv.findElement(By.id("twotabsearchtextbox"));
        Actions actions = new Actions(Browser.drv);
        actions.sendKeys(searchField, searchTerm).sendKeys(searchField, Keys.ENTER).perform();

        firstResult = Browser.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("result_0")));
    }

    @Then("^The first item in the results list has the title \"([^\"]*)\"$")
    public void theFirstItemInTheResultsListHasTheTitle(String expectedTitle) throws Throwable {

        String actualTitle = firstResult.findElement(By.xpath(".//descendant::h2")).getText();
        assertEquals("Title doesn't match expected: ", expectedTitle, actualTitle);

    }

    @And("^It has a badge \"Best Seller\"$")
    public void itHasABadgeBestSeller() throws Throwable {

        assertTrue("First item in list doesn't have badge 'Best Seller'. ",
                firstResult.findElements(By.xpath(".//descendant::span[contains(@class, 'sx-bestseller-badge')]")).size() == 1);

    }

    @And("^the type \"(.*?)\" has price \"(.*?)\"$")
    public void theTypeHasPrice(String expectedType, String expectedPrice) throws Throwable {
        String actualPrice = firstResult.findElement(By.xpath(".//descendant::h3[@data-attribute='" + expectedType +
                "']/../../following-sibling::*[1]//span[@class='a-size-base a-color-price s-price a-text-bold']")).getText();

        assertEquals("Price for type '" + expectedType + "' doesn't match expected: ", expectedPrice, actualPrice);

    }

    @When("^I click over the first item in the search result list$")
    public void iClickOverTheFirstItemInTheSearchResultList() throws Throwable {
        firstResult.findElement(By.xpath("(.//descendant::a)[1]")).click();
    }

    @Then("^The item details page is opened$")
    public void theItemDetailsPageIsOpened() throws Throwable {
        Browser.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                ".//head/title[text()='A Game of Thrones (A Song of Ice and Fire, Book 1): Amazon.co.uk: George R.R. Martin: 9780007548231: Books']")));

        detailsPageTypesAndPrices = Browser.drv.findElement(By.xpath(".//*[@id='tmmSwatches']/ul"));
    }

    @And("^the title of the item is \"([^\"]*)\"$")
    public void theTitleOfTheItemIs(String expectedTitleDetailsPage) throws Throwable {
        assertEquals("Product title on details page doesn't match expected: ",
                expectedTitleDetailsPage,
                Browser.drv.findElement(By.id("productTitle")).getText());
    }

    @And("^the item has \"([^\"]*)\" badge$")
    public void theItemHasBadge(String expectedBadgeDetailsPage) throws Throwable {

        assertTrue("Product badge on details page doesn't match expected: ",
                Browser.drv.findElement(By.xpath(".//div[@class='badge-wrapper']//i")).getText().contains(expectedBadgeDetailsPage));
    }

    @And("^the type \"([^\"]*)\" is selected and has price \"([^\"]*)\"$")
    public void theTypeIsSelectedAndHasPrice(String expectedType, String expectedPrice) throws Throwable {

        WebElement selected = detailsPageTypesAndPrices.findElement(By.xpath("//li[@class='swatchElement selected']"));

        assertTrue("Selected type is not '" + expectedType + "': ",
                selected.findElements(By.xpath(".//span[text()='" + expectedType + "']")).size() == 1);

        assertEquals("The price of the selected type does not match expected: ",
                expectedPrice,
                selected.findElement(By.xpath(".//span[@class='a-color-price']")).getText());

    }

    @Given("^I close the browser$")
    public void iCloseTheBrowser() throws Throwable {
        Browser.close();
    }

    @When("^I add product to basket$")
    public void iAddProductToBasket() throws Throwable {
        Browser.drv.findElement(By.id("add-to-cart-button")).click();


    }

    @Then("^Notification about product added to basket is shown$")
    public void notificationAboutProductAddedToBasketIsShown() throws Throwable {
        assertTrue("Confirmation about added product is not displayed. ",
                Browser.wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(
                        ".//*[@id='huc-v2-order-row-confirm-text']/h1[contains(text(), 'Added to Basket')]"))).size() == 1);
    }

    @And("^The notification shows that there (?:is|are) \"(\\d+)\" (?:item|items) added to basket$")
    public void thereIsItemAddedToBasket(int expectedNumberOfItems) throws Throwable {

        assertEquals("The number of items in the basket doesn't match expected.",
                "Basket subtotal (" + expectedNumberOfItems + " item):",
                Browser.drv.findElement(By.xpath(".//*[@id='hlb-subcart']//span[contains(@class, 'huc-subtotal')]/span[child::b]")).getText());
    }

    @When("^I click the 'Edit basket' button$")
    public void iClickTheEditBasketButton() throws Throwable {
        WebElement editCart = Browser.drv.findElement(By.id("hlb-view-cart"));
        editCart.click();
    }

    @Then("^there (?:is|are) \"(\\d+)\" (?:item|items) inside the basket$")
    public void thereIsItemInsideTheBasket(int expectedNumberOfItems) throws Throwable {

        // wait until item list inside cart is loaded:
        Browser.wait.until(ExpectedConditions.presenceOfElementLocated(By.id("activeCartViewForm")));

        // Get list of items
        basketItemsList = Browser.drv.findElements(By.xpath(".//*[@id='activeCartViewForm']//div[@class='sc-list-item-content']"));

        assertEquals("Expected number of items inside basket doesn't match.",
                expectedNumberOfItems,
                basketItemsList.size());

        System.out.println("SUCCESS: expected number of items inside basket match actual: \n" +
                "   Expected= [" + expectedNumberOfItems + "]\n" +
                "   Actual  = [" + basketItemsList.size() + "]");
    }

    @And("^the (\\d+)(?:st|nd|rd|th) item inside the basket has attributes as follows:$")
    public void theStItemInsideTheBasketHasAttributesAsFollows(int itemIndex, Map<String, String> expectedAttributes) throws Throwable {
        // convert index from human readable to zero based:
        int indexInList = itemIndex - 1;

        Map<String, String> actualItemAttributes = new HashMap<>();

        // Store actual title:
        String actualItemTitle = basketItemsList.get(indexInList).findElement(By.xpath(titleXpath)).getText().trim();
        actualItemAttributes.put("title", actualItemTitle);

        // Store actual type:
        String actualItemType = basketItemsList.get(indexInList).findElement(By.xpath(typeXpath)).getText().trim();
        actualItemAttributes.put("type", actualItemType);

        // Store actual price:
        String actualItemPrice = basketItemsList.get(indexInList).findElement(By.xpath(priceXpath)).getText().trim();
        actualItemAttributes.put("price", actualItemPrice);

        // Store actual quantity:
        String actualItemQuantity = basketItemsList.get(indexInList).findElement(By.xpath(quantityXpath)).getText().trim();
        actualItemAttributes.put("quantity", actualItemQuantity);

        assertTrue("Actual item attributes doesn't match expected. Item [" + itemIndex + "].\n" +
                        "Expected attributes: " + expectedAttributes.toString() + "\n" +
                        "Actual attributes: " + actualItemAttributes.toString(),
                actualItemAttributes.entrySet().containsAll(expectedAttributes.entrySet()));

        System.out.println("SUCCESS: expected item attributes match actual: \n" +
                "   Expected attributes: " + expectedAttributes.toString() + "\n" +
                "   Actual   attributes: " + actualItemAttributes.toString());

    }

    @And("^the basket total price is \"(.*?)\"$")
    public void theBasketTotalPriceIs(String expectedTotalPrice) throws Throwable {
        assertEquals("Expected basket total price doesn't match.",
                expectedTotalPrice,
                Browser.drv.findElement(By.xpath(".//*[@id='activeCartViewForm']/div[contains(@class, 'sc-subtotal')]//span[contains(@class,'sc-price')]")).getText().trim());

        System.out.println("SUCCESS: expected basket total price match actual: \n" +
                "   Expected= [" + expectedTotalPrice + "]\n" +
                "   Actual  = [" + expectedTotalPrice + "]");
    }
}
