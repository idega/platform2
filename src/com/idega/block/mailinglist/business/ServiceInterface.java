package com.idega.block.mailinglist.business;

import javax.mail.Message;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public interface ServiceInterface {

  public void connectToServer();

  public void disconnectFromServer();

  public Message[] getServices();

  public void sendService(Message[] messages);

  public void getProtocall();

}