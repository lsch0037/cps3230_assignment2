package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

//Page object superclass
public class PageObject {
    WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
    }
}
