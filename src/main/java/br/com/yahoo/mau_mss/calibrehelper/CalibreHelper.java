package br.com.yahoo.mau_mss.calibrehelper;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.yahoo.mau_mss.calibrehelper.controller.CalibreHelperAppAction;
import br.com.yahoo.mau_mss.calibrehelper.view.CalibreHelperApp;

/**
 * Hello world!
 *
 */
public class CalibreHelper {
  private static final Logger log = LoggerFactory.getLogger(CalibreHelper.class);
  boolean packFrame;
  
  public CalibreHelper() {
    super();
    //initLogger();
    CalibreHelperApp frame = new CalibreHelperApp();
    new CalibreHelperAppAction(frame);
    if (this.packFrame) 
      frame.pack();
    else
      frame.validate();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation( (screenSize.width - frameSize.width) /2,
                       (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }
  
  /*
  private void initLogger() {
    ch.qos.logback.classic.LoggerContext context = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
    ch.qos.logback.classic.joran.JoranConfigurator jc = new ch.qos.logback.classic.joran.JoranConfigurator();
    try {
       jc.setContext(context);
       context.reset(); // override default configuration
       jc.doConfigure(this.getClass().getResource("/logback.xml"));
    } catch (ch.qos.logback.core.joran.spi.JoranException ex) {
       java.util.logging.Logger.getLogger(CalibreHelper.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  */
  
  public static void main( String[] args )  {
     SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        new CalibreHelper();
      }
       
     });
  }
}
