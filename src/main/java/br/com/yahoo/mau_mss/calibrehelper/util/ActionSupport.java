package br.com.yahoo.mau_mss.calibrehelper.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;

/**
 * <p>Title: ActionSupport</p>
 * <p>Description:  </p>
 * <p>Date: Jun 20, 2012, 8:07:43 AM</p>
 * @author Mauricio Soares da Silva (mauricio.soares)
 */
public class ActionSupport {
  private EventListenerList listenerList = new EventListenerList();

  /**
   * Create a new instance of <code>ActionSupport</code>.
   */
  public ActionSupport() {
  }
  
  /**
   * Usa reflex�o para propagar um listener recebido
   * @param actionListener ActionListener
   */
  public void addActionListener(ActionListener actionListener) {
    listenerList.add(ActionListener.class, actionListener);
  }

  /**
   * Usa reflexão para remover um listener recebido
   * @param actionListener ActionListener
   */
  public void removeActionListener(ActionListener actionListener) {
    listenerList.remove(ActionListener.class, actionListener);
  }

  /**
   * Cria uma lista de objetos para poder processá-los na ordem inversa
   * em que forem recebidos
   * @param event ActionEvent
   */
  public void fireActionEvent(ActionEvent event) {
    Object[] listeners = listenerList.getListenerList();
    // Processa os listeners do �ltimo para o primeiro, notificando
    // aqueles que est�o interessados neste evento
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
        if (listeners[i] == ActionListener.class) {
            ((ActionListener)listeners[i+1]).actionPerformed(event);
        }
    }
  }

}
