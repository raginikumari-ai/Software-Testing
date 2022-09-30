package com.nf.mobile.web.hybrid.driver;

import java.io.IOException;
import java.util.Hashtable;

import org.openqa.selenium.WebDriver;

import com.nf.mobile.web.hybrid.util.Constants;
import com.nf.mobile.web.hybrid.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;

public class DriverScript {

	ExtentTest test;

	//AppKeywords keywords;
	GenericKeywords keywords;
	public DriverScript(ExtentTest test, AndroidDriver aDriver, WebDriver driver, String path) throws IOException {
		this.test = test;
	
		keywords =  new GenericKeywords(test, aDriver, driver, path);
	}

	public void executeKeywords(String testUnderExecution, Hashtable<String,String> testData, String datasheet) throws IOException, InterruptedException {

		String keywords_sheet = "Keywords";
		Xls_Reader xls = new Xls_Reader(datasheet);
		int rows = xls.getRowCount(keywords_sheet);


		//reading data from Data sheet_________________________________________________________________________________________
		for(int rnum=2; rnum <=rows; rnum++) {

			String tcid = xls.getCellData(keywords_sheet, Constants.TCID_COL , rnum);

			if(tcid.equals(testUnderExecution)) {

				String description = xls.getCellData(keywords_sheet, Constants.DESCRIPTION_COL , rnum);
				String keyword = xls.getCellData(keywords_sheet, Constants.KEYWORD_COL , rnum);
				String object = xls.getCellData(keywords_sheet, Constants.OBJECT_COL , rnum);
				String dataKey = xls.getCellData(keywords_sheet, Constants.DATA_COL , rnum);
				String dataKey_a = xls.getCellData(keywords_sheet, Constants.DATA_COL_a , rnum);
				String object_a = xls.getCellData(keywords_sheet, Constants.OBJECT_COL_a , rnum);
				String object_b = xls.getCellData(keywords_sheet, Constants.OBJECT_COL_b , rnum);
				String pack = xls.getCellData(keywords_sheet, Constants.pack , rnum);
				String activity = xls.getCellData(keywords_sheet, Constants.activity , rnum);

				System.out.println(object);

				test.log(LogStatus.INFO, "  Test Case :   " + description);
				String execution_result = ""; 

				if(keyword.equalsIgnoreCase("clicking"))
				{
					execution_result=keywords.clicking(object);
				}
				else if(keyword.equalsIgnoreCase("checkTheUncheckedBox"))
				{
					execution_result=keywords.checkTheUncheckedBox(object);
				}
				else if(keyword.equalsIgnoreCase("app_clearContent"))
				{
					execution_result=keywords.app_clearContent(object);
				}
				else if(keyword.equalsIgnoreCase("type"))
				{
					execution_result=keywords.type(object,testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("typeLocationInApp"))
				{
					execution_result=keywords.typeLocationInApp(object,testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("closeApp"))
				{
					execution_result=keywords.closeApp();
				}
				else if(keyword.equalsIgnoreCase("verifyText"))
				{
					execution_result=keywords.verifyText(object, testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("WaitForSometime"))
				{
					execution_result=keywords.WaitForSometime(object);
				}
				else if(keyword.equalsIgnoreCase("scrollFromElementToElement"))
				{
					execution_result=keywords.scrollFromElementToElement(object_a, object_b);
				}
				else if(keyword.equalsIgnoreCase("scrollTobottom"))
				{
					execution_result=keywords.scrollTobottom();
				}
				else if(keyword.equalsIgnoreCase("hideKeyboard"))
				{
					execution_result=keywords.hideKeyboard();
				}
				else if(keyword.equalsIgnoreCase("PressAndroidbackbutton"))
				{
					execution_result=keywords.PressAndroidbackbutton();
				}
				else if(keyword.equalsIgnoreCase("verifyOTP"))
				{
					execution_result=keywords.verifyOTP(pack, activity);
				}
				else if(keyword.equalsIgnoreCase("verifyOTPforLogin"))
				{
					execution_result=keywords.verifyOTPforLogin(pack, activity);
				}
				else if(keyword.equalsIgnoreCase("RareActions"))
				{
					execution_result=keywords.RareActions(object);
				}
				else if(keyword.equalsIgnoreCase("loadWhatsapp"))
				{
					execution_result=keywords.loadWhatsapp(pack, activity);
				}
				else if(keyword.equalsIgnoreCase("verifyWhatsAppMessage")) {
					execution_result=keywords.verifyLastWhatsappMessage(object, testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("deleteUser"))
				{
					execution_result=keywords.deleteUser(testData.get(dataKey), testData.get(dataKey_a));
				}
				else if(keyword.equalsIgnoreCase("DeleteStore"))
				{
					execution_result=keywords.DeleteStore(testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("EnableCashFreePaymentGateway"))
				{
					execution_result=keywords.EnableCashFreePaymentGateway(testData.get(dataKey));
				}
				else if(keyword.equals("EnablingWifi"))
				{
					execution_result=keywords.EnablingWifi();

				}
				else if(keyword.equalsIgnoreCase("UpdateStoreCoordinates"))
				{
					execution_result=keywords.UpdateStoreCoordinates(testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("Web_LaunchStore"))
				{
					execution_result=keywords.Web_LaunchStore(testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("MobileWeb_LaunchStore"))
				{
					execution_result=keywords.MobileWeb_LaunchStore(testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("Web_clicking"))
				{
					execution_result=keywords.Web_clicking(object);
				}
				else if(keyword.equalsIgnoreCase("Web_type"))
				{
					execution_result=keywords.Web_type(object,testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("Web_type_location"))
				{
					execution_result=keywords.Web_type_location(object,testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("Web_verifyOTP"))
				{
					execution_result=keywords.Web_verifyOTP(pack, activity);
				}
				else if(keyword.equalsIgnoreCase("Web_scrollToElement"))
				{
					execution_result=keywords.Web_scrollToElement(object);
				}
				else if(keyword.equalsIgnoreCase("RightSwipeToAccept"))
				{
					execution_result=keywords.RightSwipeToAccept(object);
				}
				else if(keyword.equalsIgnoreCase("WebWaitForSometime"))
				{
					execution_result=keywords.WebWaitForSometime(object);
				}
				else if(keyword.equalsIgnoreCase("readToastMessage"))
				{
					execution_result=keywords.readToastMessage(object,testData.get(dataKey)); //call the function to read multiple image files to identify presence of toast msg
				}
				else if(keyword.equalsIgnoreCase("scrollToTop")) 
				{
					execution_result=keywords.scrollToTop();
				}
				else if(keyword.equalsIgnoreCase("orderMoveToNextStep"))
				{
					execution_result=keywords.orderMoveToNextStep(object);
				}
				else if(keyword.equalsIgnoreCase("scrolldropdown"))
				{
					execution_result=keywords.scrolldropdown(object);
				}
				else if(keyword.equalsIgnoreCase("WebImplicitWait"))
				{
					execution_result=keywords.WebImplicitWait();
				}
				else if(keyword.equalsIgnoreCase("verifyCompleted")) 
				{
					execution_result=keywords.verifyCompleted();
				}
				else if(keyword.equalsIgnoreCase("verifyTextRejected"))
				{
					execution_result=keywords.verifyTextRejected(object, testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("WebVerifyText"))
				{
					execution_result=keywords.Web_VerifyText(object, testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("PressWebbackbutton"))
				{
					execution_result=keywords.PressWebbackbutton();
				}
				else if(keyword.equalsIgnoreCase("ScrollToElement"))
				{
					execution_result=keywords.scrollToAnElementByText(testData.get(dataKey));
				}
				else if(keyword.equalsIgnoreCase("scrollToRight"))
				{
					execution_result=keywords.scrollToRight(object);
				}
				else if(keyword.equalsIgnoreCase("waitFewSecondsInApp"))
				{
					execution_result=keywords.waitFewSecondsInApp();
				}
				else if(keyword.equalsIgnoreCase("ValidateMobileMessage_ShopBills"))
				{
					execution_result=keywords.ValidateMobileMessage_ShopBills(pack, activity, testData.get(dataKey), object);
				}
				else if(keyword.equalsIgnoreCase("Estore_WhatsAppMsgsValidation"))
				{
					execution_result=keywords.Estore_WhatsAppMsgsValidation(pack, activity);
				}
				else if(keyword.equalsIgnoreCase("Web_verifyTextAfter5Min"))
				{
					execution_result=keywords.Web_verifyTextAfter5Min(object, testData.get(dataKey));
				}
				
				//test.log(LogStatus.INFO, "Execution Result for test step  "  + " : " + description + " : " + "   is   :  " + execution_result);
				if(execution_result.equals(Constants.PASS)) {
					keywords.reportPass(execution_result, description);
				}
				else if(execution_result.equals(Constants.PASS_WEB)) {
					keywords.reportPassforWeb(execution_result, description);
				}
				else if(execution_result.equals(Constants.FAIL_WEB)) {
					keywords.reportFailureforWeb(execution_result, description);
				}
				else if(execution_result.equals(Constants.FAIL)) {
					keywords.reportFailure(execution_result, description);
				}

			}


		}


	}
	
	 public GenericKeywords getKeywords() {
		return keywords;
	}

}

