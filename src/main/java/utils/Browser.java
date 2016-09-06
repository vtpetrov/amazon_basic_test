package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class Browser {

    public static WebDriver drv;
    public static WebDriverWait wait;

    /**
     * Open selected type of browser.
     *
     * @param type 1 - Firefox; 2 - Chrome
     * @throws Throwable
     */
    public static void start(int type) throws Throwable {
        try {
            System.out.println(" start new browser and set its preferences.");

            if (type == 1) {
                System.out.println("start new FIREFOX browser.");

                // Enable this code if you have your older version of Firefox installed in a different directory than the default one:
/*
                // Put the path to your Firefox executable here (e.g. "C:\Program Files (x86)\Mozilla Firefox\v45.3.0\firefox.exe") :
                Path pathFF = Paths.get("C:", "Program Files (x86)", "Mozilla Firefox", "v45.3.0", "firefox.exe");


                String sFFpath = pathFF.toString();
                System.out.println("sFFpath = " + sFFpath);

                FirefoxBinary ffBinary = new FirefoxBinary(new File(sFFpath));
                drv = new FirefoxDriver(ffBinary, new FirefoxProfile());
*/

                // Enable this code if you have Firefox version 45 (or older) installed on your default location:
                drv = new FirefoxDriver();

            }
            if (type == 2) {
                System.out.println("start new CHROME browser.");
                System.out.println("Not implemented");
            }

            drv.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

//             Set wait variable which can be used when needed to wait for element (in seconds)
            wait = new WebDriverWait(drv, 5);

            drv.manage().window().maximize(); // Maximize browser window

        } catch (Throwable e) {
            System.out.println("Cannot open browser properly !");
            System.out.println(e.getMessage());
            System.out.println("e.getMessage() = " + e.getMessage());
            throw e;

        }

    }

    public static void close() {
        try {
            drv.quit();
        } catch (Throwable e) {
            System.out.println("e.getMessage(): " + e.getMessage());
        }

    }

}
