package com.idega.block.mailinglist.business;

import com.idega.util.SendMail;
import com.idega.block.mailinglist.data.EmailLetterData;
import java.sql.SQLException;
import javax.mail.MessagingException;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class EmailServices {

  public EmailServices() {

  }

  public static void sendServices(EmailLetterData letter) throws SQLException, MessagingException{

    SendMail.send(letter.getFromEmail(), letter.getToEmail(), letter.getCCEmail(), letter.getBCCEmail(), "mail.darenet.dk" , letter.getSubject(), letter.getBody());
  }

  public EmailLetterData[] getServices(){
    EmailLetterData[] letters;
    return null;
  }

  public String getName(){
  return (new String());
  }

  public void connect(){}

  public void disConnect(){}
}