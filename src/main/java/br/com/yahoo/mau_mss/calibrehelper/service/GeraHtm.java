package br.com.yahoo.mau_mss.calibrehelper.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: GeraHtm</p>
 * <p>Description:  </p>
 * <p>Date: Jun 20, 2012, 12:38:39 PM</p>
 * @author Mauricio Soares da Silva (mauricio.soares)
 */
public class GeraHtm {
  private int tamanhoEspecifico;
  private int atualGeral;
  private int atualEspecifico;
  private boolean feito;
  private boolean cancelado;
  private String mensagemGeral;
  private String mensagemEspecifico;
  private boolean sucesso;
  private String inputFolder;
  private String outputFolder;
  private boolean renameChapter;
  private boolean keepImagesTogether;
  private GeraHtmTask geraHtmTask;
  private TaskException taskException;
  private String errorMessage;
  public static final int ONE_SECOND = 1000;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Create a new instance of <code>GeraHtm</code>.
   */
  public GeraHtm(String inputFolder, String outputFolder, boolean renameChapter, boolean keepImagesTogether) {
    this.inputFolder = inputFolder;
    this.outputFolder = outputFolder;
    this.renameChapter = renameChapter;
    this.keepImagesTogether = keepImagesTogether;
  }
  
  public GeraHtm(String inputFolder, String outputFolder) {
    this(inputFolder, outputFolder, false, false);
  }
  
  public void go() {
    this.tamanhoEspecifico = 0;
    this.feito = false;
    this.cancelado = false;
    (this.geraHtmTask = new GeraHtmTask()).execute();
  }

  public void stop() {
    this.cancelado = true;
    this.mensagemGeral = null;
    this.mensagemEspecifico = null;
    this.geraHtmTask.cancel(true);
    this.geraHtmTask = null;
  }
  
  public int getTamanhoEspecifico() {
      return this.tamanhoEspecifico;
  }
  
  public int getAtualGeral() {
      return this.atualGeral;
  }
  
  public int getAtualEspecifico() {
      return this.atualEspecifico;
  }
  
  public boolean estaFeito() {
      return this.feito;
  }
  
  public boolean isSucesso() {
    return this.sucesso;
  }
  
  public String getMensagemGeral() {
      return this.mensagemGeral;
  }
  
  public String getMensagemEspecifico() {
      return this.mensagemEspecifico;
  }
  
  public String getErrorMessage() {
    if (this.taskException != null)
      this.errorMessage = this.taskException.process;
    return this.errorMessage;
  }
  
  private static class TaskFeedBack {
    /** posição atual geral */
    private final int masterPos;
    /** posição atual específica */
    private final int detailPos;
    /** tamanho máximo específico */
    private final int detailLength;
    /** mensagem atual geral */
    private final String masterMsg;
    /** mensagem atual específica */
    private final String detailMsg;

    TaskFeedBack(int masterPos, int detailPos, int detailLength, String masterMsg, String detailMsg) {
      this.masterPos = masterPos;
      this.detailPos = detailPos;
      this.detailLength = detailLength;
      this.masterMsg = masterMsg;
      this.detailMsg = detailMsg;
    }
  }
  
  private static class TaskException {
    private final String process;

    TaskException(String process) {
      this.process = process;
    }
  }
  
  private class GeraHtmTask extends SwingWorker<TaskException, TaskFeedBack> {
      List<String> saida = new ArrayList<String>();
      boolean hasMoreThanOneSubLevel = false;

      @Override
      protected TaskException doInBackground() throws Exception {
        int posEspecifico = 0, maxEspecifico = 4;
        try {
            Thread.sleep(ONE_SECOND); //pára por um segundo para verificar o try anterior
            try {
                if (! this.isCancelled()) {
                  this.publish(new TaskFeedBack(0, posEspecifico, maxEspecifico,
                                              null, "Montando cabeçalho..."));
                  if (StringUtils.isBlank(inputFolder))
                    throw new IllegalArgumentException("Diretório de origem indeterminado");
                  if (StringUtils.isBlank(outputFolder))
                    throw new IllegalArgumentException("Diretório de destino indeterminado");
                  if (! StringUtils.endsWith(inputFolder, SystemUtils.FILE_SEPARATOR))
                    inputFolder += SystemUtils.FILE_SEPARATOR;
                  if (! StringUtils.endsWith(outputFolder, SystemUtils.FILE_SEPARATOR))
                    outputFolder += SystemUtils.FILE_SEPARATOR;
                  criarHeader();
                  posEspecifico++;
                  Thread.sleep(ONE_SECOND * 2); // para por um segundo para devolver as mensagens gerais
                }
                if (! this.isCancelled()) {
                  this.publish(new TaskFeedBack(0, posEspecifico, maxEspecifico,
                                              null, "Buscando imagens..."));
                  Thread.sleep(ONE_SECOND); // para por um segundo para devolver as mensagens gerais
                  File rootFolder = new File(inputFolder + ".");
                  hasMoreThanOneSubLevel = (countSubLevels(rootFolder) > 1);
                  createEntry(rootFolder, 1);
                  posEspecifico++;
                  this.publish(new TaskFeedBack(0, posEspecifico, maxEspecifico,
                                              null, "Montando rodapé..."));
                  Thread.sleep(ONE_SECOND); // para por um segundo para devolver as mensagens gerais
                  criarFooter();
                  posEspecifico++;
                }
                if (! this.isCancelled()) {
                  this.publish(new TaskFeedBack(0, posEspecifico, maxEspecifico,
                                              null, "Gerando arquivo " + getNomeArquivo() + "..."));
                  Thread.sleep(ONE_SECOND); // para por um segundo para devolver as mensagens gerais
                  gerarArquivo();
                  posEspecifico++;
                  try {
                    Validate.notEmpty(saida);
                  }catch(RuntimeException re) {
                    return new TaskException("Nenhum arquivo encontrado");
                  }
                  logger.info("Número de linhas salva: " + saida.size());
                }
            } catch(Exception e) {
              logger.error("Não foi possível concluir o backup", e);
              return new TaskException("Não foi possível concluir o backup");
            } // end catch
        } catch (InterruptedException e) {
          logger.error("TarefaAtual interrompida.", e);
        } // end catch
        return null;
      }

      /**
      * Feedback para a tela de progresso da operação
      * @param feeds
      */
      @Override
      protected void process(List<TaskFeedBack> feeds) {
        TaskFeedBack feed = feeds.get(feeds.size() - 1);
        atualGeral = feed.masterPos;
        atualEspecifico = feed.detailPos;
        tamanhoEspecifico = feed.detailLength;
        mensagemGeral = feed.masterMsg;
        mensagemEspecifico = feed.detailMsg;
      }

      /**
      * Finalização do processo de leitura após terminada a execução do
      * método doInBackground
      */
      @Override
      protected void done() {
        try {
          taskException = this.get();
        } catch(InterruptedException ignore) {}
        catch(ExecutionException ee) {
          String why;
          Throwable cause = ee.getCause();
          if (cause != null)
            why = cause.getMessage();
          else
            why = ee.getMessage();
          logger.error("Erro ao executar tarefa: " + why, ee);
        }
        //atualGeral = tamanhoGeral;
        feito = true;
        mensagemGeral = "Tarefa completada";
        mensagemEspecifico = "";
      }
      
      private String getNomeArquivo() {
        String[] pastas = StringUtils.split(inputFolder, SystemUtils.FILE_SEPARATOR);
        return pastas[pastas.length - 1] + ".htm";
      }

      private void gerarArquivo() {
        try {
          PrintStream arqSaida = new PrintStream(new BufferedOutputStream(
                  new FileOutputStream(outputFolder + getNomeArquivo())));
          for (String linha : saida)
            arqSaida.println(linha);
          arqSaida.flush();
          arqSaida.close();
        } catch(IOException e) {
          logger.error("Erro ao gerar arquivo html.", e);
        }
      }
      
      private boolean isImagemValida(File file) {
        if (file != null)
          logger.debug("Verificando arquivo " + file.getName());
        return (file !=null && 
                (file.getName().endsWith(".jpg") ||
                 file.getName().endsWith(".png") ||
                 file.getName().endsWith(".gif")));
      }
      
      private String getNumberInName(String name) {
        String result = null;
        if (StringUtils.isBlank(name))
          return result;
        StringBuilder numbers = new StringBuilder(); 
        for(char c : name.toCharArray()) { 
          if (Character.isDigit(c)) 
            numbers.append(c); 
        } 
        if (numbers.length() > 0)
          result = numbers.toString(); 
        return result;
      }
      
      private int countSubLevels(File folder) {
        int count = 0;
        for (File file : folder.listFiles()) {
          if (file.isDirectory()) {
            count += countSubLevels(file) + 1;
            break;
          }
        }
        return count;
      }
      
      private boolean isLastEntryImage() {
        return (! saida.isEmpty() && saida.get(saida.size()-1).trim().startsWith("<img"));
      }
      
      /**
       * Cria um conjunto de tags de imagem envoltas em um <div> e precedidas de 
       * <h2> se existir apenas um nível de subdiretórios ou <h1> e <h2> se 
       * houverem mais níveis
       * @param folderFile File
       * @param level int
       */
      private void createEntry(File folderFile, int level) {
        if (level == 0)
          level++;
        String header = "h2";
        if (hasMoreThanOneSubLevel && level == 1)
          header = "h1";
        for (File file : folderFile.listFiles()) {
          if (file.isFile()) {
            if (StringUtils.substringBefore(file.getName(), ".").equalsIgnoreCase("UNKNOWN")) {
              String newFile = StringUtils.substringBefore(file.getPath(), "UNKNOWN") + StringUtils.substringAfter(file.getName(), ".") + ".gif";
              File file2 = new File(newFile);
              if (file.renameTo(file2))
                file = file2;
            }
            if (isImagemValida(file)) {
              if (! isLastEntryImage())
                saida.add("  <div style=\"text-align: center\">");
              saida.add("    <img src=\"." + StringUtils.substringAfter(file.getPath(), ".") + "\" alt=\"" + file.getName() + "\" />");
            }
          }
          if (file.isDirectory()) {
            String numbers = null;
            if (renameChapter)
              numbers = getNumberInName(file.getName());
            if (renameChapter && "h2".equals(header) && StringUtils.isNotBlank(numbers))
              saida.add("  <h2>Chapter " + numbers + "</h2>");
            else
              saida.add("  <" + header + ">" + file.getName() + "</" + header + ">");
            createEntry(file, (level+1));
            if (isLastEntryImage())
              saida.add("  </div>");
          }
        }
      }
      
      private void insereCss() {
        /*
         * margin: top right bottom left
         *         top right/left bottom
         *         top bottom
         *         all
         */
        if (keepImagesTogether) {
          saida.add("    body { background-image: url(../watermark.png);");
          saida.add("      background-repeat: no-repeat; background-attachment: fixed;");
          saida.add("      background-position: center top; #xpos ypos");
          saida.add("      overflow: hidden; width: 100%; margin: 80px 0 0 0; z-index: -1; } ");
          // # Title and Author
          saida.add("    h1 {text-align: center; line-height:1.6em;}");
          saida.add("    h2 {font-weight: 600; font-size: x-large; padding-bottom: 20pt; } ");
          saida.add("    h3 {padding-bottom: 20pt; font-weight: 600; font-size: xx-large }");
          saida.add("    # chapter with page break before ");
          saida.add("    h1,h2,h3,h4,h5,h6 {page-break-before: always; text-align:center;}");
          saida.add("    img {text-align: center; display: block; text-indent: 0; margin: 6px auto; max-width: 100%;}");
        } else {
          saida.add("    body { margin: 0; }");
          saida.add("    h1 {text-align: center; line-height:1.6em;}");
          saida.add("    h2 {font-weight: 600; font-size: x-large; padding-bottom: 20pt; } ");
          saida.add("    h3 {padding-bottom: 20pt; font-weight: 600; font-size: xx-large }");
          saida.add("    # chapter without page break after ");
          saida.add("    h1,h2,h3,h4,h5,h6 {page-break-after:avoid; text-align:center;}");
          saida.add("    # images with page break after ");
          saida.add("    img {text-align: center; display: block; text-indent: 0; margin: 3px auto; height: 100%; max-width: 100%; page-break-after: always;}");
        }
        saida.add("    p {margin-top: 0pt; margin-bottom: 0pt; padding: 0pt; text-indent: 15pt; text-align: justify;}");
        saida.add("    .large {font-size:200%; font-weight:bold;}");
        saida.add("    .medium {font-size:130%;}");
        saida.add("    .center {text-align:center;}");
      }
      
      /**
       * Cria html padrao para o Calibre
       * @see http://xxxholic.wikia.com/wiki/Chapter_001
       * @see http://mariolurig.com/books/build-ebook-from-html-code/
       * @see http://www.paulsalvette.com/2011/08/calibre-tutorial-ebook-formatting.html
       */
      private void criarHeader() {
        saida.add("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
        saida.add("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"");
        saida.add("    \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        saida.add("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"pt-br\" lang=\"pt-br\">");
        saida.add("<head>");
        saida.add("  <title>" + StringUtils.substringBefore(getNomeArquivo(), ".") + "</title>");
        saida.add("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
        saida.add("  <meta http-equiv=\"Content-Language\" content=\"pt-br\" />");
        saida.add("  <meta name=\"author\" content=\"Mauricio Soares da Silva\" />");
        saida.add("  <meta name=\"keywords\" content=\"Manga,anime\" />");
        saida.add("  <meta name=\"description\" content=\"Manga " + StringUtils.substringBefore(getNomeArquivo(), ".") + "\" />");
        saida.add("  <meta name=\"date\" content=\"" + DateFormatUtils.ISO_DATE_FORMAT.format(new Date()) + "\" />" );
        saida.add("  <style type=\"text/css\">");
        insereCss();
        saida.add("  </style>");
        saida.add("</head>");
        saida.add("<body>");
      }
      
      private void criarFooter() {
        saida.add("</body>");
        saida.add("</html>");
      }
     
  }

}
