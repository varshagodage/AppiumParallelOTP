package AppiumMaven.AppiumM;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.util.FileUtil;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.sun.jna.platform.FileUtils;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

/**
 * Unit test for simple App.
 *
 */
public class AppTest 
{
	private AndroidDriver<AndroidElement> driver;
	AppiumDriverLocalService service;
	
	@Parameters({"UDID","PORT"})
	@BeforeMethod
	public void setCapabilities(String UDID,int PORT) throws MalformedURLException
	{
		
		//Working Code to start appium server programattically!!
		/*AppiumServiceBuilder builder=new AppiumServiceBuilder();
		builder.usingPort(PORT);
		builder.withIPAddress("127.0.0.1");
		service = AppiumDriverLocalService.buildService(builder);
		service.start();*/
	   
	
		
		DesiredCapabilities cap=new DesiredCapabilities();
		cap.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
		cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "android");
		cap.setCapability("appPackage", "com.jio.myjiohome");
		cap.setCapability("appActivity", "com.access_company.twine.android.dmc.SplashScreenOtp");
		cap.setCapability("autoGrantPermissions", true);
		cap.setCapability("clearSystemFiles", true);
		cap.setCapability("orientation","PORTRAIT");
		cap.setCapability("noReset", false);
		cap.setCapability(MobileCapabilityType.UDID, UDID);
		if(UDID.equals("emulator-5554"))
		{
			cap.setCapability("noSign", true);
		}
		driver=new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:"+PORT+"/wd/hub"),cap);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		/*Map<String,Object> cmds=new HashMap<String,Objec;t>();
		cmds.put("command", "pm grant com.jio.myjiohome");
		cmds.put("args", Lists.newArrayList("android.permission.ACCESS_FINE_LOCATION","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"));
		driver.executeScript("mobile:shell",cmds);*/
	}
  
	@Parameters({"UDID","MobileNo","MsgActivity"})
	@Test
	public void Test1(String UDID,String MobileNo,String MsgActivity) throws InterruptedException, IOException
	{
		driver.findElementById("com.jio.myjiohome:id/btn_non_jiouser").click();
		driver.findElementById("com.jio.myjiohome:id/edt_mobile_no").sendKeys(MobileNo);
		driver.findElementById("com.jio.myjiohome:id/btn_submit").click();
		Thread.sleep(5000);
		String otp=ReadOTP.getOtp(driver, UDID,MsgActivity);
		driver.findElementById("com.jio.myjiohome:id/edt_verify_code").sendKeys(otp);
		driver.findElementById("com.jio.myjiohome:id/btn_submit").click();
		
	    driver.findElementById("com.jio.myjiohome:id/txt_skip_page").click();    
	    AndroidElement NowPlaying_button=driver.findElementById("com.jio.myjiohome:id/action_renderer");
	    if(NowPlaying_button.isDisplayed())
	    {
	    	System.out.println("Successfully Logged In");
	   
	    }
	  
	    
	}
	
	@Parameters({"UDID","MobileNo"})
	@AfterMethod
	public void quitDriver(ITestResult result, String UDID,String MobileNo) throws IOException
	{
		 String imglocation="target/screencap/"+UDID+"/";
		 new File(imglocation).mkdirs();
		 File srcfile=driver.getScreenshotAs(OutputType.FILE);
		 String timestamp=new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
		 String filename=imglocation+MobileNo+result.getMethod().getMethodName()+"_"+timestamp+".jpg";
		 FileUtil.copyFile(srcfile, new File(filename));
		driver.quit();
		//service.stop();
	}
}
