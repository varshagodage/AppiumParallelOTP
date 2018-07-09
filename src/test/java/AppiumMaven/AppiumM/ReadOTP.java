package AppiumMaven.AppiumM;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class ReadOTP {

	private static AndroidDriver<AndroidElement> driver;
	static AppiumDriverLocalService service;
	
	
	public static String getOtp(AndroidDriver<AndroidElement> driver,String UDID,String MsgActivity) throws IOException, InterruptedException
	{ 
		Runtime.getRuntime().exec("adb -s "+UDID+" shell am start -S -n "+MsgActivity);
		String text=driver.findElementsById("com.google.android.apps.messaging:id/conversation_snippet").get(0).getText();
		Pattern p=Pattern.compile("\\d{6}");
		Matcher m=p.matcher(text);
		String otp="";
		if(m.find())
		{
			otp=m.group();
		}
		System.out.println("Pressig back button");
		Runtime.getRuntime().exec("adb -s "+UDID+" shell input keyevent 4");
		//Thread.sleep(1000);
		//Runtime.getRuntime().exec("adb -s "+UDID+" input keyevent 4");
		
		Thread.sleep(2000);
		
		return otp;
		
	}
	
}
