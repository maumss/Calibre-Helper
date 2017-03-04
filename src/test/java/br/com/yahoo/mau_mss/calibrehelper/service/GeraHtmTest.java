package br.com.yahoo.mau_mss.calibrehelper.service;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author mauricio.soares
 */
public class GeraHtmTest {
  
  public GeraHtmTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }
  
  /**
   * Test of go method, of class GeraHtm.
   */
  @Test
  public void testGerarArquivo() throws IOException {
    System.out.println("gerarArquivo");
    String expResult = "xxxHOLiC";
    String[] pastas = StringUtils.split("D:\\Documentos\\Manga\\xxxHOLiC\\", "\\");
    String result = pastas[pastas.length - 1];
    System.out.println("Valor retornado: " + result);
    
    /*
    java.io.File file = new java.io.File("D:\\Internet\\transf\\Garfield\\pack\\.");
    System.out.println("lista de arquivos: ");
    for (java.io.File file1 : file.listFiles()) {
      System.out.println("["+ file1.getName() + "] " + file1.getPath());
    }
    System.out.println("name= " + file.getName());
    System.out.println("absolutePath= " + file.getAbsolutePath());
    System.out.println("canonicalPath= " + file.getCanonicalPath());
    System.out.println("path= " + file.getPath());
    System.out.println("parent= " + file.getParent());
    */
    
    assertEquals(expResult, result);
  }
  
}
