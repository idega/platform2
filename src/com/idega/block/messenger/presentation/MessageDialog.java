package com.idega.block.messenger.presentation;

import java.applet.AudioClip;
import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

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

public class MessageDialog extends Frame implements ActionListener{
  Panel panel = new Panel();
  Label senderNameLabel = new Label();
  TextArea messageArea = new TextArea("",1,1,TextArea.SCROLLBARS_VERTICAL_ONLY);
  TextField replyMessage = new TextField();
  Button sendButton = new Button();
  Message message;
  Vector messageVector;
  String lastMessageString;
  String recipientName;
  ImageLabel logo;
  ActionListener listener;
  AudioClip alertSound;
  Label status = new Label("  ");

  public MessageDialog(String title, Message message) {
    this(title,message,null);
  }

  public MessageDialog(String title, Message message, ImageLabel imageLogo) {
    super(title);
    //super(new Frame(), title, false);//if we are a dialog
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    this.message = message;
    try {
      this.logo = imageLogo;
      jbInit();
      add(this.panel);
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  void jbInit() throws Exception {
    this.recipientName = this.message.getRecipientName();
    this.panel.setBackground(Color.white);
    this.panel.setFont(new java.awt.Font("Arial", 0, 12));
    this.panel.setLayout(null);
    //panel.setSize(330,270);
    this.senderNameLabel.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    this.senderNameLabel.setForeground(Color.darkGray);
    this.senderNameLabel.setText(this.message.getSenderName());

    this.senderNameLabel.setBounds(new Rectangle(6, 59, 365, 20));
    this.messageArea.setEditable(false);
    this.messageArea.setBounds(new Rectangle(6, 81, 277, 123));
    this.messageArea.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    this.messageArea.setBackground(Color.white);
    this.messageArea.setForeground(Color.blue);



    this.replyMessage.requestFocus();
    this.replyMessage.setBounds(new Rectangle(6, 217, 278, 29));
    this.replyMessage.addActionListener(this);
    this.replyMessage.setFont(new java.awt.Font("Arial", Font.PLAIN, 14));
    /*sendButton.setActionCommand("send");
    sendButton.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    sendButton.setLabel("Send");
    sendButton.setBounds(new Rectangle(206, 222, 79, 31));
    sendButton.addActionListener(this);*/


    if( this.logo!=null ) {
      this.logo.setBounds(new Rectangle(6, 0, 126, 52));
      this.panel.add(this.logo, null);
    }


    this.panel.add(this.replyMessage, null);
    this.panel.add(this.messageArea, null);
    this.panel.add(this.senderNameLabel, null);

    this.senderNameLabel.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    this.senderNameLabel.setForeground(Color.darkGray);
    this.status.setBounds(new Rectangle(6, 260, 365, 20));

    this.panel.add(this.status,null);

  }
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      dispose();
      //setState ( Frame.ICONIFIED );
      //setState ( Frame.NORMAL );
    }
    super.processWindowEvent(e);
  }

  public void actionPerformed(ActionEvent e) {
    //if( (e.getActionCommand().equalsIgnoreCase("send")) || (e.getID() == Event.KEY_PRESS) ){
      this.lastMessageString = this.replyMessage.getText();
      if( !("".equalsIgnoreCase(this.lastMessageString)) ){
        this.messageArea.append(this.recipientName+" says:\n");
        this.messageArea.append("   "+this.lastMessageString+"\n");
        storeMessageString();
        this.replyMessage.requestFocus();

        if( this.listener!=null ) {
			this.listener.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"iw-send"));
		}

        }
  }

  public void addMessage(Message msg){
    this.message = msg;
    this.senderNameLabel.setText(this.message.getSenderName()+" - instant message");

    this.messageArea.append(this.message.getSenderName()+" says:\n");
    this.messageArea.append("   "+this.message.getMessage()+"\n");
    if(this.alertSound!=null) {
		this.alertSound.play();
	}
        //setStatus("Last message received at "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds());
        repaint();


  }

  public Vector getMessages(){
     return this.messageVector;
  }

  public void clearMessageVector(){
    this.messageVector = null;
  }

  private void storeMessageString(){
    if( this.messageVector == null ) {
		this.messageVector = new Vector();
	}
    Message msg = new Message();
    msg.setMessage(this.lastMessageString);
    msg.setRecipient(this.message.getSender());
    System.out.println("MessageDialog :  message.getSender() : "+this.message.getSender());

    if( (this.message!=null) && (this.message.getId()!=0) ){
      msg.setId(this.message.getId());
    }
    else{
      msg.setId(this.hashCode());
    }

    this.messageVector.addElement( msg );
    this.replyMessage.setText("");
    this.lastMessageString = "";
  }

  public void setStatus(String text){
    this.status.setFont(new java.awt.Font("Arial", Font.PLAIN, 12));
    this.status.setForeground(Color.darkGray);

    this.status.setText(text);
    this.status.repaint();
  }

  public void setLogoImage(Image image){
    this.logo = new ImageLabel(image);
  }

  public void setLogoImageLabel(ImageLabel imageLabel){
    this.logo = imageLabel;
  }

  public void setAudioClip(AudioClip alertSound){
    this.alertSound = alertSound;
  }



  public void paint(Graphics g){
    int iWidth = this.getBounds().width-20;
    int iHeight = this.getBounds().height-(184);
    this.messageArea.setBounds(6, 82,iWidth,iHeight);
    this.replyMessage.setBounds(6, this.messageArea.getBounds().height + 105 ,iWidth,29);
    this.status.setBounds(6, this.replyMessage.getBounds().y + 23 ,iWidth,15);

    super.paint(g);
  }

  public void addActionListener(ActionListener l) {
      this.listener = AWTEventMulticaster.add(this.listener, l);
  }

  public void removeActionListener(ActionListener l) {
      this.listener = AWTEventMulticaster.remove(this.listener, l);
  }

  private ActionListener getActionListener(){
    return this.listener;
  }

}