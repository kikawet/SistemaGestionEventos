package equipo3.ujaen.backend.sistemagestioneventos.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerSystemTests {

	@LocalServerPort
	private int serverPort;

	@Autowired
	private WebDriver driver;
	private String appUrl;

	@PostConstruct
	public void init() {
		appUrl = String.format("http://localhost:%d/sge-api", serverPort);
	}

	@Test
	void testIndexPage() throws Exception {
		driver.get(appUrl);
		assertThat(driver.getTitle()).isEqualTo("Inicio");
	}

	@TestConfiguration
	static class TestConfig {
		@Bean(destroyMethod = "quit")
		public WebDriver getDriver(@Value("${selenium.driver}") String tipoDriver) {
			WebDriver driver = null;

			switch (tipoDriver) {
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "chrome":
				driver = new ChromeDriver();
				break;
			case "opera":
				driver = new OperaDriver();
				break;
			case "safari":
				driver = new SafariDriver();
				break;
			default:
				driver = new InternetExplorerDriver();
				break;
			}

			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
			return driver;
		}
	}

	@Test
	  public void perfilTest() {
	    // Test name: PerfilTest
	    // Step # | name | target | value
	    // 1 | open | http://localhost:12021/sge-api/ | 
	    driver.get("http://localhost:12021/sge-api/");
	    // 2 | setWindowSize | 974x765 | 
	    driver.manage().window().setSize(new Dimension(974, 765));
	    // 3 | click | css=.btn:nth-child(2) | 
	    driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
	    // 4 | click | css=.card-body | 
	    driver.findElement(By.cssSelector(".card-body")).click();
	    // 5 | type | id=inputLogin | Patricio Ruiz
	    driver.findElement(By.id("inputLogin")).sendKeys("Patricio Ruiz");
	    // 6 | type | id=inputPassword | 1234
	    driver.findElement(By.id("inputPassword")).sendKeys("1234");
	    // 7 | click | css=.custom-control-label | 
	    driver.findElement(By.cssSelector(".custom-control-label")).click();
	    // 8 | click | css=.btn | 
	    driver.findElement(By.cssSelector(".btn")).click();
	    // 9 | click | linkText=Perfil | 
	    driver.findElement(By.linkText("Perfil")).click();
	    // 10 | assertText | css=.container > h2 | Patricio Ruiz
	    assertThat(driver.findElement(By.cssSelector(".container > h2")).getText()).isEqualTo("Patricio Ruiz");
	    // 11 | assertText | css=p:nth-child(5) > .glyphicon | 1
	    assertThat(driver.findElement(By.cssSelector("p:nth-child(5) > .glyphicon")).getText()).isEqualTo("1");
	    // 12 | click | css=.card:nth-child(10) .btn | 
	    driver.findElement(By.cssSelector(".card:nth-child(10) .btn")).click();
	    // 13 | click | css=.card:nth-child(1) .btn | 
	    driver.findElement(By.cssSelector(".card:nth-child(1) .btn")).click();
	    // 14 | mouseOver | css=.btn-warning | 
	    {
	      WebElement element = driver.findElement(By.cssSelector(".btn-warning"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element).perform();
	    }
	    // 15 | mouseOut | css=.btn-warning | 
	    {
	      WebElement element = driver.findElement(By.tagName("body"));
	      Actions builder = new Actions(driver);
	      builder.moveToElement(element, 0, 0).perform();
	    }
	    // 16 | click | linkText=Perfil | 
	    driver.findElement(By.linkText("Perfil")).click();
	    // 17 | click | css=#command > .btn | 
	    driver.findElement(By.cssSelector("#command > .btn")).click();
	  }
}
