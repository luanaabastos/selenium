package test.java;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Unidas {

  WebDriver driver;

  @BeforeTest
  public void beforeTest() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--remote-allow-origins=*");
    driver = new ChromeDriver(options);
    driver.manage().window().maximize();

    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
  }

  @Test
  public void procurarCidadeValida() {
    driver.get("http://unidas.com.br");

    WebElement inputCidade = driver.findElement(By.cssSelector("input[placeholder='Digite a cidade, aeroporto ou loja']"));
    inputCidade.sendKeys("Guaratingueta");

    WebElement opcaoCidade = driver.findElement(By.cssSelector("div[class='home-masterflow'] li:nth-child(1)"));
    opcaoCidade.click();

    WebElement modalData = driver.findElement(By.xpath("//div[contains(@class,'p-datepicker-multiple-month')]"));

    Assert.assertTrue(modalData.isDisplayed(),"O menu de data não apareceu na tela");

    WebElement primeiraDataValidaReserva = driver.findElement(By.xpath("(//td/span[not(contains(@class, 'p-disabled'))])[2]"));
    primeiraDataValidaReserva.click();

    WebElement primeiroHorario = driver.findElement(By.xpath("//ul[@class='hours-list']/li[1]"));
    Assert.assertTrue(primeiroHorario.isDisplayed());
    primeiroHorario.click();

    WebElement primeiraDataValidaDevolucao = driver.findElement(By.xpath("(//td/span[not(contains(@class, 'p-disabled'))])[3]"));
    primeiraDataValidaDevolucao.click();

    primeiroHorario = driver.findElement(By.xpath("//ul[@class='hours-list']/li[1]"));
    primeiroHorario.click();

    driver.findElement(By.xpath("//a[normalize-space()='Alugar']")).click();

    String tituloGrupoVeiculos = driver.findElement(By.cssSelector("div[class='col-md-12 col-sm-12'] h1")).getText();

    Assert.assertEquals(tituloGrupoVeiculos, "Escolha seu grupo de veículos", "Não foi possível redirecionar o usuário para a  página de grupo de veículos");
  }

  @Test
  public void procurarCidadeInvalida() {
    driver.get("http://unidas.com.br");

    WebElement inputCidade = driver.findElement(By.cssSelector("input[placeholder='Digite a cidade, aeroporto ou loja']"));
    inputCidade.sendKeys("Itajubá");

    WebElement botaoBuscarLojasProximas = driver.findElement(By.cssSelector(".search-closest-stores.action"));
    String textoBuscarLojasProximas = botaoBuscarLojasProximas.getText();

    Assert.assertEquals(textoBuscarLojasProximas, "BUSCAR LOJAS PRÓXIMAS", "Botão de buscar lojas proximas não aparece");

    botaoBuscarLojasProximas.click();

    WebElement headerLojasProximas = driver.findElement(By.cssSelector(".search-closest-stores.location"));
    List<WebElement> iconeLojasProximas = driver.findElements(By.xpath("(//span[@class='icon-mall'])"));
    String textoLojasProximas = headerLojasProximas.getText();

    Assert.assertEquals(textoLojasProximas, "LOJAS MAIS PRÓXIMAS");
    Assert.assertTrue(iconeLojasProximas.size() > 0);
  }

  @Test
  public void selecionarGrupoDeVeiculos() {
    procurarCidadeValida();

    if (driver.findElement(By.id("modal-cupom")).isDisplayed()) {
      WebElement botaoFecharModal = driver.findElement(By.cssSelector("#modal-fechar-cupom"));
      botaoFecharModal.click();
    }

    WebElement botaoPagarRetirada = driver.findElement(
      By.xpath("(//span[@class='label'][normalize-space()='Pagar na retirada'])[1]"));
    botaoPagarRetirada.click();

    WebElement headerResumoReserva = driver.findElement(
      By.cssSelector(".mb-1")
    );
    String textoHeaderResumoReserva = headerResumoReserva.getText();

    Assert.assertEquals(textoHeaderResumoReserva,"Resumo da reserva", "Não foi possível redirecionar o usuário para a página de resumo de reservas");

    // Seleção de proteções
    WebElement checkBoxProtecoes = driver.findElement(By.xpath("(//span[contains(text(),'Adicionar')])[2]"));
    checkBoxProtecoes.click();

    WebElement checkBoxProtecoesSelecionado = driver.findElement(By.cssSelector(".selection.selection-md.selection-normal.selected"));
    driver.findElement(By.xpath("//div[normalize-space()='Proteção Super Zero']")).click();

    Assert.assertTrue(checkBoxProtecoesSelecionado.isDisplayed(),"Não foi possível selecionar uma proteção");

    // Selecionar acessórios e serviços
    List<WebElement> checkboxAcessorios = driver.findElements(By.cssSelector("app-accessorie-box > div > div > div.selection"));
    JavascriptExecutor js = (JavascriptExecutor) driver;

    for (WebElement divCheckbox : checkboxAcessorios) {
      WebElement checkbox = divCheckbox.findElement(By.cssSelector("span"));
      js.executeScript("arguments[0].click();", checkbox);
      String classeCheckbox = divCheckbox.getAttribute("class");
      Assert.assertTrue(classeCheckbox.contains("selected"),"Não foi possivel selecionar o elemento " + checkbox.toString());
    }

    WebElement botaoAvancar = driver.findElement(By.cssSelector(".resume-action.fixed"));
    js.executeScript("arguments[0].click();", botaoAvancar);

    WebElement headerFinalizeReserva = driver.findElement(By.cssSelector(".mb-5.pt-5"));
    String textoHeader = headerFinalizeReserva.getText();
    Assert.assertEquals(textoHeader, "Confira e finalize sua reserva");

    WebElement inputCPF = driver.findElement(By.cssSelector("input[data-qa='input-document']"));
    inputCPF.sendKeys("830.580.280-91");

    WebElement inputNome = driver.findElement(By.cssSelector("input[data-qa='input-name']"));
    inputNome.sendKeys("Nome Teste");

    WebElement inputCelular = driver.findElement(By.cssSelector("input[data-qa='input-cellphone']"));
    inputCelular.sendKeys("75982876275");

    WebElement inputEmail = driver.findElement(By.cssSelector("input[data-qa='input-email']"));
    inputEmail.sendKeys("veweto6880@aramask.com");

    WebElement botaoConcluir = driver.findElement(By.cssSelector(".resume > div.resume-action.fixed"));
    // botaoConcluir.click();

  }

  @Test
  public void retiradaDevolucaoDiferentes() {
    driver.get("http://unidas.com.br");

    WebElement devolucaoNaMesmaCidade = driver.findElement(By.cssSelector(".slider.round.checked"));
    devolucaoNaMesmaCidade.click();

    WebElement inputCidadeDevolucao = driver.findElement(By.xpath("(//input[@placeholder='Digite a cidade, aeroporto ou loja'])[2]"));

    Assert.assertTrue(inputCidadeDevolucao.isDisplayed(), "Input para devolução em cidade diferente não aparece");

    WebElement inputCidadeRetirada = driver.findElement(By.cssSelector("input[placeholder='Digite a cidade, aeroporto ou loja']"));
    inputCidadeRetirada.sendKeys("Guaratingueta");

    WebElement opcaoCidade = driver.findElement(By.cssSelector("div[class='home-masterflow'] li:nth-child(1)"));
    opcaoCidade.click();

    inputCidadeDevolucao.click();
    inputCidadeDevolucao.sendKeys("São Paulo");

    opcaoCidade =
      driver.findElement(By.cssSelector("div[class='home-masterflow'] li:nth-child(1)"));
    opcaoCidade.click();

    WebElement inputDataRetirada = driver.findElement(By.cssSelector("#date-out"));
    inputDataRetirada.click();

    WebElement modalData = driver.findElement(By.xpath("//div[contains(@class,'p-datepicker-multiple-month')]"));

    Assert.assertTrue(modalData.isDisplayed(), "O menu de data não apareceu na tela");

    WebElement primeiraDataValidaReserva = driver.findElement(By.xpath("(//td/span[not(contains(@class, 'p-disabled'))])[2]"));
    primeiraDataValidaReserva.click();

    WebElement primeiroHorario = driver.findElement(By.xpath("//ul[@class='hours-list']/li[1]"));
    Assert.assertTrue(primeiroHorario.isDisplayed());
    primeiroHorario.click();

    WebElement primeiraDataValidaDevolucao = driver.findElement(By.xpath("(//td/span[not(contains(@class, 'p-disabled'))])[3]"));
    primeiraDataValidaDevolucao.click();

    primeiroHorario =driver.findElement(By.xpath("//ul[@class='hours-list']/li[1]"));
    primeiroHorario.click();

    driver.findElement(By.xpath("//a[normalize-space()='Alugar']")).click();

    String tituloGrupoVeiculos = driver.findElement(By.cssSelector("div[class='col-md-12 col-sm-12'] h1")).getText();

    Assert.assertEquals(tituloGrupoVeiculos, "Escolha seu grupo de veículos", "Não foi possível redirecionar o usuário para a página de grupo de veículos");
  }

  @AfterTest
  public void quit() {
    driver.quit();
  }
}
