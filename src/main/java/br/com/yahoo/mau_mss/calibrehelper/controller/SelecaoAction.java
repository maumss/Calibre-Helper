package br.com.yahoo.mau_mss.calibrehelper.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.yahoo.mau_mss.calibrehelper.view.DialogSelecao;

/**
 * <p>Empresa: Autocom Informática</p>
 * <p>Título: SelecaoAction.java
 * <p>Descrição: Controller da tela de seleção de arquivos </p>
 * <p>Pacote: br.com.autocominformatica.ecfserver.action </p>
 * <p>Data: 4 de Abril de 2007, 19:41 </p>
 * @author Mauricio Soares da Silva
 * @version 1.0
 */

public class SelecaoAction implements ActionListener {
  private DialogSelecao dialogSelecao;
  private String caminho = "";
  public static final String COMANDO_SELECIONAR = "ApproveSelection";
  public static final String COMANDO_SAIR = "sair";
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  /** 
   * Creates a new instance of SelecaoAction
   * @param dialogSelecao DialogSelecao
   */
  public SelecaoAction(DialogSelecao dialogSelecao) {
    this.dialogSelecao = dialogSelecao;
    this.dialogSelecao.addActionListener(this);
  }
  
  /**
   * Controla a chamada para cada comando recebido
   * @param e ActionEvent
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    logger.info("Entrou no actionPerformed com comando: " + e.getActionCommand());
    if (e.getActionCommand().equals(SelecaoAction.COMANDO_SELECIONAR)) {
      this.seleciona();
    } else {
      this.sai();
    }
  }
  
  public String getCaminho(){
    return this.caminho;
  }
  
  private void seleciona() {
    this.caminho = this.dialogSelecao.getJFileChooser1();
    this.dialogSelecao.doClose(DialogSelecao.RET_CANCEL);
  }
  
  private void sai() {
    this.dialogSelecao.doClose(DialogSelecao.RET_CANCEL);
  }
  
}
