package com.nf.mobile.web.hybrid.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
//import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.touch.TouchActions;
//import java.util.Properties;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.nf.mobile.web.hybrid.util.Constants;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
//import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.MobileCapabilityType;
//import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class GenericKeywords<AndroidElement> {

	Properties prop;
	AndroidDriver aDriver;
	ExtentTest test;
	WebDriver driver;

	public GenericKeywords(ExtentTest test, AndroidDriver aDriver, WebDriver driver, String path) throws IOException {
		this.test = test;
		this.aDriver = aDriver;
		this.driver = driver;

		prop = new Properties();
		try {
			FileInputStream fs = new FileInputStream(path);
			prop.load(fs);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			test.log(LogStatus.FAIL, e1.getMessage());
			Assert.fail();
		}
	}

	public WebElement Web_getElement(String locatorKey) throws IOException, InterruptedException {
		WebElement e = null;

		try {
			if (locatorKey.endsWith("_xpath")) {
				WebDriverWait allwait = new WebDriverWait(driver, 30);
				allwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(locatorKey))));
				e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_id")) {
				WebDriverWait allwait = new WebDriverWait(driver, 30);
				allwait.until(ExpectedConditions.presenceOfElementLocated(By.id(prop.getProperty(locatorKey))));
				e = driver.findElement(By.id(prop.getProperty(locatorKey)));

			} else {
				// test.log(LogStatus.INFO, "Locator not found");
				takeScreenshotForWeb(Constants.FAIL_WEB);
				reportFailure("Test Failed", "Locator not found");
				Assert.fail();

			}

		} catch (Exception ex) {
			takeScreenshotForWeb(Constants.FAIL_WEB);
			test.log(LogStatus.INFO, ex.getMessage());
			Assert.fail();
			String text = ex.getMessage();
			reportFailure("Test failed", text);

		}
		return e;
	}

	/*---------------------clear content of input field-----------------------------------------------*/

	public String app_clearContent(String object) throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {

			((WebElement) getElement(object)).clear();
			;
			result = Constants.PASS;
		} catch (Exception ex) {
			takeScreenshot(Constants.FAIL);
			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL;
			Assert.fail();

		}
		return result;
	}

	/*------------------------------------------------Web Verify Text------------------------------------------------------*/

	public String Web_VerifyText(String object, String text) throws IOException, InterruptedException {
		String result = Constants.FAIL_WEB;
		WebElement e = null;
		try {
			e = (WebElement) Web_getElement(object);
			result = Constants.PASS_WEB;
			String actualText = e.getText();
			System.out.println(actualText + " " + text);
			Assert.assertTrue(actualText.trim().contains(text.trim()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, e1.getMessage());
			Assert.fail();
		} catch (InterruptedException e1) {
			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, e1.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Press Web back button------------------------------------------------------*/

	public String PressWebbackbutton() throws IOException, InterruptedException {
		String result = Constants.PASS_WEB;
		try {
			driver.navigate().back();
		} catch (Exception e) {
			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, e.getMessage());
		}
		return result;
	}

	/*----------------------------------------------------------------------scroll picker-------------------------------------------------------------------------------------------------*/

	public String scrolldropdown(String locatorKey) throws IOException, InterruptedException {

		String result = null;

		try {

			WebElement efrom = aDriver.findElement(By.xpath(prop.getProperty(locatorKey)));
			int efrom_x = ((WebElement) efrom).getLocation().x;
			test.log(LogStatus.INFO, "value of x coordinate of from element is: " + "   " + efrom_x);
			int efrom_y = ((WebElement) efrom).getLocation().y;
			int eto_x = ((WebElement) efrom).getLocation().x;
			int eto_y = ((WebElement) efrom).getLocation().y + efrom.getSize().height / 5;
			TouchAction act = new TouchAction(aDriver);
			act.press(PointOption.point(efrom_x, efrom_y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
					.moveTo(PointOption.point(eto_x, eto_y)).release().perform();

			result = Constants.PASS;
		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL;
			takeScreenshot(result);
			Assert.fail();
		}
		return result;

	}

	/*----------------------------------------------------------------------------readToastMessage---------------------------------------------------------------------*/
	public String readToastMessage(String locatorKey, String validationMessage)
			throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {
			WebElement e;
			WebDriverWait allwait = new WebDriverWait(aDriver, 10);
			allwait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(locatorKey))));
			e = (WebElement) aDriver.findElement(By.xpath(prop.getProperty(locatorKey)));
			e.click();
		} catch (Exception ex) {
			test.log(LogStatus.INFO, ex.getMessage());
			//result = Constants.FAIL;
			//takeScreenshot(result);
			//Assert.fail();
		}

		// take screenshot multiple times
		Date d = new Date();
		String screenshotFile_one = d.toString().replace(":", "_").replace(" ", "_") + "first.png";
		String path = Constants.toast_screens + screenshotFile_one;

		File scrFile = ((TakesScreenshot) aDriver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(path));
		} catch (IOException e2) {
			test.log(LogStatus.INFO, e2.getMessage());
			//result = Constants.FAIL;
			//takeScreenshot(result);
			//Assert.fail();
		}

		String screenshotFile_a = d.toString().replace(":", "_").replace(" ", "_") + "second.png";
		String path_a = Constants.toast_screens + screenshotFile_a;

		File scrFile_a = ((TakesScreenshot) aDriver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile_a, new File(path_a));
		} catch (IOException e2) {
			test.log(LogStatus.INFO, e2.getMessage());
			//result = Constants.FAIL;
			//takeScreenshot(result);
			//Assert.fail();
		}

		String screenshotFile_b = d.toString().replace(":", "_").replace(" ", "_") + "third.png";
		String path_b = Constants.toast_screens + screenshotFile_b;

		File scrFile_b = ((TakesScreenshot) aDriver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile_b, new File(path_b));
		} catch (IOException e2) {
			test.log(LogStatus.INFO, e2.getMessage());
			//result = Constants.FAIL;
			//takeScreenshot(result);
			//Assert.fail();
		}

		ITesseract image = new Tesseract();
		try {
			// we are reading text from each image file
			image.setDatapath(Constants.tessData);

			image.setLanguage("eng");
			String textOfFirstImage = image.doOCR(scrFile);

			System.out.println("Text read from first image is: " + textOfFirstImage);
			test.log(LogStatus.INFO, "Text read from first image is: " + textOfFirstImage);

			String textOfSecondImage = image.doOCR(scrFile_a);
			System.out.println("Text read from second image is: " + textOfSecondImage);
			test.log(LogStatus.INFO, "Text read from second image is:  " + textOfSecondImage);

			String textOfThirdImage = image.doOCR(scrFile_b);
			System.out.println("Text read from third image is: " + textOfThirdImage);
			test.log(LogStatus.INFO, "Text read from third image is:  " + textOfThirdImage);

			if (textOfFirstImage.contains(validationMessage)) {
				System.out.println("toast captured in first image");
				test.log(LogStatus.INFO, "toast captured in first image");
				result = Constants.PASS;
			} else if (textOfSecondImage.contains(validationMessage)) {
				System.out.println("toast captured in second image");
				test.log(LogStatus.INFO, "toast captured in second image");
				result = Constants.PASS;
			} else if (textOfThirdImage.contains(validationMessage)) {
				System.out.println("toast captured in third image");
				test.log(LogStatus.INFO, "toast captured in third image");
				result = Constants.PASS;
			} else {
				result = Constants.FAIL;
				test.log(LogStatus.INFO, "toast message wasn't triggered");
			}

		} catch (TesseractException e1) {

			test.log(LogStatus.FAIL, e1.getMessage());
			result = Constants.FAIL;
			takeScreenshot(result);
			Assert.fail();
		}

		return result;
	}

	/*************************************************************
	 * EnablingWifi
	 **********************************************************/
	public String EnablingWifi() throws IOException, InterruptedException {
		String result = null;
		try {
			aDriver.toggleWifi();
			result = Constants.PASS;
		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL;
			takeScreenshot(result);
			Assert.fail();
		}

		return result;
	}

	/*************************************************************
	 * EnablingLocationAccess
	 **********************************************************/
	public String EnablingLocationAccess() {
		return Constants.PASS;
	}

	/*-------------------------------------------------------------------------------------Web_LaunchStore-------------------------------------------------------------------------------------*/
	public String Web_LaunchStore(String data) throws IOException, InterruptedException {

		String result = null;
		try {

			driver.get(data);

			driver.manage().window().maximize();
			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			Assert.fail();
		}
		return result;
	}

	/*-------------------------------------------------------------------------------------Web_LaunchStore-------------------------------------------------------------------------------------*/
	public String MobileWeb_LaunchStore(String data) throws IOException, InterruptedException {

		String result = null;
		try {

			driver.get(data);

			// driver.manage().window().maximize();
			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Web_clicking-------------------------------------------------*/
	public String Web_clicking(String object) throws IOException, InterruptedException {

		String result = Constants.FAIL_WEB;
		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(object))));
			WebElement element = driver.findElement(By.xpath(prop.getProperty(object)));
			element.click();

			result = Constants.PASS_WEB;
		} catch (Exception ex) {
			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());

			Assert.fail();
		}
		return result;
	}

	/*********************************************
	 * Web Implicit Wait
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 ***************************************************************************/

	public String WebImplicitWait() throws IOException, InterruptedException {
		String result = Constants.PASS_WEB;
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, e.getMessage());

			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Web_type_location-------------------------------------------------*/
	public String Web_type_location(String object, String data) throws IOException, InterruptedException {

		String result = Constants.FAIL_WEB;
		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(object))));
			WebElement element = driver.findElement(By.xpath(prop.getProperty(object)));
			element.sendKeys(data);
			element.sendKeys(Keys.ENTER);

			Thread.sleep(5000);
			element.sendKeys(Keys.ARROW_DOWN);
			Thread.sleep(5000);

			Thread.sleep(5000);
			List<WebElement> loc = driver.findElements(By.xpath("//*[contains(text(),'" + data + "')]"));

			WebElement anyLoc = loc.get(2);
			anyLoc.click();

			Thread.sleep(5000);
			test.log(LogStatus.INFO, "Able to select location");

			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();

		}
		return result;
	}

	/*------------------------------------------------Web_type-------------------------------------------------*/

	public String Web_type(String object, String data) throws IOException, InterruptedException {

		String result = Constants.FAIL_WEB;
		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(object))));
			WebElement element = driver.findElement(By.xpath(prop.getProperty(object)));
			element.sendKeys(data);
			element.sendKeys(Keys.ENTER);
			result = Constants.PASS_WEB;
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------------Web_scrollToElement----------------------------------------------------*/
	public String Web_scrollToElement(String object) throws IOException, InterruptedException {

		String result = Constants.FAIL_WEB;
		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(object))));
			WebElement element = driver.findElement(By.xpath(prop.getProperty(object)));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*---------------------------------------------------RightSwipeToAccept---------------------------------------------------*/

	public String RightSwipeToAccept(String locatorKey_a) throws IOException, InterruptedException {

		String result = null;

		try {

			WebElement efrom = aDriver.findElement(By.xpath(prop.getProperty(locatorKey_a)));
			int efrom_x = ((WebElement) efrom).getLocation().x;
			test.log(LogStatus.INFO, "value of x coordinate of from element is: " + "   " + efrom_x);
			int efrom_y = ((WebElement) efrom).getLocation().y;

			Dimension dim = aDriver.manage().window().getSize();
			int width = dim.width;
			int height = dim.height;
			System.out.println("Width - " + width);
			System.out.println("Height - " + height);

			// try using elements x and y coordinates

			int endX = (int) (width * .1);
			int startY = (int) (height * .9);
			int endY = (int) (height * .1);

			TouchAction act = new TouchAction(aDriver);
			act.longPress(PointOption.point(efrom_x, efrom_y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(3)))
					.moveTo(PointOption.point(endX, efrom_y)).release().perform();

			result = Constants.PASS;
		} catch (Exception ex) {

			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;

	}

	/*--------------------------------------------------------getTheValueFromUI-----------------------------------------------------------*/

	// fetches the value displayed on mobile and returns same thing in a string
	// variable

	public String getTheValueFromUI(String locatorKey) throws IOException, InterruptedException {

		String textOnUI = null;
		try {

			WebElement element = ((WebElement) getElement(locatorKey));
			textOnUI = element.getText();

		} catch (Exception ex) {

			String result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();

		}
		return textOnUI;
	}

	/*--------------checkTheUncheckedBox------------------*/
	public String checkTheUncheckedBox(String locatorKey) throws IOException, InterruptedException {
		String result = Constants.FAIL;

		try {

			WebElement element = (WebElement) getElement(locatorKey);
			if ((element.getAttribute("checked")).equalsIgnoreCase("false")) {
				element.click();
				result = Constants.PASS;
			} else {
				test.log(LogStatus.INFO, "checkbox already checked");
				result = Constants.PASS;
			}
		} catch (Exception ex) {
			result = Constants.FAIL;
			test.log(LogStatus.FAIL, ex.getMessage());
			ex.printStackTrace();

			takeScreenshot(result);
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Click Action-------------------------------------------------*/

	public String clicking(String locatorKey) throws IOException, InterruptedException {
		String result = Constants.FAIL;

		try {
			WebElement element = (WebElement) getElement(locatorKey);
			element.click();

			hideKeyboard();
			result = Constants.PASS;
		} catch (Exception ex) {
			result = Constants.FAIL;
			test.log(LogStatus.FAIL, ex.getMessage());
			ex.printStackTrace();
			takeScreenshot(result);
			Assert.fail();
		}

		return result;
	}

	/*------------------------------------------------verify if order completed--------------------------------------------*/

	public String verifyCompleted() throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {
			String orderId = null;
			WebElement e = (WebElement) getElement("EO_order_id_xpath");
			orderId = e.getText();
			String[] temp_order_id = orderId.split("#");
			orderId = temp_order_id[1];

			aDriver.navigate().back();

			WebElement completed = (WebElement) aDriver.findElement(By.xpath("//*[@text=\'Completed\']"));
			completed.click();

			WebElement firstOrder = (WebElement) getElement("EO_first_new_order_xpath");
			firstOrder.click();

			String completedOrderId = null;
			WebElement ee = (WebElement) getElement("EO_order_id_xpath");
			completedOrderId = ee.getText();
			String[] temp_completed_order_id = completedOrderId.split("#");
			completedOrderId = temp_completed_order_id[1];

			Assert.assertEquals(orderId, completedOrderId);
			result = Constants.PASS;
		} catch (Exception e) {
			result = Constants.FAIL;
			test.log(LogStatus.FAIL, e.getMessage());
			e.printStackTrace();
			takeScreenshot(result);
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Verify Rejected------------------------------------------------------*/

	public String verifyTextRejected(String object, String text) throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {
			WebElement e = (WebElement) getElement(object);
			String actualText = e.getText();
			String[] temp = actualText.split(" ");
			actualText = temp[0];
			Assert.assertEquals(actualText, text);
			result = Constants.PASS;
		} catch (Exception e) {
			result = Constants.FAIL;
			test.log(LogStatus.FAIL, e.getMessage());
			e.printStackTrace();
			takeScreenshot(result);
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------fetch locator action-------------------------------------------------*/

	public AndroidElement getElement(String locatorKey) throws IOException, InterruptedException {
		AndroidElement e = null;

		try {
			if (locatorKey.endsWith("_xpath")) {
				WebDriverWait allwait = new WebDriverWait(aDriver, 30);
				allwait.until(ExpectedConditions.elementToBeClickable(By.xpath(prop.getProperty(locatorKey))));
				// allwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(locatorKey))));
				e = (AndroidElement) aDriver.findElement(By.xpath(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_id")) {
				WebDriverWait allwait = new WebDriverWait(aDriver, 30);
				allwait.until(ExpectedConditions.elementToBeClickable(By.id(prop.getProperty(locatorKey))));
				e = (AndroidElement) aDriver.findElement(By.id(prop.getProperty(locatorKey)));
			} else {
				takeScreenshot(Constants.FAIL);
				Assert.fail();
				test.log(LogStatus.FAIL, "Locator not found");
			}

		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			takeScreenshot(Constants.FAIL);
			Assert.fail();

		}
		return e;
	}
	
	/*------------------------------------------------Wait5Min-----------------------------------------------------*/
	
	public AndroidElement Web_Wait5Min(String locatorKey) throws IOException, InterruptedException {
		AndroidElement e = null;

		try {
			if (locatorKey.endsWith("_xpath")) {
				WebDriverWait allwait = new WebDriverWait(driver, 60*5);
				allwait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(locatorKey))));
				// allwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(locatorKey))));
				e = (AndroidElement) driver.findElement(By.xpath(prop.getProperty(locatorKey)));
			} else if (locatorKey.endsWith("_id")) {
				WebDriverWait allwait = new WebDriverWait(driver, 60*5);
				allwait.until(ExpectedConditions.visibilityOfElementLocated(By.id(prop.getProperty(locatorKey))));
				e = (AndroidElement) driver.findElement(By.id(prop.getProperty(locatorKey)));
			} else {
				takeScreenshot(Constants.FAIL_WEB);
				Assert.fail();
				test.log(LogStatus.FAIL, "Locator not found");
			}

		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			takeScreenshot(Constants.FAIL_WEB);
			Assert.fail();

		}
		return e;
	}
	
	/*------------------------------------------------Web Verify Text After 5 mins-------------------------------------------------*/

	public String Web_verifyTextAfter5Min(String locatorKey, String expectedText) throws IOException, InterruptedException {
		String result = Constants.FAIL_WEB;
		try {
			String actualtext = ((WebElement) Web_Wait5Min(locatorKey)).getText();
			System.out.println(actualtext + " " + expectedText);
			if (actualtext.trim().contains(expectedText.trim()))
				result = Constants.PASS_WEB;
			else {
				takeScreenshot("FAIL");
				test.log(LogStatus.FAIL, "Test failed as the actual and expcted text Did not match, actual text is:  "
						+ actualtext + "expected text is: " + expectedText);
				result = Constants.FAIL;
			}
		} catch (Exception ex) {
			takeScreenshot("FAIL");
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();

			result = Constants.FAIL_WEB;
		}
		return result;
	}

	/*------------------------------------------------ Type Action-------------------------------------------------*/

	public String type(String locatorKey, String data) throws IOException, InterruptedException {

		String result = Constants.PASS;
		try {
			WebElement element = (WebElement) getElement(locatorKey);
			element.click();
			element.sendKeys(data);
			// result = Constants.PASS;
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL;
			takeScreenshot(result);
			Assert.fail();
		}
		hideKeyboard();
		return result;
	}

	/*------------------------------------------------ Type location in app-------------------------------------------------*/

	public String typeLocationInApp(String locatorKey, String data) throws IOException, InterruptedException {

		String result = Constants.FAIL;
		try {
			((WebElement) getElement(locatorKey)).sendKeys(data);
			Thread.sleep(1000);
			/*
			 * ((WebElement)
			 * getElement("//android.widget.EditText[@text='HSR layout']")).sendKeys(Keys.
			 * ENTER); Thread.sleep(1000); ((WebElement)
			 * getElement("//android.widget.EditText[@text='HSR layout']")).sendKeys(Keys.
			 * ARROW_DOWN);
			 */
			// aDriver.findElement(By.xpath("//*[contains(text(),'Karnataka')]")).click();

			// aDriver.findElement(By.xpath("//*[@text='HSR Layout, Bengaluru, Karnataka,
			// India']")).click();
			// Thread.sleep(3000);

			// ((WebElement) getElement(locatorKey)).sendKeys(Keys.ENTER);
			// Thread.sleep(5000);
			// ((WebElement) getElement(locatorKey)).sendKeys(Keys.ARROW_DOWN);
			// Thread.sleep(5000);
			List<WebElement> loc = null;
			loc = aDriver.findElements(By.xpath("//*[contains(text(),'HSR')]"));

			test.log(LogStatus.INFO, "elements found are: " + loc);
			// loc = aDriver.findElements(By.xpath("//*[contains(text(),'"+data+"')]"));
			WebElement anyLoc = loc.get(1);
			anyLoc.click();

			result = Constants.PASS;
			Thread.sleep(1000);
			hideKeyboard();
		} catch (Exception ex) {
			result = Constants.FAIL;
			test.log(LogStatus.FAIL, ex.getMessage());
			takeScreenshot(result);
			Assert.fail();

		}
		return result;
	}

	/*------------------------------------------------ Verify Text-------------------------------------------------*/

	public String verifyText(String locatorKey, String expectedText) throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {
			String actualtext = ((WebElement) getElement(locatorKey)).getText();
			System.out.println(actualtext + " " + expectedText);
			if (actualtext.trim().contains(expectedText.trim()))
				result = Constants.PASS;
			else {
				takeScreenshot("FAIL");
				test.log(LogStatus.FAIL, "Test failed as the actual and expcted text Did not match, actual text is:  "
						+ actualtext + "expected text is: " + expectedText);
				result = Constants.FAIL;
			}
		} catch (Exception ex) {
			takeScreenshot("FAIL");
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();

			result = Constants.FAIL;
		}
		return result;
	}

	/*--------------------------------------------------------------------waitFewSecondsInApp-----------------------------------------------------*/
	public String waitFewSecondsInApp() throws IOException, InterruptedException {
		String result = null;

		try {
			Thread.sleep(5000);
			result = Constants.PASS;
		} catch (InterruptedException e1) {

			result = Constants.FAIL;
			takeScreenshot(result);
			e1.printStackTrace();
		}
		return result;
	}

	/*----------------------------------------------------verify mobile message---------------------------------------*/

	public String ValidateMobileMessage_ShopBills(String pack, String activity, String message, String object)
			throws IOException, InterruptedException {

		AndroidElement e = null;
		try {
			Thread.sleep(9000);
			String packageName = prop.getProperty(pack);
			String activityName = prop.getProperty(activity);
			aDriver.startActivity(new Activity(packageName, activityName));

			// WebElement text = null;
			WebDriverWait allwait = new WebDriverWait(aDriver, 30);
			/*
			 * //android.widget.LinearLayout[3]/android.widget.TextView[contains(@text,'Hey'
			 * )]
			 * 
			 * //*[@text='the receipt of your recent purchase']
			 * //*[contains(@text,\"the receipt of your recent purchase\")]
			 */
			allwait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty(object))));
			// getting text of the mobile message
			String Actual_msgText = aDriver.findElement((By.xpath(prop.getProperty(object)))).getText();
			// verify if retrieved text is same as the expected text or not
			System.out.println(Actual_msgText + " " + message);
			Assert.assertTrue(Actual_msgText.contains(message));
			aDriver.activateApp("");
		}

		catch (Exception ex) {
			takeScreenshot("FAIL");
			test.log(LogStatus.INFO, ex.getMessage());
			Assert.fail();
			String text = ex.getMessage();
			try {
				reportFailure("Test failed", text);
			} catch (IOException e1) {

				e1.printStackTrace();
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}

		}

		return activity;

	}

	/*********************************************
	 * verifyOTP
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 ***************************************************************************/

	public String verifyOTP(String pack, String activity) throws IOException, InterruptedException {

		String result = Constants.FAIL;
		try {
			String packageName = prop.getProperty(pack);
			String activityName = prop.getProperty(activity);

			WebDriverWait wait = new WebDriverWait(aDriver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Verify OTP']")));
			getActivityName();
			aDriver.runAppInBackground(Duration.ofSeconds(-1));
			aDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			aDriver.startActivity(new Activity(packageName, activityName));
//-------------------------------------------------------------------------------------------read otp
			// waitFewSecondsInApp();
			wait = new WebDriverWait(aDriver, 45);
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@text,\"Welcome to . Your OTP for registration is\")]")));
			String otp = aDriver
					.findElement(By.xpath("//*[contains(@text,\"Welcome to . Your OTP for registration is\")]"))
					.getText().split(" is")[1];
			String New_OTP = otp.replaceAll("\\D+", "");

			aDriver.terminateApp(packageName);
			// re-launching 

			aDriver.activateApp("");
			String expected_verifyOTPscreen = getActivityName();

//waitFewSecondsInApp();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//android.widget.EditText[@resource-id=':id/otp_edittext1']")));

			char[] ch = new char[New_OTP.length()];
			for (int i = 0; i < New_OTP.length(); i++) {
				ch[i] = New_OTP.charAt(i);

				if (i == 0) {
					String digit = String.valueOf(ch[0]);

					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext1']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext1']"))
							.sendKeys(digit);
				} else if (i == 1) {
					String digit = String.valueOf(ch[1]);
					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext2']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext2']"))
							.sendKeys(digit);
				} else if (i == 2) {
					String digit = String.valueOf(ch[2]);
					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext3']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext3']"))
							.sendKeys(digit);
				} else if (i == 3) {
					String digit = String.valueOf(ch[3]);
					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext4']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext4']"))
							.sendKeys(digit);
				} else if (i == 4) {
					String digit = String.valueOf(ch[4]);
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext5']"))
							.click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext5']"))
							.sendKeys(digit);
					String actual_verifyOTPscreen = getActivityName();
					if (actual_verifyOTPscreen == expected_verifyOTPscreen) {
						result = Constants.PASS;
					} else {
						result = Constants.FAIL;
					}

				}

			}
		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL;
			takeScreenshot(result);
			Assert.fail();

		}

		return result;
	}

	/*---------------------------------Verify OTP for Login-------------------------------------------------*/
	public String verifyOTPforLogin(String pack, String activity) throws IOException, InterruptedException {

		String result = Constants.FAIL;
		try {
			String packageName = prop.getProperty(pack);
			String activityName = prop.getProperty(activity);

			WebDriverWait wait = new WebDriverWait(aDriver, 10);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Verify OTP']")));
			getActivityName();
			aDriver.runAppInBackground(Duration.ofSeconds(-1));
			aDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

			aDriver.startActivity(new Activity(packageName, activityName));
//-------------------------------------------------------------------------------------------read otp
			// waitFewSecondsInApp();
			wait = new WebDriverWait(aDriver, 45);
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//*[contains(@text,\"Welcome to . Your OTP to login is\")]")));

			String otp = aDriver
					.findElement(By.xpath("//*[contains(@text,\"Welcome to . Your OTP to login is\")]"))
					.getText().split(" is")[1];
			String New_OTP = otp.replaceAll("\\D+", "");

			aDriver.terminateApp(packageName);
			// re-launching 

			aDriver.activateApp("");
			// String expected_verifyOTPscreen = getActivityName();

//waitFewSecondsInApp();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
					"//android.widget.EditText[@resource-id=':id/otp_edittext1']")));

			char[] ch = new char[New_OTP.length()];
			for (int i = 0; i < New_OTP.length(); i++) {
				ch[i] = New_OTP.charAt(i);

				if (i == 0) {
					String digit = String.valueOf(ch[0]);

					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext1']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext1']"))
							.sendKeys(digit);
				} else if (i == 1) {
					String digit = String.valueOf(ch[1]);
					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext2']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext2']"))
							.sendKeys(digit);
				} else if (i == 2) {
					String digit = String.valueOf(ch[2]);
					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext3']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext3']"))
							.sendKeys(digit);
				} else if (i == 3) {
					String digit = String.valueOf(ch[3]);
					// aDriver.findElement(By.xpath("//android.widget.EditText[@resource-id=':id/otp_edittext4']")).click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext4']"))
							.sendKeys(digit);
				} else if (i == 4) {
					String digit = String.valueOf(ch[4]);
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext5']"))
							.click();
					aDriver.findElement(By.xpath(
							"//android.widget.EditText[@resource-id=':id/otp_edittext5']"))
							.sendKeys(digit);

					result = Constants.PASS;
				}

			}
		} catch (Exception ex) {
			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL;
			takeScreenshot(result);
			Assert.fail();

		}

		return result;
	}

	/*-----------------------------------------------------------------------------Web_verifyOTP-----------------------------------------------------------------------------*/
	public String Web_verifyOTP(String pack, String activity) throws IOException, InterruptedException {
		String result = Constants.FAIL_WEB;
		try {
			String packageName = prop.getProperty(pack);
			String activityName = prop.getProperty(activity);

			aDriver.startActivity(new Activity(packageName, activityName));
//-------------------------------------------------------------------------------------------read otp
			Thread.sleep(9000);

			String otp = aDriver.findElement(By.xpath("//*[contains(@text,\"Your  verification code is\")]"))
					.getText().split(" is")[1]; // take the message content as per the estore flow
			String New_OTP = otp.replaceAll("\\D+", "");
			New_OTP = New_OTP.substring(0, New_OTP.length() - 2);

			System.out.println("OTP is " + New_OTP);

			aDriver.terminateApp(prop.getProperty(pack));

			aDriver.activateApp("");

			driver.findElement(By.xpath("//input[@placeholder='Enter OTP']")).sendKeys(New_OTP);
			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();

		}

		return result;

	}
	/*----------------------------------------------------------------------scrollTobottom-------------------------------------------------------------------------------------------------*/

	public String scrollTobottom() throws IOException, InterruptedException {
		String result = null;

		try {

			Dimension dim = aDriver.manage().window().getSize();
			int width = dim.width;
			int height = dim.height;
			System.out.println("Width - " + width);
			System.out.println("Height - " + height);

			int middleX = width / 2;
			int startY = (int) (height * .9);
			int endY = (int) (height * .1);

			TouchAction act = new TouchAction(aDriver);

			act.press(PointOption.point(middleX, startY)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
					.moveTo(PointOption.point(middleX, endY)).release().perform();

			result = Constants.PASS;
		} catch (Exception ex) {

			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}

		return result;

	}

	/*----------------------------------------------------------------------Estore WhatsApp Msg Validation-------------------------------------------------------------------------------------------------*/

	public String Estore_WhatsAppMsgsValidation(String pack, String activity) throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {
			String packageName = prop.getProperty(pack);
			String activityName = prop.getProperty(activity);

			aDriver.terminateApp(prop.getProperty(pack));

			aDriver.activateApp("");
		} catch (Exception e) {
			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, e.getMessage());
			Assert.fail();
		}
		return result;

	}

	/*----------------------------------------------------------------------scrollToTop-------------------------------------------------------------------------------------------------*/

	public String scrollToTop() throws IOException, InterruptedException {
		String result = Constants.FAIL;

		try {

			Dimension dim = aDriver.manage().window().getSize();
			int width = dim.width;
			int height = dim.height;
			System.out.println("Width - " + width);
			System.out.println("Height - " + height);

			int middleX = width / 2;
			int endY = (int) (height * .9);
			int startY = (int) (height * .2);

			TouchAction act = new TouchAction(aDriver);

			act.press(PointOption.point(middleX, startY)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
					.moveTo(PointOption.point(middleX, endY)).release().perform();

			result = Constants.PASS;
		} catch (Exception ex) {

			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}

		return result;

	}

	/*----------------------------------------------------------------------scrollToRight-------------------------------------------------------------------------------------------------*/

	public String scrollToRight(String locatorKey) throws IOException, InterruptedException {

		String result = null;

		try {

			WebElement efrom = aDriver.findElement(By.xpath(prop.getProperty(locatorKey)));
			int efrom_x = ((WebElement) efrom).getLocation().x;
			int efrom_y = ((WebElement) efrom).getLocation().y;
			int eto_x = ((WebElement) efrom).getLocation().x;
			int eto_y = efrom.getSize().width;
			int Start_x = efrom_x - eto_y / 2;
			int Start_y = efrom_y;
			int end_x = efrom_x + eto_y / 2;
			int end_y = efrom_y;

			TouchAction act = new TouchAction(aDriver);
			act.press(PointOption.point(end_x, end_y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
					.moveTo(PointOption.point(Start_x, Start_y)).release().perform();

			result = Constants.PASS;
		} catch (Exception ex) {

			test.log(LogStatus.FAIL, ex.getMessage());
			result = Constants.FAIL;
			takeScreenshot(result);
			Assert.fail();
		}
		return result;

	}

	/*----------------------------------------------------------------------scrollFromElementToElement-------------------------------------------------------------------------------------------------*/

	public String scrollFromElementToElement(String locatorKey_a, String locatorKey_b)
			throws IOException, InterruptedException {

		String result = Constants.FAIL;

		try {

			WebElement efrom = aDriver.findElement(By.xpath(prop.getProperty(locatorKey_a)));
			WebElement eto = aDriver.findElement(By.xpath(prop.getProperty(locatorKey_b)));
			int efrom_x = ((WebElement) efrom).getLocation().x;
			test.log(LogStatus.INFO, "value of x coordinate of from element is: " + "   " + efrom_x);
			int efrom_y = ((WebElement) efrom).getLocation().y;
			int eto_x = ((WebElement) eto).getLocation().x;
			int eto_y = ((WebElement) eto).getLocation().y;
			TouchAction act = new TouchAction(aDriver);
			act.press(PointOption.point(efrom_x, efrom_y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(3)))
					.moveTo(PointOption.point(eto_x, eto_y)).release().perform();

			result = Constants.PASS;
		} catch (Exception ex) {

			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;

	}

	/*----------------------------------------------------------------------hideKeyboard---------------------------------------------------------------------------------------------------------*/

	public String hideKeyboard() throws IOException, InterruptedException {

		String result = Constants.FAIL;
		boolean isKeyboardShown = aDriver.isKeyboardShown();
		try {
			if (isKeyboardShown == true) {
				// aDriver.hideKeyboard();
				aDriver.navigate().back();
				result = Constants.PASS;
			} else {
				test.log(LogStatus.INFO, "keyboard isn't shown");
				result = Constants.PASS;
			}
		} catch (Exception ex) {
			result = Constants.FAIL;
			test.log(LogStatus.FAIL, ex.getMessage());
			takeScreenshot(result);
			Assert.fail();
		}
		// result = Constants.PASS;
		return result;
	}

	/*******************************************************
	 * Rare actions
	 **************************************************************************/
	public String RareActions(String locatorkey) throws InterruptedException, IOException {

		String result = Constants.FAIL;
		AndroidElement e = null;
		try {
			if (locatorkey.endsWith("_xpath")) {
				Thread.sleep(5000);
				e = (AndroidElement) aDriver.findElement(By.xpath(prop.getProperty(locatorkey)));
				if (e == null) {
					test.log(LogStatus.INFO, "No rare activity happened");
					result = Constants.PASS;

				}
			} else {
				((WebElement) getElement(locatorkey)).click();
				((WebElement) e).click();
				result = Constants.PASS;

			}
		} catch (Exception ex) {

			result = Constants.PASS;
			test.log(LogStatus.INFO, ex.getMessage());
			takeScreenshot(result);
			//Assert.fail();
		}

		return result;
	}

	/*----------------------------------------------------------------------PressAndroidbackbutton---------------------------------------------------------------------------------------------------------*/

	public String PressAndroidbackbutton() throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {
			aDriver.navigate().back();
			result = Constants.PASS;
		} catch (Exception ex) {

			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;

	}

	/*--------------------------------------------------------------------Read Elements from locator---------------------------------------------------------------------------------------------------------*/

	public List<WebElement> getElements(String locatorKey) throws IOException, InterruptedException {

		System.out.println(locatorKey);
		locatorKey = locatorKey.trim();
		List<WebElement> e = null;
		try {
			if (locatorKey.endsWith("_xpath")) {
				e = aDriver.findElements(By.xpath(prop.getProperty(locatorKey)));

			} else if (locatorKey.endsWith("_id")) {
				e = aDriver.findElements(By.id(prop.getProperty(locatorKey)));

			} else {
				takeScreenshot("FAIL");
				test.log(LogStatus.FAIL, "Locator not found");
				Assert.fail();
			}

		} catch (Exception ex) {

			takeScreenshot("FAIL");
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}

		test.log(LogStatus.INFO, "Locator key is: " + locatorKey);
		return e;
	}

	/*----------------------------------------------------------------------Load whatsapp-------------------------------------------------------------------------------------------------------------------*/

	public String loadWhatsapp(String pack, String activity) throws IOException, InterruptedException {

		String result = Constants.FAIL;
		try {
			System.out.println(pack + " " + activity);
			String packageName = prop.getProperty(pack);
			String activityName = prop.getProperty(activity);

			aDriver.startActivity(new Activity(packageName, activityName));
			result = Constants.PASS;
			Thread.sleep(8000);
		} catch (Exception e) {
			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, e.getMessage());
			Assert.fail();
		}
		return result;

	}

	/*----------------------------------------------------------------------Read last whatsapp message------------------------------------------------------------------------------------------------------*/

	public String verifyLastWhatsappMessage(String locator, String message) throws IOException, InterruptedException {

		String result = Constants.FAIL;

		try {
			List<WebElement> elements = getElements(locator);
			WebElement lastElement = elements.get(elements.size() - 1);
			String lastElementText = lastElement.getText();
			System.out.println(lastElementText);
			Assert.assertTrue(lastElementText.contains(message));
			result = Constants.PASS;
		} catch (Exception e) {
			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, e.getMessage());
			Assert.fail();
		}
		return result;

	}

	/*---------------------------------------------------------------getActivityName---------------------------------------------------------------------------------------------------------*/

	public String getActivityName() throws IOException, InterruptedException {
		String result = Constants.FAIL;
		try {
			test.log(LogStatus.INFO, "activity name is:");
			test.log(LogStatus.INFO, aDriver.currentActivity());
			result = Constants.PASS;
		} catch (Exception e) {
			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, e.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*---------------------------------------------------------------Delete User---------------------------------------------------------------------------------------------------------*/
	public String deleteUser(String phone, String key) throws InterruptedException, IOException {
		String result = Constants.FAIL_WEB;
		try {

			driver.get("https://staging..dev//index.html");
			Thread.sleep(2000);
			driver.manage().window().maximize();
			Thread.sleep(1000);
			WebElement element = driver.findElement(By.xpath("//span[@data-path=\"/api/Internal/DeleteUser\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Try it out ']")).click();
			Thread.sleep(2000);
			WebElement text = driver.findElement(By.xpath("//textarea[@class=\"body-param__text\"]"));
			String textcontent = text.getText();
			text.clear();
			Thread.sleep(2000);
			text.sendKeys("{ \"phoneNumber\": { \"phoneNumber\": \"" + phone + "\", \"region\": \"IN\" }, \"key\": \""
					+ key + "\" }");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Execute']")).click();
			Thread.sleep(5000);
			Thread.sleep(5000);
			test.log(LogStatus.INFO, "User deleted successfully");

			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*---------------------------------------------------------------DeleteStore---------------------------------------------------------------------------------------------------------*/
	public String DeleteStore(String phone) throws InterruptedException, IOException {
		String result = Constants.FAIL_WEB;

		try {

			driver.get("https://staging..dev/website//index.html");
			Thread.sleep(2000);
			driver.manage().window().maximize();
			Thread.sleep(2000);
			WebElement element = driver.findElement(
					By.xpath("//span[@data-path=\"/website/api/v1/Internal/DeleteOnlineStoreForMerchant\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Try it out ']")).click();
			Thread.sleep(2000);
			WebElement text = driver.findElement(By.xpath("//textarea[@class=\"body-param__text\"]"));

			String textcontent = text.getText();
			text.clear();
			Thread.sleep(2000);

			text.sendKeys("{\r\n" + "  \"phoneNumber\": \"" + phone + "\",\r\n" + "  \"region\": \"IN\"\r\n" + "}");

			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Execute']")).click();
			Thread.sleep(5000);
			Thread.sleep(5000);
			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Update store coordinates--------------------------------------------------------------*/
	public String UpdateStoreCoordinates(String phone) throws InterruptedException, IOException {
		String result = Constants.FAIL_WEB;
		try {

			driver.get("https://staging..dev/website//index.html");
			Thread.sleep(2000);
			driver.manage().window().maximize();
			Thread.sleep(1000);
			WebElement element = driver
					.findElement(By.xpath("//span[@data-path=\"/website/api/v1/Internal/UpdateStoreCoordinates\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Try it out ']")).click();
			Thread.sleep(2000);
			WebElement text = driver.findElement(By.xpath("//textarea[@class=\"body-param__text\"]"));

			String textcontent = text.getText();

			text.clear();
			Thread.sleep(2000);
			text.sendKeys("{ \"phoneNumber\": { \"phoneNumber\": \"" + phone + "\", \"region\": \"IN\" } }");

			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Execute']")).click();
			Thread.sleep(5000);
			Thread.sleep(5000);
			test.log(LogStatus.INFO, "Updated store coordinates successfully");

			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Order Move To Next Step--------------------------------------------------------------*/
	public String orderMoveToNextStep(String object) throws InterruptedException, IOException {
		String result = Constants.FAIL_WEB;
		try {

			String order_id = getTheValueFromUI(object); // this will fetch the orderid from mobile, pass the xpath of
															// the orderId
			String temp[] = order_id.split("#");
			order_id = temp[1];
			driver.get("https://staging..dev/hyperlocal//index.html");
			Thread.sleep(2000);
			driver.manage().window().maximize();
			Thread.sleep(1000);
			WebElement element = driver
					.findElement(By.xpath("//span[@data-path=\"/hyperlocal/api/v1/Internal/MoveToNextState\"]"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Try it out ']")).click();
			Thread.sleep(2000);
			WebElement text = driver.findElement(By.xpath("//*[@placeholder=\"OrderId\"]"));

			String textcontent = text.getText();
			text.clear();
			Thread.sleep(2000);
			text.sendKeys(order_id);

			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Execute']")).click();
			Thread.sleep(5000);
			test.log(LogStatus.INFO, "Updated store coordinates successfully");

			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Enable CashFree Payment Gateway-------------------------------------------------*/

	public String EnableCashFreePaymentGateway(String phone) throws InterruptedException, IOException {
		String result = Constants.FAIL_WEB;

		try {

			driver.get("https://staging..dev/website//index.html");
			Thread.sleep(2000);
			driver.manage().window().maximize();
			Thread.sleep(2000);
			WebElement element = driver.findElement(
					By.xpath("//span[@data-path='/website/api/v1/Internal/EnableCashfreePaymentGateway']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Try it out ']")).click();
			Thread.sleep(2000);
			WebElement text = driver.findElement(By.xpath("//textarea[@class=\"body-param__text\"]"));

			String textcontent = text.getText();
			text.clear();
			Thread.sleep(2000);
			text.sendKeys("{ \"phoneNumber\": \"" + phone + "\", \"region\": \"IN\" }");

			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Execute']")).click();
			Thread.sleep(5000);
			Thread.sleep(3000);
			test.log(LogStatus.INFO, "Allow checkout completed");

			result = Constants.PASS_WEB;
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;
	}

	/*------------------------------------------------Close App-------------------------------------------------*/

	public String closeApp() throws IOException, InterruptedException {
		String result = null;
		try {

			if (aDriver != null && driver != null) {
				aDriver.quit();
				driver.quit();

				result = Constants.PASS;
			}
		} catch (Exception ex) {

			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}

		return result;
	}

	/*------------------------------------------------Report Failure-------------------------------------------------*/

	public void reportFailure(String execution_result, String description) throws IOException, InterruptedException {
		takeScreenshot(execution_result);
		test.log(LogStatus.INFO, "Exception caught:" + description);
		test.log(LogStatus.FAIL, "Test failed");
		Assert.fail(execution_result);

	}

	/*------------------------------------------------Report Success-------------------------------------------------*/

	public void reportPass(String execution_result, String description) throws IOException, InterruptedException {
		/*
		 * try { takeScreenshot(execution_result); } catch(IOException e) {
		 * test.log(LogStatus.INFO,
		 * "Failed while capturing screenshot for passed test");
		 * //test.log(LogStatus.FAIL, e.getMessage()); }
		 */
		takeScreenshot(execution_result);
		// test.log(LogStatus.PASS, "Test successful");
	}

	/*------------------------------------------------Take screenshot-------------------------------------------------*/

	public void takeScreenshot(String execution_result) throws IOException, InterruptedException {

		Date d = new Date();
		if (execution_result == "PASS") {
			String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
			String path = Constants.Successfull_tests_screens + screenshotFile;
			try {
				File scrFile = ((TakesScreenshot) aDriver).getScreenshotAs(OutputType.FILE);

				FileUtils.copyFile(scrFile, new File(path));
				try {
					test.log(LogStatus.INFO, test.addScreenCapture(path));
					test.log(LogStatus.PASS, "Test successful");
				} catch (Exception e) {
					test.log(LogStatus.INFO, "couldn't capture screenshot");
				}
			} catch (IOException e) {
				test.log(LogStatus.INFO,
						"some issue happened while capturing screenshot for successful test" + e.getMessage());
				// test.log(LogStatus.FAIL, e.getMessage());
				// Assert.fail();
				e.printStackTrace();
			}

		} else {

			String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";

			String path = Constants.failed_tests_screens + screenshotFile;
			try {
				File scrFile = ((TakesScreenshot) aDriver).getScreenshotAs(OutputType.FILE);
//try {
				FileUtils.copyFile(scrFile, new File(path));
				try {
					test.log(LogStatus.INFO, test.addScreenCapture(path));
				} catch (Exception e) {
					test.log(LogStatus.INFO, "couldn't capture screenshot");
				}
			} catch (IOException e) {
				// test.log(LogStatus.INFO, "some issue happened while capturing screenshot for
				// successful test" + e.getMessage());
				test.log(LogStatus.FAIL, e.getMessage());
				Assert.fail();
				e.printStackTrace();
			}

		}

	}

	/*------------------------------------------------Report Success for website testcases-------------------------------------------------*/

	public void reportPassforWeb(String execution_result, String description) throws IOException, InterruptedException {
		takeScreenshotForWeb(execution_result);
		test.log(LogStatus.PASS, "Execution result: " + execution_result);

	}
	/*------------------------------------------------Report Failure for website test cases-------------------------------------------------*/

	public void reportFailureforWeb(String execution_result, String description)
			throws IOException, InterruptedException {

		takeScreenshotForWeb(execution_result);
		test.log(LogStatus.FAIL, "Execution result: " + execution_result);
		Assert.fail();
		test.log(LogStatus.INFO, description);
		driver.close();

		Assert.fail(execution_result);
	}
	/*------------------------------------------------Take screenshot for website testcases-------------------------------------------------*/

	public void takeScreenshotForWeb(String execution_result) throws IOException, InterruptedException {
		Date d = new Date();
//if(execution_result=="PASS") {
		if (execution_result == "Selenium Test successfull") {
			String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
			String path = Constants.Successfull_tests_screens + screenshotFile;
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(path));
			} catch (IOException e) {
				test.log(LogStatus.FAIL, e.getMessage());
				Assert.fail();
				e.printStackTrace();
			}

			test.log(LogStatus.INFO, test.addScreenCapture(path));
		} else if (execution_result == "Selenium Test Failed") {
			String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
			String path = Constants.failed_tests_screens + screenshotFile;
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File(path));
			} catch (IOException e) {
				test.log(LogStatus.FAIL, e.getMessage());
				Assert.fail();
				e.printStackTrace();
			}

			test.log(LogStatus.INFO, test.addScreenCapture(path));
		}

	}
	/*--------------------------------------------------------Scroll To element---------------------------------------------------------------------*/

	public String scrollToAnElementByText(String text) throws IOException, InterruptedException {
		String result = null;
		try {
			aDriver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector())"
					+ ".scrollIntoView(new UiSelector().text(\"" + text + "\"));"));
			result = Constants.PASS;
		} catch (Exception e) {
			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, e.getMessage());
			Assert.fail();
			e.printStackTrace();
		}
		return result;
	}

	/*--------------------------------------------------------WaitForSometime------------------------------------------------------------------- 	*/

	public String WaitForSometime(String locatorkey) throws InterruptedException, IOException {
		String result = null;
		try {

			WebDriverWait allwait = new WebDriverWait(aDriver, 30);

			if (locatorkey.endsWith("_xpath")) {
				allwait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(prop.getProperty(locatorkey))));
				result = Constants.PASS;

			} else if (locatorkey.endsWith("_id")) {
				allwait.until(ExpectedConditions.presenceOfElementLocated(By.id(prop.getProperty(locatorkey))));
				result = Constants.PASS;

			}
		} catch (Exception ex) {

			result = Constants.FAIL;
			takeScreenshot(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;

	}

	/*--------------------------------------------------------Web_WaitForSometime------------------------------------------------------------------- 	*/

	public String WebWaitForSometime(String locatorkey) throws InterruptedException, IOException {
		String result = null;
		try {

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

			if (locatorkey.endsWith("_xpath")) {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(prop.getProperty(locatorkey))));
				result = Constants.PASS_WEB;

			} else if (locatorkey.endsWith("_id")) {
				wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id(prop.getProperty(locatorkey))));
				result = Constants.PASS_WEB;

			}
		} catch (Exception ex) {

			result = Constants.FAIL_WEB;
			takeScreenshotForWeb(result);
			test.log(LogStatus.FAIL, ex.getMessage());
			Assert.fail();
		}
		return result;

	}

	/**********************************************************
	 * End of functions
	 ********************************************************************/
}
