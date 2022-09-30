package Car_accessories;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.nf.mobile.web.hybrid.data.DataUtil;
import com.nf.mobile.web.hybrid.driver.DriverScript;
import com.nf.mobile.web.hybrid.util.Constants;
import com.nf.mobile.web.hybrid.util.ExtentManager;
import com.nf.mobile.web.hybrid.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import com.nf.mobile.web.hybrid.util.Constants;
import com.nf.mobile.web.hybrid.util.Xls_Reader;

@Test(groups= {"Login"})

public class Login {

	static String datasheet = Constants.DATA_XLSX_CarAccessories;  
	static String path = Constants.PROPERTIES_FILE_Car_Accessories; 
	ExtentReports report = ExtentManager.getInstance();
	ExtentTest test = report.startTest("Login"); 
	DriverScript ds;
	static String testCaseName="Login";
	Xls_Reader xls = new Xls_Reader(datasheet);
	AndroidDriver aDriver = null;
	WebDriver driver = null;
	AppiumDriverLocalService service;
	Properties prop;
	
	@Test(dataProvider="getData")
	public void main(Hashtable<String,String> data) throws IOException, InterruptedException {
		
		
//-----------------------------------------------------reports-----------------------------------------------------------------------//
			
		test.log(LogStatus.INFO,
				"Starting the test       :    " + testCaseName + "     :     with test data: " + data.toString());

				prop = new Properties();

				String apk = Constants.APK;



				try {
				FileInputStream fs = new FileInputStream(path);
				prop.load(fs);
				} catch (FileNotFoundException e1) {
				e1.printStackTrace();
				}

				File app = new File(prop.getProperty(apk));


				if (!DataUtil.isTestRunnable(xls, testCaseName)
				|| !data.get(Constants.RUNMODE_COL).equalsIgnoreCase(prop.getProperty("ResourceName"))) {
				test.log(LogStatus.INFO, "Skipping the test as runmode is NO");
				throw new SkipException("Skipping the test as runmode is NO");

				} else {


				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability("deviceName", prop.getProperty("deviceName"));
				capabilities.setCapability("platformVersion", prop.getProperty("platformVersion"));
				capabilities.setCapability("platformName", prop.getProperty("platformName"));
				capabilities.setCapability("app", app.getAbsolutePath());
				capabilities.setCapability(MobileCapabilityType.NO_RESET, "false");
				capabilities.setCapability("autoGrantPermissions", "true");

				service = AppiumDriverLocalService
				.buildService(new AppiumServiceBuilder().usingDriverExecutable(new File(Constants.NODEJS_PATH))
				.withAppiumJS(new File(Constants.APPIUM_PATH)));
				service.start();
				try {
				aDriver = new AndroidDriver(new URL(prop.getProperty("hubURL")), capabilities);
				System.setProperty("webdriver.chrome.driver", Constants.ChromeDriverLoc);
				driver = new ChromeDriver();

				} catch (Exception e) {
				e.printStackTrace();
				test.log(LogStatus.INFO, e.getMessage());

				}

				ds = new DriverScript(test, aDriver, driver, path);
				ds.executeKeywords(testCaseName, data, datasheet); // new change
				Thread.sleep(150);

				}

				}
	
	@AfterMethod
	public void quit() throws IOException, InterruptedException {
		if(ds!=null) {
		ds.getKeywords().closeApp();
		ds=null;
		service.stop();
		}
		if(report!=null) {
			report.endTest(test);
			report.flush();
			report=null;
		}
	}
	
	@DataProvider
	public Object[][] getData(){
	
		return DataUtil.getData(xls, testCaseName, datasheet);
}
}