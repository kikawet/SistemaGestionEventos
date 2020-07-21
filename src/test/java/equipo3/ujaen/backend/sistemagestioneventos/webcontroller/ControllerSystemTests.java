package equipo3.ujaen.backend.sistemagestioneventos.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
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
@SpringBootTest (webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerSystemTests {

    @LocalServerPort
    private int serverPort;

    @Autowired
    private WebDriver driver;
    private String appUrl;

    @PostConstruct
    public void init()  {
	appUrl=String.format("http://localhost:%d/sge-api",serverPort);
    }


    @Test
    void testIndexPage() throws Exception {
	driver.get(appUrl);
	assertThat(driver.getTitle()).isEqualTo("Inicio");
    }


    @Test
    public void testbuscando() {
	// Test name: test buscar evento 1
	// Step # | name | target | value | comment
	// 1 | store | Evento 1 | texto |
	String texto = "1";
	// 2 | open | /webapp/ | |
	driver.get(appUrl);
	// 3 | click | name=buscarNombre | |
	driver.findElement(By.name("buscarNombre")).click();
	// 4 | type | name=buscarNombre | ${texto} |
	driver.findElement(By.name("buscarNombre")).sendKeys(texto);
	// 5 | sendKeys | name=buscarNombre | ${KEY_ENTER} |
	driver.findElement(By.name("buscarNombre")).sendKeys(Keys.ENTER);
	// 6 | assertText | css=.card-title | ${texto} |

	assertThat(driver.findElement(By.cssSelector(".card-title")).getText()).containsSequence(texto);
	// 7 | assertElementNotPresent | css=.card:nth-child(2) | |
	{
	    List<WebElement> elements = driver.findElements(By.cssSelector(".card:nth-child(2)"));
	    assertThat(elements).isEmpty();
	}
    }

    @TestConfiguration
    static class TestConfig {
	@Bean (destroyMethod = "quit")
	public WebDriver getDriver(@Value("${selenium.driver}")String tipoDriver) {
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


}
