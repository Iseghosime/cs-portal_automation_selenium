package com.airtel.cs.pagerepository.pageelements;

import org.openqa.selenium.By;

public class MsisdnSearchPage {
    public By numberSearch = By.xpath("//input[@name='search']");
    public By searchButton = By.xpath("//button[@class='search-icon-btn']");
    public By errorMessage = By.xpath("//div[@class='error top-space ng-star-inserted']");

}
