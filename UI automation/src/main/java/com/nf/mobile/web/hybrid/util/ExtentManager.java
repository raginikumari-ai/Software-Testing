//http://relevantcodes.com/Tools/ExtentReports2/javadoc/index.html?com/relevantcodes/extentreports/ExtentReports.html

package com.nf.mobile.web.hybrid.util;

import java.io.File;
import java.util.Date;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;
	
	private ExtentManager(){}

	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d= new Date();
			String fileName=d.toString().replace(":", "_").replace(" ", "_")+".html";
			extent = new ExtentReports(Constants.REPORT_PATH+fileName, true, DisplayOrder.NEWEST_FIRST);

			extent.loadConfig(new File(System.getProperty("user.dir")+"//ReportsConfig.xml"));

		
			String s = Constants.APK_FILE_PATH;
			int stop = s.length();
			int start = stop -44;
			String version = s.substring(start, stop);
			
			

			    extent.addSystemInfo("Selenium Version", "4.1.0").addSystemInfo("Environment", version);
	
		}
		return extent;
	}
}
