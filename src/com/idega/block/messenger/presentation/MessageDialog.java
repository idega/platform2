package com.idega.block.messenger.presentation;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.EventListener;
import java.net.URL;
import com.idega.block.messenger.data.Message;
import com.idega.presentation.awt.ImageLabel;

/**
 * Title:        com.idega.block.messenger.presentation
 * Description:  idega classes
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class MessageDialog extends Dialog implements ActionListener{
  Panel panel = new Panel();
  Label senderNameLabel = new Label();
  TextArea messageArea = new TextArea();
  TextField replyMessage = new TextField();
  Button sendButton = new Button();
  Message message;
  Vector messageVector;
  String lastMessageString;
  String recipientName;
  ImageLabel logo;
  ActionListener listener;

  public MessageDialog(String title, Message message) {
    super(new Frame(), title, false);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    this.message = message;
    try {
      jbInit();
      add(panel);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public MessageDialog(String title, Message message, ImageLabel imageLogo) {
    this(title, message);
    this.logo = imageLogo;
  }

  public MessageDialog(String title, Message message, Image image) {
    this(title, message,new ImageLabel(image));
  }

  void jbInit() throws Exception {
    recipientName = message.getRecipientName();
    panel.setBackground(Color.white);
    panel.setFont(new java.awt.Font("Arial", 0, 12));
    panel.setLayout(null);
    //panel.setSize(330,270);
    senderNameLabel.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    senderNameLabel.setForeground(Color.darkGray);

    //debugsenderNameLabel.setText("Unknown sender");
    senderNameLabel.setText(message.getSenderName());

    senderNameLabel.setBounds(new Rectangle(6, 59, 365, 20));
    messageArea.setEditable(false);
    messageArea.setBounds(new Rectangle(6, 81, 277, 123));
    messageArea.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    messageArea.setBackground(Color.white);
    messageArea.setForeground(Color.blue);
    replyMessage.requestFocus();
    replyMessage.setBounds(new Rectangle(6, 217, 278, 29));
    replyMessage.addActionListener(this);
    replyMessage.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
    /*sendButton.setActionCommand("send");
    sendButton.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    sendButton.setLabel("Send");
    sendButton.setBounds(new Rectangle(206, 222, 79, 31));
    sendButton.addActionListener(this);*/

    if(logo!=null){
      logo.setBounds(new Rectangle(6, 0, 126, 52));
      panel.add(logo, null);
    }


    panel.add(replyMessage, null);
    panel.add(messageArea, null);
    panel.add(senderNameLabel, null);
   // panel.add(sendButton, null);
  }
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }
  void cancel() {
    dispose();
  }

  public void actionPerformed(ActionEvent e) {
    //if( (e.getActionCommand().equalsIgnoreCase("send")) || (e.getID() == Event.KEY_PRESS) ){
      lastMessageString = replyMessage.getText();
      if( !("".equalsIgnoreCase(lastMessageString)) ){
        messageArea.append(recipientName+" says:\n");
        messageArea.append("   "+lastMessageString+"\n");
        storeMessageString();
        replyMessage.requestFocus();

        if( listener!=null ) listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"iw-send"));

        }
  }

  public void addMessage(Message msg){
    this.message = msg;
    senderNameLabel.setText(message.getSenderName()+" - instant message");
    messageArea.append(message.getSenderName()+" says:\n");
    messageArea.append("   "+message.getMessage()+"\n");
  }

  public Vector getMessages(){
     return messageVector;
  }

  public void clearMessageVector(){
    messageVector = null;
  }

  private void storeMessageString(){
    if( messageVector == null ) messageVector = new Vector();
    Message msg = new Message();
    msg.setMessage(lastMessageString);
    msg.setRecipient(message.getSender());
    System.out.println("MessageDialog :  message.getSender() : "+message.getSender());

    if( (message!=null) && (message.getId()!=0) ){
      msg.setId(message.getId());
    }
    else{
      msg.setId(this.hashCode());
    }

    messageVector.addElement( msg );
    replyMessage.setText("");
    lastMessageString = "";
  }

  public void setLogoImage(Image image){
    this.logo = new ImageLabel(image);
  }

  public void setLogoImageLabel(ImageLabel imageLabel){
    this.logo = imageLabel;
  }



  public void paint(Graphics g){
    int iWidth = this.getBounds().width-20;
    int iHeight = this.getBounds().height-(184);
    messageArea.setBounds(6, 82,iWidth,iHeight);
    replyMessage.setBounds(6, messageArea.getBounds().height + 105 ,iWidth,29);
    super.paint(g);
  }

  public void addActionListener(ActionListener l) {
      listener = AWTEventMulticaster.add(listener, l);
  }

  public void removeActionListener(ActionListener l) {
      listener = AWTEventMulticaster.remove(listener, l);
  }

  private ActionListener getActionListener(){
    return listener;
  }

}