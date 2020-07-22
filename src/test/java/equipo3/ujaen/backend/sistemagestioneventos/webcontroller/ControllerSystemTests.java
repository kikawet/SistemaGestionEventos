package equipo3.ujaen.backend.sistemagestioneventos.webcontroller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
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

	@Test
	public void crearEvento() {
		// Test name: CrearEvento
		// Step # | name | target | value
		// 1 | open | /sge-api/ |
		driver.get(appUrl);
		// 2 | setWindowSize | 1552x840 |
		driver.manage().window().setSize(new Dimension(1552, 840));
		// 3 | click | css=.btn:nth-child(2) |
		driver.findElement(By.cssSelector(".btn:nth-child(2)")).click();
		// 4 | click | id=inputLogin |
		driver.findElement(By.id("inputLogin")).click();
		// 5 | type | id=inputLogin | Patricio Ruiz
		driver.findElement(By.id("inputLogin")).sendKeys("Patricio Ruiz");
		// 6 | type | id=inputPassword | 1234
		driver.findElement(By.id("inputPassword")).sendKeys("1234");
		// 7 | click | css=.btn |
		driver.findElement(By.cssSelector(".btn")).click();
		// 8 | click | linkText=Crear Evento |
		driver.findElement(By.linkText("Crear Evento")).click();
		// 9 | click | id=inputTitulo |
		driver.findElement(By.id("inputTitulo")).click();
		// 10 | click | id=inputTitulo |
		driver.findElement(By.id("inputTitulo")).click();
		// 11 | type | id=inputTitulo | Festival Música Electrónica
		driver.findElement(By.id("inputTitulo")).sendKeys("Festival Música Electrónica");
		// 12 | click | id=inputDescripcion |
		driver.findElement(By.id("inputDescripcion")).click();
		// 13 | type | id=inputDescripcion | Aforo limitado, para un festival en el cual
		// van a venir 12 de los artistas mas destacados del momento.
		driver.findElement(By.id("inputDescripcion")).sendKeys(
				"Aforo limitado, para un festival en el cual van a venir 12 de los artistas mas destacados del momento.");
		// 14 | click | name=tipoEvento |
		driver.findElement(By.name("tipoEvento")).click();
		// 15 | click | name=tipoEvento |
		driver.findElement(By.name("tipoEvento")).click();
		// 16 | click | id=inputLugar |
		driver.findElement(By.id("inputLugar")).click();
		// 17 | type | id=inputLugar | Calle piruleta numero 1432
		driver.findElement(By.id("inputLugar")).sendKeys("Calle piruleta numero 1432");
		// 18 | click | id=inputFecha |
		driver.findElement(By.id("inputFecha")).click();
		// 19 | type | id=inputFecha | 2020-07-14T16:37
		driver.findElement(By.id("inputFecha")).sendKeys("2020-07-14T16:37");
		// 20 | type | id=inputAforoMaximo | -1
		driver.findElement(By.id("inputAforoMaximo")).sendKeys("-1");
		// 21 | click | css=.btn-primary |
		driver.findElement(By.cssSelector(".btn-primary")).click();
		driver.findElement(By.id("inputAforoMaximo")).clear();
		driver.findElement(By.id("inputFecha")).clear();

		// 22 | click | css=.form-group:nth-child(6) |
		driver.findElement(By.cssSelector(".form-group:nth-child(6)")).click();
		// 23 | click | id=inputAforoMaximo |
		driver.findElement(By.id("inputAforoMaximo")).click();
		// 24 | type | id=inputAforoMaximo | -12
		driver.findElement(By.id("inputAforoMaximo")).sendKeys("-12");
		// 25 | click | id=inputAforoMaximo |
		driver.findElement(By.id("inputAforoMaximo")).click();
		// 26 | click | css=.btn-primary |
		driver.findElement(By.cssSelector(".btn-primary")).click();
		driver.findElement(By.id("inputAforoMaximo")).clear();
		// 27 | assertElementPresent | id=aforoMaximo.errors |
		{
			List<WebElement> elements = driver.findElements(By.id("aforoMaximo.errors"));
			assert (elements.size() > 0);
		}
		// 28 | assertElementPresent | css=#inputLugar.is-valid |
		{
			List<WebElement> elements = driver.findElements(By.cssSelector("#inputLugar.is-valid"));
			assert (elements.size() > 0);
		}
		// 29 | click | id=inputAforoMaximo |
		driver.findElement(By.id("inputAforoMaximo")).click();
		// 30 | type | id=inputAforoMaximo | 10
		driver.findElement(By.id("inputAforoMaximo")).sendKeys("10");
		// 31 | click | id=inputAforoMaximo |
		driver.findElement(By.id("inputAforoMaximo")).click();
		// 32 | click | id=inputFecha |
		driver.findElement(By.id("inputFecha")).click();
		// 33 | type | id=inputFecha | 2020-07-22T16:37
		driver.findElement(By.id("inputFecha")).sendKeys("2020-07-22T16:37");
		// 34 | click | css=.form-group:nth-child(8) |
		driver.findElement(By.cssSelector(".form-group:nth-child(8)")).click();
		// 35 | click | css=.btn-primary |
		driver.findElement(By.cssSelector(".btn-primary")).click();

		driver.findElement(By.cssSelector("#command > .btn")).click();
	}

	@Test
	public void perfilTest() {
		// Test name: PerfilTest
		// Step # | name | target | value
		// 1 | open | http://localhost:12021/sge-api/ |
		driver.get(appUrl);
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
		driver.findElement(By.cssSelector(".card:nth-child(6) .btn")).click();
		// 13 | click | css=.card:nth-child(1) .btn |
		driver.findElement(By.cssSelector(".card:nth-child(1) .btn")).click();

		// 14 | mouseOut | css=.btn-warning |
		{
			WebElement element = driver.findElement(By.tagName("body"));
			Actions builder = new Actions(driver);
			builder.moveToElement(element, 0, 0).perform();
		}
		// 15 | click | linkText=Perfil |
		driver.findElement(By.linkText("Perfil")).click();
		// 16 | click | css=#command > .btn |
		driver.findElement(By.cssSelector("#command > .btn")).click();
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

			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			return driver;
		}
	}

}
